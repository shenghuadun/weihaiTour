package com.dtssAnWeihai.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dtssAnWeihai.tools.HttpUtil;
import com.dtssAnWeihai.tools.MyConfig;
import com.dtssAnWeihai.tools.MyLoginUser;
import com.dtssAnWeihai.tools.MyTools;

/**
 * 详细信息
 * 
 * @author ChenPengyan
 * @Email cpy781@163.com 2014-4-17
 */
public class ShakeDetailActivity extends Activity {
	
	private TextView shake_detail_title, shake_detail_desc, shake_detail_address;
	private ImageView shake_detail_image;
	private Button shake_detail_coupon, shake_detail_wzdw;
	
	private String id;
	private String status;
	private boolean youhuiStatus = false;
	private ProgressDialog progressDialog;
	private String sendPostStr = "";// 返回的json字符串
	// 存放经纬度
	ArrayList<String> pointList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shake_detail);
		initView();
		progressDialog = new ProgressDialog(ShakeDetailActivity.this);

		Button button = (Button) findViewById(R.id.shake_detail_back);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		status = intent.getStringExtra("status");

		getInfo();
	}

	private void initView() {
		shake_detail_title = (TextView) findViewById(R.id.shake_detail_title);
		shake_detail_desc = (TextView) findViewById(R.id.shake_detail_desc);
		shake_detail_address = (TextView) findViewById(R.id.shake_detail_address);
		
		shake_detail_image = (ImageView) findViewById(R.id.shake_detail_image);
		
		shake_detail_coupon = (Button) findViewById(R.id.shake_detail_coupon);
		shake_detail_wzdw = (Button) findViewById(R.id.shake_detail_wzdw);
		shake_detail_coupon.setOnClickListener(button_ck);
		shake_detail_wzdw.setOnClickListener(button_ck);
	}
	
	private void getInfo() {
		progressDialog.setMessage("正在获取数据...");
		progressDialog.setCancelable(false);
		progressDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("condition.infoType", status);
					params.put("condition.unitId", id);
					sendPostStr = HttpUtil.http(MyConfig.HTTPURL + "/search/benefitShop/juBenefit.action", params);
				} catch (Exception e) {
					e.printStackTrace();
				}

				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
			}
		}).start();
	}

	private void getYouhui() {
		progressDialog.setCancelable(false);
		progressDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("userId", MyLoginUser.getInstance().getUser().getId());
					params.put("benefitsId", id);
					sendPostStr = HttpUtil.http(MyConfig.HTTPURL + "/search/benefitShop/coupon.action", params);
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
					System.out.println(sendPostStr);
					JSONObject jsonObject = new JSONObject(sendPostStr);
					JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
					JSONObject object = jsonArray.getJSONObject(0);
					
					shake_detail_title.setText(object.getString("name"));
					shake_detail_desc.setText(Html.fromHtml(object.getString("benDesc")));
					shake_detail_address.setText(object.getString("address"));
					
					MyTools.showImageFromPath(shake_detail_image, object.getString("image"), 480, 320);
					if(!"".equals(object.getString("coupon"))) {
						youhuiStatus = true;
						shake_detail_coupon.setBackgroundDrawable(MyTools.getDrawableByBitmap(MyTools.returnBitMap(object.getString("coupon"))));
						shake_detail_coupon.setText("");
					}
					
					if(!"".equals(object.getString("latitude")) || !"".equals(object.getString("longitude"))) {
						pointList.add(object.getString("name")+","+object.getString("latitude")+","+object.getString("longitude"));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 2:
				progressDialog.dismiss();
				try {
					if("success".equals(sendPostStr)) {
						Toast.makeText(ShakeDetailActivity.this, "领取成功!", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(ShakeDetailActivity.this, "领取失败!", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
	};
	
	private OnClickListener button_ck = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.shake_detail_coupon:
				// 领取优惠券
				if(youhuiStatus) {
					if (MyLoginUser.getInstance().getUser().getId() == null || "".equals(MyLoginUser.getInstance().getUser().getId())) {
						Toast.makeText(ShakeDetailActivity.this, "您未登录,请先登录!", Toast.LENGTH_SHORT).show();
						intent.setClass(ShakeDetailActivity.this, LoginActivity.class);
						startActivityForResult(intent, 001);
					} else {
						getYouhui();
					}
				}
				break;
			case R.id.shake_detail_wzdw:
				// 地图定位
				if(pointList.size() > 0) {
					intent.setClass(ShakeDetailActivity.this, MapActivity.class);
					intent.putStringArrayListExtra("pointList", pointList);
					startActivity(intent);
				} else {
					Toast.makeText(ShakeDetailActivity.this, "暂无地图信息!", Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
			}
		}
	};
	
}
