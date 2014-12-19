package com.dtssAnWeihai.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.dtssAnWeihai.adapter.BenefitAdapter;
import com.dtssAnWeihai.entity.BenefitEntity;
import com.dtssAnWeihai.tools.BMapApiDemoApp;
import com.dtssAnWeihai.tools.MyConfig;

/**
 * 举优惠列表
 * 
 * @author ChenPengyan
 * @Email cpy781@163.com 2014-4-17
 */
public class ShakeListActivity extends BaseActionBarActivity implements OnClickListener {
	private ListView listView;
	private Button shake_res, shake_hotel, shake_scenic, shake_play, shake_shop;
	
	private BenefitAdapter adapter;
	private List<BenefitEntity> list = new ArrayList<BenefitEntity>();
	
	private String status;
	private ProgressDialog progressDialog;
	private String sendPostStr = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shake_list);
		initView();
		
		setTitle("摇一摇");
		
		shake_res.setBackgroundColor(android.graphics.Color.parseColor("#39ABD3"));
		progressDialog = new ProgressDialog(ShakeListActivity.this);
		
		Intent intent = getIntent();
		sendPostStr = intent.getStringExtra("sendPostStr");
		
		setInfoShow();
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				TextView id = (TextView) arg1.findViewById(R.id.listitem_id);
				Intent intent = new Intent(ShakeListActivity.this, ShakeDetailActivity.class);
				intent.putExtra("id", id.getText().toString());
				intent.putExtra("status", status);
				startActivity(intent);
			}
		});
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.shakelist_listView);
		// tab各个选项
		shake_res = (Button) findViewById(R.id.shakelist_res);
		shake_res.setOnClickListener(this);
		shake_hotel = (Button) findViewById(R.id.shakelist_hotel);
		shake_hotel.setOnClickListener(this);
		shake_scenic = (Button) findViewById(R.id.shakelist_scenic);
		shake_scenic.setOnClickListener(this);
		shake_play = (Button) findViewById(R.id.shakelist_play);
		shake_play.setOnClickListener(this);
		shake_shop = (Button) findViewById(R.id.shakelist_shop);
		shake_shop.setOnClickListener(this);
	}

	/**
	 * 连接服务器获取数据
	 * @param type
	 */
	private void searchBenefit(final String type) {
		list.clear();
		
		Message msg = locationQueryHandler.obtainMessage();
		msg.obj = type;
		locationQueryHandler.sendMessage(msg);
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
				locationQueryHandler.sendMessageDelayed(msg, 1000);
			}
			else
			{
				hideLoading();
				
				Map<String, String> params = new HashMap<String, String>();
				params.put("condition.infoType", (String)msg.obj);
				params.put("condition.areacode", MyConfig.AREAID);

				params.put("longitude", String.valueOf(currentLocation.getLongitude()));
				params.put("latitude", String.valueOf(currentLocation.getLatitude()));
				
				doNetWorkJob(MyConfig.HTTPURL + "/search/benefitShop/juBenefit.action", params, handler);
			}
		}
	};
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			hideLoading();
			setInfoShow();
		}
	};
	
	private void setInfoShow() {
		try {
			JSONObject jObject = new JSONObject(sendPostStr);
			JSONArray jsonArray = jObject.getJSONArray("data");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				BenefitEntity entity = new BenefitEntity(jsonObject.getString("unitId"), jsonObject.getString("benName"), jsonObject.getString("BenCond"), jsonObject.getString("image"), jsonObject.getString("address"), jsonObject.getString("benDesc"), jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("distance"), jsonObject.getString("BenStartDate"), jsonObject.getString("BenEndDate"), jsonObject.getString("jifenPrice"), jsonObject.getString("coupon"));
				list.add(entity);
			}
			adapter = new BenefitAdapter(ShakeListActivity.this, list, listView);
			listView.setAdapter(adapter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onClick(View v) {
		// 选中的背景色
		String downColor = "#50C2E9";
		// 未选中的背景色
		String onColor = "#39ABD3";
		switch (v.getId()) {
		case R.id.shakelist_res:
			shake_res.setBackgroundColor(android.graphics.Color.parseColor(onColor));
			shake_hotel.setBackgroundColor(android.graphics.Color.parseColor(downColor));
			shake_scenic.setBackgroundColor(android.graphics.Color.parseColor(downColor));
			shake_play.setBackgroundColor(android.graphics.Color.parseColor(downColor));
			shake_shop.setBackgroundColor(android.graphics.Color.parseColor(downColor));
			status = "res";
			searchBenefit("res");
			break;
		case R.id.shakelist_hotel:
			shake_res.setBackgroundColor(android.graphics.Color.parseColor(downColor));
			shake_hotel.setBackgroundColor(android.graphics.Color.parseColor(onColor));
			shake_scenic.setBackgroundColor(android.graphics.Color.parseColor(downColor));
			shake_play.setBackgroundColor(android.graphics.Color.parseColor(downColor));
			shake_shop.setBackgroundColor(android.graphics.Color.parseColor(downColor));
			status = "hotel";
			searchBenefit("hotel");
			break;
		case R.id.shakelist_scenic:
			shake_res.setBackgroundColor(android.graphics.Color.parseColor(downColor));
			shake_hotel.setBackgroundColor(android.graphics.Color.parseColor(downColor));
			shake_scenic.setBackgroundColor(android.graphics.Color.parseColor(onColor));
			shake_play.setBackgroundColor(android.graphics.Color.parseColor(downColor));
			shake_shop.setBackgroundColor(android.graphics.Color.parseColor(downColor));
			status = "scenic";
			searchBenefit("scenic");
			break;
		case R.id.shakelist_play:
			shake_res.setBackgroundColor(android.graphics.Color.parseColor(downColor));
			shake_hotel.setBackgroundColor(android.graphics.Color.parseColor(downColor));
			shake_scenic.setBackgroundColor(android.graphics.Color.parseColor(downColor));
			shake_play.setBackgroundColor(android.graphics.Color.parseColor(onColor));
			shake_shop.setBackgroundColor(android.graphics.Color.parseColor(downColor));
			status = "entr";
			searchBenefit("entr");
			break;
		case R.id.shakelist_shop:
			shake_res.setBackgroundColor(android.graphics.Color.parseColor(downColor));
			shake_hotel.setBackgroundColor(android.graphics.Color.parseColor(downColor));
			shake_scenic.setBackgroundColor(android.graphics.Color.parseColor(downColor));
			shake_play.setBackgroundColor(android.graphics.Color.parseColor(downColor));
			shake_shop.setBackgroundColor(android.graphics.Color.parseColor(onColor));
			status = "shop";
			searchBenefit("shop");
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		ShakeListActivity.this.finish();
	}
	
}
