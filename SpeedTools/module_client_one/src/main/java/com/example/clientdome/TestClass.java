package com.example.clientdome;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.speed.hotpatch.libs.SpeedBaseInterfaceImp;
import com.speed.hotpatch.libs.SpeedConfig;

/**
 * Created by user on 2017/5/2.
 */
public class TestClass extends SpeedBaseInterfaceImp {

    private Activity activity;
    private RelativeLayout container;

    @Override
    public void onCreate(Bundle savedInstanceState, final Activity activity) {
        this.activity=activity;

        activity.setContentView(R.layout.activity_client_main);
        activity.findViewById(R.id.showFont).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(SpeedConfig.ACTIVITY_URL));
                intent.setPackage(activity.getPackageName());
                intent.putExtra(SpeedConfig.APK_NAME,"first_apk");
                intent.putExtra(SpeedConfig.CLASS_TAG, "two_class");
                TestClass.this.activity.startActivity(intent);
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
