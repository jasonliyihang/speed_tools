package com.liyihang.jason;

import android.content.Context;
import android.content.res.Resources;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.collection.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SPThemeFactory implements LayoutInflater.Factory2 {

    private static final String TAG = "SPThemeFactory";

    public String PRE = "cxt_";
    public String PRE_FONT = "cxf_";
    private AppCompatDelegate delegate;
    private List<SPThemeView> views = new ArrayList<>();

    private SPFontInfo info;
    private List<SPFontInfo> infos = new ArrayList<>();
    private ArrayList<SPThemeEnum> themeEnums=new ArrayList<>();
    private SPUpdateUIListener updateUIListener;
    private LayoutInflater.Factory2 factory2;

    public SPUpdateUIListener getUpdateUIListener() {
        return updateUIListener;
    }

    public void setPre(String PRE) {
        this.PRE = PRE;
    }

    public void setFontPre(String PRE_FONT) {
        this.PRE_FONT = PRE_FONT;
    }

    public SPThemeFactory(AppCompatActivity delegate, ArrayList<SPThemeEnum> enums, LayoutInflater.Factory2 factory2){
        this.delegate = delegate.getDelegate();
        this.factory2=factory2;
        updateUIListener= (SPUpdateUIListener) delegate;
        views.clear();
        infos.clear();
        themeEnums.clear();
        themeEnums.add(new SPBackgroundEnum());
        themeEnums.add(new SPTextColorEnum());
        themeEnums.add(new SPSrcEnum());
        if (enums!=null)
            themeEnums.addAll(enums);
    }

    public void updateUI() {
        for (SPThemeView view : views) {
            try {
                view.use();
            } catch (RuntimeException e) {
                Log.e(TAG, "updateUI failed for a themed view", e);
            }
        }
        for (SPFontInfo fontView : infos) {
            try {
                fontView.use();
            } catch (RuntimeException e) {
                Log.e(TAG, "updateUI failed for font attr " + fontView.attrName, e);
            }
        }
    }

    public void clearAll() {
        views.clear();
        infos.clear();
        themeEnums.clear();
        updateUIListener=null;
        factory2=null;
        delegate = null;
    }

    private static final Map<String, Constructor<? extends View>> sConstructorMap
            = new ArrayMap<>();
    static final Class<?>[] sConstructorSignature = new Class[]{
            Context.class, AttributeSet.class};
    private final Object[] mConstructorArgs = new Object[2];

    private View createViewFromTag(Context context, String name, AttributeSet attrs) {
        if (name.equals("view")) {
            name = attrs.getAttributeValue(null, "class");
        }

        try {
            mConstructorArgs[0] = context;
            mConstructorArgs[1] = attrs;

            if (-1 == name.indexOf('.')) {
                // try the android.widget prefix first...
                return createView(context, name, "android.widget.");
            } else {
                return createView(context, name, null);
            }
        } catch (Exception e) {
            // Not fatal: returning null lets the real LayoutInflater try again.
            Log.d(TAG, "createViewFromTag fell back to LayoutInflater for " + name, e);
            return null;
        } finally {
            // Don't retain references on context.
            mConstructorArgs[0] = null;
            mConstructorArgs[1] = null;
        }
    }

    private View createView(Context context, String name, String prefix)
            throws ClassNotFoundException, InflateException {
        Constructor<? extends View> constructor = sConstructorMap.get(name);

        try {
            if (constructor == null) {
                // Class not found in the cache, see if it's real, and try to add it
                Class<? extends View> clazz = context.getClassLoader().loadClass(
                        prefix != null ? (prefix + name) : name).asSubclass(View.class);

                constructor = clazz.getConstructor(sConstructorSignature);
                sConstructorMap.put(name, constructor);
            }
            constructor.setAccessible(true);
            return constructor.newInstance(mConstructorArgs);
        } catch (Exception e) {
            // Not fatal: returning null lets the real LayoutInflater try again.
            Log.d(TAG, "createView fell back to LayoutInflater for " + name, e);
            return null;
        }
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        if (factory2!=null)
        {
            View view = factory2.onCreateView(parent, name, context, attrs);
            if (view!=null)
                return view;
        }
        return handleView(parent, name, context, attrs);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        if (factory2!=null)
        {
            return factory2.onCreateView(name, context,attrs);
        }
        return null;
    }

    private View handleView(View parent, String name, Context context, AttributeSet attrs) {
        if (delegate == null) {
            Log.w(TAG, "factory already released, cannot inflate " + name);
            return null;
        }
        View view = null;
        info = new SPFontInfo();
        List<SPThemeAttr> themeAttrs = getThemeAttrs(name, attrs, context);
        if (info.isExist) {
            view = delegate.createView(parent, name, context, attrs);
            if (view == null) {
                view = createViewFromTag(context, name, attrs);
            }
            info.viewWeakReference = new WeakReference<>(view);
            infos.add(info);
            info.use();
        }
        if (!themeAttrs.isEmpty()) {
            if (!info.isExist) {
                view = delegate.createView(parent, name, context, attrs);
                if (view == null) {
                    view = createViewFromTag(context, name, attrs);
                }
            }
            if (view != null) {
                SPThemeView cxThemeView = new SPThemeView(view, themeAttrs);
                views.add(cxThemeView);
                cxThemeView.use();
            }
        }
        return view;
    }

    public List<SPThemeAttr> getThemeAttrs(String name, AttributeSet attrs, Context context) {
        List<SPThemeAttr> skinAttrs = new ArrayList<>();
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);
            if ("textSize".equals(attrName)) {
                String entryName = resolveEntryName(context, name, attrName, attrValue);
                if (entryName != null && entryName.startsWith(PRE_FONT) && !info.isExist) {
                    info.isExist = true;
                    info.attrName = entryName;
                }
            }
            SPThemeEnum attrType = getSupprotAttrType(attrName);
            if (attrType == null) continue;
            String entryName = resolveEntryName(context, name, attrName, attrValue);
            if (entryName != null && entryName.startsWith(PRE)) {
                skinAttrs.add(new SPThemeAttr(entryName, attrType));
            }
        }
        return skinAttrs;
    }

    /**
     * Resolves the resource entry name behind an {@code @<id>} attribute value.
     *
     * @return the entry name, or {@code null} when the value is not a resource reference or the
     *         id cannot be resolved; unresolvable ids are logged instead of aborting inflation.
     */
    private String resolveEntryName(Context context, String viewName, String attrName, String attrValue) {
        if (attrValue == null || !attrValue.startsWith("@")) {
            return null;
        }
        int id;
        try {
            id = Integer.parseInt(attrValue.substring(1));
        } catch (NumberFormatException e) {
            Log.w(TAG, "non-numeric resource reference " + attrName + "=" + attrValue
                    + " on <" + viewName + ">", e);
            return null;
        }
        try {
            return context.getResources().getResourceEntryName(id);
        } catch (Resources.NotFoundException e) {
            Log.w(TAG, "unknown resource id for " + attrName + "=" + attrValue
                    + " on <" + viewName + ">", e);
            return null;
        }
    }

    private SPThemeEnum getSupprotAttrType(String attrName) {
        for (SPThemeEnum attrType : themeEnums) {
            if (attrType.getType().equals(attrName))
                return attrType;
        }
        return null;
    }

}
