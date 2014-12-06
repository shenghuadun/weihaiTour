package com.dtssAnWeihai.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.MenuItem;
import com.dtssAnWeihai.tools.DatabaseHelper;
import com.dtssAnWeihai.tools.MediaPlayerTool;
import com.dtssAnWeihai.tools.MyConfig;
import com.dtssAnWeihai.tools.MyTools;

/**
 * 显示搜索到的具体每一条的具体信息
 * 
 * @author ChenPengyan
 * @Email cpy781@163.com 2014-4-17
 */
public class SearchDetailActivity extends BaseActivity {
	
	private Button  search_detail_share, search_detail_book, search_detail_startvoice, search_detail_jiucuo;
	private ImageView search_detail_image;
	private ImageView search_detail_collect, search_detail_audio;
	private Button search_detail_point;
	private TextView search_detail_name, search_detail_address, search_detail_phone, search_detail_desc;
	private LinearLayout search_detail_voicelay;
	private SeekBar search_detail_seekbar;
	private ProgressBar search_detail_voicepro;
	
	private LinearLayout search_detail_calllay;
	
	private String id;
	private String status;
	private String enterType;
	private String image;
	private String latitude = "";
	private String longitude = "";
	private String voiceurl;
	private boolean playtatus = false;
	private boolean voicestatus = false;
	private boolean favoriteStatus = false;
	
	private MediaPlayerTool mediaPlayerTool;
	
	// 存放经纬度
	private JSONObject positionJSON = new JSONObject();
	
	private View btnVoice;
	private View btnGuide;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_detail);
		initView();
		
		setTitle("详情页面");
		
		search_detail_voicelay.setVisibility(View.GONE);
		search_detail_voicepro.setVisibility(View.GONE);
		
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		status = intent.getStringExtra("status");
		enterType = intent.getStringExtra("enterType");
		
		//只有景区景点进来才显示
		if(!"scenic".equals(status))
		{
			findViewById(R.id.btnContainer).setVisibility(View.GONE);
		}
		
		// 判断是否已收藏
		try {
			DatabaseHelper database = new DatabaseHelper(getApplicationContext());
			SQLiteDatabase db = database.getReadableDatabase();
			Cursor cursor = db.rawQuery("SELECT * FROM dtssAnWH_favorite", null);
			while (cursor.moveToNext()) {
				if(id.equals(cursor.getString(cursor.getColumnIndex("_proId")))) {
					search_detail_collect.setImageResource(R.drawable.collect_yes);
					favoriteStatus = true;
				}
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		getInfo();
	}
	
	private void initView() {
		search_detail_share = (Button) findViewById(R.id.search_detail_share);
		search_detail_book = (Button) findViewById(R.id.search_detail_book);
		search_detail_startvoice = (Button) findViewById(R.id.search_detail_startvoice);
		search_detail_collect = (ImageView) findViewById(R.id.search_detail_collect);
		search_detail_audio = (ImageView) findViewById(R.id.search_detail_audio);
		search_detail_point = (Button) findViewById(R.id.search_detail_point);
		search_detail_jiucuo = (Button) findViewById(R.id.search_detail_jiucuo);
		
		search_detail_image = (ImageView) findViewById(R.id.search_detail_image);
		
		search_detail_name = (TextView) findViewById(R.id.search_detail_name);
		search_detail_address = (TextView) findViewById(R.id.search_detail_address);
		search_detail_phone = (TextView) findViewById(R.id.search_detail_phone);
		search_detail_desc = (TextView) findViewById(R.id.search_detail_desc);
		
		search_detail_voicelay = (LinearLayout) findViewById(R.id.search_detail_voicelay);
		search_detail_seekbar = (SeekBar) findViewById(R.id.search_detail_seekbar);
		search_detail_voicepro = (ProgressBar) findViewById(R.id.search_detail_voicepro);
		
		search_detail_calllay = (LinearLayout) findViewById(R.id.search_detail_calllay);
		
		btnVoice = findViewById(R.id.btnVoice);
		btnGuide = findViewById(R.id.btnGuide);
		
		search_detail_calllay.setOnClickListener(button_ck);
		search_detail_share.setOnClickListener(button_ck);
		search_detail_book.setOnClickListener(button_ck);
		search_detail_collect.setOnClickListener(button_ck);
		search_detail_audio.setOnClickListener(button_ck);
		search_detail_point.setOnClickListener(button_ck);
		search_detail_jiucuo.setOnClickListener(button_ck);
		
		search_detail_startvoice.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(!voicestatus) {
					System.out.println("********** voiceurl: " + voiceurl);
					if(playtatus) {
						// 继续播放
						mediaPlayerTool.start();
						playtatus = true;
						voicestatus = true;
						search_detail_startvoice.setBackgroundResource(R.drawable.stop_btn);
					} else {
						// 缓冲并播放
						mediaPlayerTool = new MediaPlayerTool(search_detail_seekbar, search_detail_startvoice, search_detail_voicepro);
						boolean playBool = mediaPlayerTool.play(voiceurl);
						if(playBool) {
							playtatus = true;
							voicestatus = true;
							search_detail_startvoice.setBackgroundResource(R.drawable.stop_btn);
						} else {
							playtatus = false;
							voicestatus = false;
							new AlertDialog.Builder(SearchDetailActivity.this).setTitle("音频格式不正确!").setIcon(android.R.drawable.ic_dialog_info)
							.setNegativeButton("确定", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
								}
							}).show();
						}
					}
				} else {
					// 暂停
					mediaPlayerTool.pause();
					search_detail_startvoice.setBackgroundResource(R.drawable.start_btn);
					playtatus = true;
					voicestatus = false;
				}
			}
		});
		
		btnVoice.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				search_detail_audio.performClick();
			}
		});
		
		btnGuide.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Map<String, String> params = new HashMap<String, String>();
				params.put("unitId", id);
				doRequest("http://60.216.117.244/wisdomyt/detail/scenicMap.action", params, guideInfoHandler, "post");
			}
		});
	}

	private Handler guideInfoHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			try
			{
				JSONObject jsonObject = new JSONObject(result);
				
				String path = jsonObject.getString("path");
				
				Intent intent = WebviewActivity.getIntent(SearchDetailActivity.this, path, "导游导览");
				startActivity(intent);
			}
			catch (JSONException e)
			{
				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
			}

		}
	};
	
	private void getInfo() {
		showLoading();
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("infoType", status);
		params.put("unitId", id);
		if(status.equals("enter"))
			params.put("entType", enterType);
		doNetWorkJob(MyConfig.HTTPURL + "/search/infoTypeDetail.action", params, handler);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			hideLoading();
			try {
				JSONObject jsonObject = (JSONObject) msg.obj;
				
				search_detail_name.setText(jsonObject.getString("name"));
				search_detail_address.setText(jsonObject.getString("address"));
				search_detail_phone.setText(jsonObject.getString("phone"));
				search_detail_desc.setText(Html.fromHtml(jsonObject.getString("dest"))
						.toString()
						.replaceAll("<br/>", " "));
				MyTools.showImageFromPath(search_detail_image, jsonObject.getString("image"), 480, 320);
				image = jsonObject.getString("image");
				latitude = jsonObject.getString("latitude");
				longitude = jsonObject.getString("longitude");
				
				positionJSON.put("name", jsonObject.getString("name"));
				positionJSON.put("address", jsonObject.getString("address"));
				positionJSON.put("latitude", jsonObject.getString("latitude"));
				positionJSON.put("longitude", jsonObject.getString("longitude"));

				voiceurl = jsonObject.getString("audio");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	private OnClickListener button_ck = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if(voicestatus) {
				// 暂停音频
				mediaPlayerTool.pause();
				search_detail_startvoice.setBackgroundResource(R.drawable.start_btn);
				playtatus = true;
				voicestatus = false;
			}
			
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.search_detail_share:
				// 分享
				MyTools.showShare(SearchDetailActivity.this, "小伙伴们一起来“"+search_detail_name.getText().toString()+"”吧，仙境烟台，欢迎您！", image);
				break;
			case R.id.search_detail_book:
				// 预定
//				if (MyLoginUser.getInstance().getUser().getId() == null || "".equals(MyLoginUser.getInstance().getUser().getId())) {
//					Toast.makeText(SearchDetailActivity.this, "您未登录,请先登录!", Toast.LENGTH_SHORT).show();
//					intent.setClass(SearchDetailActivity.this, LoginActivity.class);
//					startActivityForResult(intent, 001);
//				} else {
//					intent.setClass(SearchDetailActivity.this, CommActivity.class);
//					intent.putExtra("status", status);
//					intent.putExtra("id", id);
//					intent.putExtra("name", search_detail_name.getText().toString());
//					startActivity(intent);
//				}
				Intent i = new Intent(SearchDetailActivity.this, WebviewActivity.class);
				i.putExtra("weburl", MyConfig.WH_YOUHUI);
				i.putExtra("title", "预定");
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

				startActivity(i);
				break;
			case R.id.search_detail_collect:
				// 收藏
				DatabaseHelper database = new DatabaseHelper(SearchDetailActivity.this);
				SQLiteDatabase db = database.getReadableDatabase();
				if(favoriteStatus) {
					db.delete("dtssAnWH_favorite", "_proId=?", new String[]{ id });
					search_detail_collect.setImageResource(R.drawable.collect_no);
					favoriteStatus = false;
				} else {
					db.execSQL("INSERT INTO dtssAnWH_favorite VALUES (?, ?, ?, ?, ?, ?)", 
							new Object[] { MyTools.createRangeName(), id, search_detail_name.getText().toString(), status, search_detail_desc.getText().toString(), image });
					search_detail_collect.setImageResource(R.drawable.collect_yes);
					favoriteStatus = true;
				}
				db.close();
				break;
			case R.id.search_detail_audio:
				// 展示音频
				if("".equals(voiceurl))
					Toast.makeText(SearchDetailActivity.this, "暂无音频", Toast.LENGTH_SHORT).show();
				else
					search_detail_voicelay.setVisibility(View.VISIBLE);
				break;
			case R.id.search_detail_point:
				// 地图定位
				if(latitude.equals("") || longitude.equals("")) {
					Toast.makeText(SearchDetailActivity.this, "暂无坐标信息!", Toast.LENGTH_SHORT).show();
					return;
				}
				
				intent.setClass(SearchDetailActivity.this, RoutePlanActivity.class);
				intent.putExtra("positionJSON", positionJSON.toString());
				startActivity(intent);
				
				break;
			case R.id.search_detail_calllay:
				// 拨打电话
				if("".equals(search_detail_phone.getText().toString().trim())) {
					Toast.makeText(SearchDetailActivity.this, "暂无电话!", Toast.LENGTH_SHORT).show();
					return;
				}

				MyTools.doCall(SearchDetailActivity.this, search_detail_phone.getText().toString());
				break;
			case R.id.search_detail_jiucuo:
				// 信息纠错
				intent.setClass(SearchDetailActivity.this, SuggessActivity.class);
				intent.putExtra("title", "信息纠错");
				intent.putExtra("id", id);
				intent.putExtra("status", status);
				startActivity(intent);
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	public void onBackPressed() {
		clearMediaPlayer();
		finish();
	};
	
	@Override
	protected void onStop() {
		clearMediaPlayer();
		super.onStop();
	}
	
	/**
	 * 清除音频缓存、暂停音频
	 */
	public void clearMediaPlayer() {
		if (playtatus) {
			mediaPlayerTool.stop();
			playtatus = false;
			voicestatus = false;
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
	    {
	    case android.R.id.home:
	    	clearMediaPlayer();
	    	finish();
	        return true;
	    }
		
		return super.onOptionsItemSelected(item);
	}


}
