package com.example.clientdome;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.speed.hotpatch.libs.SpeedBaseInterface;

/**
 * Created by user on 2017/5/2.
 */
public class TestClass implements SpeedBaseInterface {

    private Activity activity;
    private RelativeLayout container;

    @Override
    public void onCreate(Bundle savedInstanceState, Activity activity) {
        this.activity=activity;
        activity.setContentView(R.layout.activity_client_main);
        container= (RelativeLayout) activity.findViewById(R.id.container);
        container.setBackgroundResource(R.mipmap.ic_launcher);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onRestart() {

    }
}
