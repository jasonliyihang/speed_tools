package com.example.clientdome;

import com.speed.hotpatch.libs.SpeedBaseInterface;
import com.speed.hotpatch.libs.SpeedClientBaseActivity;

/**
 *  by liyihang
 */
public class ClientMainActivity extends SpeedClientBaseActivity {

    @Override
    public SpeedBaseInterface getProxyBase() {
        return new TestClass();
    }


}
