package com.dtssAnWeihai.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "dtssAnWH_db.db"; // 数据库名称
	private static final int VERSION = 1; // 数据库版本

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 行程表 line=6
		db.execSQL("CREATE TABLE dtssAnWH_scheduling " +
				"(_id VARCHAR, _name VARCHAR, _image VARCHAR, _phone VARCHAR)");
		// 收藏表 line=6
		db.execSQL("CREATE TABLE dtssAnWH_favorite " +
				"(_id VARCHAR, _proId VARCHAR, _proName VARCHAR, _proType VARCHAR, _proDesc VARCHAR, _proImage VARCHAR)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS dtssAnWH_scheduling");
		db.execSQL("DROP TABLE IF EXISTS dtssAnWH_schlist");
		db.execSQL("DROP TABLE IF EXISTS dtssAnWH_favorite");
		
		onCreate(db);
	}

}
