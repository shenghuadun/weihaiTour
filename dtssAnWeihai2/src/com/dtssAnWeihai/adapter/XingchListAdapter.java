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
import com.dtssAnWeihai.entity.XingchListEntity;

/**
 * 优惠单位查找的 适配页面
 * 
 * @author ChenPengyan
 * @Email cpy781@163.com 2014-4-21
 */

public class XingchListAdapter extends BaseAdapter {

	private Context context;
	private List<XingchListEntity> list = new ArrayList<XingchListEntity>();

	public XingchListAdapter(Context context, List<XingchListEntity> list) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.xingch_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.id = (TextView) convertView.findViewById(R.id.xingch_list_item_id);
			viewHolder.name = (TextView) convertView.findViewById(R.id.xingch_list_item_name);
			viewHolder.fromcity = (TextView) convertView.findViewById(R.id.xingch_list_item_fromcity);
			viewHolder.fromdate = (TextView) convertView.findViewById(R.id.xingch_list_item_fromdate);
			viewHolder.tocity = (TextView) convertView.findViewById(R.id.xingch_list_item_tocity);
			viewHolder.todate = (TextView) convertView.findViewById(R.id.xingch_list_item_todate);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.id.setText(list.get(position).getId());
		viewHolder.name.setText(list.get(position).getName());
		viewHolder.fromcity.setText(list.get(position).getFromCity());
		viewHolder.fromdate.setText(list.get(position).getStartDate());
		viewHolder.tocity.setText(list.get(position).getToCity());
		viewHolder.todate.setText(list.get(position).getEndDate());
		return convertView;
	}

	public static class ViewHolder {
		public TextView id;
		public TextView name;
		public TextView fromcity;
		public TextView fromdate;
		public TextView tocity;
		public TextView todate;
	}
}
