package com.dtssAnWeihai.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dtssAnWeihai.tools.MediaPlayerTool;

public class SoundPlayActivity extends BaseActionBarActivity
{
	private String audioURL = "";
	private String audioName = "";
	private String audioId = "";
	
	private Button play;
	private SeekBar seekbar;
	private ProgressBar voicepro;
	
	private TextView name;

	private MediaPlayerTool mediaPlayerTool;
	
	private boolean isPlaying = false;
	private boolean isCached = false;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.soundplay);

		setTitle("景点解说");
		
		findViews();
		setListeners();
		
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
		
		name.setText(audioName);
	}

	
	private void findViews()
	{
		play = (Button) findViewById(R.id.play);
		seekbar = (SeekBar) findViewById(R.id.seekbar);
		voicepro = (ProgressBar) findViewById(R.id.voicepro);
		
		name = (TextView) findViewById(R.id.name);
	}


	private void setListeners()
	{
		play.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				if(!isPlaying) 
				{
					if(isCached) 
					{
						// 继续播放
						mediaPlayerTool.start();
						isCached = true;
						isPlaying = true;
						play.setBackgroundResource(R.drawable.stop_btn);
					} 
					else 
					{
						// 缓冲并播放
						mediaPlayerTool = new MediaPlayerTool(seekbar, play, voicepro);
						boolean playBool = mediaPlayerTool.play(audioURL);
						if(playBool) 
						{
							isCached = true;
							isPlaying = true;
							play.setBackgroundResource(R.drawable.stop_btn);
						} 
						else 
						{
							isCached = false;
							isPlaying = false;
							new AlertDialog.Builder(SoundPlayActivity.this).setTitle("音频格式不正确!").setIcon(android.R.drawable.ic_dialog_info)
							.setNegativeButton("确定", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
								}
							}).show();
						}
					}
				}
				else 
				{
					// 暂停
					mediaPlayerTool.pause();
					play.setBackgroundResource(R.drawable.start_btn);
					isCached = true;
					isPlaying = false;
				}
			}
		});
	}


	@Override
	protected void onPause()
	{
		super.onPause();
		
		if(isPlaying)
		{
			mediaPlayerTool.pause();
			isPlaying = false;
			play.setBackgroundResource(R.drawable.start_btn);
		}
	}


	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
	
	
}
