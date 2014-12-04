package com.dtssAnWeihai.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.dtssAnWeihai.tools.DatabaseHelper;
import com.dtssAnWeihai.tools.MyConfig;
import com.dtssAnWeihai.tools.MyTools;

/**
 * 行程推荐-详情页
 * @author ChenPengyan
 * @Email cpy781@163.com
 * 2014-5-23
 */
public class LineDetailActivity extends BaseActivity {

	private TextView line_detail_name, line_detail_date, line_detail_depart, line_detail_phone, line_detail_desc, line_detail_pricedesc, line_detail_remind, line_detail_reminder;
	private ImageView line_detail_image;

	private String id = "";
	private String image = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.line_detail);
		initView();
		
		setTitle("详细信息");

		Intent intent = getIntent();
		id = intent.getStringExtra("id");

		showLoading();

		Map<String, String> params = new HashMap<String, String>();
		params.put("unitId", id);
		doNetWorkJob(MyConfig.HTTPURL + "/line/lineDetail.action", params, handler);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			hideLoading();
			try {
				JSONObject object = (JSONObject) msg.obj;

				line_detail_name.setText(object.getString("name"));
				line_detail_date.setText(object.getString("leaveDate"));
				line_detail_depart.setText(object.getString("department"));
				line_detail_phone.setText(object.getString("phone"));
				line_detail_desc.setText(Html.fromHtml(object.getString("desc")));
				line_detail_pricedesc.setText(Html.fromHtml(object.getString("priceDesc")));
				line_detail_remind.setText(Html.fromHtml(object.getString("remind")));
				line_detail_reminder.setText(Html.fromHtml(object.getString("reminder")));
				
				MyTools.showImageFromPath(line_detail_image, object.getString("image"), 480, 320);
				image = object.getString("image");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private void initView() {
		line_detail_image = (ImageView) findViewById(R.id.line_detail_image);
		line_detail_name = (TextView) findViewById(R.id.line_detail_name);
		line_detail_date = (TextView) findViewById(R.id.line_detail_date);
		line_detail_depart = (TextView) findViewById(R.id.line_detail_depart);
		line_detail_phone = (TextView) findViewById(R.id.line_detail_phone);
		line_detail_desc = (TextView) findViewById(R.id.line_detail_desc);
		line_detail_pricedesc = (TextView) findViewById(R.id.line_detail_pricedesc);
		line_detail_remind = (TextView) findViewById(R.id.line_detail_remind);
		line_detail_reminder = (TextView) findViewById(R.id.line_detail_reminder);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case 100001:
			DatabaseHelper database = new DatabaseHelper(LineDetailActivity.this);
			SQLiteDatabase db = database.getReadableDatabase();
			boolean addstatus = false;
			Cursor cursor = db.rawQuery("SELECT * FROM dtssAnWH_scheduling", null);
			while (cursor.moveToNext()) {
				if(id.equals(cursor.getString(cursor.getColumnIndex("_id")))){
					addstatus = true;
				}
			}
			cursor.close();
			if(addstatus) {
				Toast.makeText(LineDetailActivity.this, "已存在于我的行程中!", Toast.LENGTH_SHORT).show();
			} else {
				db.execSQL("INSERT INTO dtssAnWH_scheduling VALUES (?, ?, ?, ?)", 
						new Object[] { id, line_detail_name.getText().toString(), image, line_detail_phone.getText().toString() });
				Toast.makeText(LineDetailActivity.this, "已添加到我的行程!", Toast.LENGTH_SHORT).show();
			}
			db.close();
			
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(1, 100001, 1, "添加到我的行程")
		.setIcon(R.drawable.xingch_add)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		return super.onCreateOptionsMenu(menu);
	}

	
}
