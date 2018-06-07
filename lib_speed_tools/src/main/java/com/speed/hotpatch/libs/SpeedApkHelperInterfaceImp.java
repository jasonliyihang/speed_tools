package com.speed.hotpatch.libs;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.speedfragmenthotpatch.R;

import dalvik.system.DexClassLoader;

/**
 *  by liyihang
 *  blog http://sijienet.com/
 */
public class SpeedApkHelperInterfaceImp implements SpeedApkHelperInterface {

    public static final String TAG ="SpeedApkHelper";

    private String apkPath;
    private Context ctx;

    private Drawable appIcon;
    private String apkName;

    private PackageInfo packageInfo;
    private DexClassLoader dexClassLoader;
    private Resources resources;
    private Resources.Theme theme;


    @Override
    public void init(String apkPath, String dexOutPath, Context context) {
        this.apkPath = apkPath;
        this.ctx = context;

        packageInfo = SpeedUtils.getPackageInfo(context,apkPath);

        appIcon = SpeedUtils.getAppIcon(context, apkPath);
        apkName = (String) SpeedUtils.getAppLabel(context, apkPath);

        resources= SpeedUtils.readApkRes(context,apkPath);
        this.theme = resources.newTheme();
        this.theme.applyStyle(R.style.SpeedTheme,false);
        dexClassLoader= SpeedUtils.readDexFile(context,apkPath,dexOutPath);
    }

    @Override
    public Class<?> getClassById(String keyName) {
        Class<?> aClass =null;
        try {
            String string = packageInfo.applicationInfo.metaData.getString(keyName);
            aClass = dexClassLoader.loadClass(string);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG,""+e.getMessage());
        }
        return aClass;
    }

    @Override
    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    @Override
    public DexClassLoader getDexClassLoader() {
        return dexClassLoader;
    }

    @Override
    public Resources getResources() {
        return resources;
    }

    @Override
    public Resources.Theme getTheme() {
        return theme;
    }


}
