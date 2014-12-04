package com.dtssAnWeihai.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dtssAnWeihai.adapter.LineAdapter;
import com.dtssAnWeihai.adapter.SearchTypeAdapter;
import com.dtssAnWeihai.entity.LineEntity;
import com.dtssAnWeihai.entity.SearchTypeEntity;
import com.dtssAnWeihai.tools.DatabaseHelper;
import com.dtssAnWeihai.tools.MyConfig;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 行程推荐
 * 
 * @author ChenPengyan
 * @Email cpy781@163.com 2014-4-25
 */
public class LineActivity extends BaseActivity {
	
	private Button search_type, search_sort, search_find;
	private EditText search_edit;
	private RelativeLayout search_findlayout;
	private LinearLayout search_searchchoose;
	
	private PullToRefreshListView listView;
	private List<LineEntity> lineDataList = new ArrayList<LineEntity>();;
	private LineAdapter adapter;
	
	private View typeMenuView = null;
	private View sortMenuView = null;
	
	private String type = "";
	private String sort = "";

	private int currentPage = 1;// 当前显示的页数

	private ListView typeListView;
	private List<SearchTypeEntity> searchTypeEntities = new ArrayList<SearchTypeEntity>();
	private SearchTypeAdapter searchTypeAdapter;
	
	private ListView sortListView;
	private List<SearchTypeEntity> sortTypeEntities = new ArrayList<SearchTypeEntity>();
	private SearchTypeAdapter sortTypeAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_list);
		initView();
		
		// 隐藏输入键盘
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		// listView单击事件
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				TextView id = (TextView) arg1.findViewById(R.id.listitem_id);
				Intent intent = new Intent(LineActivity.this, LineDetailActivity.class);
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
				getInfo(currentPage, false);
			}

		});
		
		Intent intent = getIntent();
		String status = intent.getStringExtra("status");
		if(status.equals("1")) {
			getInfo(1, true);
			getTypeInfo();

			setTitle("行程推荐");
		} else {
			search_findlayout.setVisibility(View.GONE);
			search_searchchoose.setVisibility(View.GONE);
			getDBInfo();

			setTitle("我的行程");
		}
	}
	
	private void initView() {
		search_type = (Button) findViewById(R.id.search_type);
		search_sort = (Button) findViewById(R.id.search_sort);
		search_find = (Button) findViewById(R.id.search_find);
		search_edit = (EditText) findViewById(R.id.search_edit);
		
		listView = (PullToRefreshListView) findViewById(R.id.search_listView);
		adapter = new LineAdapter(LineActivity.this, lineDataList, listView.getRefreshableView());
		listView.setAdapter(adapter);
		listView.setMode(Mode.PULL_FROM_END);
		
		search_findlayout = (RelativeLayout) findViewById(R.id.search_findlayout);
		search_searchchoose = (LinearLayout) findViewById(R.id.search_searchchoose);
		
		typeMenuView = getLayoutInflater().inflate(R.layout.popview_typelist, null);
		typeListView = (ListView) typeMenuView.findViewById(R.id.popview_list);
		searchTypeAdapter = new SearchTypeAdapter(this, searchTypeEntities);
		typeListView.setAdapter(searchTypeAdapter);
		
		sortMenuView = getLayoutInflater().inflate(R.layout.popview_sortlist, null);
		sortListView = (ListView) sortMenuView.findViewById(R.id.popview_list);
		sortTypeEntities.add(new SearchTypeEntity("degree", "推荐"));
		sortTypeAdapter = new SearchTypeAdapter(this, sortTypeEntities);
		sortListView.setAdapter(sortTypeAdapter);
		
		search_find.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				lineDataList.clear();
				getInfo(1, true);
			}
		});
		search_type.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				popMenu(typeMenuView, search_type);
			}
		});
		search_sort.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				popMenu(sortMenuView, search_sort);
			}
		});
		
		typeListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				TextView id = (TextView) arg1.findViewById(R.id.popview_item_id);
				type = id.getText().toString();
				lineDataList.clear();
				adapter.notifyDataSetChanged();
				getInfo(1, true);
			}
		});
		sortListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				TextView id = (TextView) arg1.findViewById(R.id.popview_item_id);
				sort = id.getText().toString();
				lineDataList.clear();
				adapter.notifyDataSetChanged();
				getInfo(1, true);
			}
		});
		
	}
	
	private void getDBInfo() {
		showLoading();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try
				{
					DatabaseHelper database = new DatabaseHelper(getApplicationContext());
					SQLiteDatabase db = database.getReadableDatabase();
					Cursor cursor = db.rawQuery("SELECT * FROM dtssAnWH_scheduling", null);
					while (cursor.moveToNext())
					{
						LineEntity entity = new LineEntity(cursor.getString(cursor.getColumnIndex("_id")), cursor.getString(cursor.getColumnIndex("_name")), cursor.getString(cursor.getColumnIndex("_phone")), cursor.getString(cursor.getColumnIndex("_image")));
						lineDataList.add(entity);
					}
					cursor.close();
					db.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

				dbHandler.sendEmptyMessage(0);
			}
		}).start();
	}

	private Handler dbHandler = new Handler() 
	{
		public void handleMessage(android.os.Message msg) 
		{
			hideLoading();
			adapter.notifyDataSetChanged();
		}
	};
	
	private void getInfo(final int page, boolean showLoading) {
		if(showLoading)
		{
			showLoading();
		}
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("currentPage", String.valueOf(page));
		params.put("condition.name", search_edit.getText().toString().trim());
		params.put("condition.typeCode", type);
		params.put("condition.sort", sort);
		
		doNetWorkJob(MyConfig.HTTPURL + "/line/lineProduct.action", params, handler);
	}

	private Handler handler = new Handler() 
	{
		public void handleMessage(android.os.Message msg)
		{
			hideLoading();
			try 
			{
				JSONObject jsonObject = (JSONObject) msg.obj;
				
				if(!jsonObject.isNull("data"))
				{
					JSONArray jsonArray = jsonObject.getJSONArray("data");
					for (int i = 0; i < jsonArray.length(); i++) 
					{
						JSONObject object = jsonArray.getJSONObject(i);
						LineEntity entity = new LineEntity(object.getString("id"), object.getString("name"),object.getString("phone"), object.getString("image"));
						lineDataList.add(entity);
					}

					adapter.notifyDataSetChanged();
					currentPage++;
				}
				else
				{
					Toast.makeText(getApplicationContext(), "没有了", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			listView.onRefreshComplete();
		}
	};
	
	private void getTypeInfo() 
	{
		showLoading();
		
		doNetWorkJob(MyConfig.HTTPURL + "/line/lineType/list.action", null, typeHandler);
	}


	private Handler typeHandler = new Handler() 
	{
		public void handleMessage(android.os.Message msg) 
		{
			hideLoading();
			
			JSONObject jsonObject = (JSONObject) msg.obj;
			JSONArray jsonArray;
			try
			{
				jsonArray = jsonObject.getJSONArray("data");
				for (int i = 0; i < jsonArray.length(); i++) 
				{
					JSONObject object = jsonArray.getJSONObject(i);
					SearchTypeEntity entity = new SearchTypeEntity(object.getString("code"), object.getString("name"));
					searchTypeEntities.add(entity);
				}
				searchTypeAdapter.notifyDataSetChanged();
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
	};
	

	
	PopupWindow pop;
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
