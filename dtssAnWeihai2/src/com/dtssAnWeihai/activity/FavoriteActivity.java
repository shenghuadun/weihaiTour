package com.dtssAnWeihai.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.dtssAnWeihai.adapter.FavoriteAdapter;
import com.dtssAnWeihai.entity.FavoriteEntity;
import com.dtssAnWeihai.tools.DatabaseHelper;
import com.dtssAnWeihai.tools.MyTools;

/**
 * 我的收藏
 * 
 * @author ChenPengyan
 * @Email cpy781@163.com 2014-6-12
 */
public class FavoriteActivity extends BaseActionBarActivity {
	
	private ListView listView;
	private List<FavoriteEntity> list = new ArrayList<FavoriteEntity>();
	private FavoriteAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favorite_list);
		
		initView();
		setTitle("我的收藏");
		
		// listView单击事件
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				TextView id = (TextView) arg1.findViewById(R.id.listitem_id);
				TextView status = (TextView) arg1.findViewById(R.id.listitem_author);
				Intent intent = new Intent(FavoriteActivity.this, SearchDetailActivity.class);
				intent.putExtra("id", id.getText().toString());
				intent.putExtra("status", status.getText().toString());
				startActivity(intent);
			}
		});
		
		getInfo(1);
	}
	
	private void initView() {
		listView = (ListView) findViewById(R.id.favorite_listView);
	}
	
	private void getInfo(final int page) {
		showLoading();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					DatabaseHelper database = new DatabaseHelper(getApplicationContext());
					SQLiteDatabase db = database.getReadableDatabase();
					Cursor cursor = db.rawQuery("SELECT * FROM dtssAnWH_favorite", null);
					while (cursor.moveToNext()) {
						FavoriteEntity entity = new FavoriteEntity(MyTools.createRangeName(), cursor.getString(cursor.getColumnIndex("_proId")), cursor.getString(cursor.getColumnIndex("_proName")), cursor.getString(cursor.getColumnIndex("_proType")), cursor.getString(cursor.getColumnIndex("_proDesc")), cursor.getString(cursor.getColumnIndex("_proImage")));
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
			hideLoading();
			adapter = new FavoriteAdapter(FavoriteActivity.this, list, listView);
			listView.setAdapter(adapter);
		}
	};
	
	@Override
	public void onBackPressed() {
		finish();
	}
}
