package com.liyihang.jason.theme_dome;

import android.os.Bundle;
import android.view.View;

import com.liyihang.jason.SPFontManager;
import com.liyihang.jason.SPThemeManager;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static final String apk_file_name="black_theme-debug.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.change)
        {
            SPThemeManager.getInstance().changeTheme(apk_file_name).sendUpdateUIAction();
        }
        if (view.getId()==R.id.restore)
        {
            SPThemeManager.getInstance().changeTheme(SPThemeManager.DEFAULT_THEMES).sendUpdateUIAction();
        }
        if (view.getId()==R.id.fontSize)
        {
            SPFontManager.getInstance().changeConfig(40).updateUI();
        }

        if (view.getId()==R.id.restoreFont)
        {
            SPFontManager.getInstance().changeConfig(0).updateUI();
        }

    }
}
