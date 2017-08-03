package com.speed.hotpatch.libs;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;

/**
 *  by liyihang
 *  blog http://sijienet.com/
 */
public interface SpeedHostBaseActivityInterface {

    void init(Activity activity);

    boolean isInit();

    SpeedBaseInterface getBaserProxy(String keyName, String classTag);

    Resources getResources();

    AssetManager getAssets();

    ClassLoader getClassLoader();

    Resources.Theme getTheme();

    PackageInfo getPackageInfo();



}
