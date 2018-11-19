package com.liyihang.jason;

import android.view.View;

public class SPThemeAttr {
    private String mName;
    private SPThemeEnum themeEnum;

    public SPThemeAttr(String anme, SPThemeEnum themeEnum) {
        this.mName = anme;
        this.themeEnum = themeEnum;
    }


    public void use(View view){
        themeEnum.use(view, mName);
    }

}
