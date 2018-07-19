package com.liyihang.jason;

import android.util.Log;
import android.view.View;

public abstract class CXThemeEnum {

    private String type;

    public CXThemeEnum(String textColor) {
        type=textColor;
    }

    public String getType() {
        return type;
    }

    protected abstract void use(View view, String name);

    public static void msg(String msg){
        Log.i("theme_enum", msg);
    }

}
