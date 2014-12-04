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
import com.dtssAnWeihai.entity.LineEntity;
import com.dtssAnWeihai.tools.DebugUtil;
import com.dtssAnWeihai.tools.SyncImageLoader;

/**
 * 优惠单位查找的 适配页面
 * 
 * @author ChenPengyan
 * @Email cpy781@163.com 2014-4-21
 */

public class LineAdapter extends BaseAdapter {

	private Context context;
	private List<LineEntity> list = new ArrayList<LineEntity>();
	private ListView listView;
	SyncImageLoader syncImageLoader;

	public LineAdapter(Context context, List<LineEntity> list, ListView listView) {
		this.context = context;
		this.list = list;
		this.listView = listView;
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
		convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);
		viewHolder = new ViewHolder();
		viewHolder.id = (TextView) convertView.findViewById(R.id.listitem_id);
		viewHolder.image = (ImageView) convertView.findViewById(R.id.listitem_image);
		viewHolder.name = (TextView) convertView.findViewById(R.id.listitem_name);
		viewHolder.phone = (TextView) convertView.findViewById(R.id.listitem_info);
		convertView.setTag(position);
		try {
			viewHolder.id.setText(list.get(position).getId());
			viewHolder.name.setText(list.get(position).getName());
			viewHolder.phone.setText("电话:" + list.get(position).getPhone());
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
		public TextView phone;
	}
	
	SyncImageLoader.OnImageLoadListener imageLoadListener = new SyncImageLoader.OnImageLoadListener() {
		@Override
		public void onImageLoad(Integer t, Drawable drawable) {
			View view = listView.findViewWithTag(t);
			if (view != null) {
				ImageView iv = (ImageView) view.findViewById(R.id.listitem_image);
				iv.setBackgroundDrawable(drawable);
			}
		}

		@Override
		public void onError(Integer t) {
			LineEntity entity = (LineEntity) getItem(t);
			View view = listView.findViewWithTag(entity);
			if (view != null) {
				ImageView iv = (ImageView) view.findViewById(R.id.listitem_image);
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
				// loadImage();
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
