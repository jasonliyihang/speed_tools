package com.speed.hotpatch.libs;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.IOException;
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
            if (!isPrivateStoragePath(appContext, apkPath)
                    && !SpeedApkSignatureVerifier.isSignedByHost(appContext, apkPath)) {
                Log.e(TAG, "load refused: APK is outside private storage and is not host-signed: " + apkPath);
                return false;
            }
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

    private static boolean isPrivateStoragePath(Context context, String apkPath) {
        try {
            File dataDir = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                    ? context.getDataDir()
                    : new File(context.getApplicationInfo().dataDir);
            File canonicalDataDir = dataDir.getCanonicalFile();
            File canonicalApkPath = new File(apkPath).getCanonicalFile();
            String dataPath = canonicalDataDir.getPath();
            String apkPathValue = canonicalApkPath.getPath();
            return apkPathValue.equals(dataPath)
                    || apkPathValue.startsWith(dataPath + File.separator);
        } catch (IOException | NullPointerException e) {
            Log.w(TAG, "Unable to canonicalize APK path, treating it as external: " + apkPath, e);
            return false;
        }
    }

    @Override
    public SpeedApkHelper get(String keyName) {
        return apkHelperMap.get(keyName);
    }
}
