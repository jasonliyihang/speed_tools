package com.speed.hotpatch.libs;

/**
 * Standalone plugin APK Activity that delegates to a {@link SpeedBaseInterface} from manifest meta-data.
 */
public abstract class SpeedClientBaseActivity extends SpeedProxyBaseActivity {

    private static final String TAG = "SpeedClientBaseActivity";

    @Override
    protected SpeedBaseInterface resolveProxy() {
        String classTag = getIntent().getStringExtra(SpeedConfig.CLASS_TAG);
        SpeedBaseInterface proxy = SpeedPluginProxyFactory.createFromInstalledApp(this, classTag);
        if (proxy == null) {
            proxy = getProxyBase();
        }
        return proxy;
    }

    public abstract SpeedBaseInterface getProxyBase();

    @Override
    protected String getProxyFailureTag() {
        return TAG;
    }

    @Override
    protected String getProxyFailureLogMessage() {
        return "Plugin proxy is null";
    }

    @Override
    protected String getProxyFailureMessage() {
        return "插件入口类配置无效";
    }
}
