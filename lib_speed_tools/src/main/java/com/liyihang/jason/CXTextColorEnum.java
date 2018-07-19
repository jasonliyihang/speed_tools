package com.liyihang.jason;

import android.view.View;
import android.widget.TextView;

public class CXTextColorEnum extends CXThemeEnum {

    public CXTextColorEnum() {
        super("textColor");
    }

    @Override
    protected void use(View view, String name) {
            try {
                int color = CXThemeManager.getInstance().color(name);
                ((TextView) view).setTextColor(color);
            }catch (Exception e){
                msg("CXThemeEnum use err===="+e.getMessage());
                e.printStackTrace();
            }
    }
}
