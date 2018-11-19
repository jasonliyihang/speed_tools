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
                if (drawable==null)
                {
                    view.setBackgroundColor(SPThemeManager.getInstance().color(name));
                }else {
                    if (Build.VERSION.SDK_INT>=16) {
                        view.setBackground(drawable);
                    }else {
                        view.setBackgroundDrawable(drawable);
                    }
                }
            }catch (Exception e){
                msg("SPThemeEnum use err===="+e.getMessage());
                e.printStackTrace();
            }
    }
}
