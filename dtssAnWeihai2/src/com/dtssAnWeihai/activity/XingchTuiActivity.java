package com.dtssAnWeihai.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.dtssAnWeihai.adapter.XingchTuiAdapter;
import com.dtssAnWeihai.entity.XingchTuiEntity;
import com.dtssAnWeihai.tools.HttpUtil;
import com.dtssAnWeihai.tools.MyConfig;

/**
 * 行程推荐
 * 
 * @author ChenPengyan
 * @Email cpy781@163.com 2014-4-25
 */
public class XingchTuiActivity extends Activity {
	private Button back;
	private ListView listView;
	private List<XingchTuiEntity> list = new ArrayList<XingchTuiEntity>();
	private XingchTuiAdapter adapter;
	
	private ProgressDialog progressDialog;
	private String sendPostStr = "";// 返回的json字符串
	
	// 加载更多底部layout
	private View custom_list_footer;
	private TextView load_more;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xingch_tui);
		
		progressDialog = new ProgressDialog(XingchTuiActivity.this);
		initView();

		listView.addFooterView(custom_list_footer); // 设置列表底部视图
		
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(XingchTuiActivity.this, XingchActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		// listView单击事件
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				TextView id = (TextView) arg1.findViewById(R.id.xingch_list_item_id);
				Intent intent = new Intent(XingchTuiActivity.this, XingchTuiListActivity.class);
				intent.putExtra("id", id.getText().toString());
				startActivity(intent);
			}
		});
		
		getInfo(1);
	}
	
	private void initView() {
		back = (Button) findViewById(R.id.xingch_tui_back);
		listView = (ListView) findViewById(R.id.xingch_tui_listView);
		
		custom_list_footer = getLayoutInflater().inflate(R.layout.custom_list_footer, null);
		load_more = (TextView) custom_list_footer.findViewById(R.id.load_more);
	}
	
	private void getInfo(final int page) {
		progressDialog.setMessage("正在获取数据...");
		progressDialog.setCancelable(false);
		progressDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					sendPostStr = HttpUtil.http(MyConfig.HTTPURL + "/scheduling/recommend.action", null);
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
				load_more.setText("加载更多"); // 恢复按钮文字
				try {
					JSONObject jsonObject = new JSONObject(sendPostStr);
					JSONArray jsonArray = jsonObject.getJSONArray("data");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject object = jsonArray.getJSONObject(i);
						XingchTuiEntity entity = new XingchTuiEntity(object.getString("id"), object.getString("title"), object.getString("startDate"), object.getString("endDate"));
						list.add(entity);
					}
					adapter = new XingchTuiAdapter(XingchTuiActivity.this, list, listView);
					listView.setAdapter(adapter);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
	};
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(XingchTuiActivity.this, XingchActivity.class);
		startActivity(intent);
		finish();
	}
	
}
