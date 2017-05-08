package com.speed.hotpatch.libs;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.widget.LinearLayout;

/**
 * Created by user on 2017/5/5.
 */
public class SpeedHostActivityHelper {

    private Activity activity;
    private SpeedApkHelper apkHelper;

    public SpeedHostActivityHelper(Activity activity) {
        this.activity = activity;

        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setId(android.R.id.primary);

        activity.setContentView(linearLayout);
    }

    public boolean isInit(){
        if (apkHelper!=null)
            return true;
        return false;
    }

    public Class<?> getProxy(String keyName, String classTag){
        if (classTag==null)
        {
            classTag=SpeedApkHelper.ROOT_FRAGMENT_NAME;
        }
        apkHelper = SpeedApkManager.getInstance().getHelper(keyName);
        Class<?> classById = apkHelper.getClassById(classTag);
        return classById;
    }

    public SpeedBaseInterface getBaserProxy(String keyName, String classTag){
        Class<?> proxy = getProxy(keyName, classTag);
        SpeedBaseInterface o =null;
        try {
            o = (SpeedBaseInterface) proxy.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }

    public Resources getResources(){
        return apkHelper.getResources();
    }

    public AssetManager getAssets() {
        return apkHelper.getResources().getAssets();
    }

    public ClassLoader getClassLoader() {
        return apkHelper.getDexClassLoader();
    }


}
