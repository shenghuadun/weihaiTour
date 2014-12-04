package com.dtssAnWeihai.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dtssAnWeihai.activity.R;
import com.dtssAnWeihai.entity.SearchListEntity;
import com.dtssAnWeihai.tools.DebugUtil;
import com.dtssAnWeihai.tools.SyncImageLoader;

/**
 * 优惠单位查找的 适配页面
 * 
 * @author ChenPengyan
 * @Email cpy781@163.com 2014-4-21
 */

public class SearchListAdapter extends BaseAdapter {

	private Context context;
	private List<SearchListEntity> list = new ArrayList<SearchListEntity>();
	private ListView listView;
	private String infoType;
	SyncImageLoader syncImageLoader;

	public SearchListAdapter(Context context, List<SearchListEntity> list, ListView listView, String infoType) {
		this.context = context;
		this.list = list;
		this.listView = listView;
		this.infoType = infoType;
		
		syncImageLoader = new SyncImageLoader();
		listView.setOnScrollListener(onScrollListener);
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
		convertView = LayoutInflater.from(context).inflate(R.layout.search_list_item, null);
		viewHolder = new ViewHolder();
		viewHolder.id = (TextView) convertView.findViewById(R.id.search_list_id);
		viewHolder.image = (ImageView) convertView.findViewById(R.id.search_list_image);
		viewHolder.name = (TextView) convertView.findViewById(R.id.search_list_name);
		viewHolder.address = (TextView) convertView.findViewById(R.id.search_list_address);
		viewHolder.distance = (TextView) convertView.findViewById(R.id.search_list_distance);
		convertView.setTag(position);
		try {
			viewHolder.id.setText(list.get(position).getId());
			viewHolder.name.setText(list.get(position).getName());
			viewHolder.address.setText(list.get(position).getAddress());
			if("scenic".equals(infoType)) {
				viewHolder.distance.setText(list.get(position).getDistance());
			}
			// 显示图片
			String imageUrl = list.get(position).getImage();
			if("暂无".equals(imageUrl) || "".equals(imageUrl) || "null".equals(imageUrl)) {
				viewHolder.image.setImageResource(R.drawable.ic_launcher);
			} else {
				syncImageLoader.loadImage(position, imageUrl, imageLoadListener);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	public static class ViewHolder {
		public TextView id;
		public ImageView image;
		public TextView name;
		public TextView address;
		public TextView distance;
	}
	
	SyncImageLoader.OnImageLoadListener imageLoadListener = new SyncImageLoader.OnImageLoadListener() {
		@Override
		public void onImageLoad(Integer t, Drawable drawable) {
			View view = listView.findViewWithTag(t);
			if (view != null) {
				ImageView iv = (ImageView) view.findViewById(R.id.search_list_image);
				iv.setBackgroundDrawable(drawable);
			}
		}

		@Override
		public void onError(Integer t) {
			SearchListEntity entity = (SearchListEntity) getItem(t);
			View view = listView.findViewWithTag(entity);
			if (view != null) {
				ImageView iv = (ImageView) view.findViewById(R.id.search_list_image);
				iv.setBackgroundResource(R.drawable.ic_launcher);
			}
		}
	};
	
	public void loadImage() {
		int start = listView.getFirstVisiblePosition();
		int end = listView.getLastVisiblePosition();
		if (end >= getCount()) {
			end = getCount() - 1;
		}
		syncImageLoader.setLoadLimit(start, end);
		syncImageLoader.unlock();
	}
	
	AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
				DebugUtil.debug("SCROLL_STATE_FLING");
				syncImageLoader.lock();
				break;
			case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
				DebugUtil.debug("SCROLL_STATE_IDLE");
				loadImage();
				break;
			case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				syncImageLoader.lock();
				break;

			default:
				break;
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
		}
	};
	
}
