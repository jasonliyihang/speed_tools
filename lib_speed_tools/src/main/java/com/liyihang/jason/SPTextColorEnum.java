package com.liyihang.jason;

import android.view.View;
import android.widget.TextView;

public class SPTextColorEnum extends SPThemeEnum {

    public SPTextColorEnum() {
        super("textColor");
    }

    @Override
    protected void use(View view, String name) {
            try {
                int color = SPThemeManager.getInstance().color(name);
                ((TextView) view).setTextColor(color);
            }catch (Exception e){
                msg("SPThemeEnum use err===="+e.getMessage());
                e.printStackTrace();
            }
    }
}
