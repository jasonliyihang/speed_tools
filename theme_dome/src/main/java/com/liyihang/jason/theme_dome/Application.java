package com.liyihang.jason.theme_dome;

import com.chengxing.cxsdk.CXFontManager;
import com.chengxing.cxsdk.CXThemeManager;
import com.chengxing.cxsdk.CXToastUtils;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // theme font size
        CXFontManager.getInstance().init(this);
        // theme init
        CXThemeManager.getInstance().init(this);

        //toast tools init
        CXToastUtils.getInstance().init(this);
    }
}
