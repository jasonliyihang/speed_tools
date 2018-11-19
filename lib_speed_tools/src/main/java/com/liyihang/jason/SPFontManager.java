package com.liyihang.jason;

import android.content.Context;
import android.util.Log;

import com.speed.hotpatch.libs.SpeedUtils;

public class SPFontManager {

    private static SPFontManager manager=null;

    public static SPFontManager getInstance() {
        synchronized (SPFontManager.class) {
            if (manager==null) {
                manager=new SPFontManager();
            }
        }
        return manager;
    }

    private SPFontManager(){
    }

    public static final String KEY_NAME="font_scale_size";
    private float fontScale=0;
    private Context context;

    public float getFontScale() {
        return fontScale;
    }

    public SPFontManager init(Context context){
        this.context=context;
        fontScale = SpeedUtils.getSharedPreferences(context).getFloat(KEY_NAME, 0);
        msg("SPFontManager init fontScale=="+fontScale);
        return this;
    }

    private void msg(String msg){
        Log.i(getClass().getSimpleName(), msg);
    }

    public SPFontManager changeConfig(float s){
        this.fontScale=s;
        SpeedUtils.getSharedPreferences(context).edit().putFloat(KEY_NAME, s).apply();
        msg("SPFontManager changeConfig fontScale=="+fontScale);
        return this;
    }

    public SPFontManager updateUI(){
        SPThemeManager.getInstance().sendUpdateUIAction();
        return this;
    }

    public String getS(int rid){
        return context.getResources().getString(rid);
    }

    public static String getString(int rid){
        return SPFontManager.getInstance().getS(rid);
    }


}
