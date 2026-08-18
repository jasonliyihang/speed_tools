package com.liyihang.jason;

import android.view.View;

import com.speed.hotpatch.libs.SpeedLog;

public abstract class SPThemeEnum {

    private String type;

    public SPThemeEnum(String textColor) {
        type=textColor;
    }

    public String getType() {
        return type;
    }

    protected final void apply(View view, String name) {
        try {
            use(view, name);
        } catch (Exception e) {
            msg("SPThemeEnum use err====" + e.getMessage());
            e.printStackTrace();
        }
    }

    protected abstract void use(View view, String name);

    public static void msg(String msg) {
        SpeedLog.msg("theme_enum", msg);
    }

}
