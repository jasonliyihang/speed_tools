package com.chengxing.cxsdk;

import android.view.View;

public class CXThemeAttr {
    private String mName;
    private CXThemeEnum themeEnum;

    public CXThemeAttr(String anme, CXThemeEnum themeEnum) {
        this.mName = anme;
        this.themeEnum = themeEnum;
    }


    public void use(View view){
        themeEnum.use(view, mName);
    }

}
