package com.speed.hotpatch.libs;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Resources;

/**
 *  by liyihang
 */
public interface SpeedApkHelperInterface {

    void init(String apkPath, String dexOutPath, Context context);

    Class<?> getClassById(String keyName);

    PackageInfo getPackageInfo();

    ClassLoader getDexClassLoader();

    Resources getResources();

    Resources.Theme getTheme();

}
