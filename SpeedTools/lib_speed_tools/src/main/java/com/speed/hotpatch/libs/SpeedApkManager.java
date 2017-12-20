package com.speed.hotpatch.libs;

import android.content.Context;

/**
 *  by liyihang
 *  blog http://sijienet.com/
 */
public class SpeedApkManager {

    private static SpeedApkManager instance = null;
    private SpeedApkManagerInterface apkManagerInterface;

    private SpeedApkManager() {
        apkManagerInterface = (SpeedApkManagerInterface) new SpeedInvocationHandler().bind(SpeedApkManagerInterface.class);
        apkManagerInterface.init();
    }

    public static SpeedApkManager getInstance() {
        if (instance == null) {
            synchronized (SpeedApkManager.class) {
                if (instance == null) {
                    instance = new SpeedApkManager();
                }
            }
        }
        return instance;
    }

    public void loadApk(String keyName, String apkPath, String dexOutPath, Context context) {
        apkManagerInterface.load(keyName,apkPath,dexOutPath,context);
    }

    public SpeedApkHelper getHelper(String keyName) {
        return apkManagerInterface.get(keyName);
    }


}
