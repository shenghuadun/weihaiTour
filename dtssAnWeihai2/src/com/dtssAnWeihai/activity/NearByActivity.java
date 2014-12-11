package com.dtssAnWeihai.activity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.dtssAnWeihai.adapter.SearchListAdapter;
import com.dtssAnWeihai.entity.SearchListEntity;
import com.dtssAnWeihai.tools.BMapApiDemoApp;
import com.dtssAnWeihai.tools.MyConfig;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class NearByActivity extends BaseActivity
{
	//美食
	private static final String TYPE_FOOD = "res";
	//酒店
	private static final String TYPE_RESTAURANT = "hotel";
	//景点
	private static final String TYPE_SCENIC = "scenic";
	//娱乐
	private static final String TYPE_ENTERTAINMENT = "entr";
	
	private PullToRefreshListView food_listView;
	private PullToRefreshListView restaurant_listView;
	private PullToRefreshListView scenic_listView;
	private PullToRefreshListView entertainment_listView;
	
	private TabHost tabHost;
	
	private List<SearchListEntity> foodData = new LinkedList<SearchListEntity>();
	private List<SearchListEntity> restaurantData = new LinkedList<SearchListEntity>();
	private List<SearchListEntity> scenicData = new LinkedList<SearchListEntity>();
	private List<SearchListEntity> entertainmentData = new LinkedList<SearchListEntity>();
	
	private SearchListAdapter foodAdapter;
	private SearchListAdapter restaurantAdapter;
	private SearchListAdapter scenicAdapter;
	private SearchListAdapter entertainmentAdapter;

	/**
	 * 当前正在修改的数据
	 */
	private PullToRefreshListView currentListView = null;
	private List<SearchListEntity> currentDataList = null;
	private SearchListAdapter currentAdapter = null;
	private int currentIndex = -1;
	
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			JSONObject jsonObject = (JSONObject) msg.obj;
			
			try
			{
				if(jsonObject.getInt("count") > 0 )
				{
					JSONArray data = jsonObject.getJSONArray("data");
					
					for(int i=0; i<data.length(); i++)
					{
						JSONObject json = data.getJSONObject(i);
						
						SearchListEntity entity = new SearchListEntity();
						entity.setId(json.getString("typeId"));
						entity.setAddress(json.getString("address"));
						entity.setDistance(json.getString("distance"));
						entity.setImage(json.getString("image"));
						entity.setName(json.getString("name"));
						
						currentDataList.add(entity);
					}
					
					currentAdapter.notifyDataSetChanged();

					String page = (String) currentListView.getTag(R.id.page);
					currentListView.setTag(R.id.page, String.valueOf(Integer.valueOf(page) + 1));
				}
				else 
				{
					Toast.makeText(NearByActivity.this, "没有更多数据了...", Toast.LENGTH_SHORT).show();
				}
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
			
		
			currentListView.onRefreshComplete();
			hideLoading();
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nearby);
		
		initViews();
		setListeners();

		setTitle("身边");
		loadData(TYPE_FOOD);
	}
	
	private void loadData(String type)
	{
		getCurrentModifyingData(type);
		currentListView.setRefreshing(true);
		
		locationQueryHandler.sendEmptyMessage(currentIndex);
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
				locationQueryHandler.sendEmptyMessageDelayed(currentIndex, 1000);
			}
			else
			{
				hideLoading();
				
				int page = Integer.valueOf((String)currentListView.getTag(R.id.page));
				
				Map<String, String> params = new HashMap<String, String>();
				params.put("currentPage", String.valueOf(page));
				params.put("condition.infoType", (String)currentListView.getTag(R.id.type));
				params.put("longitude", String.valueOf(currentLocation.getLongitude()));
				params.put("latitude", String.valueOf(currentLocation.getLatitude()));
				params.put("condition.areacode", MyConfig.AREAID);
				
				doNetWorkJob(MyConfig.HTTPURL + "/search/benefitShop/juBenefit.action", params, handler);
			}
		}
		
	};

	private void getCurrentModifyingData(String type)
	{
		if(TYPE_FOOD.equals(type))
		{
			currentDataList = foodData;
			currentAdapter = foodAdapter;
			currentListView = food_listView;
			currentIndex = 0;
		}
		else if(TYPE_RESTAURANT.equals(type))
		{
			currentDataList = restaurantData;
			currentAdapter = restaurantAdapter;
			currentListView = restaurant_listView;
			currentIndex = 1;
		}
		else if(TYPE_SCENIC.equals(type))
		{
			currentDataList = scenicData;
			currentAdapter = scenicAdapter;
			currentListView = scenic_listView;
			currentIndex = 2;
		}
		else if(TYPE_ENTERTAINMENT.equals(type))
		{
			currentDataList = entertainmentData;
			currentAdapter = entertainmentAdapter;
			currentListView = entertainment_listView;
			currentIndex = 3;
		}
	}

	private void initViews()
	{
		food_listView = (PullToRefreshListView) findViewById(R.id.food_listView);
		restaurant_listView = (PullToRefreshListView) findViewById(R.id.restaurant_listView);
		scenic_listView = (PullToRefreshListView) findViewById(R.id.scenic_listView);
		entertainment_listView = (PullToRefreshListView) findViewById(R.id.entertainment_listView);

		food_listView.setTag(R.id.page, "1");
		restaurant_listView.setTag(R.id.page, "1");
		scenic_listView.setTag(R.id.page, "1");
		entertainment_listView.setTag(R.id.page, "1");

		food_listView.setTag(R.id.type, TYPE_FOOD);
		restaurant_listView.setTag(R.id.type, TYPE_RESTAURANT);
		scenic_listView.setTag(R.id.type, TYPE_SCENIC);
		entertainment_listView.setTag(R.id.type, TYPE_ENTERTAINMENT);
		
		//设置空视图
		food_listView.setEmptyView(getLayoutInflater().inflate(R.layout.pulltorefresh_empty, null));
		restaurant_listView.setEmptyView(getLayoutInflater().inflate(R.layout.pulltorefresh_empty, null));
		scenic_listView.setEmptyView(getLayoutInflater().inflate(R.layout.pulltorefresh_empty, null));
		entertainment_listView.setEmptyView(getLayoutInflater().inflate(R.layout.pulltorefresh_empty, null));
		
		//上拉刷新
		food_listView.setMode(Mode.PULL_FROM_END);
		restaurant_listView.setMode(Mode.PULL_FROM_END);
		scenic_listView.setMode(Mode.PULL_FROM_END);
		entertainment_listView.setMode(Mode.PULL_FROM_END);

		foodAdapter = new SearchListAdapter(this, foodData, food_listView.getRefreshableView(), TYPE_FOOD);
		restaurantAdapter = new SearchListAdapter(this, restaurantData, restaurant_listView.getRefreshableView(), TYPE_RESTAURANT);
		scenicAdapter = new SearchListAdapter(this, scenicData, scenic_listView.getRefreshableView(), TYPE_SCENIC);
		entertainmentAdapter = new SearchListAdapter(this, entertainmentData, entertainment_listView.getRefreshableView(), TYPE_ENTERTAINMENT);
		food_listView.setAdapter(foodAdapter);
		restaurant_listView.setAdapter(restaurantAdapter);
		scenic_listView.setAdapter(scenicAdapter);
		entertainment_listView.setAdapter(entertainmentAdapter);
		
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(); 
        
        View v = getLayoutInflater().inflate(R.layout.tab_item, null);
        ((TextView)v.findViewById(R.id.title)).setText("餐饮");
        
        tabHost.addTab(tabHost.newTabSpec(TYPE_FOOD)
                .setIndicator(v)
                .setContent(R.id.food_listView));
        
        v = getLayoutInflater().inflate(R.layout.tab_item, null);
        ((TextView)v.findViewById(R.id.title)).setText("酒店");
        
        tabHost.addTab(tabHost.newTabSpec(TYPE_RESTAURANT)
                .setIndicator(v)
                .setContent(R.id.restaurant_listView));

        v = getLayoutInflater().inflate(R.layout.tab_item, null);
        ((TextView)v.findViewById(R.id.title)).setText("景区");
        
        tabHost.addTab(tabHost.newTabSpec(TYPE_SCENIC)
                .setIndicator(v)
                .setContent(R.id.scenic_listView));

        v = getLayoutInflater().inflate(R.layout.tab_item, null);
        ((TextView)v.findViewById(R.id.title)).setText("娱乐");
        
        tabHost.addTab(tabHost.newTabSpec(TYPE_ENTERTAINMENT)
        		.setIndicator(v)
        		.setContent(R.id.entertainment_listView));
        
        tabHost.setOnTabChangedListener(onTabChangeListener);
        
	}

	private void setListeners()
	{
		food_listView.setOnRefreshListener(onRefreshListener);
		restaurant_listView.setOnRefreshListener(onRefreshListener);
		scenic_listView.setOnRefreshListener(onRefreshListener);
		entertainment_listView.setOnRefreshListener(onRefreshListener);
		
		food_listView.setOnItemClickListener(onItemClickListener);
		restaurant_listView.setOnItemClickListener(onItemClickListener);
		scenic_listView.setOnItemClickListener(onItemClickListener);
		entertainment_listView.setOnItemClickListener(onItemClickListener);
	}
	
	private OnTabChangeListener onTabChangeListener = new OnTabChangeListener()
	{
		@Override
		public void onTabChanged(String tabId)
		{
			PullToRefreshListView  listView = null;
			boolean emptyList = false;
			
			if(tabId.equals(TYPE_FOOD))
			{
				listView = food_listView;
				if(foodData.isEmpty())
				{
					emptyList = true;
				}
			}
			else if (tabId.equals(TYPE_RESTAURANT)) 
			{
				listView = restaurant_listView;
				if(restaurantData.isEmpty())
				{
					emptyList = true;
				}
			}
			else if (tabId.equals(TYPE_SCENIC)) 
			{
				listView = scenic_listView;
				if(scenicData.isEmpty())
				{
					emptyList = true;
				}
			}
			else if (tabId.equals(TYPE_ENTERTAINMENT)) 
			{
				listView = entertainment_listView;
				if(entertainmentData.isEmpty())
				{
					emptyList = true;
				}
			}
			
			if(emptyList)
			{
				loadData((String)listView.getTag(R.id.type));
				showLoading();
			}
		}
	};
	
	private OnRefreshListener2<ListView> onRefreshListener = new OnRefreshListener2<ListView>()
	{
		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
		{
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
		{
			loadData((String)refreshView.getTag(R.id.type));
		}
		
	};

	private OnItemClickListener onItemClickListener = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			Intent intent = new Intent(NearByActivity.this, SearchDetailActivity.class);
			intent.putExtra("id", currentDataList.get(position).getId());
			intent.putExtra("status", (String)currentListView.getTag(R.id.type));
//			intent.putExtra("enterType", );
			startActivity(intent);
		}
	};
	
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

}
