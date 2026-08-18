package com.liyihang.jason;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

public class SPBackgroundEnum extends SPThemeEnum {

    public SPBackgroundEnum() {
        super("background");
    }

    @Override
    protected void use(View view, String name) {
        try {
            Drawable drawable = SPThemeManager.getInstance().drawable(name);
            if (drawable == null) {
                int color = SPThemeManager.getInstance().color(name);
                if (color == SPThemeManager.COLOR_UNRESOLVED) {
                    err("background " + name + " resolves to neither drawable nor color", null);
                    return;
                }
                view.setBackgroundColor(color);
                return;
            }
            if (Build.VERSION.SDK_INT >= 16) {
                view.setBackground(drawable);
            } else {
                view.setBackgroundDrawable(drawable);
            }
        } catch (RuntimeException e) {
            err("failed to apply background " + name + " to " + view.getClass().getName(), e);
        }
    }
}
