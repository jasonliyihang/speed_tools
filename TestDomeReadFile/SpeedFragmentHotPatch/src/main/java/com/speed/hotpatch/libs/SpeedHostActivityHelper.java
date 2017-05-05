package com.speed.hotpatch.libs;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.v4.app.FragmentManager;
import android.widget.LinearLayout;

/**
 * Created by user on 2017/5/5.
 */
public class SpeedHostActivityHelper {

    private SpeedApkManager apkManager;
    private SpeedApkHelper apkHelper;
    private Activity activity;
    private FragmentManager fragmentManager;

    public SpeedHostActivityHelper(Activity activity, SpeedApkManager apkManager, FragmentManager fragmentManager) {
        this.apkManager=apkManager;
        this.activity = activity;
        this.fragmentManager=fragmentManager;

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

    public void replaceFragment(String keyName, String fragmentTag){
        if (fragmentTag==null)
        {
            fragmentTag=SpeedApkHelper.ROOT_FRAGMENT_NAME;
        }
        apkHelper = SpeedApkManager.getInstance().getHelper(keyName);
        fragmentManager.beginTransaction().replace(android.R.id.primary, apkHelper.getFragmentById(fragmentTag)).commit();
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
