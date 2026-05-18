package com.speed.hotpatch.libs;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.Intent;
import android.os.Bundle;

/**
 *  by liyihang
 */
public interface SpeedBaseInterface {

    void onCreate(Bundle savedInstanceState, Activity activity);

    void onDestroy();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onRestart();

    void onNewIntent(Intent intent);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onBackPressed();

    void onSaveInstanceState(Bundle outState);

    void onRestoreInstanceState(Bundle savedInstanceState);

    void onPostCreate(Bundle savedInstanceState);

    void onPostResume();

    void onConfigurationChanged(Configuration newConfig);

    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

    void onWindowFocusChanged(boolean hasFocus);

}
