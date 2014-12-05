package com.dtssAnWeihai.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class SoundPlayActivity extends BaseActivity
{
	private String audioURL = "";
	private String audioName = "";
	private String audioId = "";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more);

		setTitle("语音列表");
		
		Intent intent = getIntent();
		String url = intent.getStringExtra("url");
		if(null == url)
		{
			Toast.makeText(SoundPlayActivity.this, "音频地址为空！", Toast.LENGTH_LONG).show();
			finish();
		}
		else
		{
			audioId = intent.getStringExtra("id");
			audioName = intent.getStringExtra("name");
			audioURL = intent.getStringExtra("url");
		}

		Toast.makeText(SoundPlayActivity.this, audioName, Toast.LENGTH_LONG).show();
	}

	
	private void findViews()
	{
	}


	private void setListeners()
	{
	}
}
