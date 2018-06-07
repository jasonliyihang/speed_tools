package com.speed.hotpatch.libs;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

/**
 *  by liyihang
 *  blog http://sijienet.com/
 */
public abstract class SpeedClientBaseActivity extends AppCompatActivity {

    private SpeedBaseInterface proxyClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getIntentParm();
        proxyClass.onCreate(savedInstanceState,this);
    }

    private void getIntentParm() {
        try {
            String classTag = getIntent().getStringExtra(SpeedConfig.CLASS_TAG);
            PackageInfo packageInfo2 = SpeedUtils.getPackageInfo2(this, getPackageName());
            String className = packageInfo2.applicationInfo.metaData.getString(classTag);
            proxyClass = (SpeedBaseInterface) getClassLoader().loadClass(className).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (proxyClass==null)
            proxyClass=getProxyBase();
    }

    public abstract SpeedBaseInterface getProxyBase();

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
}
