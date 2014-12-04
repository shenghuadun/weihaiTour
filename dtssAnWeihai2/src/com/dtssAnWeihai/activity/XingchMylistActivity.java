package com.dtssAnWeihai.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dtssAnWeihai.adapter.XingchMylistAdapter;
import com.dtssAnWeihai.entity.XingchMylistEntity;
import com.dtssAnWeihai.tools.DatabaseHelper;

/**
 * 我的自定义行程——详情列表
 * @author ChenPengyan
 * @Email cpy781@163.com
 * 2014-6-14
 */
public class XingchMylistActivity extends Activity {
	
	private Button xingch_my_back, xingch_my_add;
	private ListView listView;
	private TextView xingch_my_name, xingch_my_date;
	private RelativeLayout xingch_my_toplay;
	private LinearLayout xingch_my_addlay;
	private Button xingch_my_shop, xingch_my_entr, xingch_my_res, xingch_my_hotel, xingch_my_scenic, xingch_my_traff;
	
	private String id;
	private String name = "";
	private String date = "";
	private ProgressDialog progressDialog;
	
	private List<XingchMylistEntity> list = new ArrayList<XingchMylistEntity>();
	private XingchMylistAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xingch_mylist);
		initView();
		progressDialog = new ProgressDialog(XingchMylistActivity.this);
		
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		name = intent.getStringExtra("name");
		date = intent.getStringExtra("date");
		xingch_my_name.setText(name);
		xingch_my_date.setText(date);
		
		int width = getWindowManager().getDefaultDisplay().getWidth();
		int height = width * 316 / 651;
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) xingch_my_toplay.getLayoutParams();
		layoutParams.width = width;
		layoutParams.height = height;
		xingch_my_toplay.setLayoutParams(layoutParams);
		
		showInfo();
	}
	
	private void initView() {
		xingch_my_back = (Button) findViewById(R.id.xingch_my_back);
		xingch_my_add = (Button) findViewById(R.id.xingch_my_add);
		
		listView = (ListView) findViewById(R.id.xingch_my_listView);
		
		xingch_my_toplay = (RelativeLayout) findViewById(R.id.xingch_my_toplay);
		xingch_my_name = (TextView) findViewById(R.id.xingch_my_name);
		xingch_my_date = (TextView) findViewById(R.id.xingch_my_data);
		
		xingch_my_addlay = (LinearLayout) findViewById(R.id.xingch_my_addlay);
		xingch_my_addlay.setVisibility(View.GONE);
		xingch_my_shop = (Button) findViewById(R.id.xingch_my_shop);
		xingch_my_entr = (Button) findViewById(R.id.xingch_my_entr);
		xingch_my_res = (Button) findViewById(R.id.xingch_my_res);
		xingch_my_hotel = (Button) findViewById(R.id.xingch_my_hotel);
		xingch_my_scenic = (Button) findViewById(R.id.xingch_my_scenic);
		xingch_my_traff = (Button) findViewById(R.id.xingch_my_traff);
		
		xingch_my_traff.setOnClickListener(button_ck);
		xingch_my_scenic.setOnClickListener(button_ck);
		xingch_my_hotel.setOnClickListener(button_ck);
		xingch_my_res.setOnClickListener(button_ck);
		xingch_my_entr.setOnClickListener(button_ck);
		xingch_my_shop.setOnClickListener(button_ck);
		
		xingch_my_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		xingch_my_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				xingch_my_addlay.setVisibility(View.VISIBLE);
			}
		});
	}
	
	private OnClickListener button_ck = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(XingchMylistActivity.this, XingchMylistAddActivity.class);
			switch (v.getId()) {
			case R.id.xingch_my_traff:
				intent.putExtra("type", "traff");
				break;
			case R.id.xingch_my_scenic:
				intent.putExtra("type", "6");//scenic
				break;
			case R.id.xingch_my_hotel:
				intent.putExtra("type", "0");//hotel
				break;
			case R.id.xingch_my_res:
				intent.putExtra("type", "1");//res
				break;
			case R.id.xingch_my_entr:
				intent.putExtra("type", "5");//entr
				break;
			case R.id.xingch_my_shop:
				intent.putExtra("type", "4");//shop
				break;
			}
			intent.putExtra("id", id);
			intent.putExtra("name", name);
			intent.putExtra("date", date);
			startActivity(intent);
			finish();
		}
	};

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
					Cursor cursor = db.rawQuery("SELECT * FROM dtssAnWH_schlist WHERE _graId = ?", new String[] { id });
					while (cursor.moveToNext()) {
						XingchMylistEntity entity = new XingchMylistEntity(cursor.getString(cursor.getColumnIndex("_id")), cursor.getString(cursor.getColumnIndex("_type")), cursor.getString(cursor.getColumnIndex("_name")), cursor.getString(cursor.getColumnIndex("_fromCity")), cursor.getString(cursor.getColumnIndex("_toCity")), cursor.getString(cursor.getColumnIndex("_startDate")), cursor.getString(cursor.getColumnIndex("_endDate")), cursor.getString(cursor.getColumnIndex("_remarks")), cursor.getString(cursor.getColumnIndex("_graId")));
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
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				progressDialog.dismiss();
				
				adapter = new XingchMylistAdapter(XingchMylistActivity.this, list);
				listView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				break;
			}
		}
	};
	
	@Override
	public void onBackPressed() {
		finish();
	}
}
