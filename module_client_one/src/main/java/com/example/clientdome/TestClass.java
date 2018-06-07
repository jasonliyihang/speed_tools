package com.example.clientdome;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.forcedome.liyihang.imgutils.ImgUtils;
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

        ImageView imageView= (ImageView) activity.findViewById(R.id.img_view);
        imageView.setVisibility(View.VISIBLE);
        ImgUtils.getInstance(activity).showImg("http://img.my.csdn.net/uploads/201309/01/1378037235_3453.jpg", imageView);

    }
}
