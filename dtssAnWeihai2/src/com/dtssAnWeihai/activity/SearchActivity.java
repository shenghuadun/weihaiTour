package com.dtssAnWeihai.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends BaseActivity implements OnClickListener{
	private static final int BACK_COLOR_SELECTED = Color.rgb(209, 231, 255);
	private static final int BACK_COLOR_NORMAL = Color.rgb(232, 243, 255);

	private static final String SCENIC = "scenic";
	private static final String HOTEL = "hotel";
	private static final String RES = "res";
	private static final String TRAFFIC = "traffic";
	private static final String SHOPPING = "shop";
	private static final String PLAY = "enter";
	
	private RelativeLayout btnSenic, btnHotel, btnRes, btnTraffic, btnShopping, btnPlay;
	
	private ListView listView;
	private ArrayAdapter<String > adapter;
	private List<String> level2Data = new ArrayList<String>();
	private Map<String, String> map = new HashMap<String, String>();
	
	private String currentType = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		setTitle("搜索");
		
		findViews();
		setListeners();
		
		btnSenic.performClick();
	}

	private void findViews()
	{
		btnSenic = (RelativeLayout) findViewById(R.id.scenic);
		btnHotel = (RelativeLayout) findViewById(R.id.hotel);
		btnRes = (RelativeLayout) findViewById(R.id.res);
		btnTraffic = (RelativeLayout) findViewById(R.id.traffic);
		btnShopping = (RelativeLayout) findViewById(R.id.shopping);
		btnPlay = (RelativeLayout) findViewById(R.id.play);
		
		listView = (ListView) findViewById(R.id.level2List);
		adapter = new ArrayAdapter<String>(SearchActivity.this, R.layout.search_level2, R.id.text, level2Data){
			@Override
			public View getView(int position, View convertView, ViewGroup parent)
			{
				View view = super.getView(position, convertView, parent);
				view.setOnClickListener(level2OnclickListener);
				
				return view;
			}
			
		};
		listView.setAdapter(adapter);
	}

	private OnClickListener level2OnclickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			try
			{
				TextView tv = (TextView) v;
				String name = tv.getText().toString();
				
				Intent intent = new Intent(SearchActivity.this, SearchDetailActivity.class);
				intent.putExtra("id", map.get(name));
				intent.putExtra("status", currentType);
				startActivity(intent);
			}
			catch (Exception e) 
			{
			}
		}
	};
	
	private void setListeners()
	{
		btnSenic.setOnClickListener(this);
		btnHotel.setOnClickListener(this);
		btnRes.setOnClickListener(this);
		btnTraffic.setOnClickListener(this);
		btnShopping.setOnClickListener(this);
		btnPlay.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		resetLevel1Background();
		
		switch (v.getId())
		{
		case R.id.scenic:
			btnSenic.setBackgroundColor(BACK_COLOR_SELECTED);
			getDataList(SCENIC);
			break;
		case R.id.hotel:
			btnHotel.setBackgroundColor(BACK_COLOR_SELECTED);
			getDataList(HOTEL);
			break;
		case R.id.res:
			btnRes.setBackgroundColor(BACK_COLOR_SELECTED);
			getDataList(RES);
			break;
		case R.id.traffic:
			btnTraffic.setBackgroundColor(BACK_COLOR_SELECTED);
			getDataList(TRAFFIC);
			break;
		case R.id.shopping:
			btnShopping.setBackgroundColor(BACK_COLOR_SELECTED);
			getDataList(SHOPPING);
			break;
		case R.id.play:
			btnPlay.setBackgroundColor(BACK_COLOR_SELECTED);
			getDataList(PLAY);
			break;

		default:
			break;
		}
	}
	
	private void resetLevel1Background()
	{
		btnSenic.setBackgroundColor(BACK_COLOR_NORMAL);
		btnHotel.setBackgroundColor(BACK_COLOR_NORMAL);
		btnRes.setBackgroundColor(BACK_COLOR_NORMAL);
		btnTraffic.setBackgroundColor(BACK_COLOR_NORMAL);
		btnShopping.setBackgroundColor(BACK_COLOR_NORMAL);
		btnPlay.setBackgroundColor(BACK_COLOR_NORMAL);
	}
	
	private void getDataList(String type)
	{
		currentType = type;
		Map<String, String > params = new HashMap<String, String>();
		params.put("infoType", type);
		doNetWorkJob("http://60.216.117.244/wisdomyt/search/typeSearch.action", params, handler);
	}
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg)
		{
			JSONObject json = (JSONObject) msg.obj;
			
			try
			{
				JSONArray list = json.getJSONArray("data");
				
				int size = list.length();
				if(size > 0)
				{
					level2Data.clear();
					map.clear();
					
					for(int i=0; i<size; i++)
					{
						JSONObject j = list.getJSONObject(i);
						map.put(j.getString("name"), j.getString("id"));
						level2Data.add(j.getString("name"));
					}
					
					adapter.notifyDataSetChanged();
				}
			}
			catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(SearchActivity.this, "没有数据！", Toast.LENGTH_SHORT).show();
			}
		}
		
	};
	
}
