package com.speed.hotpatch.libs;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import dalvik.system.DexClassLoader;

/**
 * Created by user on 2017/5/9.
 */
public class SpeedApkHelperInterfaceImp implements SpeedApkHelperInterface {

    public static final String TAG ="SpeedApkHelper";
    public static final String ROOT_FRAGMENT_NAME="root_class";

    private String apkPath;
    private Context ctx;

    private Drawable appIcon;
    private String apkName;

    private PackageInfo packageInfo;
    private DexClassLoader dexClassLoader;
    private Resources resources;


    @Override
    public void init(String apkPath, String dexOutPath, Context context) {
        this.apkPath = apkPath;
        this.ctx = context;

        packageInfo = SpeedUtils.getPackageInfo(context,apkPath);

        appIcon = SpeedUtils.getAppIcon(context, apkPath);
        apkName = (String) SpeedUtils.getAppLabel(context, apkPath);

        resources=SpeedUtils.readApkRes(context,apkPath);
        dexClassLoader=SpeedUtils.readDexFile(context,apkPath,dexOutPath);
    }

    @Override
    public Class<?> getClassById(String keyName) {
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

    @Override
    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    @Override
    public DexClassLoader getDexClassLoader() {
        return dexClassLoader;
    }

    @Override
    public Resources getResources() {
        return resources;
    }
}
