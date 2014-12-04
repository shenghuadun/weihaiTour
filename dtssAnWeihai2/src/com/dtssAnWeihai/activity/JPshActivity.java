package com.dtssAnWeihai.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

public class JPshActivity extends Activity {

	private Button jpush_back;
	private TextView jpush_desc;
	private String title = "";
	private String content = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if (null != intent) {
			Bundle bundle = getIntent().getExtras();
			title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
			content = bundle.getString(JPushInterface.EXTRA_ALERT);
		}
		setContentView(R.layout.jspsh_main);

		jpush_back = (Button) findViewById(R.id.jpush_back);
		jpush_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		jpush_desc = (TextView) findViewById(R.id.jpush_desc);
		jpush_desc.setText(content);
	}

	@Override
	public void onBackPressed() {
		finish();
	}

}
