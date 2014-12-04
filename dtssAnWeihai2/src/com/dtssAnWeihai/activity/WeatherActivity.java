package com.dtssAnWeihai.activity;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import com.dtssAnWeihai.tools.MyConfig;

public class WeatherActivity extends BaseActivity {
	
	private TextView weather_date, weather_wendu, weather_windy;
	private ImageView weather_image;
	
	private String sendPostStr;
	
	private ImageView weather2_image, weather3_image, weather4_image;
	private TextView weather2_wendu, weather3_wendu, weather4_wendu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather);
		initview();
		setTitle("天气");
		getWeatherInfo();
	}
	
	private void initview() {
		weather_date = (TextView) findViewById(R.id.weather_date);
		weather_wendu = (TextView) findViewById(R.id.weather_wendu);
		weather_windy = (TextView) findViewById(R.id.weather_windy);
		weather_image = (ImageView) findViewById(R.id.weather_image);
		
		weather2_image = (ImageView) findViewById(R.id.weather2_image);
		weather3_image = (ImageView) findViewById(R.id.weather3_image);
		weather4_image = (ImageView) findViewById(R.id.weather4_image);
		weather2_wendu = (TextView) findViewById(R.id.weather2_wendu);
		weather3_wendu = (TextView) findViewById(R.id.weather3_wendu);
		weather4_wendu = (TextView) findViewById(R.id.weather4_wendu);
	}

	private void getWeatherInfo() 
	{
		showLoading();
		doNetWorkJob(MyConfig.WEATHER, null, weather_handler, "get");
	}
	private Handler weather_handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			hideLoading();
			try {
					// ==========================解析JSON得到天气===========================
					JSONObject json = ((JSONObject)msg.obj).getJSONObject("weatherinfo");
					weather_date.setText(json.getString("date_y"));
					weather_wendu.setText(json.getString("temp1"));
					weather_windy.setText(json.getString("wind1"));
					String title = json.getString("img_title1");
					if (title.equals("晴")) {
						weather_image.setImageResource(R.drawable.weathericon_condition_01);
					} else if (title.equals("多云")) {
						weather_image.setImageResource(R.drawable.weathericon_condition_02);
					} else if (title.equals("阴")) {
						weather_image.setImageResource(R.drawable.weathericon_condition_04);
					} else if (title.equals("雾")) {
						weather_image.setImageResource(R.drawable.weathericon_condition_05);
					} else if (title.equals("沙尘暴")) {
						weather_image.setImageResource(R.drawable.weathericon_condition_06);
					} else if (title.equals("阵雨")) {
						weather_image.setImageResource(R.drawable.weathericon_condition_07);
					} else if (title.equals("小雨") || title.equals("小到中雨")) {
						weather_image.setImageResource(R.drawable.weathericon_condition_08);
					} else if (title.equals("大雨")) {
						weather_image.setImageResource(R.drawable.weathericon_condition_09);
					} else if (title.equals("雷阵雨")) {
						weather_image.setImageResource(R.drawable.weathericon_condition_10);
					} else if (title.equals("小雪")) {
						weather_image.setImageResource(R.drawable.weathericon_condition_11);
					} else if (title.equals("大雪")) {
						weather_image.setImageResource(R.drawable.weathericon_condition_12);
					} else if (title.equals("雨夹雪")) {
						weather_image.setImageResource(R.drawable.weathericon_condition_13);
					}
					
					weather2_wendu.setText(json.getString("temp2"));
					weather3_wendu.setText(json.getString("temp3"));
					weather4_wendu.setText(json.getString("temp4"));
					getWeatherImage(json.getString("img_title2"), json.getString("img_title3"), json.getString("img_title4"));
				} catch (Exception e) {
					e.printStackTrace();
				}
		};
	};
	
	private void getWeatherImage(String title2, String title3, String title4) {
		if (title2.equals("晴")) {
			weather2_image.setImageResource(R.drawable.weathericon_condition_01);
		} else if (title2.equals("多云")) {
			weather2_image.setImageResource(R.drawable.weathericon_condition_02);
		} else if (title2.equals("阴")) {
			weather2_image.setImageResource(R.drawable.weathericon_condition_04);
		} else if (title2.equals("雾")) {
			weather2_image.setImageResource(R.drawable.weathericon_condition_05);
		} else if (title2.equals("沙尘暴")) {
			weather2_image.setImageResource(R.drawable.weathericon_condition_06);
		} else if (title2.equals("阵雨")) {
			weather2_image.setImageResource(R.drawable.weathericon_condition_07);
		} else if (title2.equals("小雨") || title2.equals("小到中雨")) {
			weather2_image.setImageResource(R.drawable.weathericon_condition_08);
		} else if (title2.equals("大雨")) {
			weather2_image.setImageResource(R.drawable.weathericon_condition_09);
		} else if (title2.equals("雷阵雨")) {
			weather2_image.setImageResource(R.drawable.weathericon_condition_10);
		} else if (title2.equals("小雪")) {
			weather2_image.setImageResource(R.drawable.weathericon_condition_11);
		} else if (title2.equals("大雪")) {
			weather2_image.setImageResource(R.drawable.weathericon_condition_12);
		} else if (title2.equals("雨夹雪")) {
			weather2_image.setImageResource(R.drawable.weathericon_condition_13);
		}
		
		if (title3.equals("晴")) {
			weather3_image.setImageResource(R.drawable.weathericon_condition_01);
		} else if (title3.equals("多云")) {
			weather3_image.setImageResource(R.drawable.weathericon_condition_02);
		} else if (title3.equals("阴")) {
			weather3_image.setImageResource(R.drawable.weathericon_condition_04);
		} else if (title3.equals("雾")) {
			weather3_image.setImageResource(R.drawable.weathericon_condition_05);
		} else if (title3.equals("沙尘暴")) {
			weather3_image.setImageResource(R.drawable.weathericon_condition_06);
		} else if (title3.equals("阵雨")) {
			weather3_image.setImageResource(R.drawable.weathericon_condition_07);
		} else if (title3.equals("小雨") || title3.equals("小到中雨")) {
			weather3_image.setImageResource(R.drawable.weathericon_condition_08);
		} else if (title3.equals("大雨")) {
			weather3_image.setImageResource(R.drawable.weathericon_condition_09);
		} else if (title3.equals("雷阵雨")) {
			weather3_image.setImageResource(R.drawable.weathericon_condition_10);
		} else if (title3.equals("小雪")) {
			weather3_image.setImageResource(R.drawable.weathericon_condition_11);
		} else if (title3.equals("大雪")) {
			weather3_image.setImageResource(R.drawable.weathericon_condition_12);
		} else if (title3.equals("雨夹雪")) {
			weather3_image.setImageResource(R.drawable.weathericon_condition_13);
		}
		
		if (title4.equals("晴")) {
			weather4_image.setImageResource(R.drawable.weathericon_condition_01);
		} else if (title4.equals("多云")) {
			weather4_image.setImageResource(R.drawable.weathericon_condition_02);
		} else if (title4.equals("阴")) {
			weather4_image.setImageResource(R.drawable.weathericon_condition_04);
		} else if (title4.equals("雾")) {
			weather4_image.setImageResource(R.drawable.weathericon_condition_05);
		} else if (title4.equals("沙尘暴")) {
			weather4_image.setImageResource(R.drawable.weathericon_condition_06);
		} else if (title4.equals("阵雨")) {
			weather4_image.setImageResource(R.drawable.weathericon_condition_07);
		} else if (title4.equals("小雨") || title4.equals("小到中雨")) {
			weather4_image.setImageResource(R.drawable.weathericon_condition_08);
		} else if (title4.equals("大雨")) {
			weather4_image.setImageResource(R.drawable.weathericon_condition_09);
		} else if (title4.equals("雷阵雨")) {
			weather4_image.setImageResource(R.drawable.weathericon_condition_10);
		} else if (title4.equals("小雪")) {
			weather4_image.setImageResource(R.drawable.weathericon_condition_11);
		} else if (title4.equals("大雪")) {
			weather4_image.setImageResource(R.drawable.weathericon_condition_12);
		} else if (title4.equals("雨夹雪")) {
			weather4_image.setImageResource(R.drawable.weathericon_condition_13);
		}
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
}
