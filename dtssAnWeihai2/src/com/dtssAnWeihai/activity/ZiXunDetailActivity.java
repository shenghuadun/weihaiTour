package com.dtssAnWeihai.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebView;
import android.widget.TextView;

public class ZiXunDetailActivity extends BaseActivity{
	
	private TextView titleTextView;
	private WebView contentView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zixun_detail);
		setTitle("资讯");
		
		titleTextView = (TextView) findViewById(R.id.title);
		contentView = (WebView) findViewById(R.id.content);

		String id = getIntent().getStringExtra("id");
		
		showLoading();
		Map<String, String> params = new HashMap<String, String>();
		params.put("unitId", id);
		doNetWorkJob("http://60.216.117.244:80/wisdomyt/search/findTourismDynamicDetail.action", params, handler);
	}

	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg)
		{
			JSONObject json = (JSONObject) msg.obj;
			
			try
			{
				String title = json.getString("title");
				String content = json.getString("cont");
				
				titleTextView.setText(title);
				contentView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);  

				hideLoading();
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
		
	};
}
