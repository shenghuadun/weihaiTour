package com.dtssAnWeihai.activity;

import java.util.Map;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.dtssAnWeihai.tools.HttpUtil;

public class BaseActivity extends SherlockActivity
{
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setTheme(R.style.CustomActionBarTheme);
		
		progressDialog = new ProgressDialog(this);
		
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
	    {
	    case android.R.id.home:
	    	finish();
	        return true;
	    }
		
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 调用web接口获取数据
	 * @param url web接口地址
	 * @param params 接口参数
	 * @param handler 回调handler，返回的json格式数据在Message的obj字段
	 */
	protected void doNetWorkJob(final String url, final Map<String, String> params, final Handler handler)
	{
		doNetWorkJob(url, params, handler, "post");
	}
	
	protected void doNetWorkJob(final String url, final Map<String, String> params, final Handler handler, final String method)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run() 
			{
				JSONObject jsonObject = null;
				try 
				{
					String json;
					if("get".equalsIgnoreCase(method))
					{
						json = HttpUtil.httpGet(url, params);
					}
					else 
					{
						json = HttpUtil.http(url, params);
					}
					jsonObject = new JSONObject(json);
				} 
				catch (Exception e)
				{
					e.printStackTrace();
					
					Message msg = handler.obtainMessage();
					msg.obj = new JSONObject();
					handler.sendMessage(msg);
					
					onNetWorkErrorHandler.sendEmptyMessage(0);
					return;
				}
				
				Message msg = handler.obtainMessage();
				msg.obj = jsonObject;
				handler.sendMessage(msg);
			}

		}).start();
	}
	
	/**
	 * 在handler的msg.obj返回对应的字符串响应
	 * @param url
	 * @param params
	 * @param handler
	 * @param method
	 * @return
	 */
	public void doRequest(final String url, final Map<String, String> params, final Handler handler, final String method)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run() 
			{
				String result;
				if("get".equalsIgnoreCase(method))
				{
					result = HttpUtil.httpGet(url, params);
				}
				else 
				{
					result = HttpUtil.http(url, params);
				}
				Message msg = handler.obtainMessage();
				msg.obj = result;
				handler.sendMessage(msg);
			}

		}).start();
	}
	
	private Handler onNetWorkErrorHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			Toast.makeText(getApplicationContext(), "网络错误！", Toast.LENGTH_SHORT).show();
		}
	};
	
	@Override
	protected void onStart()
	{
		super.onStart();
	}
	protected void showLoading()
	{
		showLoading("正在获取数据...");
	}
	
	
	protected void showLoading(String message)
	{
		progressDialog.setMessage(message);
		progressDialog.setCancelable(false);
		progressDialog.show();
	}
	
	protected void hideLoading()
	{
		progressDialog.dismiss();
	}


//	/**
//	 * 用于非UI线程弹出Loading框
//	 * 0 隐藏
//	 * 1提示msg.obj对应的字符串
//	 * 其他提示"正在获取数据..."
//	 */
//	protected Handler promotionHandler = new Handler()
//	{
//		@Override
//		public void handleMessage(Message msg)
//		{
//			switch (msg.what)
//			{
//			case 0:
//				hideLoading();
//				break;
//			case 1:
//				showLoading((String)msg.obj);
//				break;
//
//			default:
//				showLoading();
//				break;
//			}
//		}
//	};
	
	protected String getSdcardDir() 
	{
		if (Environment.getExternalStorageState().equalsIgnoreCase(
				Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	public void onBackPressed()
	{
		if(progressDialog.isShowing())
		{
			hideLoading();
		}
		else
		{
			super.onBackPressed();
		}
	}

	
}
