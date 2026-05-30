package com.speed.hotpatch.libs;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.util.Log;

/**
 * Creates {@link SpeedBaseInterface} plugin entry instances from manifest meta-data.
 */
final class SpeedPluginProxyFactory {

    private static final String TAG = "SpeedPluginProxyFactory";

    private SpeedPluginProxyFactory() {
    }

    static SpeedBaseInterface createFromMetaData(
            PackageInfo packageInfo,
            ClassLoader classLoader,
            String metaDataKey) {
        if (packageInfo == null || packageInfo.applicationInfo == null || classLoader == null) {
            return null;
        }
        if (metaDataKey == null) {
            metaDataKey = SpeedConfig.ROOT_CLASS_NAME;
        }
        ApplicationInfo appInfo = packageInfo.applicationInfo;
        if (appInfo.metaData == null) {
            Log.e(TAG, "metaData is null for " + packageInfo.packageName);
            return null;
        }
        try {
            String className = appInfo.metaData.getString(metaDataKey);
            if (className == null || className.isEmpty()) {
                Log.e(TAG, "No meta-data entry: " + metaDataKey);
                return null;
            }
            Class<?> clazz = classLoader.loadClass(className);
            Object instance = clazz.getDeclaredConstructor().newInstance();
            if (instance instanceof SpeedBaseInterface) {
                return (SpeedBaseInterface) instance;
            }
            Log.e(TAG, className + " does not implement SpeedBaseInterface");
        } catch (Exception e) {
            Log.e(TAG, "Failed to create proxy for key=" + metaDataKey, e);
        }
        return null;
    }

    static SpeedBaseInterface createFromInstalledApp(Context context, String metaDataKey) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(),
                    android.content.pm.PackageManager.GET_META_DATA);
            return createFromMetaData(packageInfo, context.getClassLoader(), metaDataKey);
        } catch (Exception e) {
            Log.e(TAG, "Failed to read installed app meta-data", e);
            return null;
        }
    }
}
