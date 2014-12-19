package com.dtssAnWeihai.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dtssAnWeihai.tools.MyConfig;
import com.dtssAnWeihai.tools.MyTools;

/**
 * 烟台介绍
 * 
 * @author ChenPengyan
 * @Email cpy781@163.com 2014-4-25
 */
public class WeihaiInfoActivity extends BaseActionBarActivity {
	
	private View clud1, clud2, clud3;
	
	private ImageView info1_phone, info1_weibo, info2_image;
	
	private Button info3_feiji, info3_huoche;
	private ImageView info3_traff1_img, info3_traff2_img, info3_traff3_img, info3_traff4_img, info3_traff5_img;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weihai_info);
		initview();
		
		Intent intent = getIntent();
		
		String title = intent.getStringExtra("title");
		setTitle(title == null || title.equals("") ? "关于我们" : title);
		
		String num = intent.getStringExtra("num");
		if("1".equals(num)) {
			// 关于我们
			clud1.setVisibility(View.VISIBLE);
			clud2.setVisibility(View.GONE);
			clud3.setVisibility(View.GONE);
		} else if("2".equals(num)) {
			// 威海旅游
			clud1.setVisibility(View.GONE);
			clud2.setVisibility(View.VISIBLE);
			clud3.setVisibility(View.GONE);
			
			int width = getWindowManager().getDefaultDisplay().getWidth();
			int height = width * 780 / 800;
			LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) info2_image.getLayoutParams();
			layoutParams.width = width;
			layoutParams.height = height;
			info2_image.setLayoutParams(layoutParams);
		} else if("3".equals(num)) {
			// 交通
			clud1.setVisibility(View.GONE);
			clud2.setVisibility(View.GONE);
			clud3.setVisibility(View.VISIBLE);
//			getinfo3();
			int width = getWindowManager().getDefaultDisplay().getWidth();
			int height = width * 294 / 600;
			LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) info3_traff2_img.getLayoutParams();
			layoutParams.width = width;
			layoutParams.height = height;
			info3_traff2_img.setLayoutParams(layoutParams);
			info3_traff4_img.setLayoutParams(layoutParams);
			info3_traff2_img.setImageResource(R.drawable.traff_image02);
			info3_traff4_img.setImageResource(R.drawable.traff_image04);
		}
	}

	private void getinfo3() {
		showLoading();

		Map<String, String> params = new HashMap<String, String>();
		params.put("areaId", MyConfig.AREAID);
		doNetWorkJob(MyConfig.HTTPURL + "/menu/indexJson.action", params, handler);
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			hideLoading();
			try 
			{
//				JSONObject object = new JSONObject(sendPostStr);
//				info3_gonglu.setText(Html.fromHtml(object.getString("highway")));
//				info3_gongjiao.setText(Html.fromHtml(object.getString("bus")));
//				info3_shuiyun.setText(Html.fromHtml(object.getString("waterCarriage")));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	private void initview() {
		clud1 = findViewById(R.id.info_clud1);
		clud2 = findViewById(R.id.info_clud2);
		clud3 = findViewById(R.id.info_clud3);
		
		info1_phone = (ImageView) findViewById(R.id.info1_phone);
		info1_weibo = (ImageView) findViewById(R.id.info1_weibo);
		info1_phone.setOnClickListener(onClick);
		info1_weibo.setOnClickListener(onClick);
		
		info2_image = (ImageView) findViewById(R.id.info2_image);
		
		info3_feiji = (Button) findViewById(R.id.info3_feiji);
		info3_huoche = (Button) findViewById(R.id.info3_huoche);
		info3_feiji.setOnClickListener(onClick);
		info3_huoche.setOnClickListener(onClick);
		info3_traff2_img = (ImageView) findViewById(R.id.info3_traff2_img);
		info3_traff4_img = (ImageView) findViewById(R.id.info3_traff4_img);
//		info3_traff1_info = (TextView) findViewById(R.id.info3_traff1_info);
//		info3_traff2_info = (TextView) findViewById(R.id.info3_traff2_info);
//		info3_traff3_info = (TextView) findViewById(R.id.info3_traff3_info);
//		info3_traff4_info = (TextView) findViewById(R.id.info3_traff4_info);
//		info3_traff5_info = (TextView) findViewById(R.id.info3_traff5_info);
	}
	
	private OnClickListener onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.info1_phone:
				MyTools.doCall(WeihaiInfoActivity.this, "063112301");
				break;
			case R.id.info1_weibo:
				intent.setClass(WeihaiInfoActivity.this, WebviewActivity.class);
				intent.putExtra("weburl", "http://m.weibo.cn/n/%E5%A8%81%E6%B5%B7%E5%B8%82%E6%97%85%E6%B8%B8%E5%B1%80%E5%AE%98%E6%96%B9%E5%BE%AE%E5%8D%9A?c=spr_qdhz_bd_baidusmt_weibo_s&wm=5091_0010&from=ba_s0010");
				intent.putExtra("title", "威海市官方微博");
				startActivity(intent);
				break;
			case R.id.info3_feiji:
				// 机票查询
				intent.setClass(WeihaiInfoActivity.this, WebviewActivity.class);
				intent.putExtra("weburl", "http://touch.qunar.com/h5/flight/");
				intent.putExtra("title", "机票查询");
				startActivity(intent);
				break;
			case R.id.info3_huoche:
				// 火车票查询
				intent.setClass(WeihaiInfoActivity.this, WebviewActivity.class);
				intent.putExtra("weburl", "http://touch.qunar.com/h5/train/");
				intent.putExtra("title", "火车票查询");
				startActivity(intent);
				break;
			}
		}
	};
	
	@Override
	public void onBackPressed() {
		finish();
	}
	
}
