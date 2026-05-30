package com.speed.hotpatch.libs;

import android.content.Context;

/**
 * Singleton registry of loaded plugin APK helpers.
 */
public final class SpeedApkManager {

    private static volatile SpeedApkManager instance;
    private final SpeedApkManagerInterfaceImp apkManager;

    private SpeedApkManager() {
        apkManager = new SpeedApkManagerInterfaceImp();
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

    public boolean loadApk(String keyName, String apkPath, String dexOutPath, Context context) {
        return apkManager.load(keyName, apkPath, dexOutPath, context);
    }

    public SpeedApkHelper getHelper(String keyName) {
        return apkManager.get(keyName);
    }

    public boolean isLoaded(String keyName) {
        return apkManager.get(keyName) != null;
    }
}
