package com.dtssAnWeihai.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dtssAnWeihai.tools.HttpUtil;
import com.dtssAnWeihai.tools.MyConfig;
import com.dtssAnWeihai.tools.MyTools;

/**
 * 旅游攻略-详情页
 * @author ChenPengyan
 * @Email cpy781@163.com
 * 2014-5-23
 */
public class DarenDetailActivity extends Activity {

	private Button back, daren_detail_add;
	private TextView daren_detail_title, daren_detail_author, daren_detail_type, daren_detail_info;
	private ImageView daren_detail_image;

	private ProgressDialog progressDialog;
	private String sendPostStr = "";// 返回的json字符串
	private String id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.daren_detail);
		initView();
		progressDialog = new ProgressDialog(DarenDetailActivity.this);

		Intent intent = getIntent();
		id = intent.getStringExtra("id");

		back = (Button) findViewById(R.id.daren_detail_back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		progressDialog.setMessage("正在获取数据...");
		progressDialog.setCancelable(false);
		progressDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("superiorProgramId", id);
					sendPostStr = HttpUtil.http(MyConfig.HTTPURL + "/travelDaren/superiorViewJson.action", params);
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
					JSONObject object = new JSONObject(sendPostStr);
					
					MyTools.showImageFromPath(daren_detail_image, object.getString("image"), 480, 320);
					daren_detail_title.setText(object.getString("title"));
					daren_detail_author.setText("["+object.getString("guiname")+"]");
					daren_detail_type.setText("旅游类型："+object.getString("type"));
					daren_detail_info.setText(object.getString("summry"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
	};

	private void initView() {
		daren_detail_image = (ImageView) findViewById(R.id.daren_detail_image);
		daren_detail_title = (TextView) findViewById(R.id.daren_detail_title);
		daren_detail_author = (TextView) findViewById(R.id.daren_detail_author);
		daren_detail_type = (TextView) findViewById(R.id.daren_detail_type);
		daren_detail_info = (TextView) findViewById(R.id.daren_detail_info);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
