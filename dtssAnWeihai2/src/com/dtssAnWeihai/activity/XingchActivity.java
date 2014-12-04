package com.dtssAnWeihai.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dtssAnWeihai.tools.MyLoginUser;

/**
 * 智能行程
 * @author ChenPengyan
 * @Email cpy781@163.com
 * 2014-5-23
 */
public class XingchActivity extends Activity {
	
	private Button xingch_back;
	private ImageView xingch_tuibtn, xingch_zidingyibtn, xingch_sharebtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xingch);
		initview();
		
		int width = getWindowManager().getDefaultDisplay().getWidth();
		int height = width * 364 / 1040;
		
		LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) xingch_tuibtn.getLayoutParams();
		layoutParams1.width = width;
		layoutParams1.height = height;
		xingch_tuibtn.setLayoutParams(layoutParams1);
		xingch_tuibtn.setImageResource(R.drawable.xingch_tui01);
		
		LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) xingch_zidingyibtn.getLayoutParams();
		layoutParams2.width = width;
		layoutParams2.height = height;
		xingch_zidingyibtn.setLayoutParams(layoutParams2);
		xingch_zidingyibtn.setImageResource(R.drawable.xingch_tui02);
		
		LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) xingch_sharebtn.getLayoutParams();
		layoutParams3.width = width;
		layoutParams3.height = height;
		xingch_sharebtn.setLayoutParams(layoutParams3);
		xingch_sharebtn.setImageResource(R.drawable.xingch_tui03);
	}
	
	private void initview() {
		xingch_back = (Button) findViewById(R.id.xingch_back);
		xingch_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		xingch_tuibtn = (ImageView) findViewById(R.id.xingch_tuibtn);
		xingch_tuibtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(XingchActivity.this, XingchTuiActivity.class);
				startActivity(intent);
				finish();
			}
		});
		xingch_zidingyibtn = (ImageView) findViewById(R.id.xingch_zidingyibtn);
		xingch_zidingyibtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(XingchActivity.this, XingchAddActivity.class);
				startActivity(intent);
				finish();
			}
		});
		xingch_sharebtn = (ImageView) findViewById(R.id.xingch_sharebtn);
		xingch_sharebtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				if (MyLoginUser.getInstance().getUser().getId() == null || "".equals(MyLoginUser.getInstance().getUser().getId())) {
					Toast.makeText(XingchActivity.this, "您未登录,请先登录!", Toast.LENGTH_SHORT).show();
					intent.setClass(XingchActivity.this, LoginActivity.class);
					startActivityForResult(intent, 001);
					return;
				}
				intent.setClass(XingchActivity.this, XingchListActivity.class);
				intent.putExtra("status", "share");
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	public void onBackPressed() {
		finish();
	}
}
