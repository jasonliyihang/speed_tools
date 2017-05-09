package com.speed.hotpatch.libs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by user on 2017/5/8.
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

}
