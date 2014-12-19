package com.dtssAnWeihai.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dtssAnWeihai.entity.SearchListEntity;
import com.dtssAnWeihai.tools.MyConfig;
import com.dtssAnWeihai.tools.MyTools;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NearByDetailActivity extends BaseActionBarActivity
{
	private TextView name;
	private TextView timeBegin;
	private TextView timeEnd;
	private TextView when;
	private TextView hostCity;
	private TextView url;
	private TextView hotline;
	private TextView where;
	private TextView what;
	private TextView booking_notice;
	private TextView fee_notice;
	private TextView other_notice;
	
	private ImageView image;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nearby_detail);
		setTitle("详情");
		
		initViews();
		setListeners();
		
		loadData(getIntent().getStringExtra("id"));
	}
	
	private void loadData(String id)
	{
		showLoading();
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("unitId", id);
		params.put("areacode", MyConfig.AREAID);
		
		doNetWorkJob(MyConfig.HTTPURL + "/search/benefitShop/findjuBenefit.action", params, handler);
	}
	
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			JSONObject jsonObject = (JSONObject) msg.obj;
			
			try
			{
				MyTools.showImageFromPath(image, "http://www.sdta.cn/dtss" + jsonObject.getString("image"), 480, 320);
				
				name.setText(jsonObject.getString("name"));
				timeBegin.setText(jsonObject.getString("BenStartDate"));
				timeEnd.setText(jsonObject.getString("BenEndDate"));
//				when.setText(jsonObject.getString("BenStartDate"));
//				hostCity.setText(jsonObject.getString("BenStartDate"));
//				url.setText(jsonObject.getString("BenStartDate"));
				hotline.setText(jsonObject.getString("contact"));
				where.setText(jsonObject.getString("address"));
				what.setText(jsonObject.getString("des"));
				
				booking_notice.setText(jsonObject.getString("desc"));
				fee_notice.setText(jsonObject.getString("BenCond"));
				other_notice.setText(jsonObject.getString("Bendesc"));
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
			
			hideLoading();
		}
	};

	private void initViews()
	{
		image = (ImageView) findViewById(R.id.image);
		
		name = (TextView) findViewById(R.id.name);
		timeBegin = (TextView) findViewById(R.id.timeBegin);
		timeEnd = (TextView) findViewById(R.id.timeEnd);
		when = (TextView) findViewById(R.id.when);
		hostCity = (TextView) findViewById(R.id.hostCity);
		url = (TextView) findViewById(R.id.url);
		hotline = (TextView) findViewById(R.id.hotline);
		where = (TextView) findViewById(R.id.where);
		what = (TextView) findViewById(R.id.what);
		booking_notice = (TextView) findViewById(R.id.booking_notice);
		fee_notice = (TextView) findViewById(R.id.fee_notice);
		other_notice = (TextView) findViewById(R.id.other_notice);
	}

	private void setListeners()
	{
	}

	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

}
