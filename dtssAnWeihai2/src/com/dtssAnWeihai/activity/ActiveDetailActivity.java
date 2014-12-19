package com.dtssAnWeihai.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.dtssAnWeihai.tools.MyConfig;
import com.dtssAnWeihai.tools.MyTools;

/**
 * 节庆活动-详情页
 * @author ChenPengyan
 * @Email cpy781@163.com
 * 2014-5-23
 */
public class ActiveDetailActivity extends BaseActionBarActivity {

	private TextView active_detail_name, active_detail_startdate, active_detail_enddate, active_detail_acttime, active_detail_city, active_detail_website, active_detail_phone, active_detail_scenic, active_detail_info;
	private ImageView active_detail_image;

	private String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.active_detail);
		initView();

		setTitle("详细信息");
		
		Intent intent = getIntent();
		id = intent.getStringExtra("id");

		showLoading();

		Map<String, String> params = new HashMap<String, String>();
		params.put("unitId", id);
		doNetWorkJob(MyConfig.HTTPURL + "/activity/activityDetail.action", params, handler);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
				hideLoading();
			try {
				JSONObject object = (JSONObject) msg.obj;

//					active_detail_image.setImageBitmap(MyTools.returnBitMap(object.getString("image")));
//					Bitmap imagebBitmap = MyTools.decodeBitmapFromPath(object.getString("image"), 800, 480);
//					active_detail_image.setImageBitmap(imagebBitmap);
				
				MyTools.showImageFromPath(active_detail_image, object.getString("image"), 480, 320);
				
				active_detail_name.setText(object.getString("actName"));
				active_detail_startdate.setText(object.getString("ActStartDate"));
				active_detail_enddate.setText(object.getString("ActEndDate"));
				active_detail_acttime.setText(object.getString("ActTime"));
				active_detail_city.setText(object.getString("city"));
				active_detail_website.setText(object.getString("website"));
				active_detail_phone.setText(object.getString("phone"));
				active_detail_scenic.setText(object.getString("scenic"));
				active_detail_info.setText(Html.fromHtml(object.getString("actDesc")));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private void initView() {
		active_detail_name = (TextView) findViewById(R.id.active_detail_name);
		active_detail_startdate = (TextView) findViewById(R.id.active_detail_startdate);
		active_detail_enddate = (TextView) findViewById(R.id.active_detail_enddate);
		active_detail_acttime = (TextView) findViewById(R.id.active_detail_acttime);
		active_detail_city = (TextView) findViewById(R.id.active_detail_city);
		active_detail_website = (TextView) findViewById(R.id.active_detail_website);
		active_detail_phone = (TextView) findViewById(R.id.active_detail_phone);
		active_detail_scenic = (TextView) findViewById(R.id.active_detail_scenic);
		active_detail_info = (TextView) findViewById(R.id.active_detail_info);
		active_detail_image = (ImageView) findViewById(R.id.active_detail_image);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
