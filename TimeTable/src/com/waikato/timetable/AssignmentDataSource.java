package com.waikato.timetable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.R.string;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Reminders;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class AssignmentDataSource {

	// Database fields
	private SQLiteDatabase database;
	private AssignmentDBHelper dbHelper;
	int sYear,sMonth,sDay,eYear,eMonth,eDay,eventID;
	Context ctx;
	private String[] allColumns = { AssignmentDBHelper.COLUMN_ID,
			AssignmentDBHelper.COLUMN_PAPERID,AssignmentDBHelper.COLUMN_EVENTID,AssignmentDBHelper.COLUMN_TITLE,AssignmentDBHelper.COLUMN_TIME,AssignmentDBHelper.COLUMN_DESC};

	public AssignmentDataSource(Context context) {
		ctx = context;
		dbHelper = new AssignmentDBHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public AssignmentData createAssignmentData(int paperId,String title,String time,String desc,Activity activity) 
	{

		Log.d("ashwin12"," title "+ title + " time "+time);
		int shour=17;
		int smin =30;
		int ehour=18;
		int emin =30;
		ContentResolver cr=activity.getContentResolver();

		Calendar beginTime=Calendar.getInstance();
		//String[] sDat=start.split("-");
		String[] eDat=time.split("-");
		sDay=beginTime.get(Calendar.DAY_OF_MONTH);
		sMonth=beginTime.get(Calendar.MONTH);
		sYear=beginTime.get(Calendar.YEAR);

		eDay=Integer.parseInt(eDat[0]);
		eMonth=Integer.parseInt(eDat[1]);
		eYear=Integer.parseInt(eDat[2]);

		Log.i("msg","StartDay="+sDay+"StartMonth="+sMonth+"startYear"+sYear+"and sale end date"+eDay+"Month"+eMonth+"Year="+eYear);    
		beginTime.set(sYear, sMonth-1, sDay,shour,smin);

		long startTime=beginTime.getTimeInMillis();
		Calendar endTime=Calendar.getInstance();

		endTime.set(eYear,eMonth-1,eDay,ehour,emin);
		long end1=endTime.getTimeInMillis();
		ContentValues calEvent = new ContentValues();
		calEvent.put(CalendarContract.Events.CALENDAR_ID, 1); // XXX pick)
		calEvent.put(CalendarContract.Events.TITLE, title);
		calEvent.put(CalendarContract.Events.DTSTART, startTime);
		calEvent.put(CalendarContract.Events.DTEND, end1);
		calEvent.put(CalendarContract.Events.HAS_ALARM, 1);
		calEvent.put(CalendarContract.Events.EVENT_TIMEZONE, CalendarContract.Calendars.CALENDAR_TIME_ZONE);
		Uri uri =cr.insert(CalendarContract.Events.CONTENT_URI, calEvent);

		// The returned Uri contains the content-retriever URI for 
		// the newly-inserted event, including its id
		eventID = Integer.parseInt(uri.getLastPathSegment());

		Toast.makeText(activity, "Created Calendar Event " + eventID,
				Toast.LENGTH_SHORT).show();

		// String reminderUriString = "content://com.android.calendar/reminders";

		ContentValues reminders = new ContentValues();
		reminders.put(Reminders.EVENT_ID,eventID);
		reminders.put(Reminders.METHOD, Reminders.METHOD_ALERT);
		reminders.put(Reminders.MINUTES, 10);

		Uri uri2 = cr.insert(Reminders.CONTENT_URI, reminders);

		//int x = cr.delete(Reminders.CONTENT_URI, Reminders.EVENT_ID+ " = " +id, null);


		Toast.makeText(activity, "Reminder have been saved succes fully ", Toast.LENGTH_SHORT).show();

		ContentValues values = new ContentValues();
		values.put(AssignmentDBHelper.COLUMN_PAPERID, paperId);
		values.put(AssignmentDBHelper.COLUMN_EVENTID, eventID);
		values.put(AssignmentDBHelper.COLUMN_TITLE, title);
		values.put(AssignmentDBHelper.COLUMN_TIME, time);
		values.put(AssignmentDBHelper.COLUMN_DESC, desc);
		long insertId = database.insert(AssignmentDBHelper.TABLE_DATA, null,
				values);
		Cursor cursor = database.query(AssignmentDBHelper.TABLE_DATA,
				allColumns, AssignmentDBHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		AssignmentData newAssignmentData = cursorToAssignmentData(cursor);
		cursor.close();
		return newAssignmentData;
	}

	public void deleteAssignmentData(AssignmentData assignmentData) {
		long id = assignmentData.getId();
		System.out.println("TimetableData deleted with id: " + id);
		database.delete(AssignmentDBHelper.TABLE_DATA, AssignmentDBHelper.COLUMN_ID
				+ " = " + id, null);
		ContentResolver cr=ctx.getContentResolver();
		Log.d("ashwini "," reminder event "+ assignmentData.getEventId());
		int x = cr.delete(Reminders.CONTENT_URI, Reminders.EVENT_ID+ " = " +assignmentData.getEventId(), null);
		Log.d("ashwini "," reminder deleted "+ x);
	}

	public List<AssignmentData> getAllAssignmentData() {
		List<AssignmentData> data = new ArrayList<AssignmentData>();

		Cursor cursor = database.query(AssignmentDBHelper.TABLE_DATA,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			AssignmentData d = cursorToAssignmentData(cursor);
			data.add(d);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return data;
	}

	public List<AssignmentData> getAllPaperAssignmentData(int paperid) {
		List<AssignmentData> data = new ArrayList<AssignmentData>();

		Cursor cursor = database.query(AssignmentDBHelper.TABLE_DATA,
				allColumns, AssignmentDBHelper.COLUMN_PAPERID + " = "+ paperid, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			AssignmentData d = cursorToAssignmentData(cursor);
			data.add(d);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return data;
	}

	private AssignmentData cursorToAssignmentData(Cursor cursor) {
		AssignmentData data = new AssignmentData();
		data.setId(cursor.getLong(0));
		data.setPaperId(cursor.getInt(1));
		data.setEventId(cursor.getInt(2));
		data.setTitle(cursor.getString(3));
		data.setTime(cursor.getString(4));
		data.setDesc(cursor.getString(5));

		return data;
	}
} 