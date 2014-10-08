package com.waikato.timetable;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.os.Build;

public class AssignmentActivity extends Activity {


	int paperID;
	ListView list;
	AssignmentListAdapter adapter;
	List<AssignmentData> data;
	AssignmentDataSource source;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assignment);

		list = (ListView)findViewById(R.id.list);
		source = new AssignmentDataSource(this);
		source.open();
		paperID = (int) getIntent().getLongExtra("paperId", -1);
		Log.d("ashwin","paper id 1 "+paperID);
		data = source.getAllPaperAssignmentData(paperID);
		adapter = new AssignmentListAdapter(getApplicationContext(), data);
		list.setAdapter(adapter);
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				final int pos = arg2;
				AlertDialog.Builder alert= new AlertDialog.Builder(AssignmentActivity.this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("Delete Assignment")
				.setMessage("Are you sure?")
				.setNegativeButton(android.R.string.cancel, new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {


					}
				})
				.setPositiveButton(android.R.string.ok, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						AssignmentData t = (AssignmentData) list.getAdapter().getItem(pos);
						source.deleteAssignmentData(t);
						data = source.getAllPaperAssignmentData(paperID);
						adapter.setData(data);
						list.setAdapter(adapter);

					}
				});
				alert.show();

				return true;
			}
		});

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		data = source.getAllPaperAssignmentData(paperID);
		adapter.setData(data);
		list.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.assignment, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.menu_add) {
			/*Calendar cal = Calendar.getInstance();              
			Intent intent = new Intent(Intent.ACTION_EDIT);
			intent.setType("vnd.android.cursor.item/event");
			intent.putExtra("beginTime", cal.getTimeInMillis());
			intent.putExtra("allDay", true);
			intent.putExtra("rrule", "FREQ=YEARLY");
			//intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
			//intent.putExtra("title", "A Test Event from android app");
			startActivity(intent);*/

			Intent intent = new Intent(getApplicationContext(), AddAssignmentActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("paperId",paperID);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


}
