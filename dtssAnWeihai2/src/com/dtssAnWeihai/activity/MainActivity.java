package com.dtssAnWeihai.activity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dtssAnWeihai.tools.BMapApiDemoApp;
import com.dtssAnWeihai.tools.HttpUtil;
import com.dtssAnWeihai.tools.JPushUtil;
import com.dtssAnWeihai.tools.MyConfig;
import com.dtssAnWeihai.tools.MyTools;

public class MainActivity extends ActivityGroup {
	
	public static Button main_top_left;
	public static TextView main_title;
	public static LinearLayout main_weather;
	
	private TextView weather_wendu;
	private ImageView weather_image;
	private ImageView btnSearch;
	private ImageView qrcode;
	private LinearLayout linearLayout;
	private Button mTab1, mTab2, mTab3, mTab4, mTab5;
	
	public static MainActivity allThis;
	/** 声明意图 */
	private Intent intent = null;
	/** 当前页卡编号 */
	private int currIndex = 0;
	public final int ZERO = 0;
	public final int ONE = 1;
	public final int TWO = 2;
	public final int THREE = 3;
	public final int FOUR = 4;

	private String weatherPostStr = "";
	
	public static boolean isForeground = false;
	private String checkStr;
	private String checkDownUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTheme(android.R.style.Theme_NoTitleBar);
		
		setContentView(R.layout.main);
		allThis = this;
		initView();
		
		String udid =  JPushUtil.getImei(getApplicationContext(), "");
        if (null != udid) System.out.println("IMEI: " + udid);
        
		String appKey = JPushUtil.getAppKey(getApplicationContext());
		if (null == appKey) appKey = "AppKey异常";
		System.out.println("AppKey: " + appKey);

		String packageName =  getPackageName();
		System.out.println("PackageName: " + packageName);
		
		String versionName =  JPushUtil.GetVersion(getApplicationContext());
		System.out.println("Version: " + versionName);
		
		File f = new File(MyConfig.filepath);
		if (!f.exists()) {
			f.mkdir();
		}
		
		// 初始页面显示
		mTab1.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.main2) , null, null);
		switchActivity(ZERO);
		
		// 获取版本更新状态
		if (MyConfig.OPENNUM) {
			MyConfig.OPENNUM = false;
			new Thread(new Runnable() {
				public void run() {
					try {
						Map<String, String> params = new HashMap<String, String>();
						params.put("version", MyConfig.VERSION);
						checkStr = HttpUtil.http(MyConfig.HTTPURL + "/clientVersion/newVersion.action", params);
					} catch (Exception e) {
						e.printStackTrace();
					}
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				}
			}).start();
		}
		
		getWeatherInfo();
		
		((BMapApiDemoApp)getApplication()).startGetLocaltion();
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				try {
					JSONObject jsonObject = new JSONObject(checkStr);
					if (!"1".equals(jsonObject.getString("status"))) {
						checkDownUrl = jsonObject.getString("versionUrl");
						new AlertDialog.Builder(MainActivity.this).setTitle("检测到新版本！").setIcon(android.R.drawable.ic_dialog_info)
						.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent intent = new Intent();
								intent.setData(Uri.parse(checkDownUrl));
								intent.setAction(Intent.ACTION_VIEW);
								MainActivity.this.startActivity(intent); // 启动浏览器
							}
						}).setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
							}
						}).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
	};
	
	private void initView() {
		main_top_left = (Button) findViewById(R.id.main_top_left);
		main_title = (TextView) findViewById(R.id.main_title);
		main_weather = (LinearLayout) findViewById(R.id.main_weather);
		weather_wendu = (TextView) findViewById(R.id.weather_wendu);
		weather_image = (ImageView) findViewById(R.id.weather_image);
		
		btnSearch = (ImageView) findViewById(R.id.btnSearch);
		qrcode = (ImageView) findViewById(R.id.qrcode);
		
		linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
		mTab1 = (Button) findViewById(R.id.bottom_main);
		mTab2 = (Button) findViewById(R.id.bottom_guide);
		mTab3 = (Button) findViewById(R.id.bottom_call);
		mTab4 = (Button) findViewById(R.id.bottom_nearby);
		mTab5 = (Button) findViewById(R.id.bottom_more);
		
		mTab1.setOnClickListener(bottomClickListener);
		mTab2.setOnClickListener(bottomClickListener);
		mTab3.setOnClickListener(bottomClickListener);
		mTab4.setOnClickListener(bottomClickListener);
		mTab5.setOnClickListener(bottomClickListener);
		
		main_top_left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, WeihaiInfoActivity.class);
				intent.putExtra("title", "关于我们");
				intent.putExtra("num", "1");
				startActivity(intent);
			}
		});
		main_weather.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, WeatherActivity.class);
				startActivity(intent);
			}
		});
		
		qrcode.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Toast.makeText(MainActivity.this, "尚未实现", Toast.LENGTH_LONG).show();
				
			}
		});
	}
	
	private MyOnClickListener bottomClickListener = new MyOnClickListener();
	/**
	 * 底部点击监听
	 */
	private class MyOnClickListener implements View.OnClickListener {

		private void reset()
		{
			mTab1.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.main1) , null, null);
			mTab2.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.guide1) , null, null);
			mTab3.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.phone1) , null, null);
			mTab4.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.nearby1) , null, null);
			mTab5.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.more1) , null, null);
		}
		
		@Override
		public void onClick(View v) {
//			reset();
			switch (v.getId()) {
			case R.id.bottom_main:
//				mTab1.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.main2) , null, null);
				// 跳转事件
				switchActivity(ZERO);
				break;
			case R.id.bottom_guide:
//				mTab2.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.guide2) , null, null);
				// 跳转事件
				switchActivity(ONE);
				break;
			case R.id.bottom_call:
//				mTab3.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.phone2) , null, null);
				// 跳转事件
				switchActivity(TWO);
				break;
			case R.id.bottom_nearby:
//				mTab4.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.nearby2) , null, null);
				// 跳转事件
				switchActivity(THREE);
				break;

			case R.id.bottom_more:
				Intent intent = new Intent(MainActivity.this, MoreActivity.class);
				startActivity(intent);
				break;
			}
		}
	};
	
	/**
	 * 获取天气信息
	 */
	private void getWeatherInfo() {
		
		doNetWorkJob(MyConfig.WEATHER, null, weather_handler, "get");
	}
	private Handler weather_handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			try {
				// ==========================解析JSON得到天气===========================
				JSONObject json = ((JSONObject)msg.obj).getJSONObject("weatherinfo");
				weather_wendu.setText(json.getString("temp1"));
				String tianqi = json.getString("img_title1");// 天气状况
				if (tianqi.equals("晴")) {
					weather_image.setImageResource(R.drawable.weathericon_condition_01);
				} else if (tianqi.equals("多云")) {
					weather_image.setImageResource(R.drawable.weathericon_condition_02);
				} else if (tianqi.equals("阴")) {
					weather_image.setImageResource(R.drawable.weathericon_condition_04);
				} else if (tianqi.equals("雾")) {
					weather_image.setImageResource(R.drawable.weathericon_condition_05);
				} else if (tianqi.equals("沙尘暴")) {
					weather_image.setImageResource(R.drawable.weathericon_condition_06);
				} else if (tianqi.equals("阵雨")) {
					weather_image.setImageResource(R.drawable.weathericon_condition_07);
				} else if (tianqi.equals("小雨") || tianqi.equals("小到中雨")) {
					weather_image.setImageResource(R.drawable.weathericon_condition_08);
				} else if (tianqi.equals("大雨")) {
					weather_image.setImageResource(R.drawable.weathericon_condition_09);
				} else if (tianqi.equals("雷阵雨")) {
					weather_image.setImageResource(R.drawable.weathericon_condition_10);
				} else if (tianqi.equals("小雪")) {
					weather_image.setImageResource(R.drawable.weathericon_condition_11);
				} else if (tianqi.equals("大雪")) {
					weather_image.setImageResource(R.drawable.weathericon_condition_12);
				} else if (tianqi.equals("雨夹雪")) {
					weather_image.setImageResource(R.drawable.weathericon_condition_13);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	};
	
	/***
	 * 根据id 跳转不同的页面
	 * 
	 * @param id
	 */
	private void switchActivity(int id) {
		switch (id) {
		case ZERO:
			// 主页
			linearLayout.removeAllViews();
			intent = new Intent(this, IndexActivity.class);
			startActivity("IndexActivity");
			break;
		case ONE:
			//导游
			intent = new Intent(this, WebviewActivity.class);
			intent.putExtra("weburl", MyConfig.WHTA_GUIDE);
			intent.putExtra("title", "导游导览");
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case TWO:
			// 电话
			MyTools.doCall(MainActivity.this, "063112301");
			break;
		case THREE:
			// 身边
			intent = new Intent(this, NearByActivity.class);
			startActivity(intent);
			break;
		case FOUR:
			// 更多
			intent = new Intent(this, MoreActivity.class);
			startActivity(intent);
			break;
		}
	}

	private void startActivity(String name) {
//		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// 把view 添加到LinearLayout
		Window window = getLocalActivityManager().startActivity(name, intent);
		linearLayout.addView(window.getDecorView(), LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}

	private long exitTime = 0;

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
//				ShareSDK.stopSDK(this);
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	//for receive customer msg from jpush server
	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	
	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}
	
	public class MessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
              String messge = intent.getStringExtra(KEY_MESSAGE);
              String extras = intent.getStringExtra(KEY_EXTRAS);
              StringBuilder showMsg = new StringBuilder();
              showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
              if (!JPushUtil.isEmpty(extras)) {
            	  showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
              }
              Log.e("", "******JPush error:" + showMsg.toString());
			}
		}
	}
	
	@Override
	protected void onResume() {
		isForeground = true;
		super.onResume();
	}


	@Override
	protected void onPause() {
		isForeground = false;
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(mMessageReceiver);
		super.onDestroy();
	}
	
	protected void doNetWorkJob(final String url, final Map<String, String> params, final Handler handler, final String method)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run() 
			{
				JSONObject jsonObject = null;
				try 
				{
					String json = HttpUtil.httpGet(url, params);
					jsonObject = new JSONObject(json);
				} 
				catch (Exception e)
				{
					e.printStackTrace();
					
					Message msg = onNetWorkErrorHandler.obtainMessage();
					msg.obj = new JSONObject();

					handler.sendMessage(msg);
					onNetWorkErrorHandler.sendEmptyMessage(0);
					return;
				}
				
				Message msg = handler.obtainMessage();
				msg.obj = jsonObject;
				handler.sendMessage(msg);
			}

		}).start();
	}
	
	private Handler onNetWorkErrorHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			Toast.makeText(getApplicationContext(), "网络错误！", Toast.LENGTH_SHORT).show();
		}
	};
}
