package com.dtssAnWeihai.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dtssAnWeihai.adapter.ActiveAdapter;
import com.dtssAnWeihai.entity.ActiveEntity;
import com.dtssAnWeihai.tools.MyConfig;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 节庆活动
 * 
 * @author ChenPengyan
 * @Email cpy781@163.com 2014-4-25
 */
public class ActiveActivity extends BaseActivity{

	private Button active_year, active_month, active_feature;
	private PullToRefreshListView listView;
	private ActiveAdapter adapter;
	private List<ActiveEntity> dataList = new ArrayList<ActiveEntity>();

	private int currentPage = 1;// 当前显示的页数
	private String type = "year"; // 活动类型（year:活动年历 month:当月活动 feature:特色活动）
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.active);
		
		setTitle("活动");
		initView();

		// listView单击事件
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				TextView id = (TextView) arg1.findViewById(R.id.listitem_id);
				Intent intent = new Intent(ActiveActivity.this, ActiveDetailActivity.class);
				intent.putExtra("id", id.getText().toString());
				startActivity(intent);
			}
		});
		listView.setOnRefreshListener(new OnRefreshListener2<ListView>(){
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
			{
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
			{
					getInfo(currentPage, type, false);
			}

		});
		
		//第一次加载
		listView.setRefreshing();
		getInfo(currentPage, type, true);
	}
	
	private void initView() {
		active_year = (Button) findViewById(R.id.active_year);
		active_month = (Button) findViewById(R.id.active_month);
		active_feature = (Button) findViewById(R.id.active_feature);
		
		listView = (PullToRefreshListView) findViewById(R.id.active_listView);
		listView.setMode(Mode.PULL_FROM_END);
		// 添加adpter数据
		adapter = new ActiveAdapter(ActiveActivity.this, dataList, listView.getRefreshableView());
		listView.setAdapter(adapter);
		
		active_year.setOnClickListener(onClick);
		active_month.setOnClickListener(onClick);
		active_feature.setOnClickListener(onClick);

	}
	
	private OnClickListener onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// 选中的背景色
			String downColor = "#50C2E9";
			// 未选中的背景色
			String onColor = "#39ABD3";
			switch (v.getId()) {
			case R.id.active_year:
				active_year.setBackgroundColor(android.graphics.Color.parseColor(onColor));
				active_month.setBackgroundColor(android.graphics.Color.parseColor(downColor));
				active_feature.setBackgroundColor(android.graphics.Color.parseColor(downColor));
				type = "year";
				getInfo(1, type, true);
				break;
			case R.id.active_month:
				active_year.setBackgroundColor(android.graphics.Color.parseColor(downColor));
				active_month.setBackgroundColor(android.graphics.Color.parseColor(onColor));
				active_feature.setBackgroundColor(android.graphics.Color.parseColor(downColor));
				type = "month";
				getInfo(1, type, true);
				break;
			case R.id.active_feature:
				active_year.setBackgroundColor(android.graphics.Color.parseColor(downColor));
				active_month.setBackgroundColor(android.graphics.Color.parseColor(downColor));
				active_feature.setBackgroundColor(android.graphics.Color.parseColor(onColor));
				type = "feature";
				getInfo(1, type, true);
				break;
			default:
				break;
			}

			currentPage = 1;
			dataList.clear();
			adapter.notifyDataSetChanged();
		}
	};
	
	private void getInfo(final int page, final String type, boolean showLoading) 
	{
		if(showLoading)
		{
			showLoading();
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("currentPage", String.valueOf(page));
		params.put("condition.type", type);
		params.put("areaId", MyConfig.AREAID);
		
		doNetWorkJob(MyConfig.HTTPURL + "/search/ActivitySearch/getInfo.action", params, handler);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			try 
			{
				hideLoading();
				
				JSONObject jsonObject = (JSONObject) msg.obj;
				int count = Integer.parseInt(jsonObject.getString("count"));
				if(0 == count) 
				{
					Toast.makeText(ActiveActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
				} 
				else 
				{
					if(!jsonObject.isNull("data"))
					{
						JSONArray jsonArray = jsonObject.getJSONArray("data");
						for (int i = 0; i < jsonArray.length(); i++)
						{
							JSONObject object = jsonArray.getJSONObject(i);
							ActiveEntity entity = new ActiveEntity(object.getString("id"), object.getString("ActEndDate"), object.getString("actTime"), object.getString("image"), object.getString("ActStartDate"), object.getString("actName"), object.getString("actType"));
							dataList.add(entity);
						}
						adapter.notifyDataSetChanged();
						currentPage++;
					}
					else
					{
						Toast.makeText(ActiveActivity.this, "没有了", Toast.LENGTH_SHORT).show();
					}
				}
				
				listView.onRefreshComplete();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	};
}
