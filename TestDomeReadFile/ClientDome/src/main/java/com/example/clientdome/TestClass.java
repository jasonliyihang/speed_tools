package com.example.clientdome;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.speed.hotpatch.libs.SpeedBaseInterfaceImp;

/**
 * Created by user on 2017/5/2.
 */
public class TestClass extends SpeedBaseInterfaceImp {

    private Activity activity;
    private RelativeLayout container;

    @Override
    public void onCreate(Bundle savedInstanceState, Activity activity) {
        this.activity=activity;

        activity.setContentView(R.layout.activity_client_main);


    }

}
