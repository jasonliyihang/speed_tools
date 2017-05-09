package com.speed.hotpatch.libs;

import android.content.Context;

/**
 * Created by user on 2017/5/8.
 */
public interface SpeedApkManagerInterface {

    void init();

    void load(String keyName, String apkPath, String dexOutPath, Context context);

    SpeedApkHelper get(String keyName);

}
