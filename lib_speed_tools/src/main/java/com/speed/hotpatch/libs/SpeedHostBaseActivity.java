package com.speed.hotpatch.libs;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.LayoutInflaterCompat;

/**
 * Host Activity that delegates UI/lifecycle to a plugin {@link SpeedBaseInterface} implementation.
 */
public abstract class SpeedHostBaseActivity extends AppCompatActivity {

    private static final String TAG = "SpeedHostBaseActivity";

    private SpeedHostActivityHelper hostActivityHelper;
    private SpeedBaseInterface proxyClass;
    private String apkName;
    private String classTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        readIntentParams();
        hostActivityHelper = new SpeedHostActivityHelper(this);
        proxyClass = hostActivityHelper.getBaseProxy(apkName, classTag);

        SpeedLayoutInflaterFactory inflaterFactory = new SpeedLayoutInflaterFactory();
        inflaterFactory.setHostActivityHelper(hostActivityHelper);
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), inflaterFactory);

        super.onCreate(savedInstanceState);
        if (proxyClass == null) {
            Log.e(TAG, "Plugin proxy is null, apkName=" + apkName + ", classTag=" + classTag);
            Toast.makeText(this,
                    "插件未加载或入口类无效，请先在宿主主页等待插件加载完成",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        SpeedProxyLifecycle.onCreate(proxyClass, savedInstanceState, this);
    }

    private void readIntentParams() {
        apkName = getIntent().getStringExtra(SpeedConfig.APK_NAME);
        classTag = getIntent().getStringExtra(SpeedConfig.CLASS_TAG);
        if (apkName == null) {
            apkName = getApkKeyName();
        }
        if (classTag == null) {
            classTag = getClassTag();
        }
    }

    public abstract String getApkKeyName();

    public abstract String getClassTag();

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

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        SpeedProxyLifecycle.onRestoreInstanceState(proxyClass, savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SpeedProxyLifecycle.onPostCreate(proxyClass, savedInstanceState);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        SpeedProxyLifecycle.onPostResume(proxyClass);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        SpeedProxyLifecycle.onConfigurationChanged(proxyClass, newConfig);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SpeedProxyLifecycle.onRequestPermissionsResult(proxyClass, requestCode, permissions, grantResults);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        SpeedProxyLifecycle.onWindowFocusChanged(proxyClass, hasFocus);
    }

    @Override
    public Resources getResources() {
        if (hostActivityHelper != null && hostActivityHelper.isInit()) {
            Resources pluginRes = hostActivityHelper.getResources();
            if (pluginRes != null) {
                return pluginRes;
            }
        }
        return super.getResources();
    }

    @Override
    public AssetManager getAssets() {
        if (hostActivityHelper != null && hostActivityHelper.isInit()) {
            AssetManager assets = hostActivityHelper.getAssets();
            if (assets != null) {
                return assets;
            }
        }
        return super.getAssets();
    }

    @Override
    public ClassLoader getClassLoader() {
        if (hostActivityHelper != null && hostActivityHelper.isInit()) {
            ClassLoader loader = hostActivityHelper.getClassLoader();
            if (loader != null) {
                return loader;
            }
        }
        return super.getClassLoader();
    }

    @Override
    public Resources.Theme getTheme() {
        if (hostActivityHelper != null && hostActivityHelper.isInit()) {
            Resources.Theme theme = hostActivityHelper.getTheme();
            if (theme != null) {
                return theme;
            }
        }
        return super.getTheme();
    }
}
