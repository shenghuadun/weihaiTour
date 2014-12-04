package com.dtssAnWeihai.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dtssAnWeihai.activity.R;
import com.dtssAnWeihai.entity.XingchTuiListListEntity;

public class XingchTuiListListAdapter extends BaseAdapter {

	private Context context;
	private List<XingchTuiListListEntity> list = new ArrayList<XingchTuiListListEntity>();

	public XingchTuiListListAdapter(Context context, List<XingchTuiListListEntity> list) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.xingch_tui_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.image = (ImageView) convertView.findViewById(R.id.xingch_tui_list_item_image);
			viewHolder.id = (TextView) convertView.findViewById(R.id.xingch_tui_list_item_id);
			viewHolder.name = (TextView) convertView.findViewById(R.id.xingch_tui_list_item_name);
			viewHolder.address = (TextView) convertView.findViewById(R.id.xingch_tui_list_item_address);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.id.setText(list.get(position).getNodeId());
		viewHolder.name.setText(list.get(position).getDesc());
		viewHolder.address.setText(list.get(position).getAddress());
		
		if("3".equals(list.get(position).getNodeType())) {
			// 火车");
			viewHolder.image.setImageResource(R.drawable.icon_search_traf);
		} else if("7".equals(list.get(position).getNodeType())) {
			// 汽车
			viewHolder.image.setImageResource(R.drawable.icon_search_traf);
		} else if("2".equals(list.get(position).getNodeType())) {
			// 飞机
			viewHolder.image.setImageResource(R.drawable.icon_search_traf);
		} else if("9".equals(list.get(position).getNodeType())) {
			// 轮船
			viewHolder.image.setImageResource(R.drawable.icon_search_traf);
		} else if("6".equals(list.get(position).getNodeType())) {
			// 景区
			viewHolder.image.setImageResource(R.drawable.icon_search_scenic);
		} else if("0".equals(list.get(position).getNodeType())) {
			// 宾馆
			viewHolder.image.setImageResource(R.drawable.icon_search_hotel);
		} else if("1".equals(list.get(position).getNodeType())) {
			// 餐饮
			viewHolder.image.setImageResource(R.drawable.icon_search_res);
		} else if("5".equals(list.get(position).getNodeType())) {
			// 娱乐
			viewHolder.image.setImageResource(R.drawable.icon_search_play);
		} else if("4".equals(list.get(position).getNodeType())) {
			// 购物
			viewHolder.image.setImageResource(R.drawable.icon_search_shop);
		} else if("8".equals(list.get(position).getNodeType())) {
			// 自定义
			viewHolder.image.setImageResource(R.drawable.icon_search_zidingyi);
		}
		return convertView;
	}

	public static class ViewHolder {
		public ImageView image;
		public TextView id;
		public TextView name;
		public TextView address;
	}
}
