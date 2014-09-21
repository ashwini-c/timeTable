package com.waikato.timetable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class sqliteDBHelper extends SQLiteOpenHelper {

	public static final String TABLE_DATA = "data";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_TOTAL = "total";
	public static final String COLUMN_EVENT = "event";
	public static final String COLUMN_DAY = "day";
	public static final String COLUMN_START = "start";
	public static final String COLUMN_END = "end";
	public static final String COLUMN_LOC = "loc";

	private static final String DATABASE_NAME = "data.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_DATA + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_NAME
			+ " text not null, "+COLUMN_TOTAL + " integer not null, " + COLUMN_EVENT
			+ " text not null, " + COLUMN_DAY
			+ " text not null, " + COLUMN_START
			+ " text not null, " + COLUMN_END
			+ " text not null, " + COLUMN_LOC
			+ " text not null);";

	public sqliteDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(sqliteDBHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);
		onCreate(db);
	}

} 
