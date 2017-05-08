package com.speed.hotpatch.libs;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.util.Log;

import dalvik.system.DexClassLoader;

/**
 *  by liyihang
 */
public class SpeedApkHelper {

    public static final String TAG ="SpeedApkHelper";
    public static final String ROOT_FRAGMENT_NAME="root_class";

    private String apkPath;
    private Context ctx;

    private Drawable appIcon;
    private String apkName;

    private PackageInfo packageInfo;
    private DexClassLoader dexClassLoader;
    private Resources resources;

    public SpeedApkHelper(String apkPath, String dexOutPath, Context context) {
        this.apkPath = apkPath;
        this.ctx = context;

        packageInfo = SpeedUtils.getPackageInfo(context,apkPath);

        appIcon = SpeedUtils.getAppIcon(context, apkPath);
        apkName = (String) SpeedUtils.getAppLabel(context, apkPath);

        resources=SpeedUtils.readApkRes(context,apkPath);
        dexClassLoader=SpeedUtils.readDexFile(context,apkPath,dexOutPath);

    }

    public Class<?> getClassById(String keyName){
        Class<?> aClass =null;
        try {
            String string = packageInfo.applicationInfo.metaData.getString(keyName);
            aClass = dexClassLoader.loadClass(string);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG,""+e.getMessage());
        }
        return aClass;
    }

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public DexClassLoader getDexClassLoader() {
        return dexClassLoader;
    }

    public Resources getResources() {
        return resources;
    }

}
