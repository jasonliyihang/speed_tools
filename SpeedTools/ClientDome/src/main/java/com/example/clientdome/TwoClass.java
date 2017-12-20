package com.example.clientdome;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.speed.hotpatch.libs.SpeedBaseInterfaceImp;

/**
 * Created by user on 2017/5/11.
 */
public class TwoClass extends SpeedBaseInterfaceImp {

    @Override
    public void onCreate(Bundle savedInstanceState, Activity activity) {
        super.onCreate(savedInstanceState, activity);
        TextView textView=new TextView(activity);
        textView.setText("组件内第二个页面");
        activity.setContentView(textView);
    }
}
