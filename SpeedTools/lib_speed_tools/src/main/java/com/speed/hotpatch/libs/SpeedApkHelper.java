package com.speed.hotpatch.libs;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Resources;

import dalvik.system.DexClassLoader;

/**
 *  by liyihang
 *  blog http://sijienet.com/
 */
public class SpeedApkHelper {

    SpeedApkHelperInterface apkHelperInterface;

    public SpeedApkHelper(String apkPath, String dexOutPath, Context context) {
        apkHelperInterface= (SpeedApkHelperInterface) new SpeedInvocationHandler().bind(SpeedApkHelperInterface.class);
        apkHelperInterface.init(apkPath,dexOutPath,context);
    }

    public Class<?> getClassById(String keyName){
        return apkHelperInterface.getClassById(keyName);
    }

    public PackageInfo getPackageInfo() {
        return apkHelperInterface.getPackageInfo();
    }

    public DexClassLoader getDexClassLoader() {
        return apkHelperInterface.getDexClassLoader();
    }

    public Resources getResources() {
        return apkHelperInterface.getResources();
    }

    public Resources.Theme getTheme(){
        return apkHelperInterface.getTheme();
    }

}
