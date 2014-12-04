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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.dtssAnWeihai.adapter.DarenAdapter;
import com.dtssAnWeihai.entity.DarenEntity;
import com.dtssAnWeihai.tools.HttpUtil;
import com.dtssAnWeihai.tools.MyConfig;

/**
 * 旅游攻略
 * @author ChenPengyan
 * @Email cpy781@163.com
 * 2014-6-14
 */
public class DarenActivity extends Activity {
	private ListView listView;
	private List<DarenEntity> list = new ArrayList<DarenEntity>();
	private DarenAdapter adapter;
	
	private ProgressDialog progressDialog;
	private String sendPostStr = "";// 返回的json字符串

	private int pageAount;// 总页数
	private int pageAountNow = 1;// 当前显示的页数
	private int pageCount = 10;// 每页显示条数
	private int listOnclickStatus = 0; // 点击事件（0:初始加载 1:下拉刷新 2:加载更多）
	
	// 加载更多底部layout
	private View custom_list_footer;
	private TextView load_more;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		progressDialog = new ProgressDialog(DarenActivity.this);
		initView();
		MainActivity.main_title.setText("旅游攻略");

		listView.addFooterView(custom_list_footer); // 设置列表底部视图
		
		// 加载更多
		custom_list_footer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				load_more.setText("正在加载中..."); // 设置按钮文字
				if (pageAountNow != pageAount) {
					if (1 < pageAount) {
						pageAountNow += 1;
						listOnclickStatus = 1;
						getInfo(pageAountNow);
					}
				}
			}
		});
		// listView单击事件
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				TextView id = (TextView) arg1.findViewById(R.id.listitem_id);
				Intent intent = new Intent(DarenActivity.this, DarenDetailActivity.class);
				intent.putExtra("id", id.getText().toString());
				startActivity(intent);
			}
		});
		
		getInfo(1);
	}
	
	private void initView() {
		listView = (ListView) findViewById(R.id.list_listView);
		
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
					Map<String, String> params = new HashMap<String, String>();
					params.put("areaId", MyConfig.AREAID);
					sendPostStr = HttpUtil.http(MyConfig.HTTPURL + "/search/travelDaren/getSuperiorProgram.action", params);
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
					int count = Integer.parseInt(jsonObject.getString("count"));
					if (count % pageCount > 0)
						pageAount = ((int) (count / pageCount)) + 1;
					else
						pageAount = (int) (count / pageCount);
					JSONArray jsonArray = jsonObject.getJSONArray("date");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject object = jsonArray.getJSONObject(i);
						DarenEntity entity = new DarenEntity(object.getString("id"), object.getString("title"), object.getString("guiname"), object.getString("image"), object.getString("content"));
						list.add(entity);
					}

					if (0 == listOnclickStatus) {
						// 添加adpter数据
						adapter = new DarenAdapter(DarenActivity.this, list, listView);
						listView.setAdapter(adapter);
						adapter.notifyDataSetChanged();
					} else if (1 == listOnclickStatus) {
						// 添加adpter数据
						adapter.notifyDataSetChanged();
						// 加载更多完成
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
	};
}
