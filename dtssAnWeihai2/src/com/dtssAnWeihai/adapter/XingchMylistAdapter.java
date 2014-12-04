package com.dtssAnWeihai.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dtssAnWeihai.activity.R;
import com.dtssAnWeihai.entity.XingchMylistEntity;

public class XingchMylistAdapter extends BaseAdapter {

	private Context context;
	private List<XingchMylistEntity> list = new ArrayList<XingchMylistEntity>();

	public XingchMylistAdapter(Context context, List<XingchMylistEntity> list) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.xingch_mylist_item, null);
			viewHolder = new ViewHolder();
			viewHolder.image = (ImageView) convertView.findViewById(R.id.xingch_mylist_item_image);
			viewHolder.id = (TextView) convertView.findViewById(R.id.xingch_mylist_item_id);
			viewHolder.name = (TextView) convertView.findViewById(R.id.xingch_mylist_item_name);
			viewHolder.type = (TextView) convertView.findViewById(R.id.xingch_mylist_item_type);
			
			viewHolder.xingch_mylist_item_traff_datelay = (LinearLayout) convertView.findViewById(R.id.xingch_mylist_item_traff_datelay);
			viewHolder.fromcity = (TextView) convertView.findViewById(R.id.xingch_mylist_item_traff_fromcity);
			viewHolder.tocity = (TextView) convertView.findViewById(R.id.xingch_mylist_item_traff_tocity);
			viewHolder.traff_fromdate = (TextView) convertView.findViewById(R.id.xingch_mylist_item_traff_fromdate);
			viewHolder.traff_todate = (TextView) convertView.findViewById(R.id.xingch_mylist_item_traff_todate);
			
			viewHolder.xingch_mylist_item_other_datelay = (LinearLayout) convertView.findViewById(R.id.xingch_mylist_item_other_datelay);
			viewHolder.other_fromdate = (TextView) convertView.findViewById(R.id.xingch_mylist_item_other_fromdate);
			viewHolder.other_todate = (TextView) convertView.findViewById(R.id.xingch_mylist_item_other_todate);
			
			viewHolder.remarks = (TextView) convertView.findViewById(R.id.xingch_mylist_item_remarks);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.id.setText(list.get(position).getId());
		
		if("3".equals(list.get(position).getType())) {
			viewHolder.type.setText("火车");
			viewHolder.image.setImageResource(R.drawable.icon_search_traf);
		} else if("7".equals(list.get(position).getType())) {
			viewHolder.type.setText("汽车");
			viewHolder.image.setImageResource(R.drawable.icon_search_traf);
		} else if("2".equals(list.get(position).getType())) {
			viewHolder.type.setText("飞机");
			viewHolder.image.setImageResource(R.drawable.icon_search_traf);
		} else if("9".equals(list.get(position).getType())) {
			viewHolder.type.setText("轮船");
			viewHolder.image.setImageResource(R.drawable.icon_search_traf);
		} else if("6".equals(list.get(position).getType())) {
			viewHolder.type.setText("景区");
			viewHolder.image.setImageResource(R.drawable.icon_search_scenic);
		} else if("0".equals(list.get(position).getType())) {
			viewHolder.type.setText("宾馆");
			viewHolder.image.setImageResource(R.drawable.icon_search_hotel);
		} else if("1".equals(list.get(position).getType())) {
			viewHolder.type.setText("餐饮");
			viewHolder.image.setImageResource(R.drawable.icon_search_res);
		} else if("5".equals(list.get(position).getType())) {
			viewHolder.type.setText("娱乐");
			viewHolder.image.setImageResource(R.drawable.icon_search_play);
		} else if("4".equals(list.get(position).getType())) {
			viewHolder.type.setText("购物");
			viewHolder.image.setImageResource(R.drawable.icon_search_shop);
		}
		System.out.println("************************* "+list.get(position).getType());
		if("3".equals(list.get(position).getType()) || "7".equals(list.get(position).getType()) || "2".equals(list.get(position).getType()) || "9".equals(list.get(position).getType())) {
			viewHolder.xingch_mylist_item_traff_datelay.setVisibility(View.VISIBLE);
			viewHolder.xingch_mylist_item_other_datelay.setVisibility(View.GONE);
			viewHolder.fromcity.setText(list.get(position).getFromCity());
			viewHolder.tocity.setText(list.get(position).getToCity());
			viewHolder.traff_fromdate.setText(list.get(position).getStartDate());
			viewHolder.traff_todate.setText(list.get(position).getEndDate());
		} else {
			viewHolder.xingch_mylist_item_traff_datelay.setVisibility(View.GONE);
			viewHolder.xingch_mylist_item_other_datelay.setVisibility(View.VISIBLE);
			viewHolder.type.setVisibility(View.GONE);
			viewHolder.other_fromdate.setText("开始时间 "+list.get(position).getStartDate());
			viewHolder.other_todate.setText("结束时间 "+list.get(position).getEndDate());
		}
		viewHolder.name.setText(list.get(position).getName());
		viewHolder.remarks.setText(list.get(position).getRemarks());
		return convertView;
	}

	public static class ViewHolder {
		public ImageView image;
		public TextView id;
		public TextView name;
		public TextView type;
		public TextView fromcity;
		public TextView tocity;
		public LinearLayout xingch_mylist_item_traff_datelay;
		public TextView traff_fromdate;
		public TextView traff_todate;
		public LinearLayout xingch_mylist_item_other_datelay;
		public TextView other_fromdate;
		public TextView other_todate;
		public TextView remarks;
	}
}
