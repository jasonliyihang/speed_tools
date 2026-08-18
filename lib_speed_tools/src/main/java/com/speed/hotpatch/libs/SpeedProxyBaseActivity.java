package com.speed.hotpatch.libs;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity base class that delegates lifecycle callbacks to a plugin proxy.
 */
public abstract class SpeedProxyBaseActivity extends AppCompatActivity {

    private SpeedBaseInterface proxyClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        proxyClass = resolveProxy();
        super.onCreate(savedInstanceState);
        if (proxyClass == null) {
            Log.e(getProxyFailureTag(), getProxyFailureLogMessage());
            Toast.makeText(this, getProxyFailureMessage(), Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        SpeedProxyLifecycle.onCreate(proxyClass, savedInstanceState, this);
    }

    protected abstract SpeedBaseInterface resolveProxy();

    protected String getProxyFailureTag() {
        return "SpeedProxyBaseActivity";
    }

    protected String getProxyFailureLogMessage() {
        return "Plugin proxy is null";
    }

    protected abstract String getProxyFailureMessage();

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
}
