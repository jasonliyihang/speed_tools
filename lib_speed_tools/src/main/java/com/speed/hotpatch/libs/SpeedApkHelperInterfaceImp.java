package com.speed.hotpatch.libs;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.util.Log;

import com.example.speedfragmenthotpatch.R;

/**
 * Loads dex/resources for one plugin APK path.
 */
public class SpeedApkHelperInterfaceImp implements SpeedApkHelperInterface {

    private static final String TAG = "SpeedApkHelper";

    private String apkPath;
    private PackageInfo packageInfo;
    private ClassLoader dexClassLoader;
    private Resources resources;
    private Resources.Theme theme;

    @Override
    public void init(String apkPath, String dexOutPath, Context context) {
        this.apkPath = apkPath;
        Context appContext = context.getApplicationContext();

        packageInfo = SpeedUtils.getPackageInfo(appContext, apkPath);
        resources = SpeedUtils.createResourcesFromApk(appContext, apkPath);
        dexClassLoader = SpeedUtils.readDexFile(appContext, apkPath, dexOutPath);

        if (resources != null) {
            theme = resources.newTheme();
            theme.applyStyle(R.style.SpeedTheme, false);
        }
    }

    public boolean isValid() {
        return packageInfo != null
                && packageInfo.applicationInfo != null
                && dexClassLoader != null
                && resources != null
                && theme != null;
    }

    @Override
    public Class<?> getClassById(String keyName) {
        if (!isValid()) {
            Log.e(TAG, "getClassById: helper not valid, apkPath=" + apkPath);
            return null;
        }
        if (packageInfo.applicationInfo.metaData == null) {
            Log.e(TAG, "getClassById: metaData is null, apkPath=" + apkPath);
            return null;
        }
        try {
            String className = packageInfo.applicationInfo.metaData.getString(keyName);
            if (className == null || className.isEmpty()) {
                Log.e(TAG, "getClassById: no meta-data for key=" + keyName);
                return null;
            }
            return dexClassLoader.loadClass(className);
        } catch (Exception e) {
            Log.e(TAG, "getClassById failed key=" + keyName, e);
            return null;
        }
    }

    @Override
    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    @Override
    public ClassLoader getDexClassLoader() {
        return dexClassLoader;
    }

    @Override
    public Resources getResources() {
        return resources;
    }

    @Override
    public Resources.Theme getTheme() {
        return theme;
    }
}
