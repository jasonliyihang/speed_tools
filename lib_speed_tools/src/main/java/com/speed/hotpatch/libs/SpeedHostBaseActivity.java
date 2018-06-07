package com.speed.hotpatch.libs;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

/**
 *  by liyihang
 *  blog http://sijienet.com/
 */
public abstract class SpeedHostBaseActivity extends AppCompatActivity {

    private SpeedHostActivityHelper hostActivityHelper;
    private SpeedBaseInterface proxyClass;
    private String apkName;
    private String classTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(),new SpeedLayoutInflaterFactory());
        super.onCreate(savedInstanceState);
        getIntentParm();
        hostActivityHelper = new SpeedHostActivityHelper(this);
        proxyClass = hostActivityHelper.getBaserProxy(apkName, classTag);
        proxyClass.onCreate(savedInstanceState,this);
    }

    private void getIntentParm() {
        apkName = getIntent().getStringExtra(SpeedConfig.APK_NAME);
        classTag=getIntent().getStringExtra(SpeedConfig.CLASS_TAG);
        if (apkName==null)
            apkName = getApkKeyName();

        if (classTag==null)
            classTag = getClassTag();
    }

    public abstract String getApkKeyName();
    public abstract String getClassTag();

    @Override
    protected void onDestroy() {
        proxyClass.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        proxyClass.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        proxyClass.onResume();
    }

    @Override
    protected void onPause() {
        proxyClass.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        proxyClass.onStop();
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        proxyClass.onRestart();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        proxyClass.onNewIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        proxyClass.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onBackPressed() {
        proxyClass.onBackPressed();
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        proxyClass.onSaveInstanceState(outState);
    }

    @Override
    public Resources getResources() {
        if (hostActivityHelper != null && hostActivityHelper.isInit()) {
            return hostActivityHelper.getResources();
        }
        return super.getResources();
    }

    @Override
    public AssetManager getAssets() {
        if (hostActivityHelper != null && hostActivityHelper.isInit()) {
            return hostActivityHelper.getAssets();
        }
        return super.getAssets();
    }

    @Override
    public ClassLoader getClassLoader() {
        if (hostActivityHelper != null && hostActivityHelper.isInit()) {
            return hostActivityHelper.getClassLoader();
        }
        return super.getClassLoader();
    }

    @Override
    public Resources.Theme getTheme() {
        if (hostActivityHelper!=null && hostActivityHelper.isInit())
        {
            return hostActivityHelper.getTheme();
        }
        return super.getTheme();
    }

}
