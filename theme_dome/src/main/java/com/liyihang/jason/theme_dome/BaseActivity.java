package com.liyihang.jason.theme_dome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.chengxing.cxsdk.CXThemeManager;
import com.chengxing.cxsdk.CXUpdateUIListener;

public class BaseActivity extends AppCompatActivity implements CXUpdateUIListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        CXThemeManager.getInstance().registerUpdateUI(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        CXThemeManager.getInstance().unRegisterUpdateUI(this);
        super.onDestroy();
    }

    @Override
    public void updateUI(boolean isFistLoading) {
        // call code update time
        Toast.makeText(this, "layout no refresh ok", Toast.LENGTH_SHORT).show();
    }
}
