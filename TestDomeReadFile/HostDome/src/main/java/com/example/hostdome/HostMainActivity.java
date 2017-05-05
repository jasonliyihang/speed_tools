package com.example.hostdome;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.speed.hotpatch.libs.SpeedApkHelper;
import com.speed.hotpatch.libs.SpeedApkManager;
import com.speed.hotpatch.libs.SpeedHostActivityHelper;

public class HostMainActivity extends AppCompatActivity {

    private SpeedHostActivityHelper hostActivityHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hostActivityHelper = new SpeedHostActivityHelper(this, SpeedApkManager.getInstance(), getSupportFragmentManager());

        hostActivityHelper.replaceFragment(MyApplication.FIRST_APK_KEY, null);
    }

    @Override
    public Resources getResources() {
        if ( hostActivityHelper!=null &&  hostActivityHelper.isInit())
            return hostActivityHelper.getResources();
        return super.getResources();
    }

    @Override
    public AssetManager getAssets() {
        if ( hostActivityHelper!=null &&hostActivityHelper.isInit())
            return hostActivityHelper.getAssets();
        return super.getAssets();
    }

    @Override
    public ClassLoader getClassLoader() {
        if ( hostActivityHelper!=null &&hostActivityHelper.isInit())
            return hostActivityHelper.getClassLoader();
        return super.getClassLoader();
    }

}
