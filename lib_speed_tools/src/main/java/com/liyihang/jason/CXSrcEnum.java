package com.liyihang.jason;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

public class CXSrcEnum extends CXThemeEnum {

    public CXSrcEnum() {
        super("src");
    }

    @Override
    protected void use(View view, String name) {
            try {
                Drawable drawable = CXThemeManager.getInstance().drawable(name);
                if (drawable == null) return;
                ((ImageView) view).setImageDrawable(drawable);
            }catch (Exception e){
                msg("CXThemeEnum use err===="+e.getMessage());
                e.printStackTrace();
            }
    }
}
