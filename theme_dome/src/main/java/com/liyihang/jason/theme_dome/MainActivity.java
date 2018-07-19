package com.liyihang.jason.theme_dome;

import android.os.Bundle;
import android.view.View;

import com.liyihang.jason.CXFontManager;
import com.liyihang.jason.CXThemeManager;

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
            CXThemeManager.getInstance().changeTheme(apk_file_name).sendUpdateUIAction();
        }
        if (view.getId()==R.id.restore)
        {
            CXThemeManager.getInstance().changeTheme(CXThemeManager.DEFAULT_THEMES).sendUpdateUIAction();
        }
        if (view.getId()==R.id.fontSize)
        {
            CXFontManager.getInstance().changeConfig(40).updateUI();
        }

        if (view.getId()==R.id.restoreFont)
        {
            CXFontManager.getInstance().changeConfig(0).updateUI();
        }

    }
}
