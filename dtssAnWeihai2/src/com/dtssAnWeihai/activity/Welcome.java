package com.dtssAnWeihai.activity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.dtssAnWeihai.tools.MyConfig;
import com.dtssAnWeihai.tools.MyStatusEntity;

public class Welcome extends BaseActionBarActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getSupportActionBar().hide();
		
		setContentView(R.layout.welcome);
		
		File f = new File(MyConfig.filepath);
		if (!f.exists()) {
			f.mkdir();
		}
		
		// 获取搜索类型数据
		Map<String, String> params = new HashMap<String, String>();
		params.put("infoType", "all");
		
		doNetWorkJob(MyConfig.HTTPURL + "/search/typeSearch.action", params, handler);
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) 
		{
			MyStatusEntity.getInstance().setSearchtypeString(((JSONObject)msg.obj).toString());
			Intent intent = new Intent(Welcome.this, MainActivity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			Welcome.this.finish();
		}
	};
}
