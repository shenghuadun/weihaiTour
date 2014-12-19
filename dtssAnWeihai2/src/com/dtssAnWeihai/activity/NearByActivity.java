package com.dtssAnWeihai.activity;

import java.util.ArrayList;
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
	
	private PullToRefreshListView scenic_listView;
	private List<SearchListEntity> scenicData = new ArrayList<SearchListEntity>();
	private SearchListAdapter scenicAdapter;

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
						entity.setId(json.getString("unitId"));
						entity.setAddress(json.getString("address"));
						entity.setDistance(json.getString("distance"));
						entity.setImage(json.getString("image"));
						entity.setName(json.getString("name"));
						
						scenicData.add(entity);
					}
					
					scenicAdapter.notifyDataSetChanged();

					String page = (String) scenic_listView.getTag(R.id.page);
					scenic_listView.setTag(R.id.page, String.valueOf(Integer.valueOf(page) + 1));
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
			
		
			scenic_listView.onRefreshComplete();
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

		loadData();
	}
	
	private void loadData()
	{
		scenic_listView.setRefreshing(true);
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
				showLoading();
				int page = Integer.valueOf((String)scenic_listView.getTag(R.id.page));
				
				Map<String, String> params = new HashMap<String, String>();
				params.put("currentPage", String.valueOf(page));
				params.put("condition.infoType", (String)scenic_listView.getTag(R.id.type));
				params.put("longitude", String.valueOf(currentLocation.getLongitude()));
				params.put("latitude", String.valueOf(currentLocation.getLatitude()));
				params.put("condition.areacode", MyConfig.AREAID);
				
				doNetWorkJob(MyConfig.HTTPURL + "/search/benefitShop/juBenefit.action", params, handler);
			}
		}
		
	};

	private void initViews()
	{
		scenic_listView = (PullToRefreshListView) findViewById(R.id.scenic_listView);

		scenic_listView.setTag(R.id.page, "1");
		scenic_listView.setTag(R.id.type, TYPE_SCENIC);
		scenic_listView.setEmptyView(getLayoutInflater().inflate(R.layout.pulltorefresh_empty, null));
		
		scenic_listView.setMode(Mode.PULL_FROM_END);
		scenicAdapter = new SearchListAdapter(this, scenicData, scenic_listView.getRefreshableView(), TYPE_SCENIC);
		scenic_listView.setAdapter(scenicAdapter);
        
	}

	private void setListeners()
	{
		scenic_listView.setOnRefreshListener(onRefreshListener);
		scenic_listView.setOnItemClickListener(onItemClickListener);
	}

	private OnRefreshListener2<ListView> onRefreshListener = new OnRefreshListener2<ListView>()
	{
		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
		{
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
		{
			loadData();
		}
		
	};

	private OnItemClickListener onItemClickListener = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			Intent intent = new Intent(NearByActivity.this, NearByDetailActivity.class);
			intent.putExtra("id", scenicData.get(position - 1).getId());
//			intent.putExtra("status", (String)scenic_listView.getTag(R.id.type));
//			intent.putExtra("enterType", );
//			intent.putExtra("isFromNearBy", true);
			startActivity(intent);
		}
	};
	
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

}
