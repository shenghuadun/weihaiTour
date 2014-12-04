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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dtssAnWeihai.tools.DatabaseHelper;
import com.dtssAnWeihai.tools.MyTools;

/**
 * 添加我的自定义行程详情
 * @author ChenPengyan
 * @Email cpy781@163.com
 * 2014-6-14
 */
public class XingchMylistAddActivity extends Activity {
	
	private Button traff_back, traff_submit;
	private TextView traff_type, traff_fromdate, traff_todate;
	private EditText traff_name, traff_fromcity, traff_tocity, traff_remarks;
	
	private Button other_back, other_submit;
	private EditText other_name, other_remarks;
	private TextView other_startdate, other_enddate;
	
	private String id;
	private String name = "";
	private String date = "";
	private String type = "";
	
	private AlertDialog.Builder builder;
	private ButtonOnClick buttonOnClick;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		name = intent.getStringExtra("name");
		date = intent.getStringExtra("date");
		type = intent.getStringExtra("type");
		
		if("traff".equals(type)) {
			setContentView(R.layout.xingch_mylist_add_traff);
			initViewTraff();
		} else {
			setContentView(R.layout.xingch_mylist_add_other);
			initViewOther();
		}
	}
	
	private void initViewTraff() {
		traff_back = (Button) findViewById(R.id.xingch_mylist_add_traff_back);
		traff_submit = (Button) findViewById(R.id.xingch_mylist_add_traff_submit);
		traff_back.setOnClickListener(button_ck);
		traff_submit.setOnClickListener(button_ck);
		
		traff_type = (TextView) findViewById(R.id.xingch_mylist_add_traff_type);
		traff_fromdate = (TextView) findViewById(R.id.xingch_mylist_add_traff_fromdate);
		traff_todate = (TextView) findViewById(R.id.xingch_mylist_add_traff_todate);
		traff_type.setOnClickListener(textView_ck);
		traff_fromdate.setOnClickListener(textView_ck);
		traff_todate.setOnClickListener(textView_ck);
		
		traff_name = (EditText) findViewById(R.id.xingch_mylist_add_traff_name);
		traff_fromcity = (EditText) findViewById(R.id.xingch_mylist_add_traff_fromcity);
		traff_tocity = (EditText) findViewById(R.id.xingch_mylist_add_traff_tocity);
		traff_remarks = (EditText) findViewById(R.id.xingch_mylist_add_traff_remarks);
	}
	
	private void initViewOther() {
		other_back = (Button) findViewById(R.id.xingch_mylist_add_other_back);
		other_submit = (Button) findViewById(R.id.xingch_mylist_add_other_submit);
		other_back.setOnClickListener(button_ck);
		other_submit.setOnClickListener(button_ck);
		
		other_name = (EditText) findViewById(R.id.xingch_mylist_add_other_name);
		other_remarks = (EditText) findViewById(R.id.xingch_mylist_add_other_remarks);
		
		other_startdate = (TextView) findViewById(R.id.xingch_mylist_add_other_startdate);
		other_enddate = (TextView) findViewById(R.id.xingch_mylist_add_other_enddate);
		other_startdate.setOnClickListener(textView_ck);
		other_enddate.setOnClickListener(textView_ck);
	}
	
	/**
	 * “请选择”的点击事件
	 */
	private OnClickListener textView_ck = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.xingch_mylist_add_traff_type:
				String[] typeStrings = new String[] { "火车", "汽车", "飞机", "轮船" };
				String[] typeStringIds = new String[] { "3", "7", "2", "9" };
				buttonOnClick = new ButtonOnClick(0, typeStrings, typeStringIds);
				builder = new AlertDialog.Builder(XingchMylistAddActivity.this);
				builder.setTitle("请选择交通类型");
				builder.setSingleChoiceItems(typeStrings, 0, buttonOnClick);
				builder.setPositiveButton("确定", buttonOnClick);
				builder.setNegativeButton("取消", buttonOnClick);
				builder.show();
				break;
			case R.id.xingch_mylist_add_traff_fromdate:
				choiceDateInfo(traff_fromdate);
				break;
			case R.id.xingch_mylist_add_traff_todate:
				choiceDateInfo(traff_todate);
				break;
			case R.id.xingch_mylist_add_other_startdate:
				choiceDateInfo(other_startdate);
				break;
			case R.id.xingch_mylist_add_other_enddate:
				choiceDateInfo(other_enddate);
				break;
			default:
				break;
			}
		}
	};
	
	/**
	 * 交通类型单选弹出框
	 * @author ChenPengyan
	 * @Email cpy781@163.com
	 * 2014-6-12
	 */
	private class ButtonOnClick implements DialogInterface.OnClickListener {
		private int index; // 表示选项的索引
		private String[] strings;
		private String[] strings2;

		public ButtonOnClick(int index, String[] strings, String[] strings2) {
			this.index = index;
			this.strings = strings;
			this.strings2 = strings2;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// which表示单击的按钮索引，所有的选项索引都是大于0，按钮索引都是小于0的。
			if (which >= 0) {
				index = which;
			} else {
				// 确定
				if (which == DialogInterface.BUTTON_POSITIVE) {
					traff_type.setText(strings[index]);
					type = strings2[index];
				}
			}
		}
	}
	
	/**
	 * 按钮的点击事件
	 */
	private OnClickListener button_ck = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.xingch_mylist_add_traff_back:
				break;
			case R.id.xingch_mylist_add_traff_submit:
				addTraffInfo();
				break;
			case R.id.xingch_mylist_add_other_back:
				break;
			case R.id.xingch_mylist_add_other_submit:
				addOtherInfo();
				break;
			}
			Intent intent = new Intent(XingchMylistAddActivity.this, XingchMylistActivity.class);
			intent.putExtra("id", id);
			intent.putExtra("name", name);
			intent.putExtra("date", date);
			startActivity(intent);
			finish();
		}
	};
	
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
	 * 添加交通信息
	 */
	private void addTraffInfo() {
		try {
			if(!checkTraffInfo())
				return;
			String tableId = MyTools.createRangeName();
			DatabaseHelper database = new DatabaseHelper(XingchMylistAddActivity.this);
			SQLiteDatabase db = database.getReadableDatabase();
			db.execSQL("INSERT INTO dtssAnWH_schlist VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 
					new Object[] { tableId, type, traff_name.getText().toString(), 
					traff_fromcity.getText().toString(), traff_tocity.getText().toString(), traff_fromdate.getText().toString(), 
					traff_todate.getText().toString(), MyTools.getNowDate(), traff_remarks.getText().toString(), id });
			db.close();
			Toast.makeText(XingchMylistAddActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(XingchMylistAddActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 添加其他信息
	 */
	private void addOtherInfo() {
		try {
			if(!checkOtherInfo())
				return;
			String tableId = MyTools.createRangeName();
			DatabaseHelper database = new DatabaseHelper(XingchMylistAddActivity.this);
			SQLiteDatabase db = database.getReadableDatabase();
			db.execSQL("INSERT INTO dtssAnWH_schlist VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 
					new Object[] { tableId, type, other_name.getText().toString(), 
					"///", "///", other_startdate.getText().toString(), 
					other_enddate.getText().toString(), MyTools.getNowDate(), other_remarks.getText().toString(), id });
			db.close();
			Toast.makeText(XingchMylistAddActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(XingchMylistAddActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 验证交通信息
	 * @return
	 */
	private boolean checkTraffInfo() {
		if("".equals(traff_name.getText().toString().trim())) {
			Toast.makeText(XingchMylistAddActivity.this, "请输入航班!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if("请选择".equals(traff_type.getText().toString())) {
			Toast.makeText(XingchMylistAddActivity.this, "请点击选择交通类型!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if("".equals(traff_fromcity.getText().toString().trim())) {
			Toast.makeText(XingchMylistAddActivity.this, "请输入出发城市!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if("请选择".equals(traff_fromdate.getText().toString())) {
			Toast.makeText(XingchMylistAddActivity.this, "请点击选择出发时间!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if("".equals(traff_tocity.getText().toString().trim())) {
			Toast.makeText(XingchMylistAddActivity.this, "请输入到达城市!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if("请选择".equals(traff_todate.getText().toString())) {
			Toast.makeText(XingchMylistAddActivity.this, "请点击选择到达时间!", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	/**
	 * 验证其他信息
	 * @return
	 */
	private boolean checkOtherInfo() {
		if("".equals(other_name.getText().toString().trim())) {
			Toast.makeText(XingchMylistAddActivity.this, "请输入名称!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if("请选择".equals(other_startdate.getText().toString())) {
			Toast.makeText(XingchMylistAddActivity.this, "请点击选择开始时间!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if("请选择".equals(other_enddate.getText().toString())) {
			Toast.makeText(XingchMylistAddActivity.this, "请点击选择结束时间!", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(XingchMylistAddActivity.this, XingchMylistActivity.class);
		intent.putExtra("id", id);
		intent.putExtra("name", name);
		intent.putExtra("date", date);
		startActivity(intent);
		finish();
	}
}
