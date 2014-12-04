package com.dtssAnWeihai.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Process;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MapView;
import com.dtssAnWeihai.entity.LocationEntity;
import com.dtssAnWeihai.tools.BMapApiDemoApp;
import com.dtssAnWeihai.tools.MyConfig;
import com.dtssAnWeihai.tools.MyStatusEntity;

/**
 * 地图展示
 * @author ChenPengyan
 * @Email cpy781@163.com
 * 2014-6-10
 */
public class MapActivity extends Activity {
//	private Button map_back;
//	private MapView mapView = null;
//	
//	/**
//	 * String的格式为：title,latitude,longitude
//	 */
//	ArrayList<String> pointList;
//	
//	// 其它的弹出泡泡
//	private MapView.LayoutParams layoutParam = null;
//	private Button viewCacheButton;
//	private View popview;
//	private PopupOverlay pop = null;
//	private View map_title;
//	
//	// 定位
//	private LocationData locData = null;
//	// 定位图层
//	locationOverlay myLocationOverlay = null;
//	public MyLocationListenner myListener = new MyLocationListenner();
//	private LocationClient mLocationClient = null;
//	private MapController mMapController = null;
//	private MyOverlay mOverlay = null;
//	
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		/**
//		 * 使用地图sdk前需先初始化BMapManager. BMapManager是全局的，可为多个MapView共用，它需要地图模块创建前创建，
//		 * 并在地图地图模块销毁后销毁，只要还有地图模块在使用，BMapManager就不应该销毁
//		 */
//		BMapApiDemoApp app = (BMapApiDemoApp) getApplication();
//		if (app.mBMapManager == null) {
//			app.mBMapManager = new BMapManager(this);
//			// 如果BMapManager没有初始化则初始化BMapManager
//			app.mBMapManager.init(MyConfig.BAIDUMAPKEY, new BMapApiDemoApp.MyGeneralListener());
//		}
//		app.mBMapManager.start();
//		// 由于MapView在setContentView()中初始化,所以它需要在BMapManager初始化之后
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.search_map);
//		
//		map_back = (Button) findViewById(R.id.map_back);
//		map_back.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				finish();
//			}
//		});
//		mapView = (MapView) findViewById(R.id.mapView);
//		
//		// 接收传过来的list经纬度值
//		Intent intent = getIntent();
//		pointList = intent.getStringArrayListExtra("pointList");
//		
//		// 获取地图控制器
//		mMapController = mapView.getController();
//		// 设置地图是否响应点击事件.
//		mMapController.enableClick(true);
//		// 设置地图缩放级别
//		mMapController.setZoom(14);
//		// 设置为卫星地图
//		mapView.setSatellite(false);
//		// 显示内置缩放控件
//		mapView.setBuiltInZoomControls(true);
//		// 显示标记点，自定义view
//		showMyOverlayItem();
//		// 刷新地图
//		mapView.refresh();
////		// 设置地图的中心点.判断中心点是否为当前位置 location:当前位置,否则:自定义的位置
////		GeoPoint point = new GeoPoint((int) (pointLat * 1e6), (int) (pointLon * 1e6));
////		mMapController.setCenter(point);
//
//		// 定位
//		Toast.makeText(getApplicationContext(), "正在定位...", Toast.LENGTH_SHORT).show();
//		getLocaltion();
//		// 定位图层初始化
//		myLocationOverlay = new locationOverlay(mapView);
//		// 设置定位数据
//		myLocationOverlay.setData(locData);
//		// 添加定位图层
//		mapView.getOverlays().add(myLocationOverlay);
//		myLocationOverlay.enableCompass();
//		// 刷新地图
//		mapView.refresh();
//		
//		viewCacheButton = new Button(this);
//		viewCacheButton.setBackgroundResource(R.drawable.popup);
//	}
//	
//	// 绘制弹出的泡泡
//	private void showMyOverlayItem() {
//		// 创建自定义overlay
//		mOverlay = new MyOverlay(getResources().getDrawable(R.drawable.icon_map), mapView);
//		try {
//			// 绘制标记点
//			for (int i = 0; i < pointList.size(); i++) {
//				System.out.println("******* " + pointList.get(i));
//				String[] split = pointList.get(i).split(",");
//				String latitude = split[1];
//				String longitude = split[2];
//				
//				// 设置地图的中心点.判断中心点是否为当前位置 location:当前位置,否则:自定义的位置
//				GeoPoint point0 = new GeoPoint((int) (Double.parseDouble(latitude) * 1e6), (int) (Double.parseDouble(longitude) * 1e6));
//				mMapController.setCenter(point0);
//				
//				GeoPoint point = new GeoPoint((int) (Double.parseDouble(latitude) * 1E6), (int) (Double.parseDouble(longitude) * 1E6));
//				// 构造OverlayItem的三个参数依次为：标记点的位置，标题文本，文字片段
//				OverlayItem item = new OverlayItem(point, split[0], "");
//				item.setMarker(getResources().getDrawable(R.drawable.icon_map));
//
//				mOverlay.addItem(item);
//			}
//			// 将overlay 添加至MapView中
//			mapView.getOverlays().add(mOverlay);
//			// 刷新地图
//			mapView.refresh();
//			// 向地图添加自定义View
//			popview = getLayoutInflater().inflate(R.layout.popview, null);
//			map_title = (View) popview.findViewById(R.id.map_title);
//			// 创建弹出的泡泡
//			PopupClickListener popListener = new PopupClickListener() {
//				@Override
//				public void onClickedPopup(int index) {
//					Log.v("click", "clickpaopao");
//				}
//			};
//			pop = new PopupOverlay(mapView, popListener);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	// 继承MyLocationOverlay重写dispatchTap实现点击处理
//	public class locationOverlay extends MyLocationOverlay {
//
//		public locationOverlay(MapView mapView) {
//			super(mapView);
//		}
//
//		@Override
//		protected boolean dispatchTap() {
//			// 处理点击事件,弹出泡泡
////			pop2.showPopup(BMapUtil.getBitmapFromView(maptext2_lay), new GeoPoint((int) (locData.latitude * 1e6), (int) (locData.longitude * 1e6)), 8);
//			return true;
//		}
//		
//	}
//	
//	public class MyOverlay extends ItemizedOverlay<OverlayItem> {
//		public MyOverlay(Drawable defaultMarker, MapView mapView) {
//			super(defaultMarker, mapView);
//		}
//
//		@Override
//		public boolean onTap(int index) {
//			Log.i("", "***************index: " + index);
//			OverlayItem item = getItem(index);
//			viewCacheButton.setText(getItem(index).getTitle());
//			viewCacheButton.setTextColor(Color.WHITE);
//			GeoPoint point = item.getPoint();
//			// 创建布局参数
//			layoutParam = new MapView.LayoutParams(
//			// 控件宽,继承自ViewGroup.LayoutParams
//					MapView.LayoutParams.WRAP_CONTENT,
//					// 控件高,继承自ViewGroup.LayoutParams
//					MapView.LayoutParams.WRAP_CONTENT,
//					// 使控件固定在某个地理位置
//					point, 0, -32,
//					// 控件对齐方式
//					MapView.LayoutParams.BOTTOM_CENTER);
//			// 添加View到MapView中
//			mMapView.addView(viewCacheButton, layoutParam);
//			
////			popupTitle.setText(getItem(index).getTitle());
////			Bitmap[] bitMaps = { BMapUtil.getBitmapFromView(maptext_lay) };
////			pop.showPopup(bitMaps, item.getPoint(), 32);
//			return true;
//		}
//
//		@Override
//		public boolean onTap(GeoPoint pt, MapView mMapView) {
//			mapView.removeView(viewCacheButton);
//			return false;
//		}
//	}
//	
//	// 定位SDK监听函数
//	public class MyLocationListenner implements BDLocationListener {
//
//		@Override
//		public void onReceiveLocation(BDLocation location) {
//			if (location == null)
//				return;
//			
//			locData.latitude = location.getLatitude();
//			locData.longitude = location.getLongitude();
//			// 如果不显示定位精度圈，将accuracy赋值为0即可
//			locData.accuracy = 0;
//			locData.direction = location.getDerect();
//			// 更新定位数据
//			myLocationOverlay.setData(locData);
//			// 更新图层数据执行刷新后生效
//			mapView.refresh();
//
//			LocationEntity locationEntity = new LocationEntity();
//			locationEntity.setLatitude(location.getLatitude());
//			locationEntity.setLontitude(location.getLongitude());
//			locationEntity.setAddress(location.getAddrStr());
//			locationEntity.setProvince(location.getProvince());
//			locationEntity.setCity(location.getCity());
//			locationEntity.setDistrict(location.getDistrict());
//			locationEntity.setStreet(location.getStreet());
//			locationEntity.setStreetNo(location.getStreetNumber());
//			MyStatusEntity.getInstance().setLocationEntity(locationEntity);
//
//			Log.i("", "*****getPoint: " + location.getLatitude() + "," + location.getLongitude());
//			String addresString = location.getProvince() + " " + location.getCity() + " " + location.getDistrict() + " " + location.getStreet() + " " + location.getStreetNumber();
//			Log.i("", "*****getAddress: " + addresString);
//		}
//
//		public void onReceivePoi(BDLocation poiLocation) {
//			if (poiLocation == null) {
//				return;
//			}
//		}
//	}
//	
//	// 定位初始化
//	private void getLocaltion() {
//		mLocationClient = new LocationClient(this);
//		mLocationClient.registerLocationListener(myListener);
//		locData = new LocationData();
//		LocationClientOption option = new LocationClientOption();
//		option.setOpenGps(true); // 打开gps
//		option.setCoorType("bd09ll"); // 设置坐标类型
//		option.setServiceName("com.baidu.location.service_v2.9");
//		option.setPoiExtraInfo(true);
//		option.setAddrType("all");
//		option.setScanSpan(5000); // 设置定位模式:定时定位
//		option.setPriority(LocationClientOption.GpsFirst); // gps优先
//		option.setPoiNumber(10);
//		option.disableCache(true);
//
//		mLocationClient.setLocOption(option);
//		mLocationClient.start();
//
//		int count = 1;
//		Log.d("", "... mStartBtn onClick... pid=" + Process.myPid() + " count=" + count++);
//	}
//	
//	@Override
//	protected void onPause() {
//		mapView.onPause();
//		super.onPause();
//	}
//
//	@Override
//	protected void onResume() {
//		mapView.onResume();
//		super.onResume();
//	}
//
//	@Override
//	protected void onDestroy() {
//		mapView.destroy();
//		super.onDestroy();
//	}
//
//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//		mapView.onSaveInstanceState(outState);
//	}
//
//	@Override
//	protected void onRestoreInstanceState(Bundle savedInstanceState) {
//		super.onRestoreInstanceState(savedInstanceState);
//		mapView.onRestoreInstanceState(savedInstanceState);
//	}
//
//	@Override
//	public void onBackPressed() {
//		finish();
//	}
//	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		switch (keyCode) {
//		case KeyEvent.KEYCODE_BACK:
//			finish();
//			break;
//		default:
//			break;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
//	
//}
///**
// * 继承MapView重写onTouchEvent实现泡泡处理操作
// */
//class MyLocationMapView extends MapView {
//	static PopupOverlay pop = null;// 弹出泡泡图层，点击图标使用
//
//	public MyLocationMapView(Context context) {
//		super(context);
//	}
//
//	public MyLocationMapView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//	}
//
//	public MyLocationMapView(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//	}
//
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		if (!super.onTouchEvent(event)) {
//			// 消隐泡泡
//			if (pop != null && event.getAction() == MotionEvent.ACTION_UP)
//				pop.hidePop();
//		}
//		return true;
//	}
}
