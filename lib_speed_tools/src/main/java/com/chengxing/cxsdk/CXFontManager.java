package com.chengxing.cxsdk;

import android.content.Context;

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
        fontScale = CXUtils.getSharedPreferences(context).getFloat(KEY_NAME, 0);
        CXUtils.msg("CXFontManager init fontScale=="+fontScale);
        return this;
    }

    public CXFontManager changeConfig(float s){
        this.fontScale=s;
        CXUtils.getSharedPreferences(context).edit().putFloat(KEY_NAME, s).apply();
        CXUtils.msg("CXFontManager changeConfig fontScale=="+fontScale);
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
