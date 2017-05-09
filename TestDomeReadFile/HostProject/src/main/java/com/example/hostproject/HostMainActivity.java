package com.example.hostproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.speed.hotpatch.libs.SpeedApkManager;
import com.speed.hotpatch.libs.SpeedUtils;

public class HostMainActivity extends AppCompatActivity implements Runnable,Handler.Callback {


    public static final String FIRST_APK_KEY="first_apk";
    public static final String TWO_APK_KEY="other_apk";

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_main);

        handler=new Handler(this);
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String s = SpeedUtils.getRootPath(this) + "/ClientDome-debug.apk";
        String dexOutPath="dex_output2";
        SpeedApkManager.getInstance().loadApk(FIRST_APK_KEY, s, dexOutPath, this);

        String s2 = SpeedUtils.getRootPath(this) + "/ClientDome2-debug.apk";
        String dexOutPath2="dex_output3";
        SpeedApkManager.getInstance().loadApk(TWO_APK_KEY, s2, dexOutPath2, this);

        handler.sendEmptyMessage(0x78);
    }

    @Override
    public boolean handleMessage(Message message) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("选择进入界面");
        builder.setPositiveButton("第一个apk界面", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(HostMainActivity.this, ApkActivity.class));
            }
        });
        builder.setNegativeButton("第二个apk界面", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(HostMainActivity.this, OtherApkActivity.class));
            }
        });
        builder.show();
        return false;
    }

}
