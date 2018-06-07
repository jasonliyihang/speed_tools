package com.example.clientdome2;

import com.speed.hotpatch.libs.SpeedBaseInterface;
import com.speed.hotpatch.libs.SpeedClientBaseActivity;

/**
 *  by liyihang
 *  blog http://sijienet.com/
 */
public class OtherMainActivity extends SpeedClientBaseActivity {

    @Override
    public SpeedBaseInterface getProxyBase() {
        return new OtherClass();
    }
}
