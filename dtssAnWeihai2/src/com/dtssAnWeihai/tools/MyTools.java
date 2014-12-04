package com.dtssAnWeihai.tools;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.dtssAnWeihai.activity.R;
import com.dtssAnWeihai.tools.AsynImageLoader.ImageCallback;

public class MyTools {

	/**
	 * 显示网络图片（单张）
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap returnBitMap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	/**
	 * 显示网络图片（单张）
	 * 
	 * @param url
	 * @return
	 */
	public static InputStream returnInputStream(String url) {
		URL myFileUrl = null;
		InputStream is = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			is = conn.getInputStream();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is;
	}

	/**
	 * 将Drawable转换成Bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap getBitmapByDrawable(Drawable drawable) {
		BitmapDrawable bd = (BitmapDrawable) drawable;
		Bitmap bm = bd.getBitmap();
		return bm;
	}

	/**
	 * 将Bitmap转换成Drawable
	 * 
	 * @param bitmap
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static BitmapDrawable getDrawableByBitmap(Bitmap bitmap) {
		BitmapDrawable drawable = new BitmapDrawable(bitmap);
		return drawable;
	}

	/**
	 * 图片的缩放方法
	 * 
	 * @param bgimage
	 *            源图片资源
	 * @param newWidth
	 *            缩放后宽度
	 * @param newHeight
	 *            缩放后高度
	 * @return
	 */
	public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
		// 获取这个图片的宽和高
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
		return bitmap;
	}

	/**
	 * 根据传入的图片的信息，压缩处理，得到Bitmap图片
	 * 
	 * @param picPath
	 *            本地图片地址
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeBitmapFromPath(String picPath, int reqWidth, int reqHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(picPath, options);
		if (options.mCancel || options.outWidth == -1 || options.outHeight == -1) {
			Log.d("", "****" + String.valueOf(options.mCancel) + "" + options.outWidth + options.outHeight);
		}
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		Log.d("", "inSampleSize: " + options.inSampleSize);
		options.inJustDecodeBounds = false;
		options.inDither = false;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap imagebBitmap = BitmapFactory.decodeFile(picPath, options);
		return imagebBitmap;
	}

	/**
	 * 根据传入的Image压缩图片为合适的比例
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// 源图片的高度和宽度
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// 计算出实际宽高和目标宽高的比率
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	/**
	 * 产生随机数，一般用来定义文件名（年月日时分秒毫秒）
	 */
	public static String createRangeName() {
		Calendar cld = Calendar.getInstance();
		Date date = new Date();
		cld.setTime(date);
		int year = cld.get(Calendar.YEAR);
		int month = cld.get(Calendar.MONTH) + 1;
		int day = cld.get(Calendar.DAY_OF_MONTH);
		int hour = cld.get(Calendar.HOUR_OF_DAY);
		int min = cld.get(Calendar.MINUTE);
		int second = cld.get(Calendar.SECOND);
		int rand = (int) ((Math.random() * 10000));
		StringBuffer sb = new StringBuffer();
		sb.append(year);
		sb.append(month);
		sb.append(day);
		sb.append(hour);
		sb.append(min);
		sb.append(second);
		sb.append(rand);
		return sb.toString();
	}

	/**
	 * 得到当前时间，格式为：xxxx-xx-xx xx:xx
	 * 
	 * @return
	 */
	public static String getNowDate() {
		Date date = new Date();
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(date);
	}

	/**
	 * 根据传入的图片路径显示图片展示
	 * 
	 * @param imageView
	 *            要显示的图片View
	 * @param imageUrl
	 *            图片路径
	 * @param width
	 *            要显示的图片宽度
	 * @param height
	 *            要显示的图片高度
	 */
	@SuppressWarnings("deprecation")
	public static void showImageFromPath(final ImageView imageView, String imageUrl, final int width, final int height) {
		AsynImageLoader asynImageLoader = new AsynImageLoader();
		if ("暂无".equals(imageUrl) || "".equals(imageUrl) || "null".equals(imageUrl)) {
			imageView.setImageResource(R.drawable.ic_launcher);
		} else {
			Drawable cachedImage = asynImageLoader.loadDrawable(imageUrl, new ImageCallback() {
				public void imageLoaded(Drawable imageDrawable, String imageUrl) {
					if (null != imageDrawable) {
						imageView.setImageBitmap(MyTools.zoomImage(MyTools.getBitmapByDrawable(imageDrawable), width, height));
					}
				}
			});
			if (cachedImage != null)
				imageView.setImageBitmap(MyTools.zoomImage(MyTools.getBitmapByDrawable(cachedImage), width, height));
			else
				imageView.setImageResource(R.drawable.image_indicator);
		}
	}

	/**
	 * 根据传入的图片路径显示图片展示
	 * 
	 * @param imageView
	 *            要显示的图片View
	 * @param imageUrl
	 *            图片路径
	 * @param width
	 *            要显示的图片宽度
	 * @param height
	 *            要显示的图片高度
	 */
	@SuppressWarnings("deprecation")
	public static void showButtonImageFromPath(final Button imageView, String imageUrl, final int width, final int height) {
		AsynImageLoader asynImageLoader = new AsynImageLoader();
		if ("暂无".equals(imageUrl) || "".equals(imageUrl) || "null".equals(imageUrl)) {
			imageView.setBackgroundResource(R.drawable.ic_launcher);
		} else {
			Drawable cachedImage = asynImageLoader.loadDrawable(imageUrl, new ImageCallback() {
				public void imageLoaded(Drawable imageDrawable, String imageUrl) {
					if (null != imageDrawable) {
						imageView.setBackgroundDrawable(MyTools.getDrawableByBitmap(MyTools.zoomImage(MyTools.getBitmapByDrawable(imageDrawable), width, height)));
					}
				}
			});
			if (cachedImage != null)
				imageView.setBackgroundDrawable(MyTools.getDrawableByBitmap(MyTools.zoomImage(MyTools.getBitmapByDrawable(cachedImage), width, height)));
		}
	}
	
	/**
	 * 判断str1字符串中包含str2字符的个数
	 * @param str1 原字符串
	 * @param str2 要查找个数的字符串
	 * @return counter
	 */
	public static int countStr(String str1, String str2) {
		int counter = 0;
		if (str1.indexOf(str2) == -1) {
			return 0;
		} else if (str1.indexOf(str2) != -1) {
			counter++;
			countStr(str1.substring(str1.indexOf(str2) + str2.length()), str2);
			return counter;
		}
		return 0;
	}

	/**
	 * 分享到 微博、微信、QQ
	 * 
	 * @param context
	 * @param shareInfo
	 * @param imageUrl
	 */
	public static void showShare(Context context, String shareInfo, String imageUrl) {
		ShareSDK.initSDK(context);
		ShareSDK.initSDK(context, "3632096792");
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		oks.setNotification(R.drawable.ic_launcher, MyConfig.APPNAME);
		oks.setTitle("分享");
		oks.setTitleUrl("http://www.jutingting.cn");
		oks.setText(shareInfo);
		if (!"".equals(imageUrl))
			oks.setImageUrl(imageUrl);
		oks.setSite(MyConfig.APPNAME);
		oks.setSiteUrl("http://www.jutingting.cn");

		// 启动分享GUI
		oks.show(context);
	}

	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
	
	/**
	 * 请求用户确认，并拨打电话
	 * @param context
	 * @param num
	 */
	public static void doCall(final Activity activity, final String num)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle("提示")
		.setMessage("确定要拨打电话" + num + "?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				Intent intent2 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + num));
				activity.startActivity(intent2);
			}
        });
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int id)
			{
				dialog.dismiss();
			}
        });

		builder.create().show();
	}

}
