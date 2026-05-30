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
            updateUIListener.getUpdateUIListener().updateUI(false);
            updateUIListener.updateUI();
        }
        return this;
    }

    public SPThemeManager updateThemeConfig(Context context, String name){
        SpeedUtils.getSharedPreferences(context).edit().putString(THEME_KEY_NAME, name).apply();
        return this;
    }

    public SPThemeManager init(Context context) {
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
        return context.getResources()==mResources;
    }

    public SPThemeManager changeTheme(String tname){
        if (tname.equals(DEFAULT_THEMES)){
            defaultTheme(context);
        }else {
            File file = moveApkToAppPath(context, tname);
            if (file == null) {
                defaultTheme(context);
            } else {
                PackageInfo info = getPackageInfo(context, file.getAbsolutePath());
                if (info == null) {
                    defaultTheme(context);
                    return this;
                }
                packageName = info.packageName;
                mResources = getApkResources(context, file.getAbsolutePath());
                SpeedUtils.getSharedPreferences(context).edit().putString(THEME_KEY_NAME, tname).apply();
                msg("changeTheme select=="+tname);
            }
        }
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
        return getResId(mResources, rid, type, packageName);
    }

    public int rid(int rid, String type){
        return rid(SpeedUtils.getNameByRid(context, rid), type);
    }

    public int color(String rid){
        try {
            int resId = getResId( mResources, rid, RES_COLOR, packageName);
            return mResources.getColor(resId, null);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public int color(int rid){
        return color(SpeedUtils.getNameByRid(context, rid));
    }

    public Drawable drawable(String rid){
        try {
            int resId = getResId( mResources, rid, RES_DRABLE, packageName);
            if (resId==0)
            {
                resId=getResId( mResources, rid, RES_MIPMAP, packageName);
            }
            return mResources.getDrawable(resId, null);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Drawable drawable(int rid){
        return drawable(SpeedUtils.getNameByRid(context, rid));
    }



}
