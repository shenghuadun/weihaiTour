package com.dtssAnWeihai.activity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.dtssAnWeihai.zxing.camera.CameraManager;
import com.dtssAnWeihai.zxing.decoding.CaptureActivityHandler;
import com.dtssAnWeihai.zxing.decoding.InactivityTimer;
import com.dtssAnWeihai.zxing.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

/**
 * 二维码扫描
 */
public class QRCodeActivity extends BaseActionBarActivity implements SurfaceHolder.Callback{


	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	
	private String qrText;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		getSupportActionBar().hide();
		setContentView(R.layout.qrcode);
		// 初始化 CameraManager
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		viewfinderView.startAnimation(AnimationUtils.loadAnimation(QRCodeActivity.this, R.anim.rotate));
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface)
		{
			initCamera(surfaceHolder);
		}
		else
		{
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL)
		{
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		if (handler != null)
		{
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy()
	{
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	private void initCamera(SurfaceHolder surfaceHolder)
	{
		try
		{
			CameraManager.get().openDriver(surfaceHolder);
			
			setCameraDir();
		}
		catch (IOException ioe)
		{
			return;
		}
		catch (RuntimeException e)
		{
			return;
		}
		if (handler == null)
		{
			handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		if (!hasSurface)
		{
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView()
	{
		return viewfinderView;
	}

	public Handler getHandler()
	{
		return handler;
	}

	public void drawViewfinder()
	{
		viewfinderView.drawViewfinder();

	}

	public void handleDecode(final Result obj, Bitmap barcode)
	{
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		
		
		qrText = obj.getText();
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", qrText);
		doNetWorkJob("http://60.216.117.244/wisdomyt/search/listenVideo.action", params, scanHandler);
	}
	
	private Handler scanHandler = new Handler(){

		@Override
		public void handleMessage(Message msg)
		{
			JSONObject json = (JSONObject) msg.obj;
			
			if(json.has("path"))
			{
				Intent intent = new Intent(QRCodeActivity.this, SoundPlayActivity.class);
				try
				{
					intent.putExtra("url", json.getString("path"));
					intent.putExtra("name", json.getString("name"));
					intent.putExtra("id", json.getString("id"));
					
					startActivity(intent);
					finish();
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
			else 
			{
//				Toast.makeText(QRCodeActivity.this, "没有找到解说音频！", Toast.LENGTH_LONG).show();
				
				AlertDialog.Builder builder = new AlertDialog.Builder(QRCodeActivity.this);
				builder.setTitle("确定用浏览器打开吗？")
				.setMessage(qrText);
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
						Intent intent = new Intent();
						intent.setAction(Intent.ACTION_VIEW);
						intent.setData(Uri.parse(qrText));
						startActivity(intent);

						finish();
					}
		        });
				builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int id)
					{
						dialog.dismiss();
						
						startActivity(new Intent(getApplicationContext(), QRCodeActivity.class));
						finish();
					}
		        });
		
				builder.create().show();
			}
		}
	};

	private void initBeepSound()
	{
		if (playBeep && mediaPlayer == null)
		{
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
			try
			{
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			}
			catch (IOException e)
			{
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate()
	{
		if (playBeep && mediaPlayer != null)
		{
			mediaPlayer.start();
		}
		if (vibrate)
		{
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener()
	{
		public void onCompletion(MediaPlayer mediaPlayer)
		{
			mediaPlayer.seekTo(0);
		}
	};
	
	private void setCameraDir()
	{
	      int rotation = this.getWindowManager().getDefaultDisplay().getRotation();  
	      int degrees = 0;  
	      switch (rotation) {  
	      case Surface.ROTATION_0:  
	          degrees = 0;  
	          break;  
	      case Surface.ROTATION_90:  
	          degrees = 90;  
	          break;  
	      case Surface.ROTATION_180:  
	          degrees = 180;  
	          break;  
	      case Surface.ROTATION_270:  
	          degrees = 270;  
	          break;  
	      }  
	      
	      CameraInfo info = new android.hardware.Camera.CameraInfo();  
	      Camera.getCameraInfo(0, info); 
          int result = (info.orientation - degrees + 360) % 360; 
          
          CameraManager.get().setCameraDir(result);
	       
	}
}
