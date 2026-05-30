package com.example.hostproject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.speed.hotpatch.libs.SpeedApkManager;
import com.speed.hotpatch.libs.SpeedUtils;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Host demo: loads plugin APKs then opens them via {@link ApkActivity}.
 */
public class HostMainActivity extends AppCompatActivity implements Handler.Callback, View.OnClickListener {

    public static final String FIRST_APK_KEY = "first_apk";
    public static final String TWO_APK_KEY = "other_apk";

    private static final String EXTERNAL_APK_DIR = "/sdcard/Downloads";
    private static final String FIRST_PLUGIN_APK = "module_client_one-debug.apk";
    private static final String SECOND_PLUGIN_APK = "module_client_two-debug.apk";
    private static final int MSG_LOAD_SUCCESS = 0x78;
    private static final int MSG_LOAD_FAILED = 0x79;

    private final ExecutorService loaderExecutor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper(), this);

    private TextView showFont;
    private ProgressBar progressBar;
    private Button openOneApk;
    private Button openTwoApk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_main);

        showFont = findViewById(R.id.show_font);
        progressBar = findViewById(R.id.progressbar);
        openOneApk = findViewById(R.id.open_one_apk);
        openTwoApk = findViewById(R.id.open_two_apk);

        loaderExecutor.execute(this::loadPluginsOnBackground);
    }

    private void loadPluginsOnBackground() {
        boolean ok = loadPlugin(FIRST_APK_KEY, FIRST_PLUGIN_APK, "dex_output2")
                && loadPlugin(TWO_APK_KEY, SECOND_PLUGIN_APK, "dex_output3");
        mainHandler.sendEmptyMessage(ok ? MSG_LOAD_SUCCESS : MSG_LOAD_FAILED);
    }

    private boolean loadPlugin(String key, String assetFileName, String dexOutKey) {
        File apkFile = SpeedUtils.resolvePluginApk(getApplicationContext(), EXTERNAL_APK_DIR, assetFileName);
        if (apkFile == null) {
            return false;
        }
        return SpeedApkManager.getInstance().loadApk(
                key, apkFile.getAbsolutePath(), dexOutKey, getApplicationContext());
    }

    @Override
    public boolean handleMessage(@NonNull Message message) {
        if (message.what == MSG_LOAD_FAILED) {
            showFont.setText("插件 apk 加载失败，请检查 /sdcard/Downloads 或 assets 资源");
            progressBar.setVisibility(View.GONE);
            return true;
        }
        if (message.what != MSG_LOAD_SUCCESS) {
            return true;
        }
        showFont.setText("当前是主宿主 apk\n插件 apk 加载完毕");
        progressBar.setVisibility(View.GONE);
        openOneApk.setVisibility(View.VISIBLE);
        openTwoApk.setVisibility(View.VISIBLE);
        openOneApk.setOnClickListener(this);
        openTwoApk.setOnClickListener(this);
        return true;
    }

    @Override
    protected void onDestroy() {
        loaderExecutor.shutdownNow();
        mainHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.open_one_apk) {
            SpeedUtils.goActivity(this, FIRST_APK_KEY, null);
        } else if (id == R.id.open_two_apk) {
            SpeedUtils.goActivity(this, TWO_APK_KEY, null);
        }
    }
}
