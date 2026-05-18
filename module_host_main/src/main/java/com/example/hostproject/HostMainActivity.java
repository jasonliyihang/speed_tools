package com.example.hostproject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.speed.hotpatch.libs.SpeedApkManager;
import com.speed.hotpatch.libs.SpeedUtils;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  by liyihang
 */
public class HostMainActivity extends AppCompatActivity implements Runnable,Handler.Callback, View.OnClickListener {


    public static final String FIRST_APK_KEY="first_apk";
    public static final String TWO_APK_KEY="other_apk";
    private static final String EXTERNAL_APK_DIR = "/sdcard/Downloads";
    private static final int MSG_LOAD_SUCCESS = 0x78;
    private static final int MSG_LOAD_FAILED = 0x79;

    private Handler handler;
    private final ExecutorService loaderExecutor = Executors.newSingleThreadExecutor();

    private TextView showFont;
    private ProgressBar progressBar;
    private Button openOneApk;
    private Button openTwoApk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_main);

        showFont= (TextView) findViewById(R.id.show_font);
        progressBar= (ProgressBar) findViewById(R.id.progressbar);
        openOneApk= (Button) findViewById(R.id.open_one_apk);
        openTwoApk= (Button) findViewById(R.id.open_two_apk);

        handler = new Handler(Looper.getMainLooper(), this);
        loaderExecutor.execute(this);
    }

    @Override
    public void run() {
        String firstPluginApkName = "module_client_one-debug.apk";
        String firstDexOutPath="dex_output2";
        File firstPluginApkPath = SpeedUtils.getNativeApkPathByDir(EXTERNAL_APK_DIR, firstPluginApkName);
        if (firstPluginApkPath == null) {
            firstPluginApkPath = SpeedUtils.getNativeApkPath(getApplicationContext(), firstPluginApkName);
        }
        if (firstPluginApkPath == null) {
            handler.sendEmptyMessage(MSG_LOAD_FAILED);
            return;
        }
        SpeedApkManager.getInstance().loadApk(FIRST_APK_KEY, firstPluginApkPath.getAbsolutePath(), firstDexOutPath, this);

        String secondPluginApkName = "module_client_two-debug.apk";
        String secondDexOutPath="dex_output3";
        File secondPluginApkPath = SpeedUtils.getNativeApkPathByDir(EXTERNAL_APK_DIR, secondPluginApkName);
        if (secondPluginApkPath == null) {
            secondPluginApkPath = SpeedUtils.getNativeApkPath(getApplicationContext(), secondPluginApkName);
        }
        if (secondPluginApkPath == null) {
            handler.sendEmptyMessage(MSG_LOAD_FAILED);
            return;
        }
        SpeedApkManager.getInstance().loadApk(TWO_APK_KEY, secondPluginApkPath.getAbsolutePath(), secondDexOutPath, this);

        handler.sendEmptyMessage(MSG_LOAD_SUCCESS);
    }

    @Override
    public boolean handleMessage(Message message) {
        if (message.what == MSG_LOAD_FAILED) {
            showFont.setText("插件apk加载失败，请检查 /sdcard/Downloads 或 assets 资源");
            progressBar.setVisibility(View.GONE);
            return true;
        }
        if (message.what != MSG_LOAD_SUCCESS) {
            return true;
        }
        showFont.setText("当前是主宿主apk\n插件apk加载完毕");
        progressBar.setVisibility(View.GONE);
        openOneApk.setVisibility(View.VISIBLE);
        openTwoApk.setVisibility(View.VISIBLE);
        openOneApk.setOnClickListener(this);
        openTwoApk.setOnClickListener(this);
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loaderExecutor.shutdownNow();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.open_one_apk)
        {
            SpeedUtils.goActivity(this, FIRST_APK_KEY, null);
        }
        if (v.getId()==R.id.open_two_apk)
        {
            SpeedUtils.goActivity(this, TWO_APK_KEY, null);
        }
    }
}
