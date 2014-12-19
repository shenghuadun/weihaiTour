package com.dtssAnWeihai.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dtssAnWeihai.tools.MyConfig;

public class IndexActivity extends Activity {
	private RelativeLayout fram;
	private Button main_youhui, main_scenic, main_xingch, main_zixun, main_huodong, main_gonglue, main_res, main_enter, main_shop, main_hotel, main_traff, main_suggess;

	private ImageView btn_english, btn_kr, btn_tw, btn_ru;
	
	/** 滑动图片 */
	private ScheduledExecutorService scheduledExecutorService;
	private ViewPager viewPager; // android-support-v4中的滑动组件
	private List<ImageView> imageViews; // 滑动的图片集合
	private int[] imageResId = new int[] { R.drawable.main_img05, R.drawable.main_img04 }; // 图片
	private String[] imageResTitle = new String[] { "威海旅游", "威海旅游" }; // 图片
	private List<View> dots; // 图片点
	private int currentItem = 0; // 当前图片的索引号

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.index);
		MainActivity.hideTitle();
		initView();

		fram = (RelativeLayout) findViewById(R.id.fram);
		int width = getWindowManager().getDefaultDisplay().getWidth();
		int height = width * 285 / 640;
		LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) fram.getLayoutParams();
		layoutParams.width = width;
		layoutParams.height = height;
		fram.setLayoutParams(layoutParams);

		// 初始化图片资源
		imageViews = new ArrayList<ImageView>();
		for (int i = 0; i < imageResId.length; i++) {
			final int index = i;
			ImageView imageView = new ImageView(this);
			imageView.setImageResource(imageResId[i]);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageViews.add(imageView);
			imageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
//					if(1 == index) {
//						intent.setClass(IndexActivity.this, WeihaiInfoActivity.class);
//						intent.putExtra("num", "2");
//					} else {
//						intent.setClass(IndexActivity.this, WebviewActivity.class);
//						intent.putExtra("weburl", MyConfig.WHTA_TA);
//					}
					intent.setClass(IndexActivity.this, WebviewActivity.class);
					if(0 == index)
					{
						intent.putExtra("weburl", MyConfig.WHTA_TA);
					}
					else if(1 == index)
					{
						intent.putExtra("weburl", MyConfig.WHTA_TA_M);
					}
					
					
					intent.putExtra("title", imageResTitle[index]);
					startActivity(intent);
				}
			});
		}
		// 初始化标记点
		dots = new ArrayList<View>();
		dots.add(findViewById(R.id.v_dot0));
		dots.add(findViewById(R.id.v_dot1));

		viewPager = (ViewPager) findViewById(R.id.viewpager);
		viewPager.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
		// 设置一个监听器，当ViewPager中的页面改变时调用
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
	}

	private void initView() {
		main_youhui = (Button) findViewById(R.id.main_youhui);
		main_scenic = (Button) findViewById(R.id.main_scenic);
		main_xingch = (Button) findViewById(R.id.main_xingch);
		
		main_zixun = (Button) findViewById(R.id.main_zixun);
		main_huodong = (Button) findViewById(R.id.main_huodong);
		main_gonglue = (Button) findViewById(R.id.main_gonglue);
		
		main_res = (Button) findViewById(R.id.main_res);
		main_enter = (Button) findViewById(R.id.main_enter);
		main_shop = (Button) findViewById(R.id.main_shop);
		main_hotel = (Button) findViewById(R.id.main_hotel);
		main_traff = (Button) findViewById(R.id.main_traff);
		main_suggess = (Button) findViewById(R.id.main_suggess);
		
		main_youhui.setOnClickListener(button_ck);
		main_scenic.setOnClickListener(button_ck);
		main_xingch.setOnClickListener(button_ck);

		main_zixun.setOnClickListener(button_ck);
		main_huodong.setOnClickListener(button_ck);
		main_gonglue.setOnClickListener(button_ck);
		
		main_res.setOnClickListener(button_ck);
		main_enter.setOnClickListener(button_ck);
		main_shop.setOnClickListener(button_ck);
		main_hotel.setOnClickListener(button_ck);
		main_traff.setOnClickListener(button_ck);
		main_suggess.setOnClickListener(button_ck);
		
		
		btn_english = (ImageView)findViewById(R.id.btn_english);
		btn_kr = (ImageView)findViewById(R.id.btn_kr);
		btn_tw = (ImageView)findViewById(R.id.btn_tw);
		btn_ru = (ImageView)findViewById(R.id.btn_ru);
		
		btn_english.setOnClickListener(btnNaviClickListener);
		btn_kr.setOnClickListener(btnNaviClickListener);
		btn_tw.setOnClickListener(btnNaviClickListener);
		btn_ru.setOnClickListener(btnNaviClickListener);
	}
	
	private OnClickListener btnNaviClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent();
			intent.setClass(IndexActivity.this, WebviewActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("title", "威海旅游");
			switch (v.getId())
			{
			case R.id.btn_english:
				intent.putExtra("weburl", MyConfig.NAVI_COM);
				break;
			case R.id.btn_kr:
				intent.putExtra("weburl", MyConfig.NAVI_KR);
				break;
			case R.id.btn_tw:
				intent.putExtra("weburl", MyConfig.NAVI_TW);
				break;
			case R.id.btn_ru:
				intent.putExtra("weburl", MyConfig.NAVI_RU);
				break;

			default:
				break;
			}

			startActivity(intent);
		}
	};
	
	private OnClickListener button_ck = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.main_youhui:
//				intent.setClass(MainActivity.allThis, ShakeActivity.class);
				intent.setClass(IndexActivity.this, WebviewActivity.class);
				intent.putExtra("weburl", MyConfig.WH_YOUHUI);
				intent.putExtra("title", "我们最优惠");
				break;
			case R.id.main_scenic:
				intent.setClass(MainActivity.allThis, SearchListActivity.class);
				intent.putExtra("status", "scenic");
				intent.putExtra("title", "景区景点");

//				intent.setClass(IndexActivity.this, WebviewActivity.class);
//				intent.putExtra("weburl", MyConfig.WHTA_GUIDE);
//				intent.putExtra("title", "景区景点");
				break;
			case R.id.main_xingch:
				// 行程推荐
//				intent.setClass(MainActivity.allThis, XingchActivity.class);
				intent.setClass(MainActivity.allThis, LineActivity.class);
				intent.putExtra("status", "1");
				break;

			case R.id.main_zixun:
				// 资讯
				intent.setClass(IndexActivity.this, ZiXunListActivity.class);
				break;
			case R.id.main_huodong:
				// 活动
				intent.setClass(IndexActivity.this, ActiveActivity.class);
				break;
			case R.id.main_gonglue:
				// 攻略
				intent.setClass(IndexActivity.this, GonglueActivity.class);
				break;
			case R.id.main_res:
				intent.setClass(MainActivity.allThis, SearchListActivity.class);
				intent.putExtra("status", "res");
				intent.putExtra("title", "美食");
				break;
			case R.id.main_enter:
				intent.setClass(MainActivity.allThis, SearchListActivity.class);
				intent.putExtra("status", "enter");
				intent.putExtra("title", "娱乐");
				break;
			case R.id.main_shop:
				intent.setClass(MainActivity.allThis, SearchListActivity.class);
				intent.putExtra("status", "shop");
				intent.putExtra("title", "购物");
				break;
			case R.id.main_hotel:
				intent.setClass(MainActivity.allThis, SearchListActivity.class);
				intent.putExtra("status", "hotel");
				intent.putExtra("title", "住宿");
				
//				intent.setClass(IndexActivity.this, WebviewActivity.class);
//				intent.putExtra("weburl", MyConfig.HOTELURL);
//				intent.putExtra("title", "住宿");
				break;
			case R.id.main_traff:
//				intent.setClass(MainActivity.allThis, SearchListActivity.class);
//				intent.putExtra("status", "traffic");
				intent.setClass(MainActivity.allThis, WeihaiInfoActivity.class);
				intent.putExtra("title", "交通");
				intent.putExtra("num", "3");
				break;
			case R.id.main_suggess:
				intent.setClass(MainActivity.allThis, SuggessActivity.class);
				break;
			}
			startActivity(intent);
		}
	};
	
	@Override
	protected void onStart() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每两秒钟切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 6, TimeUnit.SECONDS);
		super.onStart();
	}

	@Override
	protected void onStop() {
		// 当Activity不可见的时候停止切换
		scheduledExecutorService.shutdown();
		super.onStop();
	}

	/**
	 * 切换当前显示的图片
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			viewPager.setCurrentItem(currentItem);// 切换当前显示的图片
		};
	};

	/**
	 * 换行切换任务
	 */
	private class ScrollTask implements Runnable {
		public void run() {
			synchronized (this) {
				currentItem = (currentItem + 1) % imageViews.size();
				handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
			}
		}
	}

	/**
	 * 填充ViewPager页面的适配器
	 */
	private class MyAdapter extends PagerAdapter {

		public int getCount() {
			return imageResId.length;
		}

		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(imageViews.get(arg1));
			return imageViews.get(arg1);
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		public Parcelable saveState() {
			return null;
		}

		public void startUpdate(View arg0) {

		}

		public void finishUpdate(View arg0) {

		}
	}

	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 */
	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			currentItem = position;
			dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = position;
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}
}
