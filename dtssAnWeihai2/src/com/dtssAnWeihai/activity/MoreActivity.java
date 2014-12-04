package com.dtssAnWeihai.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.dtssAnWeihai.tools.JPushUtil;
import com.dtssAnWeihai.tools.MyTools;

public class MoreActivity extends BaseActivity implements OnClickListener
{
	private RelativeLayout personalInfo;
	private ImageView btnPushMsg;
	private RelativeLayout clearCache;
	private RelativeLayout contact;
	private RelativeLayout about;
	private RelativeLayout feedBack;
	private TextView verionTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more);

		setTitle("更多");
		findViews();
		setListeners();
		

		if(!JPushUtil.isPushMessageEnabled(getApplicationContext()))
		{
			btnPushMsg.setImageResource(R.drawable.switch_off);
		}
		
		try {  
		    PackageInfo info = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);  
		    // 当前应用的版本名称  
		    String versionName = info.versionName;  
		    verionTextView.setText("版本号："+versionName);
		} catch (Exception e) {  
			e.printStackTrace();  
		}
	}

	
	private void findViews()
	{
		personalInfo = (RelativeLayout) findViewById(R.id.personalInfo);
		btnPushMsg = (ImageView) findViewById(R.id.btnPushMsg);
		clearCache = (RelativeLayout) findViewById(R.id.clearCache);
		contact = (RelativeLayout) findViewById(R.id.contact);
		about = (RelativeLayout) findViewById(R.id.about);
		feedBack = (RelativeLayout) findViewById(R.id.feedBack);
		verionTextView = (TextView) findViewById(R.id.verionTextView);		
	}


	private void setListeners()
	{
		personalInfo.setOnClickListener(this);
		btnPushMsg.setOnClickListener(this);
		clearCache.setOnClickListener(this);
		contact.setOnClickListener(this);
		about.setOnClickListener(this);
		feedBack.setOnClickListener(this);
	}


	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}


	@Override
	public void onClick(View v)
	{
		Intent intent = null;
		switch (v.getId())
		{
		case R.id.personalInfo:
			intent = new Intent(this, PersonalActivity.class);
			startActivity(intent);
			break;
		case R.id.btnPushMsg:
			togglePushStatus();
			break;
		case R.id.clearCache:
			Toast.makeText(this, "清除缓存，待实现", Toast.LENGTH_LONG).show();
			break;
		case R.id.contact:
			MyTools.doCall(MoreActivity.this, "063112301");
			break;
		case R.id.about:
			intent = new Intent(this, WeihaiInfoActivity.class);
			intent.putExtra("num", "1");
			startActivity(intent);
			break;
		case R.id.feedBack:
			intent = new Intent(this, SuggessActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
		
	}

	/**
	 * 切换推送服务状态
	 */
	private void togglePushStatus()
	{
		if(JPushUtil.isPushMessageEnabled(getApplicationContext()))
		{
			JPushUtil.setPushMessageStatus(getApplicationContext(), false);
			JPushInterface.stopPush(getApplicationContext());
			btnPushMsg.setImageResource(R.drawable.switch_off);
		}
		else
		{
			JPushUtil.setPushMessageStatus(getApplicationContext(), true);
			JPushInterface.resumePush(getApplicationContext());
			btnPushMsg.setImageResource(R.drawable.switch_on);
		}
	}
}
