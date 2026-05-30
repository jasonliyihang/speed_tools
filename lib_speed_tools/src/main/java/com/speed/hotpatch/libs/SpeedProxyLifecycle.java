package com.speed.hotpatch.libs;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

/**
 * Forwards Activity lifecycle callbacks to a plugin proxy when present.
 */
final class SpeedProxyLifecycle {

    private SpeedProxyLifecycle() {
    }

    static void onCreate(SpeedBaseInterface proxy, Bundle state, Activity activity) {
        if (proxy != null) {
            proxy.onCreate(state, activity);
        }
    }

    static void onDestroy(SpeedBaseInterface proxy) {
        if (proxy != null) {
            proxy.onDestroy();
        }
    }

    static void onStart(SpeedBaseInterface proxy) {
        if (proxy != null) {
            proxy.onStart();
        }
    }

    static void onResume(SpeedBaseInterface proxy) {
        if (proxy != null) {
            proxy.onResume();
        }
    }

    static void onPause(SpeedBaseInterface proxy) {
        if (proxy != null) {
            proxy.onPause();
        }
    }

    static void onStop(SpeedBaseInterface proxy) {
        if (proxy != null) {
            proxy.onStop();
        }
    }

    static void onRestart(SpeedBaseInterface proxy) {
        if (proxy != null) {
            proxy.onRestart();
        }
    }

    static void onNewIntent(SpeedBaseInterface proxy, Intent intent) {
        if (proxy != null) {
            proxy.onNewIntent(intent);
        }
    }

    static void onActivityResult(SpeedBaseInterface proxy, int requestCode, int resultCode, Intent data) {
        if (proxy != null) {
            proxy.onActivityResult(requestCode, resultCode, data);
        }
    }

    static void onBackPressed(SpeedBaseInterface proxy) {
        if (proxy != null) {
            proxy.onBackPressed();
        }
    }

    static void onSaveInstanceState(SpeedBaseInterface proxy, Bundle outState) {
        if (proxy != null) {
            proxy.onSaveInstanceState(outState);
        }
    }

    static void onRestoreInstanceState(SpeedBaseInterface proxy, Bundle savedInstanceState) {
        if (proxy != null) {
            proxy.onRestoreInstanceState(savedInstanceState);
        }
    }

    static void onPostCreate(SpeedBaseInterface proxy, Bundle savedInstanceState) {
        if (proxy != null) {
            proxy.onPostCreate(savedInstanceState);
        }
    }

    static void onPostResume(SpeedBaseInterface proxy) {
        if (proxy != null) {
            proxy.onPostResume();
        }
    }

    static void onConfigurationChanged(SpeedBaseInterface proxy, Configuration newConfig) {
        if (proxy != null) {
            proxy.onConfigurationChanged(newConfig);
        }
    }

    static void onRequestPermissionsResult(
            SpeedBaseInterface proxy, int requestCode, String[] permissions, int[] grantResults) {
        if (proxy != null) {
            proxy.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    static void onWindowFocusChanged(SpeedBaseInterface proxy, boolean hasFocus) {
        if (proxy != null) {
            proxy.onWindowFocusChanged(hasFocus);
        }
    }
}
