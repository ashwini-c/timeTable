package com.waikato.timetable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AssignmentDBHelper extends SQLiteOpenHelper {

	public static final String TABLE_DATA = "data1";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_PAPERID = "COLUMN_PAPERID";
	public static final String COLUMN_EVENTID = "COLUMN_EVENTID";
	public static final String COLUMN_TITLE = "COLUMN_TITLE";
	public static final String COLUMN_TIME = "COLUMN_TIME";

	private static final String DATABASE_NAME = "data1.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_DATA + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_PAPERID
			+ " integer, "+COLUMN_EVENTID + " integer, " + COLUMN_TITLE
			+ " text not null, " + COLUMN_TIME
			+ " text not null);" ;

	public AssignmentDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(AssignmentDBHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);
		onCreate(db);
	}

} 
