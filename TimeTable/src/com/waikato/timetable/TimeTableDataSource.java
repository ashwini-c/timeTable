package com.waikato.timetable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.R.string;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

public class TimeTableDataSource {

	// Database fields
	private SQLiteDatabase database;
	private sqliteDBHelper dbHelper;
	private String[] allColumns = { sqliteDBHelper.COLUMN_ID,
			sqliteDBHelper.COLUMN_NAME,sqliteDBHelper.COLUMN_TOTAL,sqliteDBHelper.COLUMN_EVENT,sqliteDBHelper.COLUMN_DAY,sqliteDBHelper.COLUMN_START,sqliteDBHelper.COLUMN_END,sqliteDBHelper.COLUMN_LOC};

	public TimeTableDataSource(Context context) {
		dbHelper = new sqliteDBHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public TimetableData createTimetableData(String name,int total,
			ArrayList< String> eventList ,
			ArrayList< String> dayList ,
			ArrayList< String> startList ,
			ArrayList< String> endList,
			ArrayList< String> locList) 
	{

		String listEvent = TextUtils.join(",", eventList);
		String listDay = TextUtils.join(",", dayList);
		String listStart = TextUtils.join(",", startList);
		String listEnd = TextUtils.join(",", endList);
		String listLoc = TextUtils.join(",", locList);

		ContentValues values = new ContentValues();
		values.put(sqliteDBHelper.COLUMN_NAME, name);
		values.put(sqliteDBHelper.COLUMN_TOTAL, total);
		values.put(sqliteDBHelper.COLUMN_EVENT, listEvent);
		values.put(sqliteDBHelper.COLUMN_DAY, listDay);
		values.put(sqliteDBHelper.COLUMN_START, listStart);
		values.put(sqliteDBHelper.COLUMN_END, listEnd);
		values.put(sqliteDBHelper.COLUMN_LOC, listLoc);
		long insertId = database.insert(sqliteDBHelper.TABLE_DATA, null,
				values);
		Cursor cursor = database.query(sqliteDBHelper.TABLE_DATA,
				allColumns, sqliteDBHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		TimetableData newTimetableData = cursorToTimetableData(cursor);
		cursor.close();
		return newTimetableData;
	}

	public void deleteTimetableData(TimetableData timetable) {
		long id = timetable.getId();
		System.out.println("TimetableData deleted with id: " + id);
		database.delete(sqliteDBHelper.TABLE_DATA, sqliteDBHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public List<TimetableData> getAllTimetableData() {
		List<TimetableData> data = new ArrayList<TimetableData>();

		Cursor cursor = database.query(sqliteDBHelper.TABLE_DATA,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			TimetableData d = cursorToTimetableData(cursor);
			data.add(d);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return data;
	}

	private TimetableData cursorToTimetableData(Cursor cursor) {
		TimetableData data = new TimetableData();
		data.setId(cursor.getLong(0));
		data.setName(cursor.getString(1));
		data.setTotal(cursor.getInt(2));
		ArrayList< String> event = new ArrayList<String>();
		ArrayList< String> day = new ArrayList<String>();
		ArrayList< String> start = new ArrayList<String>();
		ArrayList< String> end = new ArrayList<String>();
		ArrayList< String> loc = new ArrayList<String>();

		for (String str:cursor.getString(3).split("\\s*,\\s*"))
		{
			event.add(str);
		}
		for (String str:cursor.getString(4).split("\\s*,\\s*"))
		{
			day.add(str);
		}
		for (String str:cursor.getString(5).split("\\s*,\\s*"))
		{
			start.add(str);
		}
		for (String str:cursor.getString(6).split("\\s*,\\s*"))
		{
			end.add(str);
		}
		for (String str:cursor.getString(7).split("\\s*,\\s*"))
		{
			loc.add(str);
		}
		data.setEvent((ArrayList<String>) event);
		data.setDay((ArrayList<String>)day);
		data.setStart((ArrayList<String>)start);
		data.setEnd((ArrayList<String>)end);
		data.setLoc((ArrayList<String>)loc);
		return data;
	}
} 