package com.example.clientdome2;

import android.app.Activity;
import android.os.Bundle;

import com.speed.hotpatch.libs.SpeedBaseInterfaceImp;

/**
 * Created by user on 2017/5/9.
 */
public class OtherClass extends SpeedBaseInterfaceImp {

    @Override
    public void onCreate(Bundle savedInstanceState, Activity activity) {
        super.onCreate(savedInstanceState, activity);
        activity.setContentView(R.layout.activity_other_main);
    }
}
