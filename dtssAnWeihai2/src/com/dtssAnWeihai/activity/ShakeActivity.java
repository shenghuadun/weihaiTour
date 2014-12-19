package com.dtssAnWeihai.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.dtssAnWeihai.tools.BMapApiDemoApp;
import com.dtssAnWeihai.tools.MyConfig;
import com.dtssAnWeihai.tools.ShakeListener;
import com.dtssAnWeihai.tools.ShakeListener.OnShakeListener;

/**
 * 举优惠主页
 * 
 * @author ChenPengyan
 * @Email cpy781@163.com 2014-4-17
 */
public class ShakeActivity extends BaseActionBarActivity {

	private ImageView image;
	AnimationDrawable animationDrawable;
	private String sendPostStr = "";

	// 自定义监听器
	private ShakeListener mShakeListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shake);
		
		setTitle("摇一摇");
		
		image = (ImageView) findViewById(R.id.shake_image);

		// 循环播放动画
		image.setImageResource(R.drawable.ani);
		animationDrawable = (AnimationDrawable) image.getDrawable();
		image.post(new Runnable() {
			@Override
			public void run() {
				animationDrawable.start();
			}
		});
		// 举
		mShakeListener = new ShakeListener(this);
		mShakeListener.setOnShakeListener(new OnShakeListener() {
			public void onShake() {
				mShakeListener.stop();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						mShakeListener.start();
						searchBenefit();
					}
				}, 0);
			}
		});
	}

	/**
	 * 连接服务器获取数据
	 * 
	 * @param type
	 */
	private void searchBenefit() 
	{
		locationQueryHandler.sendEmptyMessage(0);
	}
	

	private Handler locationQueryHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			BDLocation currentLocation = ((BMapApiDemoApp)getApplication()).getCurrentLocation();
		
			if(null == currentLocation)
			{
				showLoading("正在定位...");
				locationQueryHandler.sendEmptyMessageDelayed(0, 1000);
			}
			else
			{
				hideLoading();
				
				Map<String, String> params = new HashMap<String, String>();
				params.put("condition.infoType", "res");
				params.put("condition.areacode", MyConfig.AREAID);
				params.put("longitude", String.valueOf(currentLocation.getLongitude()));
				params.put("latitude", String.valueOf(currentLocation.getLatitude()));
				
				doNetWorkJob(MyConfig.HTTPURL + "/search/benefitShop/juBenefit.action", params, handler);
			}
		}
	};
	

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Intent intent = new Intent(ShakeActivity.this, ShakeListActivity.class);
			intent.putExtra("sendPostStr", sendPostStr);
			startActivity(intent);
		}
	};

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mShakeListener != null) {
			mShakeListener.stop();
		}
	}

}