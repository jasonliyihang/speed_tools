package com.liyihang.jason;

import android.view.View;
import android.widget.TextView;

public class SPTextColorEnum extends SPThemeEnum {

    public SPTextColorEnum() {
        super("textColor");
    }

    @Override
    protected void use(View view, String name) {
        if (!(view instanceof TextView)) {
            err("textColor attribute on non-TextView " + view.getClass().getName() + ", name=" + name, null);
            return;
        }
        int color = SPThemeManager.getInstance().color(name);
        if (color == SPThemeManager.COLOR_UNRESOLVED) {
            return;
        }
        try {
            ((TextView) view).setTextColor(color);
        } catch (RuntimeException e) {
            err("failed to apply textColor " + name + " to " + view.getClass().getName(), e);
        }
    }
}
