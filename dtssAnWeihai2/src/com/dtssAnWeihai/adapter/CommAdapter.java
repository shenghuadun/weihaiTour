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
import com.dtssAnWeihai.entity.CommEntity;

/**
 * 优惠单位查找的 适配页面
 * 
 * @author ChenPengyan
 * @Email cpy781@163.com 2014-4-21
 */

public class CommAdapter extends BaseAdapter {

	private Context context;
	private List<CommEntity> list = new ArrayList<CommEntity>();

	public CommAdapter(Context context, List<CommEntity> list) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.comm_list, null);
			viewHolder = new ViewHolder();
			viewHolder.id = (TextView) convertView.findViewById(R.id.comm_list_id);
			viewHolder.name = (TextView) convertView.findViewById(R.id.comm_list_username);
			viewHolder.feel = (TextView) convertView.findViewById(R.id.comm_list_feel);
			viewHolder.time = (TextView) convertView.findViewById(R.id.comm_list_time);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.id.setText(list.get(position).getId());
		viewHolder.name.setText(list.get(position).getProdName());
		viewHolder.feel.setText(list.get(position).getFeel());
		viewHolder.time.setText(list.get(position).getCreateTime());
		return convertView;
	}

	public static class ViewHolder {
		public TextView id;
		public TextView name;
		public TextView feel;
		public TextView time;
	}
}
