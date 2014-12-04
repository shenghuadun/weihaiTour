package com.dtssAnWeihai.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.dtssAnWeihai.tools.BMapApiDemoApp;

public class TestMapActivity extends BaseActivity{

	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private UiSettings mUiSettings;
	

	private RouteLine route = null;
	
    //搜索相关
    RoutePlanSearch mSearch = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		BDLocation currentLocation = ((BMapApiDemoApp)getApplication()).getCurrentLocation();
		if (null != currentLocation) {
			//设置中心点为指定点
			LatLng p = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
			mMapView = new MapView(this,
					new BaiduMapOptions().mapStatus(new MapStatus.Builder()
							.target(p).build()));
		} else {
			mMapView = new MapView(this, new BaiduMapOptions());
		}
		setContentView(mMapView);
		mBaiduMap = mMapView.getMap();
		mUiSettings = mBaiduMap.getUiSettings();
		
		//开启定位
		mBaiduMap.setMyLocationEnabled(true);
		//显示指南针
		mUiSettings.setCompassEnabled(true);

		MapStatus ms = new MapStatus.Builder().overlook(0).build();
		MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(ms);
		mBaiduMap.animateMapStatus(u, 1000);
		
		//搜索
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(getRoutePlanResultListener);
		
		refreshPostionHandler.sendEmptyMessageDelayed(0, 1000);
	}
	
	private void getRoute()
	{
        //重置浏览节点的路线数据
        route = null;
        mBaiduMap.clear();
        
        BDLocation currentLocation = ((BMapApiDemoApp)getApplication()).getCurrentLocation();
        //设置起终点信息，对于tranist search 来说，城市名无意义
        PlanNode stNode = PlanNode.withCityNameAndPlaceName("青岛", "市政府");
        PlanNode enNode = PlanNode.withLocation(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));

//        // 实际使用中请对起点终点城市进行正确的设定
//        if (v.getId() == R.id.drive) {
//            mSearch.drivingSearch((new DrivingRoutePlanOption())
//                    .from(stNode)
//                    .to(enNode));
//        } else if (v.getId() == R.id.transit) {
//            mSearch.transitSearch((new TransitRoutePlanOption())
//                    .from(stNode)
//                    .city("北京")
//                    .to(enNode));
//        } else if (v.getId() == R.id.walk) {
            mSearch.walkingSearch((new WalkingRoutePlanOption())
                    .from(stNode)
                    .to(enNode));
//        }	
	}
	
	private OnGetRoutePlanResultListener getRoutePlanResultListener = new OnGetRoutePlanResultListener()
	{
		
		@Override
		public void onGetWalkingRouteResult(WalkingRouteResult result)
		{
			 if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
		            Toast.makeText(getApplicationContext(), "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
		        }
		        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
		            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
		            //result.getSuggestAddrInfo()
		            return;
		        }
		        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
		            route = result.getRouteLines().get(0);
		            WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaiduMap);
		            mBaiduMap.setOnMarkerClickListener(overlay);
		            overlay.setData(result.getRouteLines().get(0));
		            overlay.addToMap();
		            overlay.zoomToSpan();
		        }
		}
		
		@Override
		public void onGetTransitRouteResult(TransitRouteResult arg0)
		{
			
		}
		
		@Override
		public void onGetDrivingRouteResult(DrivingRouteResult arg0)
		{
			
		}
	};
	
	private Handler refreshPostionHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			BDLocation currentLocation = ((BMapApiDemoApp)getApplication()).getCurrentLocation();
			if(null == currentLocation)
			{
				showLoading("正在定位...");

				refreshPostionHandler.sendEmptyMessageDelayed(0, 1000);
			}
			else
			{
				hideLoading();

				getRoute();
				if (mMapView != null)
				{
					MyLocationData locData = new MyLocationData.Builder()
							.accuracy(currentLocation.getRadius())
							// 此处设置开发者获取到的方向信息，顺时针0-360
							.direction(100).latitude(currentLocation.getLatitude())
							.longitude(currentLocation.getLongitude()).build();
					mBaiduMap.setMyLocationData(locData);
					
					LatLng ll = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
					MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
					mBaiduMap.animateMapStatus(u);
				}
			}
		}
	};

	@Override
	protected void onPause() {
		super.onPause();
		// activity 暂停时同时暂停地图控件
		mMapView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// activity 恢复时同时恢复地图控件
		mMapView.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		// activity 销毁时同时销毁地图控件
		mMapView.onDestroy();
		mMapView = null;
	}
	
	
}
