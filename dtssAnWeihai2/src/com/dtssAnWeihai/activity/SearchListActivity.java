package com.dtssAnWeihai.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.dtssAnWeihai.adapter.SearchListAdapter;
import com.dtssAnWeihai.adapter.SearchTypeAdapter;
import com.dtssAnWeihai.entity.SearchListEntity;
import com.dtssAnWeihai.entity.SearchTypeEntity;
import com.dtssAnWeihai.tools.BMapApiDemoApp;
import com.dtssAnWeihai.tools.MyConfig;
import com.dtssAnWeihai.tools.MyStatusEntity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class SearchListActivity extends BaseActivity {

	private Button  search_type, search_sort, search_find;
	private EditText search_edit;
	
	private PullToRefreshListView listView;
	private List<SearchListEntity> allEntities = new ArrayList<SearchListEntity>();
	private List<SearchListEntity> showingEntities = new ArrayList<SearchListEntity>();
	private SearchListAdapter dataAdapter;
	
	private LinearLayout search_searchchoose;
	
	//已显示的页数（每页10项）
	private int currentPage = 1;
	
	private ProgressDialog progressDialog;
	private String sendPostStr = "";// 返回的json字符串
	private String infoType = "";
	private String type = "";
	private String sort = "default";

	private String sendPostTypeStr = "";// 返回的json字符串
	private View typeMenuView = null;
	private View sortMenuView = null;
	private ListView typeListView, sortListView;
	PopupWindow pop;
	
	private String[] sortId = new String[] {"default", "degree", "level"};
	private String[] sortStr = new String[] {"默认", "推荐度", "级别"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_list);
		progressDialog = new ProgressDialog(SearchListActivity.this);
		initView();

		Intent intent = getIntent();
		infoType = intent.getStringExtra("status");
		setTitle(intent.getStringExtra("title"));
		if(infoType.equals("enter")) {
			type = "golf";
			search_type.setText("高尔夫");
		}
		// 隐藏输入键盘
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		// listView单击事件
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				TextView id = (TextView) arg1.findViewById(R.id.search_list_id);
				Intent intent = new Intent(SearchListActivity.this, SearchDetailActivity.class);
				intent.putExtra("id", id.getText().toString());
				intent.putExtra("status", infoType);
				intent.putExtra("enterType", type);
				startActivity(intent);
			}
		});
		

		listView.setMode(Mode.PULL_FROM_END);
		dataAdapter = new SearchListAdapter(SearchListActivity.this, showingEntities, listView.getRefreshableView(), infoType);
		listView.setAdapter(dataAdapter);
		listView.setOnRefreshListener(new OnRefreshListener2<ListView>(){
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
			{
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
			{
				fillCurrentPage(1000);
			}

		});
		
		typeListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				TextView id = (TextView) arg1.findViewById(R.id.popview_item_id);
				TextView name = (TextView) arg1.findViewById(R.id.popview_item_info);
				type = id.getText().toString();
				search_type.setText(name.getText().toString());
				
				currentPage = 1;
				showingEntities.clear();
				allEntities.clear();
				dataAdapter.notifyDataSetChanged();
				
				getInfo();
			}
		});
		sortListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				TextView id = (TextView) arg1.findViewById(R.id.popview_item_id);
				TextView name = (TextView) arg1.findViewById(R.id.popview_item_info);
				sort = id.getText().toString();
				search_sort.setText(name.getText().toString());

				currentPage = 1;
				showingEntities.clear();
				allEntities.clear();
				dataAdapter.notifyDataSetChanged();
				getInfo();
			}
		});

		getInfo();
	}

	/**
	 * 根据page将当前页数据加入页面显示
	 */
	private void fillCurrentPage(long delay)
	{
		fackLoadHandler.sendEmptyMessageDelayed(1, delay);
	}

	private Handler fackLoadHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			for(int i = (currentPage - 1) *10; i<allEntities.size() && i<currentPage*10; i++)
    		{
    			showingEntities.add(allEntities.get(i));
    		}
    		currentPage++;
    		
    		dataAdapter.notifyDataSetChanged();

        	listView.onRefreshComplete();
		}
		
	};
	
	private void initView() {
		search_type = (Button) findViewById(R.id.search_type);
		search_sort = (Button) findViewById(R.id.search_sort);
		search_find = (Button) findViewById(R.id.search_find);
		search_edit = (EditText) findViewById(R.id.search_edit);
		listView = (PullToRefreshListView) findViewById(R.id.search_listView);
		search_searchchoose = (LinearLayout) findViewById(R.id.search_searchchoose);

		typeMenuView = getLayoutInflater().inflate(R.layout.popview_typelist, null);
		typeListView = (ListView) typeMenuView.findViewById(R.id.popview_list);
		
		sortMenuView = getLayoutInflater().inflate(R.layout.popview_sortlist, null);
		sortListView = (ListView) sortMenuView.findViewById(R.id.popview_list);
		
		search_find.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {

				currentPage = 1;
				showingEntities.clear();
				allEntities.clear();
				dataAdapter.notifyDataSetChanged();
				
				getInfo();
			}
		});
		search_type.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showTypePop(typeListView);
				popMenu(typeMenuView, search_type);
			}
		});
		search_sort.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showSortPop(sortListView);
				popMenu(sortMenuView, search_sort);
			}
		});
	}
	
	private void showTypePop(ListView listView) {
		try {
			List<SearchTypeEntity> listType = new ArrayList<SearchTypeEntity>();
			if("enter".equals(infoType)) {
//				jsonArray = new JSONArray(jsonObject.getString("enter"));
				SearchTypeEntity entity1 = new SearchTypeEntity("ent", "休闲娱乐");
				SearchTypeEntity entity2 = new SearchTypeEntity("golf", "高尔夫");
				SearchTypeEntity entity3 = new SearchTypeEntity("ski", "滑雪");
				listType.add(entity1);
				listType.add(entity2);
				listType.add(entity3);
			} else {
				if("".equals(MyStatusEntity.getInstance().getSearchtypeString())) {
					getTypeInfo();
				}
				JSONObject jsonObject = new JSONObject(MyStatusEntity.getInstance().getSearchtypeString());
				JSONArray jsonArray = null;
				if("scenic".equals(infoType))
					jsonArray = new JSONArray(jsonObject.getString("scenic"));
				if("hotel".equals(infoType))
					jsonArray = new JSONArray(jsonObject.getString("hotel"));
				if("res".equals(infoType))
					jsonArray = new JSONArray(jsonObject.getString("res"));
				if("traffic".equals(infoType))
					jsonArray = new JSONArray(jsonObject.getString("traffic"));
				if("shop".equals(infoType))
					jsonArray = new JSONArray(jsonObject.getString("shop"));
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);
					SearchTypeEntity entity = new SearchTypeEntity(object.getString("id"), object.getString("name"));
					listType.add(entity);
				}
			}
			SearchTypeAdapter adapter = new SearchTypeAdapter(SearchListActivity.this, listType);;
			listView.setAdapter(adapter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void showSortPop(ListView listView) {
		try {
			List<SearchTypeEntity> listType = new ArrayList<SearchTypeEntity>();
			int sortLength = 3;
			if("shop".equals(infoType) || "enter".equals(infoType)) {
				// 购物、娱乐 无级别排序
				sortLength = 2;
			}
			for (int i = 0; i < sortLength; i++) {
				SearchTypeEntity entity = new SearchTypeEntity(sortId[i], sortStr[i]);
				listType.add(entity);
			}
			if("scenic".equals(infoType)) {
				// 景区 + 距离 排序
				SearchTypeEntity entity = new SearchTypeEntity("distance", "距离");
				listType.add(entity);
			}
			SearchTypeAdapter adapter = new SearchTypeAdapter(SearchListActivity.this, listType);;
			listView.setAdapter(adapter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getInfo() {
		showLoading();
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
				Map<String, String> params = new HashMap<String, String>();
				params.put("infoType", infoType);
				params.put("longitude", String.valueOf(currentLocation.getLongitude()));
				params.put("latitude", String.valueOf(currentLocation.getLatitude()));
				params.put("condition.areacode", MyConfig.AREAID);
				params.put("condition.keyword", search_edit.getText().toString().trim());
				if(infoType.equals("enter")) {
					params.put("entType", type);
				} else {
					params.put("condition.type", type);
				}
				params.put("condition.sort", sort);
				
				doNetWorkJob(MyConfig.HTTPURL + "/search/infoTypeSearch.action", params, handler);
			}
		}
	};
	

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			
			hideLoading();
			
			try {
				JSONObject jsonObject = (JSONObject) msg.obj;
				JSONArray jsonArray = jsonObject.getJSONArray("data");
				int length = jsonArray.length();
				for (int i = 0; i < length; i++) {
					JSONObject object = jsonArray.getJSONObject(i);
					SearchListEntity entity = new SearchListEntity(object.getString("id"), object.getString("name"), object.getString("address"), object.getString("image"), object.getString("distance"));
					allEntities.add(entity);
				}
				
				fillCurrentPage(10);
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(SearchListActivity.this, "暂未查找到数据!", Toast.LENGTH_SHORT).show();
			}
		}
	};
	

	private void getTypeInfo() {
		showLoading();
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("infoType", "all");
		doNetWorkJob(MyConfig.HTTPURL + "/search/typeSearch.action", params, handler2);
	}
	
	private Handler handler2 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			
			hideLoading();

			if("".equals(sendPostTypeStr)) {
				Toast.makeText(SearchListActivity.this, "连接服务器失败,无数据!", Toast.LENGTH_SHORT).show();
			}
			MyStatusEntity.getInstance().setSearchtypeString(sendPostTypeStr);
			showTypePop(typeListView);
		}
	};
	
	public void popMenu(View menuview, View view) {
		if (pop == null) {
			pop = new PopupWindow(menuview, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
			pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			pop.setAnimationStyle(R.style.AnimationFade);
			pop.setOutsideTouchable(true);
			pop.showAsDropDown(view, Gravity.CENTER_HORIZONTAL, 0);
			pop.update();
		} else {
			if (pop.isShowing()) {
				pop.dismiss();
				pop = null;
			} else {
				pop = null;
				pop = new PopupWindow(menuview, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
				pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				pop.setAnimationStyle(R.style.AnimationFade);
				pop.setOutsideTouchable(false);
				pop.showAsDropDown(view, Gravity.CENTER_HORIZONTAL, 0);
				pop.update();
			}
		}
	}

	@Override
	public void onBackPressed() {
		finish();
	}
}
