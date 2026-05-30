package com.speed.hotpatch.libs;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;

/**
 * Bridges host Activity to loaded plugin resources and proxy instances.
 */
public class SpeedHostActivityHelper {

    private final SpeedHostBaseActivityInterfaceImp hostDelegate = new SpeedHostBaseActivityInterfaceImp();

    public SpeedHostActivityHelper(Activity activity) {
        hostDelegate.init(activity);
    }

    public boolean isInit() {
        return hostDelegate.isInit();
    }

    public SpeedBaseInterface getBaseProxy(String keyName, String classTag) {
        return hostDelegate.getBaseProxy(keyName, classTag);
    }

    /** @deprecated Use {@link #getBaseProxy(String, String)}. */
    @Deprecated
    public SpeedBaseInterface getBaserProxy(String keyName, String classTag) {
        return getBaseProxy(keyName, classTag);
    }

    public Resources getResources() {
        return hostDelegate.getResources();
    }

    public AssetManager getAssets() {
        return hostDelegate.getAssets();
    }

    public ClassLoader getClassLoader() {
        return hostDelegate.getClassLoader();
    }

    public Resources.Theme getTheme() {
        return hostDelegate.getTheme();
    }

    public PackageInfo getPackageInfo() {
        return hostDelegate.getPackageInfo();
    }
}
