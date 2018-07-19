package com.liyihang.jason;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

public class CXBackgroundEnum extends CXThemeEnum {

    public CXBackgroundEnum() {
        super("background");
    }

    @Override
    protected void use(View view, String name) {
            try {
                Drawable drawable = CXThemeManager.getInstance().drawable(name);
                if (drawable==null)
                {
                    view.setBackgroundColor(CXThemeManager.getInstance().color(name));
                }else {
                    if (Build.VERSION.SDK_INT>=16) {
                        view.setBackground(drawable);
                    }else {
                        view.setBackgroundDrawable(drawable);
                    }
                }
            }catch (Exception e){
                msg("CXThemeEnum use err===="+e.getMessage());
                e.printStackTrace();
            }
    }
}
