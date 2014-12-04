package com.dtssAnWeihai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dtssAnWeihai.tools.MyLoginUser;

/**
 * 个人中心
 * 
 * @author ChenPengyan
 * @Email cpy781@163.com 2014-2-21
 */
public class PersonalActivity extends BaseActivity implements OnClickListener {

	private LinearLayout personal_userinfo, personal_gologin;
	private TextView personal_name, personal_address, personal_email, personal_login;
	private LinearLayout personal_shoucang, personal_youhui, personal_xingcheng, personal_dianping;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal);
		
		setTitle("个人中心");
		initView();
		setUserInfo();
		
		int width = getWindowManager().getDefaultDisplay().getWidth();
		int height = width * 216 / 605;
		LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) personal_userinfo.getLayoutParams();
		layoutParams.width = width - 20;
		layoutParams.height = height;
		layoutParams.setMargins(10, 10, 10, 0);
		personal_userinfo.setLayoutParams(layoutParams);
		LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) personal_gologin.getLayoutParams();
		layoutParams2.width = width - 20;
		layoutParams2.height = height;
		layoutParams2.setMargins(10, 10, 10, 0);
		personal_gologin.setLayoutParams(layoutParams2);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 001) {
			setUserInfo();
		}
	}

	private void initView() {
		personal_userinfo = (LinearLayout) findViewById(R.id.personal_userinfo);
		personal_gologin = (LinearLayout) findViewById(R.id.personal_gologin);
		
		personal_name = (TextView) findViewById(R.id.personal_name);
		personal_address = (TextView) findViewById(R.id.personal_address);
		personal_email = (TextView) findViewById(R.id.personal_email);
		
		personal_login = (TextView) findViewById(R.id.personal_login);
		personal_login.setOnClickListener(this);
		
		personal_shoucang = (LinearLayout) findViewById(R.id.personal_shoucang);
		personal_shoucang.setOnClickListener(this);
		personal_youhui = (LinearLayout) findViewById(R.id.personal_youhui);
		personal_youhui.setOnClickListener(this);
		personal_xingcheng = (LinearLayout) findViewById(R.id.personal_xingcheng);
		personal_xingcheng.setOnClickListener(this);
		personal_dianping = (LinearLayout) findViewById(R.id.personal_dianping);
		personal_dianping.setOnClickListener(this);
	}

	private void setUserInfo() {
		if(!"".equals(MyLoginUser.getInstance().getUser().getId()) && null != MyLoginUser.getInstance().getUser().getId()) {
			personal_userinfo.setVisibility(View.VISIBLE);
			personal_gologin.setVisibility(View.GONE);
			personal_name.setText(MyLoginUser.getInstance().getUser().getUserName());
			personal_address.setText(MyLoginUser.getInstance().getUser().getAddress());
			personal_email.setText(MyLoginUser.getInstance().getUser().getEmail());
		} else {
			personal_userinfo.setVisibility(View.GONE);
			personal_gologin.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void onClick(View arg0) {
		Intent intent = new Intent();
		if(arg0.getId() == R.id.personal_login) {
			// 登录
			intent.setClass(PersonalActivity.this, LoginActivity.class);
			startActivityForResult(intent, 001);
		}
		if (MyLoginUser.getInstance().getUser().getId() == null || "".equals(MyLoginUser.getInstance().getUser().getId())) {
			Toast.makeText(PersonalActivity.this, "您未登录,请先登录!", Toast.LENGTH_SHORT).show();
			intent.setClass(PersonalActivity.this, LoginActivity.class);
			startActivityForResult(intent, 001);
		} else {
			switch (arg0.getId()) {
			case R.id.personal_shoucang:
				// 我的收藏
				intent.setClass(PersonalActivity.this, FavoriteActivity.class);
				startActivity(intent);
				break;
			case R.id.personal_youhui:
				// 我的优惠券
				break;
			case R.id.personal_xingcheng:
				// 我的行程
				intent.setClass(PersonalActivity.this, LineActivity.class);
				intent.putExtra("status", "2");
				startActivity(intent);
				break;
			case R.id.personal_dianping:
				// 我的点评
				intent.setClass(PersonalActivity.this, MyCommActivity.class);
				startActivity(intent);
				break;
			}
		}
	}

}
