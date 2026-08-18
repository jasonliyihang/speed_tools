package com.speed.hotpatch.libs;

import android.util.Log;

public final class SpeedLog {

    private SpeedLog() {
    }

    public static void msg(String tag, String message) {
        Log.i(tag, message);
    }
}
