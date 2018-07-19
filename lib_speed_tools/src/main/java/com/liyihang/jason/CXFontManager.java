package com.liyihang.jason;

import android.content.Context;
import android.util.Log;

import com.speed.hotpatch.libs.SpeedUtils;

public class CXFontManager {

    private static CXFontManager manager=null;

    public static CXFontManager getInstance() {
        synchronized (CXFontManager.class) {
            if (manager==null) {
                manager=new CXFontManager();
            }
        }
        return manager;
    }

    private CXFontManager(){
    }

    public static final String KEY_NAME="font_scale_size";
    private float fontScale=0;
    private Context context;

    public float getFontScale() {
        return fontScale;
    }

    public CXFontManager init(Context context){
        this.context=context;
        fontScale = SpeedUtils.getSharedPreferences(context).getFloat(KEY_NAME, 0);
        msg("CXFontManager init fontScale=="+fontScale);
        return this;
    }

    private void msg(String msg){
        Log.i(getClass().getSimpleName(), msg);
    }

    public CXFontManager changeConfig(float s){
        this.fontScale=s;
        SpeedUtils.getSharedPreferences(context).edit().putFloat(KEY_NAME, s).apply();
        msg("CXFontManager changeConfig fontScale=="+fontScale);
        return this;
    }

    public CXFontManager updateUI(){
        CXThemeManager.getInstance().sendUpdateUIAction();
        return this;
    }

    public String getS(int rid){
        return context.getResources().getString(rid);
    }

    public static String getString(int rid){
        return CXFontManager.getInstance().getS(rid);
    }


}
