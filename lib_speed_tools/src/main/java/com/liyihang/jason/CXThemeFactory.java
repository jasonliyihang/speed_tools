package com.liyihang.jason;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CXThemeFactory implements LayoutInflater.Factory2 {

    public String PRE = "cxt_";
    public String PRE_FONT = "cxf_";
    private AppCompatDelegate delegate;
    private List<CXThemeView> views = new ArrayList<>();

    private CXFontInfo info;
    private List<CXFontInfo> infos = new ArrayList<>();
    private ArrayList<CXThemeEnum> themeEnums=new ArrayList<>();
    private CXUpdateUIListener updateUIListener;
    private LayoutInflater.Factory2 factory2;

    public CXUpdateUIListener getUpdateUIListener() {
        return updateUIListener;
    }

    public void setPre(String PRE) {
        this.PRE = PRE;
    }

    public void setFontPre(String PRE_FONT) {
        this.PRE_FONT = PRE_FONT;
    }

    public CXThemeFactory(AppCompatActivity delegate, ArrayList<CXThemeEnum> enums, LayoutInflater.Factory2 factory2){
        this.delegate = delegate.getDelegate();
        this.factory2=factory2;
        updateUIListener= (CXUpdateUIListener) delegate;
        views.clear();
        infos.clear();
        themeEnums.clear();
        themeEnums.add(new CXBackgroundEnum());
        themeEnums.add(new CXTextColorEnum());
        themeEnums.add(new CXSrcEnum());
        if (enums!=null)
            themeEnums.addAll(enums);
    }

    public void updateUI() {
        for (CXThemeView view : views) {
            view.use();
        }
        for (CXFontInfo fontView : infos) {
            fontView.use();
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
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
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
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
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
        View view = null;
        info = new CXFontInfo();
        List<CXThemeAttr> themeAttrs = getThemeAttrs(name, attrs, context);
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
                CXThemeView cxThemeView = new CXThemeView(view, themeAttrs);
                views.add(cxThemeView);
                cxThemeView.use();
            }
        }
        return view;
    }

    public List<CXThemeAttr> getThemeAttrs(String name, AttributeSet attrs, Context context) {
        List<CXThemeAttr> skinAttrs = new ArrayList<>();
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);
            if ("textSize".equals(attrName)) {
                if (attrValue.startsWith("@")) {
                    int id = Integer.parseInt(attrValue.substring(1));
                    try {
                        String entryName = context.getResources().getResourceEntryName(id);
                        if (entryName.startsWith(PRE_FONT) && !info.isExist) {
                            info.isExist = true;
                            info.attrName = entryName;
                        }
                    } catch (Exception e) {
                        CXThemeEnum.msg("getThemeAttrs error attrName--" + attrName + "--attrValue--" + attrValue + "--err--" + e.getMessage() + "---name---" + name);
                        e.printStackTrace();
                    }
                }
            }
            CXThemeEnum attrType = getSupprotAttrType(attrName);
            if (attrType == null) continue;
            if (attrValue.startsWith("@")) {
                int id = Integer.parseInt(attrValue.substring(1));
                try {
                    String entryName = context.getResources().getResourceEntryName(id);
                    if (entryName.startsWith(PRE)) {
                        skinAttrs.add(new CXThemeAttr(entryName, attrType));
                    }
                } catch (Exception e) {
                    CXThemeEnum.msg("getThemeAttrs error attrName--" + attrName + "--attrValue--" + attrValue + "--err--" + e.getMessage() + "---name---" + name);
                    e.printStackTrace();
                }
            }
        }
        return skinAttrs;
    }

    private CXThemeEnum getSupprotAttrType(String attrName) {
        for (CXThemeEnum attrType : themeEnums) {
            if (attrType.getType().equals(attrName))
                return attrType;
        }
        return null;
    }

}
