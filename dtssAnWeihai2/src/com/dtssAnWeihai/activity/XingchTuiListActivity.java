package com.dtssAnWeihai.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.dtssAnWeihai.adapter.XingchTuiListAdapter;
import com.dtssAnWeihai.entity.XingchTuiListEntity;
import com.dtssAnWeihai.tools.HttpUtil;
import com.dtssAnWeihai.tools.MyConfig;

/**
 * 我的自定义行程——详情列表
 * @author ChenPengyan
 * @Email cpy781@163.com
 * 2014-6-14
 */
public class XingchTuiListActivity extends Activity {
	
	private Button back;
	private TextView xingch_tui_list_title, xingch_tui_list_days;
	private ListView listView;
	
	private String id;
	private String sendPostStr;
	private ProgressDialog progressDialog;
	private List<XingchTuiListEntity> list = new ArrayList<XingchTuiListEntity>();
	private XingchTuiListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xingch_tui_list);
		initView();
		progressDialog = new ProgressDialog(XingchTuiListActivity.this);
		
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		
		showInfo();
	}
	
	private void initView() {
		back = (Button) findViewById(R.id.xingch_tui_list_back);
		xingch_tui_list_title = (TextView) findViewById(R.id.xingch_tui_list_title);
		xingch_tui_list_days = (TextView) findViewById(R.id.xingch_tui_list_days);
		listView = (ListView) findViewById(R.id.xingch_tui_list_listView);
		
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	private void showInfo() {
		progressDialog.setMessage("正在获取数据...");
		progressDialog.setCancelable(false);
		progressDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("unitId", id);
					sendPostStr = HttpUtil.http(MyConfig.HTTPURL + "/scheduling/schedulingDetail.action", params);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
			}
		}).start();
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				progressDialog.dismiss();
				try {
					JSONObject jsonObject = new JSONObject(sendPostStr);
					xingch_tui_list_title.setText(jsonObject.getString("title"));
					xingch_tui_list_days.setText("行程：" + jsonObject.getString("days") + "天");
					if(!"0".equals(jsonObject.getString("count"))) {
						JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject object = jsonArray.getJSONObject(i);
							XingchTuiListEntity entity = new XingchTuiListEntity(object.getString("startDate"), object.getString("dayDetail"), object.getString("day"));
							list.add(entity);
						}
						adapter = new XingchTuiListAdapter(XingchTuiListActivity.this, list);
						listView.setAdapter(adapter);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
	};
	
	@Override
	public void onBackPressed() {
		finish();
	}
}
