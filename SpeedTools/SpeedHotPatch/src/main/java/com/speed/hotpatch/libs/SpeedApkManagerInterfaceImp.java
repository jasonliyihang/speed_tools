package com.speed.hotpatch.libs;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by user on 2017/5/8.
 */
public class SpeedApkManagerInterfaceImp implements SpeedApkManagerInterface {

    private HashMap<String, SpeedApkHelper> apkHelperHashMap;

    @Override
    public void init() {
        apkHelperHashMap=new HashMap<>();
    }

    @Override
    public void load(String keyName, String apkPath, String dexOutPath, Context context) {
        SpeedApkHelper speedApkHelper = new SpeedApkHelper(apkPath, dexOutPath, context);
        apkHelperHashMap.put(keyName, speedApkHelper);
    }

    @Override
    public SpeedApkHelper get(String keyName) {
        return apkHelperHashMap.get(keyName);
    }
}
