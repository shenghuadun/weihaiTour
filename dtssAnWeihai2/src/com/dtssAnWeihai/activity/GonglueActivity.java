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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.dtssAnWeihai.adapter.GonglueAdapter;
import com.dtssAnWeihai.entity.GonglueEntity;
import com.dtssAnWeihai.tools.MyConfig;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 攻略
 * @author ChenPengyan
 * @Email cpy781@163.com
 * 2014-6-14
 */
public class GonglueActivity extends BaseActivity {
	private PullToRefreshListView listView;
	private List<GonglueEntity> list = new ArrayList<GonglueEntity>();
	private GonglueAdapter adapter;
	
	private int currentPage = 1;// 当前显示的页数
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		setTitle("旅游攻略");
		initView();

		// listView单击事件
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				TextView id = (TextView) arg1.findViewById(R.id.listitem_id);
				Intent intent = new Intent(GonglueActivity.this, GonglueDetailActivity.class);
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
				getInfo(currentPage);
			}

		});
		
		getInfo(currentPage);
	}
	
	private void initView() {
		listView = (PullToRefreshListView) findViewById(R.id.list_listView);

		listView.setMode(Mode.PULL_FROM_END);
		adapter = new GonglueAdapter(GonglueActivity.this, list, listView.getRefreshableView());
		listView.setAdapter(adapter);
	}
	
	private void getInfo(final int page) {
		showLoading();
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("areaId", MyConfig.AREAID);
		params.put("currentPage", String.valueOf(currentPage));
	
		doNetWorkJob(MyConfig.HTTPURL + "/search/SubjectSearch/searchBySubjectProduct.action", params, handler);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			
			hideLoading();
			
			try {
				JSONObject jsonObject = (JSONObject) msg.obj;
				
				JSONArray jsonArray = jsonObject.getJSONArray("data");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);
					GonglueEntity entity = new GonglueEntity(object.getString("id"), object.getString("name"), object.getString("image"), object.getString("type"));
					list.add(entity);
				}
				currentPage++;
				adapter.notifyDataSetChanged();
				listView.onRefreshComplete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
}
