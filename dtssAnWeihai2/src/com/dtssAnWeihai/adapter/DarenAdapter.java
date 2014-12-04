package com.dtssAnWeihai.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dtssAnWeihai.activity.R;
import com.dtssAnWeihai.entity.DarenEntity;
import com.dtssAnWeihai.tools.AsynImageLoader;
import com.dtssAnWeihai.tools.AsynImageLoader.ImageCallback;
import com.dtssAnWeihai.tools.MyTools;

/**
 * 优惠单位查找的 适配页面
 * 
 * @author ChenPengyan
 * @Email cpy781@163.com 2014-4-21
 */

public class DarenAdapter extends BaseAdapter {

	private Context context;
	private List<DarenEntity> list = new ArrayList<DarenEntity>();
	private ListView listView;
	private AsynImageLoader asynImageLoader;

	public DarenAdapter(Context context, List<DarenEntity> list, ListView listView) {
		this.context = context;
		this.list = list;
		this.listView = listView;
		asynImageLoader = new AsynImageLoader();
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
			convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.id = (TextView) convertView.findViewById(R.id.listitem_id);
			viewHolder.image = (ImageView) convertView.findViewById(R.id.listitem_image);
			viewHolder.name = (TextView) convertView.findViewById(R.id.listitem_name);
			viewHolder.author = (TextView) convertView.findViewById(R.id.listitem_author);
			viewHolder.price = (TextView) convertView.findViewById(R.id.listitem_info);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.id.setText(list.get(position).getId());
		// 显示图片
		String imageUrl = list.get(position).getImage();
		viewHolder.image.setTag(imageUrl);
		if("暂无".equals(imageUrl) || "".equals(imageUrl) || "null".equals(imageUrl)) {
			viewHolder.image.setImageResource(R.drawable.ic_launcher);
		} else {
			Drawable cachedImage = asynImageLoader.loadDrawable(imageUrl, new ImageCallback() {
				public void imageLoaded(Drawable imageDrawable, String imageUrl) {
					ImageView imageViewByTag = (ImageView) listView.findViewWithTag(imageUrl);
					if (imageViewByTag != null) {
						if(null != imageDrawable) {
							imageViewByTag.setImageBitmap(MyTools.zoomImage(MyTools.getBitmapByDrawable(imageDrawable), 120, 100));
						}
					}
				}
			});
			if (cachedImage != null) {
				viewHolder.image.setImageBitmap(MyTools.zoomImage(MyTools.getBitmapByDrawable(cachedImage), 120, 100));
			} else 
				viewHolder.image.setImageResource(R.drawable.image_indicator);
		}
		viewHolder.name.setText(list.get(position).getTitle());
		viewHolder.author.setVisibility(View.VISIBLE);
		viewHolder.author.setText("["+list.get(position).getGuiname()+"]");
		viewHolder.price.setText(list.get(position).getContent());
		return convertView;
	}

	public static class ViewHolder {
		public TextView id;
		public ImageView image;
		public TextView name;
		public TextView author;
		public TextView price;
	}
}
