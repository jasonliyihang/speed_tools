package com.speed.hotpatch.libs;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Resources;

import dalvik.system.DexClassLoader;

/**
 * Created by user on 2017/5/9.
 */
public interface SpeedApkHelperInterface {

    void init(String apkPath, String dexOutPath, Context context);

    Class<?> getClassById(String keyName);

    PackageInfo getPackageInfo();

    DexClassLoader getDexClassLoader();

    Resources getResources();

    Resources.Theme getTheme();

}
