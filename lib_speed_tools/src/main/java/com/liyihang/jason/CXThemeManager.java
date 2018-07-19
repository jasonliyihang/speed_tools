package com.liyihang.jason;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;

import com.speed.hotpatch.libs.SpeedUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CXThemeManager {

    private static CXThemeManager manager=null;

    public static CXThemeManager getInstance() {
        synchronized (CXThemeManager.class) {
            if (manager==null) {
                manager=new CXThemeManager();
            }
        }
        return manager;
    }

    private CXThemeManager(){
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

    private List<CXThemeFactory> updateUIListeners=new ArrayList<>();


    public void registerUpdateUI(AppCompatActivity delegate, ArrayList<CXThemeEnum> enums, LayoutInflater.Factory2 factory2, String pre, String fontPre) {
        if (delegate!=null)
        {
            CXThemeFactory contains = isExist((CXUpdateUIListener) delegate);
            if (contains==null)
            {
                CXThemeFactory cxThemeFactory = new CXThemeFactory(delegate, enums, factory2);
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

    public CXThemeFactory isExist(CXUpdateUIListener listener){
        for (CXThemeFactory item : updateUIListeners) {
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
            CXThemeFactory contains = isExist((CXUpdateUIListener) delegate);
            if (contains!=null)
            {
                contains.clearAll();
//                LayoutInflaterCompat.setFactory2(LayoutInflater.from(delegate), new MyFactory());
                updateUIListeners.remove(contains);
            }
        }
    }

    public CXThemeManager sendUpdateUIAction(){
        for (CXThemeFactory updateUIListener : updateUIListeners) {
            updateUIListener.getUpdateUIListener().updateUI(false);
            updateUIListener.updateUI();
        }
        return this;
    }

    public CXThemeManager updateThemeConfig(Context context, String name){
        SpeedUtils.getSharedPreferences(context).edit().putString(THEME_KEY_NAME, name).apply();
        return this;
    }

    public CXThemeManager init(Context context) {
        this.context=context;
        String tname = SpeedUtils.getSharedPreferences(context).getString(THEME_KEY_NAME, DEFAULT_THEMES);
        msg("init theme config name=="+tname);
        changeTheme( tname);
        return this;
    }

    public static void msg(String msg){
        Log.i(CXThemeManager.class.getSimpleName(), msg);
    }

    public Resources getResources() {
        return mResources;
    }

    public boolean isDefaultTheme(){
        return context.getResources()==mResources;
    }

    public CXThemeManager changeTheme(String tname){
        if (tname.equals(DEFAULT_THEMES)){
            defaultTheme(context);
        }else {
            File file = moveApkToAppPath(context, tname);
            if (file==null) {
                defaultTheme(context);
            }else{
                packageName=getPackageInfo(context, file.getAbsolutePath()).packageName;
                mResources=getApkResources(context, file.getAbsolutePath());
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


    public static File moveApkToAppPath(Context context, String name){
        File file=null;
        try {
            InputStream open = context.getAssets().open(name);
            File my_cache = context.getDir("skin_theme", Context.MODE_PRIVATE);
            file = new File(my_cache.getAbsolutePath()+'/' + name);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            int len=-1;
            byte[] arr=new byte[1024];
            while ( (len=open.read(arr))!=-1 )
            {
                fileOutputStream.write(arr,0,len);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            open.close();
        } catch (Exception e) {
            file=null;
            msg("moveApkToAppPath err=="+e.getMessage());
            e.printStackTrace();
        }
        return file;
    }

    public static Resources getApkResources(Context context, String apkPath){
        Resources resources1=null;
        try {
            AssetManager assetManager=AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getDeclaredMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, apkPath);
            Resources resources = context.getResources();
            resources1 = new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());
        } catch (Exception e) {
            msg("getApkResources err=="+e.getMessage());
            e.printStackTrace();
        }
        return resources1;
    }

    public static int getResId(Resources resources, String resName, String defType, String packageName){
        return resources.getIdentifier(resName, defType, packageName);
    }

    public static PackageInfo getPackageInfo(Context context, String apkFilepath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = pm.getPackageArchiveInfo(apkFilepath, PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES | PackageManager.GET_META_DATA);
            msg("package=="+pkgInfo.packageName+"==path==="+apkFilepath);
        } catch (Exception e) {
            e.printStackTrace();
            msg(""+e.getMessage());
        }
        return pkgInfo;
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
            return mResources.getColor(resId);
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
            return mResources.getDrawable(resId);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Drawable drawable(int rid){
        return drawable(SpeedUtils.getNameByRid(context, rid));
    }



}
