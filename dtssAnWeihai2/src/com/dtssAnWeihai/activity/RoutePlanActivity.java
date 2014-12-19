package com.dtssAnWeihai.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.lbsapi.auth.LBSAuthManagerListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption.DrivingPolicy;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.navisdk.BNaviEngineManager.NaviEngineInitListener;
import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.CommonParams.Const.ModelName;
import com.baidu.navisdk.CommonParams.NL_Net_Mode;
import com.baidu.navisdk.comapi.mapcontrol.BNMapController;
import com.baidu.navisdk.comapi.mapcontrol.MapParams.Const.LayerMode;
import com.baidu.navisdk.comapi.routeguide.RouteGuideParams.RGLocationMode;
import com.baidu.navisdk.comapi.routeplan.BNRoutePlaner;
import com.baidu.navisdk.comapi.routeplan.IRouteResultObserver;
import com.baidu.navisdk.comapi.routeplan.RoutePlanParams.NE_RoutePlan_Mode;
import com.baidu.navisdk.model.NaviDataEngine;
import com.baidu.navisdk.model.RoutePlanModel;
import com.baidu.navisdk.model.datastruct.RoutePlanNode;
import com.baidu.navisdk.ui.routeguide.BNavConfig;
import com.baidu.navisdk.ui.routeguide.BNavigator;
import com.baidu.navisdk.ui.widget.RoutePlanObserver;
import com.baidu.nplatform.comapi.basestruct.GeoPoint;
import com.dtssAnWeihai.tools.BMapApiDemoApp;

/**
 * 导航地图
 * @author ChenPengyan
 * @Email cpy781@163.com
 * 2014-7-9
 */
public class RoutePlanActivity extends BaseActionBarActivity {

	// UI相关
	private Button mBtnDrive = null; // 驾车搜索
	private Button mBtnTransit = null; // 公交搜索
	private Button mBtnWalk = null; // 步行搜索
	
	private TextView start;
	private TextView end;
	private TextView endLabel;
	
	private View floatBar;

	// 浏览路线节点相关
	private Button mBtnPre = null;// 上一个节点
	private Button mBtnNext = null;// 下一个节点
	private Button btnNavi;
	
	private int nodeIndex = -2;// 节点索引,供浏览节点时使用
	
	private MapView mMapView = null; // 地图View
	private BaiduMap mBaiduMap;
	private UiSettings mUiSettings;
	private RoutePlanSearch mSearch = null;
	
	private RouteLine route = null;// 保存驾车/步行路线数据的变量，供浏览节点时使用
	//导航前规划的路线，供导航使用
	private RoutePlanModel mRoutePlanModel = null;
	
	private JSONObject positionJSON;
	
	private Animation showAnimation;
	private Animation hideAnimation;
	private boolean isFloatBarShowing = true;
	
	private static boolean isEngineInitSuccess = false;
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.routeplan);
		setTitle("地图定位");
		
		// UI初始化
		initUI();
		
		showAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.show);
		showAnimation.setFillAfter(true);
		hideAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.hide);
		hideAnimation.setFillAfter(true);
		
		// 接收传过来的list经纬度值
		try
		{
			Intent intent = getIntent();
			positionJSON = new JSONObject(intent.getStringExtra("positionJSON"));
			endLabel.setText("终点：" + positionJSON.getString("name"));
			end.setText(positionJSON.getString("address"));
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		// 初始化地图
		mBaiduMap = mMapView.getMap();
		mUiSettings = mBaiduMap.getUiSettings();
		
		//开启定位
		mBaiduMap.setMyLocationEnabled(true);
		//显示指南针
		mUiSettings.setCompassEnabled(true);

		MapStatus ms = new MapStatus.Builder().overlook(0).zoom(17).build();
		MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(ms);
		mBaiduMap.animateMapStatus(u, 1000);
		
		//搜索
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(getRoutePlanResultListener);

        // 定位
		refreshPostionHandler.sendEmptyMessageDelayed(0, 10);

		// 地图点击事件处理
		mBaiduMap.setOnMapClickListener(new OnMapClickListener()
		{
			@Override
			public boolean onMapPoiClick(MapPoi arg0)
			{
				return false;
			}
			
			@Override
			public void onMapClick(LatLng latlng)
			{
				toggleFloatBar();
			}
		});
		
		floatBar.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if(event.getAction() == MotionEvent.ACTION_DOWN  && !isFloatBarShowing)
				{
					showFloatBar();
					return true;
				}
				return false;
			}
		});
	}

	private void initUI()
	{
		mBtnDrive = (Button) findViewById(R.id.drive);
		mBtnTransit = (Button) findViewById(R.id.transit);
		mBtnWalk = (Button) findViewById(R.id.walk);

		mBtnDrive.setOnClickListener(clickListener);
		mBtnTransit.setOnClickListener(clickListener);
		mBtnWalk.setOnClickListener(clickListener);
		
		mBtnPre = (Button) findViewById(R.id.pre);
		mBtnNext = (Button) findViewById(R.id.next);
		mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);
		mBtnPre.setOnClickListener(nodeClickListener);
		mBtnNext.setOnClickListener(nodeClickListener);
		
		btnNavi = (Button) findViewById(R.id.btnNavi);
		btnNavi.setVisibility(View.INVISIBLE);
		btnNavi.setOnClickListener(navigateClickListener);
		
		// 处理搜索按钮响应
		start = (TextView) findViewById(R.id.start);
		end = (TextView) findViewById(R.id.end);
		endLabel = (TextView) findViewById(R.id.endLabel);
		
		floatBar = findViewById(R.id.floatBar);

		mMapView = (MapView) findViewById(R.id.bmapView);
	}

	private OnClickListener navigateClickListener = new OnClickListener() 
	{
		public void onClick(View v) 
		{
			if(!isEngineInitSuccess)
			{
				//初始化导航引擎
				showLoading("正在初始化导航引擎");
				BaiduNaviManager.getInstance().initEngine(RoutePlanActivity.this, getSdcardDir(),
			        mNaviEngineInitListener, new LBSAuthManagerListener() {
			            @Override
			            public void onAuthResult(int status, String msg) {
			                String str = null;
			                if (0 == status) {
			                    str = "key校验成功!";
			                } else {
			                    str = "key校验失败, " + msg;
			                    Toast.makeText(RoutePlanActivity.this, str, Toast.LENGTH_LONG).show();
			                }
			            }
		        });
			}
			else
			{
				naviEngineInitHandler.sendEmptyMessage(1);
			}
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

				if (mMapView != null)
				{
					//地图定位到当前地点
					MyLocationData locData = new MyLocationData.Builder()
							.accuracy(currentLocation.getRadius())
							// 此处设置开发者获取到的方向信息，顺时针0-360
							.direction(100).latitude(currentLocation.getLatitude())
							.longitude(currentLocation.getLongitude()).build();
					mBaiduMap.setMyLocationData(locData);
					
					LatLng ll = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
					MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
					mBaiduMap.animateMapStatus(u);
					
					start.setText("(" + currentLocation.getAddrStr() + ")");
				}
			}
		}
	};
	
	private OnClickListener clickListener = new OnClickListener() 
	{
		public void onClick(View v)
		{
	        mBtnPre.setVisibility(View.INVISIBLE);
	        mBtnNext.setVisibility(View.INVISIBLE);
	        btnNavi.setVisibility(View.INVISIBLE);
	        
	        showLoading("查询中...");
	        
			//重置浏览节点的路线数据
	        route = null;
	        mBaiduMap.clear();
	        
	        BDLocation currentLocation = ((BMapApiDemoApp)getApplication()).getCurrentLocation();
	        //设置起终点信息，对于tranist search 来说，城市名无意义
	        PlanNode stNode = PlanNode.withLocation(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
	        PlanNode enNode = null;
			try
			{
				enNode = PlanNode.withLocation(new LatLng(positionJSON.getDouble("latitude"), positionJSON.getDouble("longitude")));
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}

	        if (v.getId() == R.id.drive) {
	            mSearch.drivingSearch((new DrivingRoutePlanOption())
	                    .from(stNode)
	                    .policy(DrivingPolicy.ECAR_TIME_FIRST)
	                    .to(enNode));
	        } else if (v.getId() == R.id.transit) {
	            mSearch.transitSearch((new TransitRoutePlanOption())
	                    .from(stNode)
	                    .city("威海")
	                    .to(enNode));
	        } else if (v.getId() == R.id.walk) {
	            mSearch.walkingSearch((new WalkingRoutePlanOption())
	                    .from(stNode)
	                    .to(enNode));
	        }	
		}
	};
	
	private OnClickListener nodeClickListener = new OnClickListener() 
	{
		public void onClick(View v) 
		{
			if (route == null || route.getAllStep() == null) {
	            return;
	        }
	        if (nodeIndex == -1 && v.getId() == R.id.pre) {
	        	return;
	        }
	        //设置节点索引
	        if (v.getId() == R.id.next) 
	        {
	            if (nodeIndex < route.getAllStep().size() - 1)
	            {
	            	nodeIndex++;
	            } 
	            else 
	            {
	            	return;
	            }
	        }
	        else if (v.getId() == R.id.pre) 
	        {
	        	if (nodeIndex > 0) 
	        	{
	        		nodeIndex--;
	        	}
	        	else 
	        	{
	            	return;
	            }
	        }
	        //获取节结果信息
	        LatLng nodeLocation = null;
	        String nodeTitle = null;
	        Object step = route.getAllStep().get(nodeIndex);
	        if (step instanceof DrivingRouteLine.DrivingStep) {
	            nodeLocation = ((DrivingRouteLine.DrivingStep) step).getEntrace().getLocation();
	            nodeTitle = ((DrivingRouteLine.DrivingStep) step).getInstructions();
	        } else if (step instanceof WalkingRouteLine.WalkingStep) {
	            nodeLocation = ((WalkingRouteLine.WalkingStep) step).getEntrace().getLocation();
	            nodeTitle = ((WalkingRouteLine.WalkingStep) step).getInstructions();
	        } else if (step instanceof TransitRouteLine.TransitStep) {
	            nodeLocation = ((TransitRouteLine.TransitStep) step).getEntrace().getLocation();
	            nodeTitle = ((TransitRouteLine.TransitStep) step).getInstructions();
	        }

	        if (nodeLocation == null || nodeTitle == null) {
	            return;
	        }
	        //移动节点至中心
	        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(nodeLocation));
	        // show popup
	        TextView popupText = new TextView(RoutePlanActivity.this);
	        popupText.setBackgroundResource(R.drawable.popup);
	        popupText.setTextColor(0xFF000000);
	        popupText.setText(nodeTitle);
	        mBaiduMap.showInfoWindow(new InfoWindow(popupText, nodeLocation, 0));
		}
	};
	

	private IRouteResultObserver mRouteResultObserver = new IRouteResultObserver() 
	{
		@Override
		public void onRoutePlanYawingSuccess() {
		}

		@Override
		public void onRoutePlanYawingFail() {
		}

		@Override
		public void onRoutePlanSuccess() 
		{
			hideLoading();
			
			Log.d("RoutePlanActivity", "路径规划完成");
			
			BNMapController.getInstance().setLayerMode(
					LayerMode.MAP_LAYER_MODE_ROUTE_DETAIL);
			mRoutePlanModel = (RoutePlanModel) NaviDataEngine.getInstance()
					.getModel(ModelName.ROUTE_PLAN);
			
			if (mRoutePlanModel == null) 
			{
				Log.d("RoutePlanActivity", "导航路线规划失败！");
				Toast.makeText(getApplicationContext(), "导航路线规划失败！", Toast.LENGTH_LONG).show();
				return;
			}
			// 获取路线规划结果起点
			RoutePlanNode startNode = mRoutePlanModel.getStartNode();
			// 获取路线规划结果终点
			RoutePlanNode endNode = mRoutePlanModel.getEndNode();
			if (null == startNode || null == endNode) {
				return;
			}
			// 获取路线规划算路模式
			int calcMode = BNRoutePlaner.getInstance().getCalcMode();
			final Bundle bundle = new Bundle();
			bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_VIEW_MODE,
					BNavigator.CONFIG_VIEW_MODE_INFLATE_MAP);
			bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_CALCROUTE_DONE,
					BNavigator.CONFIG_CLACROUTE_DONE);
			bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_START_X,
					startNode.getLongitudeE6());
			bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_START_Y,
					startNode.getLatitudeE6());
			bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_END_X, endNode.getLongitudeE6());
			bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_END_Y, endNode.getLatitudeE6());
			bundle.putString(BNavConfig.KEY_ROUTEGUIDE_START_NAME,
					mRoutePlanModel.getStartName(getApplicationContext(), false));
			bundle.putString(BNavConfig.KEY_ROUTEGUIDE_END_NAME,
					mRoutePlanModel.getEndName(getApplicationContext(), false));
			bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_CALCROUTE_MODE, calcMode);
			bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_LOCATE_MODE, RGLocationMode.NE_Locate_Mode_GPS);
			

			Log.d("RoutePlanActivity", "启动导航...");
			
			AlertDialog.Builder builder = new AlertDialog.Builder(RoutePlanActivity.this);
			builder.setTitle("提示")
			.setMessage("是否开启语音?")
			.setPositiveButton("开启", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int id)
				{
					Intent intent = new Intent(getApplicationContext(), NavigatorActivity.class);
					intent.putExtras(bundle);
					intent.putExtra("ttsOn", true);
					startActivity(intent);
					
					dialog.dismiss();
				}
	        })
			.setNegativeButton("不开启", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int id)
				{
					Intent intent = new Intent(getApplicationContext(), NavigatorActivity.class);
					intent.putExtras(bundle);
					intent.putExtra("ttsOn", false);
					startActivity(intent);
					
					dialog.dismiss();
				}
	        }).create().show();
			
		}

		@Override
		public void onRoutePlanFail() {

			Log.d("RoutePlanActivity", "路径规划失败");
			hideLoading();
		}

		@Override
		public void onRoutePlanCanceled() {
			Log.d("RoutePlanActivity", "路径规划取消");
			hideLoading();
		}

		@Override
		public void onRoutePlanStart() {
		}

	};
	
	private OnGetRoutePlanResultListener getRoutePlanResultListener = new OnGetRoutePlanResultListener()
	{
		@Override
		public void onGetWalkingRouteResult(WalkingRouteResult result)
		{
			hideLoading();
			
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) 
			{
				Toast.makeText(getApplicationContext(), "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
			}
			else if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) 
			{
			    //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
				//result.getSuggestAddrInfo();

				Toast.makeText(getApplicationContext(), "起终点地址有岐义", Toast.LENGTH_SHORT).show();
			    return;
			}
			if (result.error == SearchResult.ERRORNO.NO_ERROR) 
			{
				nodeIndex = -1;
	            mBtnPre.setVisibility(View.VISIBLE);
	            mBtnNext.setVisibility(View.VISIBLE);
	            btnNavi.setVisibility(View.GONE);
	            
	            hideFloatBar();
				
			    route = result.getRouteLines().get(0);
			    WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaiduMap);
			    mBaiduMap.setOnMarkerClickListener(overlay);
			    overlay.setData(result.getRouteLines().get(0));
			    overlay.addToMap();
			    overlay.zoomToSpan();
			}
		}
		
		@Override
		public void onGetTransitRouteResult(TransitRouteResult result)
		{
			hideLoading();
			
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) 
			{
				Toast.makeText(getApplicationContext(), "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
			}
			else if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) 
			{
			    //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
				//result.getSuggestAddrInfo();

				Toast.makeText(getApplicationContext(), "起终点地址有岐义", Toast.LENGTH_SHORT).show();
			    return;
			}
			if (result.error == SearchResult.ERRORNO.NO_ERROR) 
			{
				nodeIndex = -1;
	            mBtnPre.setVisibility(View.VISIBLE);
	            mBtnNext.setVisibility(View.VISIBLE);
	            btnNavi.setVisibility(View.GONE);

	            hideFloatBar();
				
			    route = result.getRouteLines().get(0);
			    TransitRouteOverlay overlay = new TransitRouteOverlay(mBaiduMap);
			    mBaiduMap.setOnMarkerClickListener(overlay);
			    overlay.setData(result.getRouteLines().get(0));
			    overlay.addToMap();
			    overlay.zoomToSpan();
			}
		}
		
		@Override
		public void onGetDrivingRouteResult(DrivingRouteResult result)
		{
			hideLoading();
			
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) 
			{
				Toast.makeText(getApplicationContext(), "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
			}
			else if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) 
			{
			    //起终点或途经点地址有岐义，通过以下接口获取建议查询信息

				Toast.makeText(getApplicationContext(), "起终点地址有岐义", Toast.LENGTH_SHORT).show();
			    return;
			}
			if (result.error == SearchResult.ERRORNO.NO_ERROR) 
			{
				nodeIndex = -1;
	            mBtnPre.setVisibility(View.VISIBLE);
	            mBtnNext.setVisibility(View.VISIBLE);
	            btnNavi.setVisibility(View.VISIBLE);

	            hideFloatBar();
				
			    route = result.getRouteLines().get(0);
			    DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);
			    mBaiduMap.setOnMarkerClickListener(overlay);
			    overlay.setData(result.getRouteLines().get(0));
			    overlay.addToMap();
			    overlay.zoomToSpan();
			}
		}
	};
	
	private NaviEngineInitListener mNaviEngineInitListener = new NaviEngineInitListener()
	{
		public void engineInitSuccess() 
		{
			naviEngineInitHandler.sendEmptyMessage(1);

			Log.d("RoutePlanActivity", "引擎初始化成功");
		}
		
		public void engineInitStart() {
		}
		
		public void engineInitFail() {
			naviEngineInitHandler.sendEmptyMessage(-1);
			Log.d("RoutePlanActivity", "引擎初始化失败");
		}
	};
	
	private Handler naviEngineInitHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			hideLoading();
			switch (msg.what)
			{
			case -1:
				isEngineInitSuccess = false;
				break;

			case 1:
				isEngineInitSuccess = true;
		
				try
				{
					startCalcRoute();
				}
				catch (JSONException e)
				{
					e.printStackTrace();
					hideLoading();

					Toast.makeText(getApplicationContext(), "线路规划失败", Toast.LENGTH_LONG).show();
				}
				break;

			default:
				break;
			}
		}
	};

	
	private void startCalcRoute() throws JSONException 
	{
		showLoading("正在规划路线，请稍后...");
		
		BDLocation currentLocation = ((BMapApiDemoApp)getApplication()).getCurrentLocation();
		
		GeoPoint start = new GeoPoint((int)(currentLocation.getLongitude() * 1e5), (int)(currentLocation.getLatitude() * 1e5));
		
		GeoPoint end = new GeoPoint((int)(Double.valueOf(positionJSON.getString("longitude")) * 1e5), (int)(Double.valueOf(positionJSON.getString("latitude")) * 1e5));
		
		//起点
		RoutePlanNode startNode = new RoutePlanNode(start, start,
				RoutePlanNode.FROM_MAP_POINT, currentLocation.getStreet(), currentLocation.getStreet());
		//终点
		RoutePlanNode endNode = new RoutePlanNode(end, end,
				RoutePlanNode.FROM_MAP_POINT, positionJSON.getString("name"), positionJSON.getString("name"));
		//将起终点添加到nodeList
		ArrayList<RoutePlanNode> nodeList = new ArrayList<RoutePlanNode>(2);
		nodeList.add(startNode);
		nodeList.add(endNode);
		BNRoutePlaner.getInstance().setObserver(new RoutePlanObserver(this, null));
		//设置算路方式
		BNRoutePlaner.getInstance().setCalcMode(NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TIME);
		// 设置算路结果回调
		BNRoutePlaner.getInstance().setRouteResultObserver(mRouteResultObserver);
		// 设置起终点并算路
		boolean ret = BNRoutePlaner.getInstance().setPointsToCalcRoute(nodeList, NL_Net_Mode.NL_Net_Mode_OnLine);
		if(!ret){
			Toast.makeText(this, "规划失败", Toast.LENGTH_SHORT).show();
		}
	}

	
	
	@Override
	protected void onPause() {
		super.onPause();
		
		mMapView.onPause();

		if(isEngineInitSuccess)
		{
			BNRoutePlaner.getInstance().setRouteResultObserver(null);
			BNMapController.getInstance().onPause();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
		
		if(isEngineInitSuccess)
		{
			BNMapController.getInstance().onResume();
		}
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
	}
    

	private void toggleFloatBar()
	{
		if(isFloatBarShowing)
		{
			hideFloatBar();
		}
		else
		{
			showFloatBar();
		}
	}
	private void showFloatBar()
	{
		if(!isFloatBarShowing)
		{
	        floatBar.startAnimation(showAnimation);
	        isFloatBarShowing = true;
		}
	}
	private void hideFloatBar()
	{
		if(isFloatBarShowing)
		{
			floatBar.startAnimation(hideAnimation);
			isFloatBarShowing = false;
		}
	}
}
