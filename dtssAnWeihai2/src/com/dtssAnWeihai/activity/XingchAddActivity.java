package com.dtssAnWeihai.activity;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dtssAnWeihai.tools.DatabaseHelper;
import com.dtssAnWeihai.tools.MyTools;

/**
 * 添加我的自定义行程
 * @author ChenPengyan
 * @Email cpy781@163.com
 * 2014-6-14
 */
public class XingchAddActivity extends Activity {
	
	private Button xingch_add_back, xingch_add_submit;
	private EditText xingch_add_name, xingch_add_fromcity, xingch_add_tocity;
	private TextView xingch_add_fromdate, xingch_add_todate;
	private ImageView xingch_add_image;
	
	private String id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xingch_add);
		
		xingch_add_back = (Button) findViewById(R.id.xingch_add_back);
		xingch_add_submit = (Button) findViewById(R.id.xingch_add_submit);
		xingch_add_name = (EditText) findViewById(R.id.xingch_add_name);
		xingch_add_fromcity = (EditText) findViewById(R.id.xingch_add_fromcity);
		xingch_add_tocity = (EditText) findViewById(R.id.xingch_add_tocity);
		xingch_add_fromdate = (TextView) findViewById(R.id.xingch_add_fromdate);
		xingch_add_todate = (TextView) findViewById(R.id.xingch_add_todate);
		xingch_add_image = (ImageView) findViewById(R.id.xingch_add_image);
		
		int width = getWindowManager().getDefaultDisplay().getWidth();
		int height = width * 218 / 617;
		LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) xingch_add_image.getLayoutParams();
		layoutParams.width = width - 15;
		layoutParams.height = height;
		layoutParams.setMargins(5, 5, 5, 5);
		xingch_add_image.setLayoutParams(layoutParams);
		
		xingch_add_fromdate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				choiceDateInfo(xingch_add_fromdate);
			}
		});
		xingch_add_todate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				choiceDateInfo(xingch_add_todate);
			}
		});
		xingch_add_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(XingchAddActivity.this, XingchActivity.class);
				startActivity(intent);
				finish();
			}
		});
		xingch_add_submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(!checkInfo()) {
					return;
				}
				try {
					id = MyTools.createRangeName();
					DatabaseHelper database = new DatabaseHelper(XingchAddActivity.this);
					SQLiteDatabase db = database.getReadableDatabase();
					db.execSQL("INSERT INTO dtssAnWH_scheduling VALUES (?, ?, ?, ?, ?, ?)", 
							new Object[] { id, xingch_add_name.getText().toString(), xingch_add_fromcity.getText().toString(), 
							xingch_add_tocity.getText().toString(), xingch_add_fromdate.getText().toString(), xingch_add_todate.getText().toString()});
					db.close();
					Toast.makeText(XingchAddActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(XingchAddActivity.this, XingchMylistActivity.class);
					intent.putExtra("id", id);
					intent.putExtra("name", xingch_add_name.getText().toString());
					intent.putExtra("date", xingch_add_fromdate.getText().toString() +" 至 "+xingch_add_todate.getText().toString());
					startActivity(intent);
					XingchAddActivity.this.finish();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(XingchAddActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}
	
	/**
	 * 选取时间 选择器,格式为：xxxx-xx-xx xx:xx
	 * @param textView
	 */
	private void choiceDateInfo(final TextView textView) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = View.inflate(this, R.layout.date_time_dialog, null);
		final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
		final TimePicker timePicker = (android.widget.TimePicker) view.findViewById(R.id.time_picker);
		builder.setView(view);

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);

		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(Calendar.MINUTE);

		final int inType = textView.getInputType();
		textView.setInputType(InputType.TYPE_NULL);
		textView.setInputType(inType);

		builder.setTitle("选取时间");
		builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				StringBuffer sb = new StringBuffer();
				sb.append(String.format("%d-%02d-%02d", datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
				sb.append("  ");
				sb.append(timePicker.getCurrentHour()).append(":").append(timePicker.getCurrentMinute());
				textView.setText(sb);
				dialog.cancel();
			}
		});

		Dialog dialog = builder.create();
		dialog.show();
	}
	
	/**
	 * 验证交通信息
	 * @return
	 */
	private boolean checkInfo() {
		if("".equals(xingch_add_name.getText().toString().trim())) {
			Toast.makeText(XingchAddActivity.this, "请输入名称!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if("".equals(xingch_add_fromcity.getText().toString().trim())) {
			Toast.makeText(XingchAddActivity.this, "请输入出发城市!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if("请选择".equals(xingch_add_fromdate.getText().toString())) {
			Toast.makeText(XingchAddActivity.this, "请点击选择出发时间!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if("".equals(xingch_add_tocity.getText().toString().trim())) {
			Toast.makeText(XingchAddActivity.this, "请输入到达城市!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if("请选择".equals(xingch_add_todate.getText().toString())) {
			Toast.makeText(XingchAddActivity.this, "请点击选择到达时间!", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(XingchAddActivity.this, XingchActivity.class);
		startActivity(intent);
		finish();
	}
}
