package com.liyihang.jason;

import android.util.Log;
import android.view.View;

public abstract class SPThemeEnum {

    static final String TAG = "theme_enum";

    private String type;

    public SPThemeEnum(String textColor) {
        type=textColor;
    }

    public String getType() {
        return type;
    }

    protected abstract void use(View view, String name);

    public static void msg(String msg){
        Log.i(TAG, msg);
    }

    static void err(String msg, Throwable t){
        Log.e(TAG, msg, t);
    }

}
