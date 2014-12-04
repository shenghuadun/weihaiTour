package com.dtssAnWeihai.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dtssAnWeihai.adapter.XingchListAdapter;
import com.dtssAnWeihai.entity.XingchListEntity;
import com.dtssAnWeihai.tools.DatabaseHelper;
import com.dtssAnWeihai.tools.HttpUtil;
import com.dtssAnWeihai.tools.MyConfig;
import com.dtssAnWeihai.tools.MyLoginUser;

public class XingchListActivity extends Activity {
	
	private Button xingch_list_back;
	private TextView xingch_list_title;
	private ListView listView;
	
	private String sendPostStr = "";
	private ProgressDialog progressDialog;
	
	private String status;
	private List<XingchListEntity> list = new ArrayList<XingchListEntity>();
	private XingchListAdapter adapter;
	
	String idStr;
	String nameStr;
	String startdateStr;
	String enddateStr;
	String fromCityStr;
	String toCityStr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xingch_list);
		initView();
		progressDialog = new ProgressDialog(XingchListActivity.this);
		
		Intent intent = getIntent();
		status = intent.getStringExtra("status");
		if("list".equals(status)) {
			xingch_list_title.setText("我的自定义行程");
		} else {
			xingch_list_title.setText("行程分享");
		}
		
		showInfo();
	}
	
	private void initView() {
		xingch_list_back = (Button) findViewById(R.id.xingch_list_back);
		xingch_list_title = (TextView) findViewById(R.id.xingch_list_title);
		listView = (ListView) findViewById(R.id.xingch_list_listView);
		
		xingch_list_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(XingchListActivity.this, XingchActivity.class);
				startActivity(intent);
				finish();
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				TextView id = (TextView) arg1.findViewById(R.id.xingch_list_item_id);
				TextView name = (TextView) arg1.findViewById(R.id.xingch_list_item_name);
				TextView startdate = (TextView) arg1.findViewById(R.id.xingch_list_item_fromdate);
				TextView enddate = (TextView) arg1.findViewById(R.id.xingch_list_item_todate);
				TextView fromCity = (TextView) arg1.findViewById(R.id.xingch_list_item_fromcity);
				TextView toCity = (TextView) arg1.findViewById(R.id.xingch_list_item_tocity);
				
				idStr = id.getText().toString();
				nameStr = name.getText().toString();
				startdateStr = startdate.getText().toString();
				enddateStr = enddate.getText().toString();
				fromCityStr = fromCity.getText().toString();
				toCityStr = toCity.getText().toString();
				
				if("list".equals(status)) {
					// 展示“我的自定义行程列表”
					Intent intent = new Intent(XingchListActivity.this, XingchMylistActivity.class);
					intent.putExtra("id", idStr);
					intent.putExtra("name", nameStr);
					intent.putExtra("date", startdateStr + " 至 " + enddateStr);
					startActivity(intent);
				} else {
					// 分享行程
					new AlertDialog.Builder(XingchListActivity.this).setTitle("确定分享该行程吗?").setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton("分享", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							shareInfo();
						}
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					}).show();
				}
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
					DatabaseHelper database = new DatabaseHelper(getApplicationContext());
					SQLiteDatabase db = database.getReadableDatabase();
					Cursor cursor = db.rawQuery("SELECT * FROM dtssAnWH_scheduling", null);
					while (cursor.moveToNext()) {
						XingchListEntity entity = new XingchListEntity(cursor.getString(cursor.getColumnIndex("_id")), cursor.getString(cursor.getColumnIndex("_name")), cursor.getString(cursor.getColumnIndex("_fromCity")), cursor.getString(cursor.getColumnIndex("_toCity")), cursor.getString(cursor.getColumnIndex("_startDate")), cursor.getString(cursor.getColumnIndex("_endDate")));
						list.add(entity);
					}
					cursor.close();
					db.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
			}
		}).start();
	}
	
	private void shareInfo() {
		progressDialog.setMessage("正在分享我的行程...");
		progressDialog.setCancelable(false);
		progressDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				DatabaseHelper database = new DatabaseHelper(getApplicationContext());
				SQLiteDatabase db = database.getReadableDatabase();
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("dto.userId", MyLoginUser.getInstance().getUser().getId());
					params.put("dto.title", nameStr);
					params.put("dto.startDate", startdateStr);
					params.put("dto.endDate", enddateStr);
					params.put("dto.fromCity", fromCityStr);
					params.put("dto.toCity", toCityStr);

					String nodeId = "";
					String nodeType = ""; // 节点类型
					String startDate = ""; // 开始时间
					String endDate = ""; // 结束时间
					String day = "";
					String fromCityId = ""; // 到达城市
					String toCityId = ""; // 出发城市
					String ownerCityId = "";
					String createTime = ""; // 创建时间
					String dest = ""; // 节点描述
					Cursor cursor = db.rawQuery("SELECT * FROM dtssAnWH_schlist WHERE _graId = ?", new String[] { idStr });
					while (cursor.moveToNext()) {
						nodeId += "///,";
						nodeType += cursor.getString(cursor.getColumnIndex("_type")) + ",";
						startDate += cursor.getString(cursor.getColumnIndex("_startDate")) + ",";
						endDate += cursor.getString(cursor.getColumnIndex("_endDate")) + ",";
						day += "///,";
						fromCityId += cursor.getString(cursor.getColumnIndex("_fromCity")) + ",";
						toCityId += cursor.getString(cursor.getColumnIndex("_toCity")) + ",";
						ownerCityId += "///,";
						createTime += cursor.getString(cursor.getColumnIndex("_createtime")) + ",";
						dest += cursor.getString(cursor.getColumnIndex("_remarks")) + ",";
					}
					cursor.close();
					params.put("nodeId", nodeId.substring(0, nodeId.length() - 1));
					params.put("nodeType", nodeType.substring(0, nodeType.length() - 1));
					params.put("startDate", startDate.substring(0, startDate.length() - 1));
					params.put("endDate", endDate.substring(0, endDate.length() - 1));
					params.put("day", day.substring(0, day.length() - 1));
					params.put("toCityId", toCityId.substring(0, toCityId.length() - 1));
					params.put("fromCityId", fromCityId.substring(0, fromCityId.length() - 1));
					params.put("ownerCityId", ownerCityId.substring(0, ownerCityId.length() - 1));
					params.put("createTime", createTime.substring(0, createTime.length() - 1));
					params.put("dest", dest.substring(0, dest.length() - 1));
					
					sendPostStr = HttpUtil.http(MyConfig.HTTPURL + "/scheduling/save.action", params);
					System.out.println("********* "+sendPostStr);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					db.close();
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
				adapter = new XingchListAdapter(XingchListActivity.this, list);
				listView.setAdapter(adapter);
				break;
			case 2:
				progressDialog.dismiss();
				try {
					JSONObject object = new JSONObject(sendPostStr);
					if("1".equals(object.getString("result"))) {
						Toast.makeText(XingchListActivity.this, "分享成功!", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(XingchListActivity.this, "分享失败!", Toast.LENGTH_SHORT).show();
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
		Intent intent = new Intent(XingchListActivity.this, XingchActivity.class);
		startActivity(intent);
		finish();
	}
}
