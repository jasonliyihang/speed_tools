package com.speed.hotpatch.libs;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by user on 2017/5/4.
 */
public class SpeedApkManager {

    private HashMap<String, SpeedApkHelper> apkHelperHashMap;

    private static SpeedApkManager instance=null;

    private SpeedApkManager(){
        apkHelperHashMap=new HashMap<>();
    }

    public static SpeedApkManager getInstance(){
        if (instance==null)
        {
            synchronized (SpeedApkManager.class)
            {
                if (instance==null)
                {
                    instance = new SpeedApkManager();
                }
            }
        }
        return instance;
    }

    public void loadApk(String keyName, String apkPath, String dexOutPath, Context context){
        SpeedApkHelper speedApkHelper = new SpeedApkHelper(apkPath, dexOutPath, context);
        apkHelperHashMap.put(keyName, speedApkHelper);
    }

    public SpeedApkHelper getHelper(String keyName){
        return apkHelperHashMap.get(keyName);
    }




}
