package com.dtssAnWeihai.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dtssAnWeihai.activity.R;
import com.dtssAnWeihai.entity.XingchTuiListEntity;
import com.dtssAnWeihai.entity.XingchTuiListListEntity;

public class XingchTuiListAdapter extends BaseAdapter {

	private Context context;
	private List<XingchTuiListEntity> list = new ArrayList<XingchTuiListEntity>();

	private XingchTuiListListAdapter adapter;
	private List<XingchTuiListListEntity> listList;

	public XingchTuiListAdapter(Context context, List<XingchTuiListEntity> list) {
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
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.xingch_tui_list_item1, null);
			viewHolder = new ViewHolder();
			viewHolder.title = (TextView) convertView.findViewById(R.id.xingch_tui_list_item1_title);
			viewHolder.listView = (ListView) convertView.findViewById(R.id.xingch_tui_list_item1_listView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.title.setText(list.get(position).getDay() + "  " + list.get(position).getStartDate());
		try {
			JSONArray jsonArray = new JSONArray(list.get(position).getDayDetail());
			listList = new ArrayList<XingchTuiListListEntity>();
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				XingchTuiListListEntity entity = new XingchTuiListListEntity(object.getString("nodeId"), object.getString("desc"), object.getString("address"), object.getString("nodeType"));
				listList.add(entity);
			}
			adapter = new XingchTuiListListAdapter(context, listList);
			viewHolder.listView.setAdapter(adapter);
			setListViewHeightBasedOnChildren(viewHolder.listView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	public static class ViewHolder {
		public TextView title;
		public ListView listView;
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

}
