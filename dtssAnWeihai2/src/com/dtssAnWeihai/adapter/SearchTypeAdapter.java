package com.dtssAnWeihai.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dtssAnWeihai.activity.R;
import com.dtssAnWeihai.entity.SearchTypeEntity;

public class SearchTypeAdapter extends BaseAdapter {
	
	private Context context;
	private List<SearchTypeEntity> list = new ArrayList<SearchTypeEntity>();

	public SearchTypeAdapter(Context context, List<SearchTypeEntity> list) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.popview_item, null);
			viewHolder = new ViewHolder();
			viewHolder.id = (TextView) convertView.findViewById(R.id.popview_item_id);
			viewHolder.info = (TextView) convertView.findViewById(R.id.popview_item_info);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.id.setText(list.get(position).getId());
		viewHolder.info.setText(list.get(position).getName());
		return convertView;
	}

	public class ViewHolder {
		public TextView id;
		public TextView info;
	}
}
