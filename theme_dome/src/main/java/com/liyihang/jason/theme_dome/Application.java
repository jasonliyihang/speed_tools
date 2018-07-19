package com.liyihang.jason.theme_dome;

import com.liyihang.jason.CXFontManager;
import com.liyihang.jason.CXThemeManager;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // theme font size
        CXFontManager.getInstance().init(this);
        // theme init
        CXThemeManager.getInstance().init(this);

    }
}
