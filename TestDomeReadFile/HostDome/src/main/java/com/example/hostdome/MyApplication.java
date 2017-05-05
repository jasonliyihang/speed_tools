package com.example.hostdome;

import android.app.Application;

import com.speed.hotpatch.libs.SpeedApkManager;
import com.speed.hotpatch.libs.SpeedUtils;

/**
 * Created by user on 2017/5/4.
 */
public class MyApplication extends Application {

    public static final String FIRST_APK_KEY="one_apk";

    @Override
    public void onCreate() {
        super.onCreate();

        String s = SpeedUtils.getRootPath(this) + "/installApk.apk";
        String dexOutPath="dex2";
        SpeedApkManager.getInstance().loadApk(FIRST_APK_KEY, s, dexOutPath,this);
    }

}
