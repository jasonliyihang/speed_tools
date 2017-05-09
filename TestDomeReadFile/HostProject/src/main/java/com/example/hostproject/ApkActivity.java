package com.example.hostproject;

import com.speed.hotpatch.libs.SpeedHostBaseActivity;

/**
 * Created by user on 2017/5/9.
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
