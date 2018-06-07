package com.speed.hotpatch.libs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 *  by liyihang
 *  blog http://sijienet.com/
 */
public class SpeedUtils {

    public static final String tag="SpeedUtils";

    public static void msg(String tag, String msg){
        Log.i(tag, msg);
    }

    public static void goActivity(Activity activity, String apkName, String className){
        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(SpeedConfig.ACTIVITY_URL));
        intent.setPackage(activity.getPackageName());
        intent.putExtra(SpeedConfig.APK_NAME, apkName);
        intent.putExtra(SpeedConfig.CLASS_TAG, className);
        msg(tag, "goActivity=="+apkName+"=="+className);
        activity.startActivity(intent);
        msg(tag, "goActivity end");
    }

    public static File getNativeApkPath(Context context,String name){
        File file=null;
        try {
            InputStream open = context.getAssets().open(name);
            File my_cache = context.getDir("my_cache", Context.MODE_PRIVATE);
            file = new File(my_cache.getAbsolutePath() + name);
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
            e.printStackTrace();
        }
        return file;
    }

    @SuppressWarnings("deprecation")
    public static Resources readApkRes(Context context, String apkPath){
        Resources resources1=null;
        try {
            AssetManager assetManager=AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getDeclaredMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, apkPath);
            Resources resources = context.getResources();
            resources1 = new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(tag,""+e.getMessage());
        }
        return resources1;
    }

    public static String getOutDexpaPath(Context context, String apkPath){
        return context.getDir(apkPath ,Context.MODE_PRIVATE).getAbsolutePath();
    }

    public static String getRootPath(Context context){
        String path=null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            if (Environment.getExternalStorageDirectory().exists())
            {
                path = Environment.getExternalStorageDirectory().getAbsolutePath();
            }
        }
        if (path==null)
        {
            File filesDir = context.getFilesDir();
            if (filesDir.exists())
            {
                path = filesDir.getAbsolutePath();
            }
        }
        return path;
    }

    public static PackageInfo getPackageInfo(Context context, String apkFilepath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = pm.getPackageArchiveInfo(apkFilepath, PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES | PackageManager.GET_META_DATA);
            Log.i(tag,"package obje=="+pkgInfo+"==path==="+apkFilepath);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(tag,""+e.getMessage());
        }
        return pkgInfo;
    }

    public static PackageInfo getPackageInfo2(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES | PackageManager.GET_META_DATA);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(tag,""+e.getMessage());
        }
        return pkgInfo;
    }

    public static Drawable getAppIcon(Context context, String apkFilepath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = getPackageInfo(context, apkFilepath);
        if (pkgInfo == null) {
            return null;
        }

        ApplicationInfo appInfo = pkgInfo.applicationInfo;
        if (Build.VERSION.SDK_INT >= 8) {
            appInfo.sourceDir = apkFilepath;
            appInfo.publicSourceDir = apkFilepath;
        }

        return pm.getApplicationIcon(appInfo);
    }

    public static DexClassLoader readDexFile(Context context, String apkPath, String outDexPath){
        DexClassLoader dexClassLoader=null;
        try {
            dexClassLoader=new DexClassLoader(apkPath, getOutDexpaPath(context, outDexPath), context.getApplicationInfo().nativeLibraryDir, context.getClassLoader());
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(tag,""+e.getMessage());
        }
        return dexClassLoader;
    }

    public static CharSequence getAppLabel(Context context, String apkFilepath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = getPackageInfo(context, apkFilepath);
        if (pkgInfo == null) {
            return null;
        }

        ApplicationInfo appInfo = pkgInfo.applicationInfo;
        if (Build.VERSION.SDK_INT >= 8) {
            appInfo.sourceDir = apkFilepath;
            appInfo.publicSourceDir = apkFilepath;
        }

        return pm.getApplicationLabel(appInfo);
    }

    public static Object getObj(String name, ClassLoader classLoader) {
        String imp = "Imp";
        Object object2 = null;
        try {
            String s = name + imp;
            Class<?> aClass = classLoader.loadClass(s);
            object2 = aClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(tag, "" + e.getMessage());
        }
        return object2;
    }


}
