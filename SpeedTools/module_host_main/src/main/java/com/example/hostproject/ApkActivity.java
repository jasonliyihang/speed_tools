package com.example.hostproject;

import com.speed.hotpatch.libs.SpeedHostBaseActivity;

/**
 *  by liyihang
 *  blog http://sijienet.com/
 */
public class ApkActivity extends SpeedHostBaseActivity {


    @Override
    public String getApkKeyName() {
        return HostMainActivity.FIRST_APK_KEY;
    }

    @Override
    public String getClassTag() {
        return null;
    }
}
