package com.dtssAnWeihai.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import com.dtssAnWeihai.adapter.CommAdapter;
import com.dtssAnWeihai.entity.CommEntity;
import com.dtssAnWeihai.tools.MyConfig;
import com.dtssAnWeihai.tools.MyLoginUser;

public class MyCommActivity extends BaseActionBarActivity {
	
	private ListView listView;
	
	private List<CommEntity> list = new ArrayList<CommEntity>();
	private CommAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mycomm);
		
		setTitle("我的评论");
		
		listView = (ListView) findViewById(R.id.mycomm_listView);
		
		getInfo();
	}
	
	private void getInfo() {
		showLoading();

		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", MyLoginUser.getInstance().getUser().getId());
		doNetWorkJob(MyConfig.HTTPURL + "/comment/myComment.action", params, handler);
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			hideLoading();
			try {
				JSONObject jsonObject = (JSONObject) msg.obj;
				JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);
					CommEntity entity = new CommEntity(object.getString("id"), object.getString("feel"), object.getString("createTime"), object.getString("prodName"), object.getString("prodId"));
					list.add(entity);
				}
				adapter = new CommAdapter(MyCommActivity.this, list);
				listView.setAdapter(adapter);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	public void onBackPressed() {
		finish();
	}
}
