package com.speed.hotpatch.libs;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;

/**
 * Created by user on 2017/5/9.
 */
public interface SpeedHostBaseActivityInterface {

    void init(Activity activity);

    boolean isInit();

    SpeedBaseInterface getBaserProxy(String keyName, String classTag);

    Resources getResources();

    AssetManager getAssets();

    ClassLoader getClassLoader();

    Resources.Theme getTheme();



}
