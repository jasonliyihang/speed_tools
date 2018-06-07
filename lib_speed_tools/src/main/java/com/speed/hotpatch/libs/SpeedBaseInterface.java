package com.speed.hotpatch.libs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

/**
 *  by liyihang
 *  blog http://sijienet.com/
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

    boolean onTouchEvent(MotionEvent event);

    void onSaveInstanceState(Bundle outState);

}
