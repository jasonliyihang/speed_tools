package com.example.hostdome;

import com.speed.hotpatch.libs.SpeedHostBaseActivity;

public class HostMainActivity extends SpeedHostBaseActivity {

    @Override
    public String getApkKeyName() {
        return MyApplication.FIRST_APK_KEY;
    }

    @Override
    public String getClassTag() {
        return null;
    }
}
