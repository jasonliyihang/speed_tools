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
            try {
                Drawable drawable = SPThemeManager.getInstance().drawable(name);
                if (drawable == null) return;
                ((ImageView) view).setImageDrawable(drawable);
            }catch (Exception e){
                msg("SPThemeEnum use err===="+e.getMessage());
                e.printStackTrace();
            }
    }
}
