package com.speed.hotpatch.libs;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;

/**
 *  by liyihang
 *  blog http://sijienet.com/
 */
public class SpeedHostActivityHelper {

    SpeedHostBaseActivityInterface hostBaseActivityInterface;

    public SpeedHostActivityHelper(Activity activity) {
        hostBaseActivityInterface= (SpeedHostBaseActivityInterface) new SpeedInvocationHandler().bind(SpeedHostBaseActivityInterface.class);
        hostBaseActivityInterface.init(activity);
    }

    public boolean isInit(){
        return hostBaseActivityInterface.isInit();
    }

    public SpeedBaseInterface getBaserProxy(String keyName, String classTag){
        return hostBaseActivityInterface.getBaserProxy(keyName, classTag);
    }

    public Resources getResources(){
        return hostBaseActivityInterface.getResources();
    }

    public AssetManager getAssets() {
        return hostBaseActivityInterface.getAssets();
    }

    public ClassLoader getClassLoader() {
        return hostBaseActivityInterface.getClassLoader();
    }

    public Resources.Theme getTheme(){
        return hostBaseActivityInterface.getTheme();
    }

    public PackageInfo getPackageInfo(){
        return hostBaseActivityInterface.getPackageInfo();
    }


}
