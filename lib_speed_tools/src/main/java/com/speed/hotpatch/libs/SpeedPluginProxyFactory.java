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
        return createFromMetaData(packageInfo, classLoader, metaDataKey, TAG);
    }

    static SpeedBaseInterface createFromMetaData(
            PackageInfo packageInfo,
            ClassLoader classLoader,
            String metaDataKey,
            String logTag) {
        Class<?> proxyClass = getClassByMetaData(packageInfo, classLoader, metaDataKey, logTag);
        if (proxyClass == null) {
            return null;
        }
        return createFromClass(proxyClass, logTag);
    }

    static Class<?> getClassByMetaData(
            PackageInfo packageInfo,
            ClassLoader classLoader,
            String metaDataKey,
            String logTag) {
        if (packageInfo == null || packageInfo.applicationInfo == null || classLoader == null) {
            return null;
        }
        if (metaDataKey == null) {
            metaDataKey = SpeedConfig.ROOT_CLASS_NAME;
        }
        ApplicationInfo appInfo = packageInfo.applicationInfo;
        if (appInfo.metaData == null) {
            Log.e(logTag, "metaData is null for " + packageInfo.packageName);
            return null;
        }
        try {
            String className = appInfo.metaData.getString(metaDataKey);
            if (className == null || className.isEmpty()) {
                Log.e(logTag, "No meta-data entry: " + metaDataKey);
                return null;
            }
            return classLoader.loadClass(className);
        } catch (Exception e) {
            Log.e(logTag, "Failed to load proxy for key=" + metaDataKey, e);
        }
        return null;
    }

    static SpeedBaseInterface createFromClass(Class<?> proxyClass, String logTag) {
        try {
            Object instance = proxyClass.getDeclaredConstructor().newInstance();
            if (instance instanceof SpeedBaseInterface) {
                return (SpeedBaseInterface) instance;
            }
            Log.e(logTag, proxyClass.getName() + " does not implement SpeedBaseInterface");
        } catch (Exception e) {
            Log.e(logTag, "Failed to instantiate " + proxyClass.getName(), e);
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
