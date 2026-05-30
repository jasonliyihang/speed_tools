package com.speed.hotpatch.libs;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

/**
 * Resolves plugin proxy classes and plugin-scoped resources for the host Activity.
 */
public class SpeedHostBaseActivityInterfaceImp implements SpeedHostBaseActivityInterface {

    private static final String TAG = "SpeedHostBaseActivity";

    private SpeedApkHelper apkHelper;

    @Override
    public void init(Activity activity) {
        // Reserved for future host shell setup.
    }

    @Override
    public boolean isInit() {
        return apkHelper != null;
    }

    public Class<?> getProxyClass(String keyName, String classTag) {
        if (classTag == null) {
            classTag = SpeedConfig.ROOT_CLASS_NAME;
        }
        apkHelper = SpeedApkManager.getInstance().getHelper(keyName);
        if (apkHelper == null) {
            Log.e(TAG, "Plugin not loaded for key=" + keyName);
            return null;
        }
        return apkHelper.getClassById(classTag);
    }

    @Override
    public SpeedBaseInterface getBaseProxy(String keyName, String classTag) {
        Class<?> proxyClass = getProxyClass(keyName, classTag);
        if (proxyClass == null) {
            return null;
        }
        try {
            Object instance = proxyClass.getDeclaredConstructor().newInstance();
            if (instance instanceof SpeedBaseInterface) {
                return (SpeedBaseInterface) instance;
            }
            Log.e(TAG, proxyClass.getName() + " does not implement SpeedBaseInterface");
        } catch (Exception e) {
            Log.e(TAG, "Failed to instantiate " + proxyClass.getName(), e);
        }
        return null;
    }

    @Override
    @Deprecated
    public SpeedBaseInterface getBaserProxy(String keyName, String classTag) {
        return getBaseProxy(keyName, classTag);
    }

    @Override
    public Resources getResources() {
        return apkHelper != null ? apkHelper.getResources() : null;
    }

    @Override
    public AssetManager getAssets() {
        Resources res = getResources();
        return res != null ? res.getAssets() : null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return apkHelper != null ? apkHelper.getDexClassLoader() : null;
    }

    @Override
    public Resources.Theme getTheme() {
        return apkHelper != null ? apkHelper.getTheme() : null;
    }

    @Override
    public PackageInfo getPackageInfo() {
        return apkHelper != null ? apkHelper.getPackageInfo() : null;
    }
}
