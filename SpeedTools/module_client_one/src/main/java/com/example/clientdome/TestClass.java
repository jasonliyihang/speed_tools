package com.example.clientdome;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.speed.hotpatch.libs.SpeedBaseInterfaceImp;
import com.speed.hotpatch.libs.SpeedUtils;

/**
 *  by liyihang
 *  blog http://sijienet.com/
 */
public class TestClass extends SpeedBaseInterfaceImp {

    private Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState, final Activity activity) {
        this.activity=activity;

        activity.setContentView(R.layout.activity_client_main);

        activity.findViewById(R.id.jump).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpeedUtils.goActivity(activity,"first_apk", "two_class");
            }
        });

    }
}
