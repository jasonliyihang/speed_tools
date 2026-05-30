package com.speed.hotpatch.libs;

import android.content.Context;
import android.util.Log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe store of loaded plugin helpers.
 */
public class SpeedApkManagerInterfaceImp implements SpeedApkManagerInterface {

    private static final String TAG = "SpeedApkManager";

    private final Map<String, SpeedApkHelper> apkHelperMap = new ConcurrentHashMap<>();

    @Override
    public void init() {
        // no-op; map is initialized at field declaration
    }

    @Override
    public boolean load(String keyName, String apkPath, String dexOutPath, Context context) {
        if (keyName == null || apkPath == null || dexOutPath == null || context == null) {
            Log.e(TAG, "load: invalid arguments");
            return false;
        }
        try {
            Context appContext = context.getApplicationContext();
            SpeedApkHelper helper = new SpeedApkHelper(apkPath, dexOutPath, appContext);
            if (!helper.isValid()) {
                Log.e(TAG, "load failed validation key=" + keyName + " path=" + apkPath);
                return false;
            }
            apkHelperMap.put(keyName, helper);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "load failed key=" + keyName, e);
            return false;
        }
    }

    @Override
    public SpeedApkHelper get(String keyName) {
        return apkHelperMap.get(keyName);
    }
}
