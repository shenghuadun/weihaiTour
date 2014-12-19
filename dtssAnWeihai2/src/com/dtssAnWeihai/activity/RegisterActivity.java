package com.dtssAnWeihai.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.dtssAnWeihai.tools.MyConfig;

public class RegisterActivity extends BaseActionBarActivity {

	private Button register_login, register_submit;
	private EditText register_name, register_pass, register_pass2, register_phone, register_email, register_address, register_realname;
	private RadioButton register_sex_man, register_sex_woman;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		setTitle("注册");
		initView();
	}

	private void initView() {
		register_login = (Button) findViewById(R.id.register_login);
		register_submit = (Button) findViewById(R.id.register_submit);
		register_login.setOnClickListener(button_ck);
		register_submit.setOnClickListener(button_ck);

		register_name = (EditText) findViewById(R.id.register_name);
		register_pass = (EditText) findViewById(R.id.register_pass);
		register_pass2 = (EditText) findViewById(R.id.register_pass2);
		register_phone = (EditText) findViewById(R.id.register_phone);
		register_email = (EditText) findViewById(R.id.register_email);
		register_address = (EditText) findViewById(R.id.register_address);
		register_realname = (EditText) findViewById(R.id.register_realname);
		
		register_sex_man = (RadioButton) findViewById(R.id.register_sex_man);
		register_sex_woman = (RadioButton) findViewById(R.id.register_sex_woman);
	}

	private OnClickListener button_ck = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.register_login:
				break;
			case R.id.register_submit:
				if (checkView())
				{
					showLoading("正在注册信息...");
					
					Map<String, String> params = new HashMap<String, String>();
					params.put("memberInfo.userId", register_name.getText().toString());
					params.put("memberInfo.password", register_pass.getText().toString());
					params.put("memberInfo.userName", register_realname.getText().toString());
					if(register_sex_man.isChecked())
						params.put("sex", "1");// 1 男 2女
					if(register_sex_woman.isChecked())
						params.put("sex", "2");// 1 男 2女
					params.put("memberInfo.tel", register_phone.getText().toString());
					params.put("memberInfo.email", register_email.getText().toString());
					params.put("memberInfo.address", register_address.getText().toString());
					
					doNetWorkJob(MyConfig.HTTPURL + "/search/toregister.action", params, handler);
				}
				break;
			default:
				break;
			}
		}
	};

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) 
		{
			hideLoading();
			try
			{
				JSONObject jsonObject = (JSONObject) msg.obj;
				if ("0".equals(jsonObject.getString("result"))) {
					Toast.makeText(RegisterActivity.this, "注册成功!", Toast.LENGTH_SHORT).show();
					finish();
				} else {
					Toast.makeText(RegisterActivity.this, "注册失败!请检查您的网络!", Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private boolean checkView() {
		if("".equals(register_name.getText().toString())) {
			Toast.makeText(RegisterActivity.this, "请输入用户名!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if("".equals(register_pass.getText().toString())) {
			Toast.makeText(RegisterActivity.this, "请输入密码!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if("".equals(register_pass2.getText().toString())) {
			Toast.makeText(RegisterActivity.this, "请输入确认密码!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if((register_pass.getText().toString()).length() < 6) {
			Toast.makeText(RegisterActivity.this, "密码必须大于6位!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(!(register_pass.getText().toString()).equals(register_pass2.getText().toString())) {
			Toast.makeText(RegisterActivity.this, "两次密码不一致!", Toast.LENGTH_SHORT).show();
			return false;
		}
		String phone = "(^(\\d{7,8})(-(\\d{3,}))?$)|(^(013|13|8613|015|15|8615|018|18|8618)\\d{9}$)";
		if (!(register_phone.getText().toString()).matches(phone)) {
			Toast.makeText(RegisterActivity.this, "请输入正确的电话号码!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(!"".equals(register_email.getText().toString())) {
			String format = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			if (!(register_email.getText().toString()).matches(format)) {
				Toast.makeText(RegisterActivity.this, "请输入正确的邮箱地址!", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		if("".equals(register_realname.getText().toString())) {
			Toast.makeText(RegisterActivity.this, "请输入真实姓名!", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
