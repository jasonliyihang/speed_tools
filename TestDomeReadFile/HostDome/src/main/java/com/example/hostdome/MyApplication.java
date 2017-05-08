package com.example.hostdome;

import android.app.Application;
import android.os.Handler;

import com.speed.hotpatch.libs.SpeedApkManager;
import com.speed.hotpatch.libs.SpeedUtils;

/**
 * Created by user on 2017/5/4.
 */
public class MyApplication extends Application {

    public static final String FIRST_APK_KEY="first_apk";

    @Override
    public void onCreate() {
        super.onCreate();

        String s = SpeedUtils.getRootPath(this) + "/ClientDome-debug.apk";
        String dexOutPath="dex_output2";
        SpeedApkManager.getInstance().loadApk(FIRST_APK_KEY, s, dexOutPath, this);
    }
}
