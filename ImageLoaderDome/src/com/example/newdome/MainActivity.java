package com.example.newdome;

import com.net.sijienet.imageloader.ImageLoader;
import com.net.sijienet.imageloader.ImageLoader.OnImageLoaderListener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;



/**
 * 
 * @author  json liyihang
 *
 */
public class MainActivity extends Activity {

	ListView show;

	int typeId = 0;

	@Override
	protected void onDestroy() {
		ImageLoader.getInstance().cancelAllTasks();
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 初始化默认最大宽度和保存文件夹 可以放到 application onCreate 方法中
		ImageLoader.getInstance().init(750, "/sijienet_com");

		show = (ListView) findViewById(R.id.show);
		show.setAdapter(baseAdapter);

	}

	BaseAdapter baseAdapter = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView = new ImageView(MainActivity.this);
			if (typeId == 0) {
				ImageLoader.getInstance().displayImage(imageUrls[position],
						imageView);
			}
			if (typeId == 1) {
				ImageLoader.getInstance().displayImage(imageUrls[position],
						imageView, new OnImageLoaderListener() {
							@Override
							public void setImageView(ImageView imageView,
									Bitmap bitmap) {
								imageView.setImageBitmap(ImageLoader
										.getOvalBitmap(bitmap));
							}
						});
			}
			if (typeId == 2) {
				ImageLoader.getInstance().displayImage(imageUrls[position],
						imageView, new OnImageLoaderListener() {
							@Override
							public void setImageView(ImageView imageView,
									Bitmap bitmap) {
								imageView.setImageBitmap(ImageLoader
										.getRoundedCornerBitmap(bitmap, 100));
							}
						});
			}
			if (typeId == 3) {
				ImageLoader.getInstance().displayImage(imageUrls[position],
						imageView, 250, null);
			}
			return imageView;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public int getCount() {
			return imageUrls.length;
		}
	};

	public void mClick(View view) {
		if (view.getId() == R.id.one) {
			typeId = 0;
			baseAdapter.notifyDataSetChanged();
		}
		if (view.getId() == R.id.two) {
			typeId = 1;
			baseAdapter.notifyDataSetChanged();
		}
		if (view.getId() == R.id.three) {
			typeId = 2;
			baseAdapter.notifyDataSetChanged();
		}
		if (view.getId() == R.id.four) {
			typeId = 3;
			baseAdapter.notifyDataSetChanged();
		}
	}

	public final static String[] imageUrls = new String[] {
			"http://img.my.csdn.net/uploads/201309/01/1378037235_3453.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037235_9280.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037234_3539.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037234_6318.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037194_2965.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037193_1687.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037193_1286.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037192_8379.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037178_9374.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037177_1254.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037177_6203.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037152_6352.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037151_9565.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037151_7904.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037148_7104.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037129_8825.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037128_5291.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037128_3531.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037127_1085.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037095_7515.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037094_8001.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037093_7168.jpg",
			"http://img.my.csdn.net/uploads/201309/01/1378037091_4950.jpg",
			"http://img.my.csdn.net/uploads/201308/31/1377949643_6410.jpg",
			"http://img.my.csdn.net/uploads/201308/31/1377949642_6939.jpg",
			"http://img.my.csdn.net/uploads/201308/31/1377949630_4505.jpg",
			"http://img.my.csdn.net/uploads/201308/31/1377949630_4593.jpg",
			"http://img.my.csdn.net/uploads/201308/31/1377949629_7309.jpg",
			"http://img.my.csdn.net/uploads/201308/31/1377949629_8247.jpg",
			"http://img.my.csdn.net/uploads/201308/31/1377949615_1986.jpg",
			"http://img.my.csdn.net/uploads/201308/31/1377949614_8482.jpg",
			"http://img.my.csdn.net/uploads/201308/31/1377949614_3743.jpg",
			"http://img.my.csdn.net/uploads/201308/31/1377949614_4199.jpg",
			"http://img.my.csdn.net/uploads/201308/31/1377949599_3416.jpg",
			"http://img.my.csdn.net/uploads/201308/31/1377949599_5269.jpg",
			"http://img.my.csdn.net/uploads/201308/31/1377949598_7858.jpg",
			"http://img.my.csdn.net/uploads/201308/31/1377949598_9982.jpg",
			"http://img.my.csdn.net/uploads/201308/31/1377949578_2770.jpg",
			"http://img.my.csdn.net/uploads/201308/31/1377949578_8744.jpg",
			"http://img.my.csdn.net/uploads/201308/31/1377949577_5210.jpg",
			"http://img.my.csdn.net/uploads/201308/31/1377949577_1998.jpg",
			"http://img.my.csdn.net/uploads/201308/31/1377949482_8813.jpg",
			"http://img.my.csdn.net/uploads/201308/31/1377949481_6577.jpg",
			"http://img.my.csdn.net/uploads/201308/31/1377949480_4490.jpg",
			"http://img.my.csdn.net/uploads/201308/31/1377949455_6792.jpg",
			"http://img.my.csdn.net/uploads/201308/31/1377949455_6345.jpg",
			"http://img.my.csdn.net/uploads/201308/31/1377949442_4553.jpg",
			"http://img.my.csdn.net/uploads/201308/31/1377949441_8987.jpg",
			"http://img.my.csdn.net/uploads/201308/31/1377949441_5454.jpg",
			"http://img.my.csdn.net/uploads/201308/31/1377949454_6367.jpg",
			"http://img.my.csdn.net/uploads/201308/31/1377949442_4562.jpg" };

}
