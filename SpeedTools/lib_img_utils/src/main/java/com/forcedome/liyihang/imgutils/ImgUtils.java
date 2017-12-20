package com.forcedome.liyihang.imgutils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * 作者： 李一航
 * 创建时间 ： 17-8-3
 */

public class ImgUtils {

    public static final String tag="ImgUtils";

    private static ImgUtils instance;

    private ImgUtils(Context context) {
        init(context);
    }

    public static ImgUtils getInstance(Context context) {
        if (instance == null) {
            synchronized (ImgUtils.class) {
                if (instance == null) {
                    instance=new ImgUtils(context);
                }
            }
        }
        return instance;
    }

    public void init(Context context){
        Log.i(tag, "ImageLoader init");
        ImageLoaderConfiguration init=ImageLoaderConfiguration.createDefault(context);
        ImageLoader.getInstance().init(init);
    }

    public void showImg(String url, ImageView imageView){
        ImageLoader.getInstance().displayImage(url, imageView);
    }

}
