package com.speed.hotpatch.libs;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Resources;

/**
 * Facade for a single loaded plugin APK (dex, resources, manifest meta-data).
 */
public class SpeedApkHelper {

    private final SpeedApkHelperInterfaceImp impl;

    public SpeedApkHelper(String apkPath, String dexOutPath, Context context) {
        impl = new SpeedApkHelperInterfaceImp();
        impl.init(apkPath, dexOutPath, context);
    }

    public boolean isValid() {
        return impl.isValid();
    }

    public Class<?> getClassById(String keyName) {
        return impl.getClassById(keyName);
    }

    public PackageInfo getPackageInfo() {
        return impl.getPackageInfo();
    }

    public ClassLoader getDexClassLoader() {
        return impl.getDexClassLoader();
    }

    public Resources getResources() {
        return impl.getResources();
    }

    public Resources.Theme getTheme() {
        return impl.getTheme();
    }
}
