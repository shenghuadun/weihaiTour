package com.dtssAnWeihai.tools;

import android.app.Application;
import android.app.Service;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.widget.LinearLayout;
import cn.jpush.android.api.JPushInterface;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;

/**
 * 地图控制类
 * 
 * @author ChenPengyan
 * @Email cpy781@163.com 2014-4-24
 */
public class BMapApiDemoApp extends Application {
	
	public static BMapApiDemoApp bMapApiDemoApp;
	boolean m_bKeyRight = true;
	public Vibrator mVibrator01;
	public LinearLayout main_layout10;
	public Handler handler;
	
	private BDLocation currentLocation = new BDLocation();

	public LocationClient mLocationClient;
	public MyLocationListener mMyLocationListener;

	@Override
	public void onCreate() {
		super.onCreate();
		bMapApiDemoApp = this;
		
		if(JPushUtil.isPushMessageEnabled(this))
		{
			JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
	        JPushInterface.init(this);     		// 初始化 JPush
		}

		//百度地图初始化
		SDKInitializer.initialize(this);
		
		//百度定位初始化
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		
		mVibrator01 =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
	}

	public static BMapApiDemoApp getInstance() {
		return bMapApiDemoApp;
	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			//Receive Location 
			BMapApiDemoApp.this.setCurrentLocation(location);
			
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\terror code : ");
			sb.append(location.getLocType());
			sb.append("\tlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\tlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\tradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation){
				sb.append("\tspeed : ");
				sb.append(location.getSpeed());
				sb.append("\tsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\tdirection : ");
				sb.append("\taddr : ");
				sb.append(location.getAddrStr());
				sb.append(location.getDirection());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append("\taddr : ");
				sb.append(location.getAddrStr());
				//运营商信息
				sb.append("\toperationers : ");
				sb.append(location.getOperators());
			}
			Log.i("onReceiveLocation", sb.toString());
		}

	}

	//开始定位
	public void startGetLocaltion() 
	{
		LocationClientOption option = new LocationClientOption();
		//设置定位模式
		option.setLocationMode(LocationMode.Hight_Accuracy);
		//返回的定位结果是百度经纬度，默认值gcj02
		option.setCoorType("gcj02");
		int span=1000;
		option.setScanSpan(span);//设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}

	public BDLocation getCurrentLocation()
	{
		return currentLocation;
	}

	public void setCurrentLocation(BDLocation currentLocation)
	{
		this.currentLocation = currentLocation;
	}
}
