package com.example.clientdome2;

import com.speed.hotpatch.libs.SpeedBaseInterface;
import com.speed.hotpatch.libs.SpeedClientBaseActivity;

/**
 *  by liyihang
 */
public class OtherMainActivity extends SpeedClientBaseActivity {

    @Override
    public SpeedBaseInterface getProxyBase() {
        return new OtherClass();
    }
}
