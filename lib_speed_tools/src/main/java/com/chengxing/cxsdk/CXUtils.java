package com.chengxing.cxsdk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CXUtils {

    private static String key = null;

    public static String getKey() {

        if (key == null) {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
            key = md5(simpleDateFormat.format(new Date()) + "chengxing");
        }
        return key;
    }

    public static void msg(String msg) {
        Log.i("chengxing", msg);
    }

    public static View inflate(Context context, int rid){
        return LayoutInflater.from(context).inflate(rid, null);
    }

    public static void toast(Context context, String msg) {
        CXToastUtils.getInstance().showToastByTime(msg);
    }

    public static void toast( String msg) {
        CXToastUtils.getInstance().showToastByTime(msg);
    }

    public static String getString(Context context, int rid){
        return context.getResources().getString(rid);
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("cx_config", Context.MODE_PRIVATE);
    }

    public static boolean savePhotoToSDCard(Bitmap photoBitmap, String path, String photoName) {
        boolean ret = false;
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File photoFile = new File(path, photoName + ".jpg");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(photoFile);
            if (photoBitmap != null) {
                if (photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)) {
                    fileOutputStream.flush();
                    ret = true;
                }
            }
        } catch (FileNotFoundException e) {
            photoFile.delete();
            e.printStackTrace();
        } catch (IOException e) {
            photoFile.delete();
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public static void setWifiCallMe(Context context, boolean yes){
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(CXConfig.wifi_network_call_me, yes);
        edit.apply();
    }

    public static boolean isWifiCallMe(Context context){
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getBoolean(CXConfig.wifi_network_call_me, true);
    }

    public static void setNewMsgCallMe(Context context, boolean yes){
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(CXConfig.new_msg_network_call_me, yes);
        edit.apply();
    }

    public static boolean isNewMsgCallMe(Context context){
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getBoolean(CXConfig.new_msg_network_call_me, true);
    }




    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static Intent changeIntent(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);

        // Is somebody else trying to intercept our IAP call?
        if (resolveInfos == null || resolveInfos.size() != 1) {
            return null;
        }

        ResolveInfo serviceInfo = resolveInfos.get(0);
        String packageName = serviceInfo.activityInfo.packageName;
        String className = serviceInfo.activityInfo.name;
        ComponentName component = new ComponentName(packageName, className);
        Intent iapIntent = new Intent(intent);
        iapIntent.setComponent(component);
        return iapIntent;
    }

    public static String getClassNameForIntent(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);

        // Is somebody else trying to intercept our IAP call?
        if (resolveInfos == null || resolveInfos.size() != 1) {
            return null;
        }

        ResolveInfo serviceInfo = resolveInfos.get(0);
        return serviceInfo.activityInfo.name;
    }


    public static NotificationChannel initNotificationChannel(Context context, String id, String name) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= 26) {
            channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        return channel;
    }

    public static void runOnUI(Runnable runnable){
        runOnUI(0, runnable);
    }

    public static void runOnUI(int time, Runnable runnable){
        new Handler(Looper.getMainLooper()).postDelayed(runnable, time);
    }

    public static void runThread(Runnable runnable){
        new Thread(runnable).start();
    }

    public static void isOpenSoftInput(Activity context, EditText text, boolean yes) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager==null)
            return;
        if (yes) {
//            context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        } else {
            manager.hideSoftInputFromWindow(text.getWindowToken(), 0);
        }
    }

    public static String getNameByRid(Context context, int rid) {
        String name = null;
        try {
            name = context.getResources().getResourceEntryName(rid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    public static AlertDialog getInputDialog(final EditText et, String title, String okFont, DialogInterface.OnClickListener listener, String cancelFont){
        return new AlertDialog.Builder(et.getContext())
                .setTitle(title)
                .setView(et)
                .setPositiveButton(okFont, listener)
                .setNegativeButton(cancelFont, null).create();
    }






    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            @SuppressLint("MissingPermission") NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }


    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            @SuppressLint("MissingPermission") NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }


    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            @SuppressLint("MissingPermission") NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }








    public static int isMobileNO(String mobiles) {
        String telRegex = "[1][3456789]\\d{9}";
        if (TextUtils.isEmpty(mobiles))
            return -1;
        else
            return mobiles.matches(telRegex)? 0 : -2;
    }


    public static String getStringParm(Map<String,String> parm){
        StringBuilder buffer=new StringBuilder();
        Set<String> strings = parm.keySet();
        for (String string : strings) {
            buffer.append("_").append(parm.get(string));
        }
        return buffer.toString();
    }

    public static String getRequestId(String... arr){
        StringBuilder buffer=new StringBuilder();
        for (String s : arr) {
            buffer.append("_").append(s);
        }
        return buffer.toString();
    }

    public static void show(Dialog dialog){
        if (!dialog.isShowing())
            dialog.show();
    }

    public static void cancel(Dialog dialog){
        if (dialog.isShowing())
            dialog.cancel();
    }









}
