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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dtssAnWeihai.adapter.CommAdapter;
import com.dtssAnWeihai.entity.CommEntity;
import com.dtssAnWeihai.tools.HttpUtil;
import com.dtssAnWeihai.tools.MyConfig;
import com.dtssAnWeihai.tools.MyLoginUser;

/**
 * 评论
 * 
 * @author ChenPengyan
 * @Email cpy781@163.com 2014-6-10
 */
public class CommActivity extends Activity {
	
	private Button comm_back, comm_submit;
	private EditText comm_desc;
	private ListView comm_listView;
	
	private String status;
	private String id;
	private String name;
	private ProgressDialog progressDialog;
	private String sendPostStr = "";// 返回的json字符串
	
	private List<CommEntity> list = new ArrayList<CommEntity>();
	private CommAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comm);
		progressDialog = new ProgressDialog(CommActivity.this);
		
		Intent intent = getIntent();
		status = intent.getStringExtra("status");
		id = intent.getStringExtra("id");
		name = intent.getStringExtra("name");
		
		comm_desc = (EditText) findViewById(R.id.comm_desc);
		comm_back = (Button) findViewById(R.id.comm_back);
		comm_submit = (Button) findViewById(R.id.comm_submit);
		comm_listView = (ListView) findViewById(R.id.comm_listView);
		
		comm_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		comm_submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if("".equals(comm_desc.getText().toString().trim())) {
					Toast.makeText(CommActivity.this, "请输入您的评论内容!", Toast.LENGTH_SHORT).show();
					return;
				}
				addComm();
			}
		});
		getCommList();
	}
	
	private void addComm() {
		progressDialog.setMessage("正在提交信息...");
		progressDialog.setCancelable(false);
		progressDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					if("scenic".equals(status)) {
						params.put("commentDTO.prodType", "01");
					} else if("res".equals(status)) {
						params.put("commentDTO.prodType", "05");
					} else if("hotel".equals(status)) {
						params.put("commentDTO.prodType", "00");
					} else if("enter".equals(status)) {
						params.put("commentDTO.prodType", "04");
					} else if("shop".equals(status)) {
						params.put("commentDTO.prodType", "06");
					}
					params.put("commentDTO.prodId", id);
					params.put("commentDTO.prodName", name);
					params.put("commentDTO.feel", comm_desc.getText().toString());
					params.put("commentDTO.userId", MyLoginUser.getInstance().getUser().getId());
					params.put("commentDTO.destinationId", MyConfig.AREAID);
					sendPostStr = HttpUtil.http(MyConfig.HTTPURL + "/comment/save.action", params);
				} catch (Exception e) {
					e.printStackTrace();
				}

				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
			}
		}).start();
	}
	
	private void getCommList() {
		progressDialog.setMessage("正在获取数据...");
		progressDialog.setCancelable(false);
		progressDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("unitId", id);
					params.put("infoType", status);
					sendPostStr = HttpUtil.http(MyConfig.HTTPURL + "/comment/all.action", params);
				} catch (Exception e) {
					e.printStackTrace();
				}

				Message msg = new Message();
				msg.what = 2;
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
					if("1".equals(jsonObject.getString("result"))) {
						Toast.makeText(CommActivity.this, "评论成功!", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(CommActivity.this, "评论失败!", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(CommActivity.this, "评论失败!", Toast.LENGTH_SHORT).show();
				}
				finish();
				break;
			case 2:
				progressDialog.dismiss();
				try {
					JSONObject jsonObject = new JSONObject(sendPostStr);
					JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject object = jsonArray.getJSONObject(i);
						CommEntity entity = new CommEntity(object.getString("id"), object.getString("feel"), object.getString("createTime"), object.getString("prodName"), object.getString("prodId"));
						list.add(entity);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				adapter = new CommAdapter(CommActivity.this, list);
				comm_listView.setAdapter(adapter);
				break;
			}
		}
	};
	
	@Override
	public void onBackPressed() {
		finish();
	}
}
