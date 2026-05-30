package com.speed.hotpatch.libs;

import android.content.Context;

/**
 *  by liyihang
 */
public interface SpeedApkManagerInterface {

    void init();

    boolean load(String keyName, String apkPath, String dexOutPath, Context context);

    SpeedApkHelper get(String keyName);

}
