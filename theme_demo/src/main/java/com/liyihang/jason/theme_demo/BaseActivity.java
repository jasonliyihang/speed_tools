package com.liyihang.jason.theme_demo;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.liyihang.jason.SPThemeManager;
import com.liyihang.jason.SPUpdateUIListener;

public class BaseActivity extends AppCompatActivity implements SPUpdateUIListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SPThemeManager.getInstance().registerUpdateUI(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        SPThemeManager.getInstance().unRegisterUpdateUI(this);
        super.onDestroy();
    }

    @Override
    public void updateUI(boolean isFistLoading) {
        // call code update time
        Toast.makeText(this, "layout no refresh ok", Toast.LENGTH_SHORT).show();
    }
}
