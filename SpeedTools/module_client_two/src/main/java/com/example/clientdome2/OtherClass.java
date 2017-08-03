package com.example.clientdome2;

import android.app.Activity;
import android.os.Bundle;

import com.speed.hotpatch.libs.SpeedBaseInterfaceImp;

/**
 *  by liyihang
 *  blog http://sijienet.com/
 */
public class OtherClass extends SpeedBaseInterfaceImp {

    @Override
    public void onCreate(Bundle savedInstanceState, Activity activity) {
        super.onCreate(savedInstanceState, activity);

        activity.setContentView(R.layout.activity_other_main);
    }
}
