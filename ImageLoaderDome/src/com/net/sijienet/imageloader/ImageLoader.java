package com.net.sijienet.imageloader;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by json li on 2017/3/24.
 */
@SuppressLint("NewApi") 
public class ImageLoader {

    public static ImageLoader instance=null;
    
    private final LruCache<String, Bitmap> mCache;
    
    private final String TAG="Imageloader";

    private List<LoadTask> loadTasks=new ArrayList<LoadTask>();
    
    private int defaultReqWidth=720;
    
    private String sdPath="/image_file_cache";
    
    public static ImageLoader getInstance(){
        if (instance==null){
            synchronized (ImageLoader.class){
                if (instance==null){
                    instance=new ImageLoader();
                }
            }
        }
        return instance;
    }
    
    public void init(int defaultReqWitdth , String sdPath){
    	this.sdPath = sdPath;
    	this.defaultReqWidth = defaultReqWitdth;
    }

    private ImageLoader(){
        int cSize = 10*1024*1024;
        Log.i(TAG, "max Memory="+cSize);
        mCache = new LruCache<String, Bitmap>(cSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    void setImgBitmap(ImageView imageView, Bitmap bitmap,int reqWidth, OnImageLoaderListener imageLoaderListener){
    	if (imageLoaderListener!=null) {
			imageLoaderListener.setImageView(imageView, bitmap);
			return;
		}
        imageView.setImageBitmap(bitmap);
    }
    
    public void displayImage(String url, ImageView imageView){
    	displayImage(url, imageView, defaultReqWidth, null);
    }
    
    public void displayImage(String url, ImageView imageView,OnImageLoaderListener imageLoaderListener){
    	displayImage(url, imageView, defaultReqWidth, imageLoaderListener);
    }

    public void displayImage(String url, ImageView imageView, int reqWidth, OnImageLoaderListener imageLoaderListener){
        if (url==null || imageView==null){
            return;
        }
        Bitmap bitmap=getBitmapFromMemoryCache(url, reqWidth);
        if (bitmap !=null){
            setImgBitmap(imageView, bitmap, reqWidth,imageLoaderListener);
        }else {
            LoadTask loadTask=new LoadTask(new WeakReference<ImageView>(imageView), url, reqWidth, imageLoaderListener);
            loadTasks.add(loadTask);
            loadTask.execute(url);
        }
    }

    public void cancelAllTasks() {
        if (loadTasks != null) {
            for (LoadTask task : loadTasks) {
                task.cancel(false);
            }
            Log.i(TAG,"cancel size=="+ loadTasks.size());
            loadTasks.clear();
        }
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key, defaultReqWidth) == null) {
            mCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemoryCache(String key, int reqWidth) {
        Bitmap bitmap=null;
        if (mCache.get(key)!=null)
        {
            return mCache.get(key);
        }
        String path= Environment.getExternalStorageDirectory().getAbsolutePath()+sdPath;
        String fileName = getMd5String(key)+".jpg";
        String allPath = path+"/"+fileName;
        if (new File(allPath).exists())
        {
            return decodeSampledBitmapFromResource(allPath, reqWidth);
        }
        return bitmap;
    }

    class LoadTask extends AsyncTask<String, Void, Bitmap> {

        WeakReference<ImageView> imageView;
        String url;
        int reqWidth;
        OnImageLoaderListener imageLoaderListener;

        public LoadTask(WeakReference<ImageView> imageView, String url,
				int reqWidth, OnImageLoaderListener imageLoaderListener) {
			super();
			this.imageView = imageView;
			this.url = url;
			this.reqWidth = reqWidth;
			this.imageLoaderListener = imageLoaderListener;
		}

		@Override
        protected Bitmap doInBackground(String... arg0) {
            Bitmap bitmap = downloadBitmap(url);
            if (bitmap != null) {
                addBitmapToMemoryCache(url, bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            ImageView imageView = this.imageView.get();
            if (imageView != null && result != null) {
                Log.i(TAG,"set imageView url=="+ url);
                setImgBitmap(imageView, result,reqWidth, imageLoaderListener);
            }
            loadTasks.remove(this);
        }

        Bitmap downloadBitmap(String imageUrl) {
            Bitmap bitmap = null;
            HttpURLConnection con = null;
            FileOutputStream fileOutputStream=null;
            try {
                URL url = new URL(imageUrl);
                con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(2 * 1000);
                con.setReadTimeout(10 * 1000);
                con.setDoInput(true);
                con.setDoOutput(true);
                String path= Environment.getExternalStorageDirectory().getAbsolutePath()+sdPath;
                File pathFile = new File(path);
                if (! pathFile.exists())
                    pathFile.mkdir();
                String fileName = getMd5String(this.url)+".jpg";
                String allPath = path+"/"+fileName;
                fileOutputStream=new FileOutputStream(allPath);
                byte[] arr=new byte[1024];
                int len=-1;
                while( (len=con.getInputStream().read(arr))!=-1 ){
                    fileOutputStream.write(arr,0,len);
                }
                bitmap=decodeSampledBitmapFromResource(allPath, reqWidth);
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG,"image load error=="+ e.getMessage());
            } finally {
            	if (fileOutputStream!=null) {
            		try {
            			fileOutputStream.flush();
    					fileOutputStream.close();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
                if (con != null) {
                    con.disconnect();
                }
            }
            return bitmap;
        }

    }
    
    public interface OnImageLoaderListener{
    	void setImageView(ImageView imageView, Bitmap bitmap);
    }


    public static String getMd5String(String str){
        StringBuilder sb = new StringBuilder(40);
        try {
            MessageDigest md5=MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(str.getBytes());
            for(byte x:bs) {
                if((x & 0xff)>>4 == 0) {
                    sb.append("0").append(Integer.toHexString(x & 0xff));
                } else {
                    sb.append(Integer.toHexString(x & 0xff));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,float roundPx){
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap getOvalBitmap(Bitmap bitmap){
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
    
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth) {
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (width > reqWidth) {
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(String pathName, int reqWidth) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);
    }


}