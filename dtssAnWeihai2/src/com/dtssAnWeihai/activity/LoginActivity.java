package com.dtssAnWeihai.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dtssAnWeihai.entity.LoginUser;
import com.dtssAnWeihai.tools.MyConfig;
import com.dtssAnWeihai.tools.MyLoginUser;

public class LoginActivity extends BaseActionBarActivity {
	private EditText login_name, login_pass;
	private Button login_register, login_submit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		setTitle("登录");
		initView();
	}
	
	private void initView() {
		login_name = (EditText) findViewById(R.id.login_name);
		login_pass = (EditText) findViewById(R.id.login_pass);
		
		login_register = (Button) findViewById(R.id.login_register);
		login_submit = (Button) findViewById(R.id.login_submit);
		login_register.setOnClickListener(button_ck);
		login_submit.setOnClickListener(button_ck);
	}
	
	private OnClickListener button_ck = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.login_register:
				Intent intent2 = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(intent2);
				break;
			case R.id.login_submit:
				if(!checkView())
					return;
				showLoading("正在登录...");

				Map<String, String> params = new HashMap<String, String>();
				params.put("userId", login_name.getText().toString());
				params.put("password", login_pass.getText().toString());
				doNetWorkJob(MyConfig.HTTPURL + "/myCenter/toLogin.action", params, handler);
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
			try {
				JSONObject jsonObject = (JSONObject) msg.obj;
				if("0".equals(jsonObject.getString("isLogin"))) {
					Toast.makeText(LoginActivity.this, "登录成功!", Toast.LENGTH_SHORT).show();
					
					String address = "";
					if(!"".equals(jsonObject.getString("address")) && !"null".equals(jsonObject.getString("address")))
						address = jsonObject.getString("address");
					String email = "";
					if(!"".equals(jsonObject.getString("email")) && !"null".equals(jsonObject.getString("email")))
						email = jsonObject.getString("email");
					String phone = "";
					if(!"".equals(jsonObject.getString("tel")) && !"null".equals(jsonObject.getString("tel")))
						phone = jsonObject.getString("tel");
					
					LoginUser loginUser = new LoginUser(jsonObject.getString("id"), jsonObject.getString("userId"), address, email, jsonObject.getString("sex"), phone, jsonObject.getString("userName"));
					MyLoginUser.getInstance().setUser(loginUser);
					
					Intent intent = new Intent();
					setResult(0, intent);
					LoginActivity.this.finish();
				} else {
					Toast.makeText(LoginActivity.this, "登录失败!请检查您的用户名和密码是否正确!", Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	private boolean checkView() {
		if("".equals(login_name.getText().toString())) {
			Toast.makeText(this, "请输入用户名!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if("".equals(login_pass.getText().toString())) {
			Toast.makeText(this, "请输入密码!", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		setResult(0, intent);
		LoginActivity.this.finish();
	}
}
