package com.dtssAnWeihai.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.dtssAnWeihai.tools.MyConfig;
import com.dtssAnWeihai.tools.MyLoginUser;

public class SuggessActivity extends BaseActionBarActivity {
	
	private EditText sugges_desc, sugges_phone, sugges_email;
	
	private String id;
	private String status;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.suggess);
		
		sugges_desc = (EditText) findViewById(R.id.sugges_desc);
		sugges_phone = (EditText) findViewById(R.id.sugges_phone);
		sugges_email = (EditText) findViewById(R.id.sugges_email);

		final Intent intent = getIntent();
		String title = intent.getStringExtra("title");
		setTitle((title==null || title.equals("")) ? "意见反馈" : title);
		
		findViewById(R.id.sugges_submit).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if("信息纠错".equals(getTitle())) 
				{
					id = getIntent().getStringExtra("id");
					status = getIntent().getStringExtra("status");
					infoJiucuo();
				} 
				else 
				{
					infoFankui();
				}
			}
		});
	}
	
	/**
	 * 意见反馈 提交成功  提交失败
	 */
	private void infoFankui() {
		showLoading();

		Map<String, String> params = new HashMap<String, String>();
		params.put("suggestionDTO.userId", MyLoginUser.getInstance().getUser().getId());
		params.put("suggestionDTO.content", sugges_desc.getText().toString());
		params.put("suggestionDTO.contact", sugges_phone.getText().toString());
		params.put("suggestionDTO.email", sugges_email.getText().toString());
		params.put("suggestionDTO.destinitionId", MyConfig.AREAID);
		
		doNetWorkJob(MyConfig.HTTPURL + "/suggestion/save.action", params, handler);
	}
	
	/**
	 * 信息纠错
	 */
	private void infoJiucuo() {
		showLoading();

		Map<String, String> params = new HashMap<String, String>();
		params.put("errorCorrectionDTO.eleType", status);
		params.put("errorCorrectionDTO.eleId", id);
		params.put("errorCorrectionDTO.userId", MyLoginUser.getInstance().getUser().getId());
		params.put("errorCorrectionDTO.content", sugges_desc.getText().toString());
		params.put("errorCorrectionDTO.contact", sugges_phone.getText().toString());
		params.put("errorCorrectionDTO.email", sugges_email.getText().toString());
		params.put("errorCorrectionDTO.destinitionId", MyConfig.AREAID);

		doNetWorkJob(MyConfig.HTTPURL + "/correction/save.action", params, handler);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) 
		{
				hideLoading();
				try 
				{
					JSONObject jsonObject = (JSONObject) msg.obj;
					if("0".equals(jsonObject.getString("result"))) 
					{
						Toast.makeText(SuggessActivity.this, "提交成功!", Toast.LENGTH_SHORT).show();
					} 
					else 
					{
						Toast.makeText(SuggessActivity.this, "提交失败!", Toast.LENGTH_SHORT).show();
					}
				} 
				catch (Exception e)
				{
					e.printStackTrace();
					Toast.makeText(SuggessActivity.this, "提交失败!", Toast.LENGTH_SHORT).show();
				}
				finish();
		}
	};

	@Override
	public void onBackPressed() {
		finish();
	}
}
