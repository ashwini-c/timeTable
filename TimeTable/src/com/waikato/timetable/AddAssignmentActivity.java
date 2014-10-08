package com.waikato.timetable;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.os.Build;

public class AddAssignmentActivity extends Activity {
	EditText e1,e2;
	Button b1;
	String title,time;
	Activity ac = this;
	int paperID;
	AssignmentDataSource sqlite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_assignment);
		sqlite = new AssignmentDataSource(this);
		sqlite.open();
		e1 = (EditText) findViewById(R.id.editText1);
		e2 = (EditText) findViewById(R.id.editText2);
		b1 = (Button) findViewById(R.id.button1);
		paperID = getIntent().getIntExtra("paperId", -1);
		b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				title = e1.getEditableText().toString();
				time = e2.getEditableText().toString();
				Log.d("ashwin1"," title "+ title + " time "+time);
				sqlite.createAssignmentData(paperID, title, time,ac);
				finish();

			}
		});


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_assignment, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


}
