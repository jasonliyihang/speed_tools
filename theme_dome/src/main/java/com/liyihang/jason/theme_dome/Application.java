package com.liyihang.jason.theme_dome;

import com.liyihang.jason.SPFontManager;
import com.liyihang.jason.SPThemeManager;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // theme font size
        SPFontManager.getInstance().init(this);
        // theme init
        SPThemeManager.getInstance().init(this);

    }
}
