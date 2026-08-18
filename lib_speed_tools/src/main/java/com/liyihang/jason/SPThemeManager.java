package com.liyihang.jason;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.LayoutInflaterCompat;

import com.speed.hotpatch.libs.SpeedUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SPThemeManager {

    private static final String TAG = "SPThemeManager";

    /** Returned by {@link #color(String)} when the resource cannot be resolved. */
    public static final int COLOR_UNRESOLVED = 0;

    private static volatile SPThemeManager manager;

    public static SPThemeManager getInstance() {
        if (manager == null) {
            synchronized (SPThemeManager.class) {
                if (manager == null) {
                    manager = new SPThemeManager();
                }
            }
        }
        return manager;
    }

    private SPThemeManager(){
    }

    public static final String RES_ID = "id";
    public static final String RES_STRING = "string";
    public static final String RES_DRABLE = "drawable";
    public static final String RES_MIPMAP="mipmap";
    public static final String RES_LAYOUT = "layout";
    public static final String RES_STYLE = "style";
    public static final String RES_COLOR = "color";
    public static final String RES_DIMEN = "dimen";
    public static final String RES_ANIM = "anim";
    public static final String RES_MENU = "menu";


    public static final String THEME_KEY_NAME="theme_name";
    public static final String DEFAULT_THEMES="default";
    private Resources mResources;
    private Context context;
    private String packageName;

    private final List<SPThemeFactory> updateUIListeners = new ArrayList<>();


    public void registerUpdateUI(AppCompatActivity delegate, ArrayList<SPThemeEnum> enums, LayoutInflater.Factory2 factory2, String pre, String fontPre) {
        if (delegate!=null)
        {
            SPThemeFactory contains = isExist((SPUpdateUIListener) delegate);
            if (contains==null)
            {
                SPThemeFactory cxThemeFactory = new SPThemeFactory(delegate, enums, factory2);
                if (pre!=null)
                    cxThemeFactory.setPre(pre);
                if (fontPre!=null)
                    cxThemeFactory.setFontPre(fontPre);

                LayoutInflaterCompat.setFactory2(LayoutInflater.from(delegate), cxThemeFactory);
                updateUIListeners.add(cxThemeFactory);
            }
        }
    }

    public void registerUpdateUI(AppCompatActivity delegate){
        registerUpdateUI(delegate,null,null,null, null);
    }

    public SPThemeFactory isExist(SPUpdateUIListener listener){
        for (SPThemeFactory item : updateUIListeners) {
            if (item.getUpdateUIListener()==listener)
            {
                return item;
            }
        }
        return null;
    }

    public void unRegisterUpdateUI(AppCompatActivity delegate){
        if (delegate!=null)
        {
            SPThemeFactory contains = isExist((SPUpdateUIListener) delegate);
            if (contains!=null)
            {
                contains.clearAll();
//                LayoutInflaterCompat.setFactory2(LayoutInflater.from(delegate), new MyFactory());
                updateUIListeners.remove(contains);
            }
        }
    }

    public SPThemeManager sendUpdateUIAction(){
        for (SPThemeFactory updateUIListener : updateUIListeners) {
            SPUpdateUIListener listener = updateUIListener.getUpdateUIListener();
            try {
                if (listener != null) {
                    listener.updateUI(false);
                }
                updateUIListener.updateUI();
            } catch (RuntimeException e) {
                Log.e(TAG, "sendUpdateUIAction failed for listener="
                        + (listener == null ? "<null>" : listener.getClass().getName()), e);
            }
        }
        return this;
    }

    public SPThemeManager updateThemeConfig(Context context, String name){
        SpeedUtils.getSharedPreferences(context).edit().putString(THEME_KEY_NAME, name).apply();
        return this;
    }

    public SPThemeManager init(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context must not be null");
        }
        this.context=context;
        String tname = SpeedUtils.getSharedPreferences(context).getString(THEME_KEY_NAME, DEFAULT_THEMES);
        msg("init theme config name=="+tname);
        changeTheme( tname);
        return this;
    }

    public static void msg(String msg){
        Log.i(SPThemeManager.class.getSimpleName(), msg);
    }

    public Resources getResources() {
        return mResources;
    }

    public boolean isDefaultTheme(){
        return context != null && context.getResources()==mResources;
    }

    /**
     * Applies the skin APK named {@code tname}, falling back to the host theme when the
     * skin cannot be copied, parsed or opened. Every fallback is logged with its cause.
     */
    public SPThemeManager changeTheme(String tname){
        if (context == null) {
            throw new IllegalStateException("init(Context) must be called before changeTheme");
        }
        if (tname == null || DEFAULT_THEMES.equals(tname)) {
            if (tname == null) {
                Log.e(TAG, "changeTheme called with null name, using " + DEFAULT_THEMES);
            }
            defaultTheme(context);
            return this;
        }
        File file = moveApkToAppPath(context, tname);
        if (file == null) {
            Log.e(TAG, "changeTheme: skin asset not available: " + tname);
            defaultTheme(context);
            return this;
        }
        PackageInfo info = getPackageInfo(context, file.getAbsolutePath());
        if (info == null) {
            Log.e(TAG, "changeTheme: cannot parse skin apk: " + file.getAbsolutePath());
            defaultTheme(context);
            return this;
        }
        Resources skinResources = getApkResources(context, file.getAbsolutePath());
        if (skinResources == null) {
            Log.e(TAG, "changeTheme: cannot open skin resources: " + file.getAbsolutePath());
            defaultTheme(context);
            return this;
        }
        packageName = info.packageName;
        mResources = skinResources;
        SpeedUtils.getSharedPreferences(context).edit().putString(THEME_KEY_NAME, tname).apply();
        msg("changeTheme select=="+tname);
        return this;
    }

    private void defaultTheme(Context context) {
        packageName=context.getPackageName();
        mResources=context.getResources();
        SpeedUtils.getSharedPreferences(context).edit().putString(THEME_KEY_NAME, DEFAULT_THEMES).apply();
        msg("changeTheme select=="+DEFAULT_THEMES);
    }


    public static File moveApkToAppPath(Context context, String name) {
        return SpeedUtils.copyAssetToCache(context, name, "skin_theme");
    }

    public static Resources getApkResources(Context context, String apkPath) {
        return SpeedUtils.createResourcesFromApk(context, apkPath);
    }

    public static int getResId(Resources resources, String resName, String defType, String packageName){
        return resources.getIdentifier(resName, defType, packageName);
    }

    public static PackageInfo getPackageInfo(Context context, String apkFilepath) {
        return SpeedUtils.getPackageInfo(context, apkFilepath);
    }

    public int rid(String rid, String type){
        if (!isReady("rid", rid)) {
            return 0;
        }
        return getResId(mResources, rid, type, packageName);
    }

    public int rid(int rid, String type){
        return rid(SpeedUtils.getNameByRid(context, rid), type);
    }

    /**
     * @return the themed color, or {@link #COLOR_UNRESOLVED} when the name is missing from the
     *         current theme; the reason is always logged.
     */
    public int color(String rid){
        if (!isReady("color", rid)) {
            return COLOR_UNRESOLVED;
        }
        int resId = getResId(mResources, rid, RES_COLOR, packageName);
        if (resId == 0) {
            Log.w(TAG, "color not found in theme " + packageName + ": " + rid);
            return COLOR_UNRESOLVED;
        }
        try {
            return mResources.getColor(resId, null);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "color lookup failed for " + rid + " in " + packageName, e);
            return COLOR_UNRESOLVED;
        }
    }

    public int color(int rid){
        return color(SpeedUtils.getNameByRid(context, rid));
    }

    /**
     * @return the themed drawable, or {@code null} when the name is missing from the current
     *         theme; the reason is always logged.
     */
    public Drawable drawable(String rid){
        if (!isReady("drawable", rid)) {
            return null;
        }
        int resId = getResId(mResources, rid, RES_DRABLE, packageName);
        if (resId==0)
        {
            resId=getResId( mResources, rid, RES_MIPMAP, packageName);
        }
        if (resId == 0) {
            Log.w(TAG, "drawable not found in theme " + packageName + ": " + rid);
            return null;
        }
        try {
            return mResources.getDrawable(resId, null);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "drawable lookup failed for " + rid + " in " + packageName, e);
            return null;
        }
    }

    public Drawable drawable(int rid){
        return drawable(SpeedUtils.getNameByRid(context, rid));
    }

    private boolean isReady(String operation, String resName) {
        if (mResources == null || packageName == null) {
            Log.e(TAG, operation + "(" + resName + ") called before init(Context)");
            return false;
        }
        if (resName == null) {
            Log.e(TAG, operation + " called with null resource name");
            return false;
        }
        return true;
    }



}
