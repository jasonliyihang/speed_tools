package com.speed.hotpatch.libs;

import android.content.res.AssetManager;
import android.content.res.Resources;

import androidx.core.view.LayoutInflaterCompat;

/**
 * Host Activity that delegates UI/lifecycle to a plugin {@link SpeedBaseInterface} implementation.
 */
public abstract class SpeedHostBaseActivity extends SpeedProxyBaseActivity {

    private static final String TAG = "SpeedHostBaseActivity";

    private SpeedHostActivityHelper hostActivityHelper;
    private String apkName;
    private String classTag;

    @Override
    protected SpeedBaseInterface resolveProxy() {
        readIntentParams();
        hostActivityHelper = new SpeedHostActivityHelper(this);
        SpeedBaseInterface proxy = hostActivityHelper.getBaseProxy(apkName, classTag);

        SpeedLayoutInflaterFactory inflaterFactory = new SpeedLayoutInflaterFactory();
        inflaterFactory.setHostActivityHelper(hostActivityHelper);
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), inflaterFactory);
        return proxy;
    }

    @Override
    protected String getProxyFailureTag() {
        return TAG;
    }

    @Override
    protected String getProxyFailureLogMessage() {
        return "Plugin proxy is null, apkName=" + apkName + ", classTag=" + classTag;
    }

    @Override
    protected String getProxyFailureMessage() {
        return "插件未加载或入口类无效，请先在宿主主页等待插件加载完成";
    }

    private void readIntentParams() {
        apkName = getIntent().getStringExtra(SpeedConfig.APK_NAME);
        classTag = getIntent().getStringExtra(SpeedConfig.CLASS_TAG);
        if (apkName == null) {
            apkName = getApkKeyName();
        }
        if (classTag == null) {
            classTag = getClassTag();
        }
    }

    public abstract String getApkKeyName();

    public abstract String getClassTag();

    @Override
    public Resources getResources() {
        if (hostActivityHelper != null && hostActivityHelper.isInit()) {
            Resources pluginRes = hostActivityHelper.getResources();
            if (pluginRes != null) {
                return pluginRes;
            }
        }
        return super.getResources();
    }

    @Override
    public AssetManager getAssets() {
        if (hostActivityHelper != null && hostActivityHelper.isInit()) {
            AssetManager assets = hostActivityHelper.getAssets();
            if (assets != null) {
                return assets;
            }
        }
        return super.getAssets();
    }

    @Override
    public ClassLoader getClassLoader() {
        if (hostActivityHelper != null && hostActivityHelper.isInit()) {
            ClassLoader loader = hostActivityHelper.getClassLoader();
            if (loader != null) {
                return loader;
            }
        }
        return super.getClassLoader();
    }

    @Override
    public Resources.Theme getTheme() {
        if (hostActivityHelper != null && hostActivityHelper.isInit()) {
            Resources.Theme theme = hostActivityHelper.getTheme();
            if (theme != null) {
                return theme;
            }
        }
        return super.getTheme();
    }
}
