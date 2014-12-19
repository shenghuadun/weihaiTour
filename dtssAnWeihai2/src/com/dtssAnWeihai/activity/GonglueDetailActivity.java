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
 * 攻略-详情页
 * @author ChenPengyan
 * @Email cpy781@163.com
 * 2014-5-23
 */
public class GonglueDetailActivity extends BaseActionBarActivity {

	private TextView gonglue_detail_title, gonglue_detail_type, gonglue_detail_area, gonglue_detail_tourtime, gonglue_detail_recDegree, gonglue_detail_sernote, gonglue_detail_desc;
	private ImageView gonglue_detail_image;

	private String id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gonglue_detail);
		initView();
		
		setTitle("攻略");
		
		Intent intent = getIntent();
		id = intent.getStringExtra("id");

		showLoading();
		Map<String, String> params = new HashMap<String, String>();
		params.put("unit", id);
		doNetWorkJob(MyConfig.HTTPURL + "/search/SubjectSearch/detailBySubjectProduct.action", params, handler);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			hideLoading();
			try {
				JSONObject object = (JSONObject) msg.obj;
				
				gonglue_detail_title.setText(object.getString("name"));
				gonglue_detail_type.setText(object.getString("type"));
				gonglue_detail_area.setText(object.getString("area"));
				gonglue_detail_tourtime.setText(object.getString("tourtime"));
				gonglue_detail_sernote.setText(object.getString("sernote"));
				gonglue_detail_desc.setText(Html.fromHtml(object.getString("subIntr")));
				// ★★★
				String recDegreeStr = "";
				int recDegree = object.getInt("recDegree");
				for (int i = 0; i < recDegree; i++) {
					recDegreeStr += "★";
				}
				gonglue_detail_recDegree.setText(recDegreeStr);
				MyTools.showImageFromPath(gonglue_detail_image, object.getString("image"), 480, 320);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private void initView() {
		gonglue_detail_image = (ImageView) findViewById(R.id.gonglue_detail_image);
		gonglue_detail_title = (TextView) findViewById(R.id.gonglue_detail_title);
		gonglue_detail_type = (TextView) findViewById(R.id.gonglue_detail_type);
		gonglue_detail_area = (TextView) findViewById(R.id.gonglue_detail_area);
		gonglue_detail_tourtime = (TextView) findViewById(R.id.gonglue_detail_tourtime);
		gonglue_detail_recDegree = (TextView) findViewById(R.id.gonglue_detail_recDegree);
		gonglue_detail_sernote = (TextView) findViewById(R.id.gonglue_detail_sernote);
		gonglue_detail_desc = (TextView) findViewById(R.id.gonglue_detail_desc);
	}
}
