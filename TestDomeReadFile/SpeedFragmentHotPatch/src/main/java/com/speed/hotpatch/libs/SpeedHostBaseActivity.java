package com.speed.hotpatch.libs;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by user on 2017/5/8.
 */
public abstract class SpeedHostBaseActivity extends AppCompatActivity {

    private SpeedHostActivityHelper hostActivityHelper;
    private SpeedBaseInterface proxyClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hostActivityHelper = new SpeedHostActivityHelper(this);
        proxyClass = hostActivityHelper.getBaserProxy(getApkKeyName(), getClassTag());
        proxyClass.onCreate(savedInstanceState,this);
    }

    public abstract String getApkKeyName();
    public abstract String getClassTag();

    @Override
    protected void onDestroy() {
        proxyClass.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        proxyClass.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        proxyClass.onResume();
    }

    @Override
    protected void onPause() {
        proxyClass.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        proxyClass.onStop();
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        proxyClass.onRestart();
    }

    @Override
    public Resources getResources() {
        if (hostActivityHelper != null && hostActivityHelper.isInit()) {
            return hostActivityHelper.getResources();
        }
        return super.getResources();
    }

    @Override
    public AssetManager getAssets() {
        if (hostActivityHelper != null && hostActivityHelper.isInit()) {
            return hostActivityHelper.getAssets();
        }
        return super.getAssets();
    }

    @Override
    public ClassLoader getClassLoader() {
        if (hostActivityHelper != null && hostActivityHelper.isInit()) {
            return hostActivityHelper.getClassLoader();
        }
        return super.getClassLoader();
    }

}
