package com.dtssAnWeihai.tools;

import java.util.Timer;
import java.util.TimerTask;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;

public class MediaPlayerTool implements OnBufferingUpdateListener, OnCompletionListener, MediaPlayer.OnPreparedListener {
	public MediaPlayer mediaPlayer;
	private SeekBar skbProgress;
	private Button button;
	private ProgressBar loadingText;
	private Timer mTimer = new Timer();
	private String videoUrl;
	private boolean pause;
	private int playPosition;
	private Thread thread;
	
	private boolean playstatus = true;

	public MediaPlayerTool(SeekBar skbProgress, Button button, ProgressBar loadingText) {
		this.skbProgress = skbProgress;
		this.button = button;
		this.loadingText = loadingText;
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnBufferingUpdateListener(this);
			mediaPlayer.setOnPreparedListener(this);
		} catch (Exception e) {
			Log.e("mediaPlayer", "error", e);
		}

		mTimer.schedule(mTimerTask, 0, 1000);
	}

	// 通过定时器和Handler来更新进度条
	TimerTask mTimerTask = new TimerTask() {
		@Override
		public void run() {
			if (mediaPlayer == null)
				return;
			if (mediaPlayer.isPlaying() && skbProgress.isPressed() == false) {
				handleProgress.sendEmptyMessage(0);
			}
		}
	};

	Handler handleProgress = new Handler() {
		public void handleMessage(Message msg) {
			int position = mediaPlayer.getCurrentPosition();
			int duration = mediaPlayer.getDuration();
			if (duration > 0) {
				long pos = skbProgress.getMax() * position / duration;
				skbProgress.setProgress((int) pos);
			}
		};
	};

	Handler handler2 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				loadingText.setVisibility(View.GONE);
				button.setVisibility(View.VISIBLE);
			}
		};
	};

	/**
	 * 来电话了
	 */
	public void callIsComing() {
		if (mediaPlayer.isPlaying()) {
			playPosition = mediaPlayer.getCurrentPosition();// 获得当前播放位置
			mediaPlayer.stop();
		}
	}

	/**
	 * 通话结束
	 */
	public void callIsDown() {
		if (playPosition > 0) {
			playNet(playPosition);
			playPosition = 0;
		}
	}

	/**
	 * 开始缓冲播放
	 */
	public boolean play(String videoUrl) {
		this.videoUrl = videoUrl; 
		return playNet(0);// 开始缓冲播放
	}
	
	/**
	 * 继续播放
	 */
	public void start(){
		if (pause) {// 如果处于暂停状态
			mediaPlayer.start();// 继续播放
			pause = false;
		}
	}

	/**
	 * 暂停
	 */
	public boolean pause() {
		if (mediaPlayer.isPlaying()) {// 如果正在播放
			mediaPlayer.pause();// 暂停
			pause = true;
		}
		return pause;
	}

	/**
	 * 停止
	 */
	public void stop() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {// 如果处在播放状态
			mediaPlayer.seekTo(0);
			mediaPlayer.stop();// 停止播放
		}
	}

	/**
	 * 重播
	 */
	public void replay() {
		// 从开始位置开始播放音频
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.seekTo(0);
		} else {
			playNet(0);
		}
	}
	
	/**
	 * 播放音乐
	 */
	private boolean playNet(final int playPosition) {
		loadingText.setVisibility(View.VISIBLE);
		button.setVisibility(View.GONE);
		
		thread = new Thread(new Runnable() {
			public void run() {
		try {
			mediaPlayer.reset();// 把各项参数设置成初始状态
			mediaPlayer.setDataSource(videoUrl);
			mediaPlayer.prepare();// 进行
			mediaPlayer.setOnPreparedListener(new MyPreparedListener(playPosition));
		} catch (Exception e) {
			e.printStackTrace();
			playstatus = false;
		}
		
		Message msg = new Message();
		msg.what = 1;
		handler2.sendMessage(msg);
			}
		});
		thread.start();
		return playstatus;
	}

	@Override
	/**  
	 * 通过onPrepared播放  
	 */
	public void onPrepared(MediaPlayer arg0) {
		arg0.start();
		Log.e("mediaPlayer", "onPrepared");
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		Log.e("mediaPlayer", "onCompletion");
	}

	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
		skbProgress.setSecondaryProgress(bufferingProgress);
		int currentProgress = skbProgress.getMax() * mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();
//		Log.e(currentProgress + "% play", bufferingProgress + "% buffer");
	}

	private final class MyPreparedListener implements android.media.MediaPlayer.OnPreparedListener {
		private int playPosition;

		public MyPreparedListener(int playPosition) {
			this.playPosition = playPosition;
		}

		@Override
		public void onPrepared(MediaPlayer mp) {
			mediaPlayer.start();// 开始播放
			if (playPosition > 0) {
				mediaPlayer.seekTo(playPosition);
			}
		}
	}

}
