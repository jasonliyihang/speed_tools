package com.liyihang.jason;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

public class SPSrcEnum extends SPThemeEnum {

    public SPSrcEnum() {
        super("src");
    }

    @Override
    protected void use(View view, String name) {
        if (!(view instanceof ImageView)) {
            err("src attribute on non-ImageView " + view.getClass().getName() + ", name=" + name, null);
            return;
        }
        Drawable drawable = SPThemeManager.getInstance().drawable(name);
        if (drawable == null) {
            return;
        }
        try {
            ((ImageView) view).setImageDrawable(drawable);
        } catch (RuntimeException e) {
            err("failed to apply src " + name + " to " + view.getClass().getName(), e);
        }
    }
}
