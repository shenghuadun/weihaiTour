package com.dtssAnWeihai.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Environment;

/**
 * project属性
 * @author ChenPengyan
 * @Email cpy781@163.com
 * 2014-4-24
 */
public class MyConfig {
	/**
	 * 威海
	 */
	public static final String AREANAME = "威海";
	/**
	 * 威海旅游
	 */
	public static final String APPNAME = "威海旅游";
	/**
	 * 服务器地址
	 */
	public static final String HTTPURL = "http://60.216.117.244/wisdomyt";
	/**
	 * 威海—区号
	 */
	public static final String AREAID = "371000";
	/**
	 * 版本号
	 */
	public static final String VERSION = "1.0";
	/**
	 *  是否是第一次打开
	 */
	public static boolean OPENNUM = true;
	/**
	 * 威海—休闲护照网址
	 */
	public static final String WH_LVHZ = "http://www.whta.gov.cn/gov/lyhz/index.html";
	/**
	 * 威海—休闲护照网址
	 */
	public static final String WH_YOUHUI = "http://221.2.140.184:8090/whto/app/youhui.html";
	/**
	 * 威海旅游政务网—手机版网址
	 */
	public static final String WHTA_TA = "http://www.whta.gov.cn/wap.html";
	public static final String WHTA_TA_M = "http://m.whta.cn";
	
	/**
	 * 导游
	 */
	public static final String WHTA_GUIDE = "http://221.2.140.184:8090/whto/app/dydl.html";
	
	public static final String NAVI_COM = "http://www.visitweihai.com";
	public static final String NAVI_KR = "http://www.visitweihai.kr";
	public static final String NAVI_TW = "http://www.visitweihai.tw";
	public static final String NAVI_RU = "http://www.visitweihai.ru";
	/**
	 * 住宿——携程网址
	 */
	public static final String HOTELURL = "http://m.ctrip.com/webapp/hotel/";
	/**
	 * 百度API key
	 */
	public static String BAIDUMAPKEY = "XKPEFdOeC9MoMdwjIFTgWLlL";
	/**
	 * 反地理 key
	 */
	public static String BAIDUMAPLOCATIONKEY = "05SNv9zaY1WOQwqGMBs4gOzj";
	/**
	 * 天气区号
	 */
	public static String WEATHER = "http://m.weather.com.cn/atad/101121301.html";
	/**
	 * 项目存储数据的文件夹
	 */
	public static String filepath = Environment.getExternalStorageDirectory().getPath() + "/dtssWeihai/";

	/**
	 * 判断网络环境
	 * 
	 * @param context
	 * @return
	 */
	public static String getNetworkState(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 手机网络判断
		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return "MOBILE";
		}
		// Wifi网络判断
		state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return "WIFI";
		}
		// 网络未连接
		return "NONE";
	}
}
