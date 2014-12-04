package com.dtssAnWeihai.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dtssAnWeihai.entity.ZiXunEntity;
import com.dtssAnWeihai.tools.MyConfig;

public class ZiXunListActivity extends BaseActivity {

	private ListView listView;
	
	private ZiXunListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zixun_list);
		initView();
		setTitle("资讯");

		// 隐藏输入键盘
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		getInfo();
	}

	private void initView() 
	{
		listView = (ListView) findViewById(R.id.zixun_listView);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				Intent intent = new Intent(ZiXunListActivity.this, ZiXunDetailActivity.class);
//				intent.putExtra("id", ((ZiXunListAdapter.ViewHolder)arg1.getTag()).id.getText());
//				startActivity(intent);
			}
		});
	}
	
	private void getInfo() {
		showLoading();
		
		doNetWorkJob(MyConfig.HTTPURL + "/search/findTourismDynamicList.action", null, handler);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			hideLoading();
			try {
				JSONObject jsonObject = (JSONObject) msg.obj;
				JSONArray jsonArray = jsonObject.getJSONArray("data");
				List<ZiXunEntity> list = new ArrayList<ZiXunEntity>();
				int length = jsonArray.length();
				if(jsonArray.length() > 25) {
					length = 25;
				}
				for (int i = 0; i < length; i++) {
					JSONObject object = jsonArray.getJSONObject(i);
					ZiXunEntity entity = new ZiXunEntity(object.getString("id"), object.getString("title"));
					list.add(entity);
				}
				adapter = new ZiXunListAdapter(ZiXunListActivity.this, list);
				listView.setAdapter(adapter);
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(ZiXunListActivity.this, "暂未查找到数据!", Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	public void onBackPressed() {
		finish();
	}
	
	
	static class ZiXunListAdapter extends BaseAdapter {

		private Context context;
		private List<ZiXunEntity> list = new ArrayList<ZiXunEntity>();

		public ZiXunListAdapter(Context context, List<ZiXunEntity> list) {
			this.context = context;
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null)
			{
				convertView = LayoutInflater.from(context).inflate(R.layout.zixun_list_item, null);
				ViewHolder viewHolder = new ViewHolder();
				convertView.setTag(viewHolder);
			}
			ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.title = (TextView) convertView.findViewById(R.id.zixun_title);
			viewHolder.title.setText(list.get(position).getTitle());
			
			return convertView;
		}

		public static class ViewHolder {
			public TextView id;
			public TextView title;
		}
	}
}
