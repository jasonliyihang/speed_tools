package com.speed.hotpatch.libs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Standalone plugin APK Activity that delegates to a {@link SpeedBaseInterface} from manifest meta-data.
 */
public abstract class SpeedClientBaseActivity extends AppCompatActivity {

    private static final String TAG = "SpeedClientBaseActivity";

    private SpeedBaseInterface proxyClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        proxyClass = resolveProxy();
        if (proxyClass == null) {
            Log.e(TAG, "Plugin proxy is null");
            Toast.makeText(this, "插件入口类配置无效", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        SpeedProxyLifecycle.onCreate(proxyClass, savedInstanceState, this);
    }

    private SpeedBaseInterface resolveProxy() {
        String classTag = getIntent().getStringExtra(SpeedConfig.CLASS_TAG);
        SpeedBaseInterface proxy = SpeedPluginProxyFactory.createFromInstalledApp(this, classTag);
        if (proxy == null) {
            proxy = getProxyBase();
        }
        return proxy;
    }

    public abstract SpeedBaseInterface getProxyBase();

    @Override
    protected void onDestroy() {
        SpeedProxyLifecycle.onDestroy(proxyClass);
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SpeedProxyLifecycle.onStart(proxyClass);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SpeedProxyLifecycle.onResume(proxyClass);
    }

    @Override
    protected void onPause() {
        SpeedProxyLifecycle.onPause(proxyClass);
        super.onPause();
    }

    @Override
    protected void onStop() {
        SpeedProxyLifecycle.onStop(proxyClass);
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SpeedProxyLifecycle.onRestart(proxyClass);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        SpeedProxyLifecycle.onNewIntent(proxyClass, intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SpeedProxyLifecycle.onActivityResult(proxyClass, requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        SpeedProxyLifecycle.onBackPressed(proxyClass);
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SpeedProxyLifecycle.onSaveInstanceState(proxyClass, outState);
    }
}
