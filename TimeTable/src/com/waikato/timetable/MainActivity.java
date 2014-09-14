package com.waikato.timetable;


import java.util.ArrayList;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.SearchView.OnQueryTextListener;

public class MainActivity extends Activity implements OnQueryTextListener{

	TextView t1;
	String data;
	ProgressDialog mProgress;
	String resulttxt;
	String format = "code";
	AlertDialog.Builder alert;
	Activity context;
	ArrayList< String> nameList = new ArrayList<String>();
	ArrayList< String> codeList = new ArrayList<String>();
	PaperlistAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context= MainActivity.this;
		setContentView(R.layout.activity_main);

		t1 = (TextView)findViewById(R.id.textview);

		alert = new AlertDialog.Builder(MainActivity.this)
		.setTitle("Connection failed")
		.setMessage("Check your internet connection and try again.")
		.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) { 
				// continue with delete
			}
		})

		.setIcon(android.R.drawable.ic_dialog_alert);
	}


	private void searchPaper()
	{

		mProgress  = new ProgressDialog(MainActivity.this);
		mProgress.setTitle("WAIKATO TIMETABLE");
		mProgress.setIndeterminate(false);
		mProgress.setCancelable(false);
		mProgress.setMessage("Loading..........");
		mProgress.show();
		Intent intent = new Intent(getApplicationContext(), TimetableClient.class);
		intent.putExtra(TimetableClient.REQUEST_DATA, data);
		intent.putExtra(TimetableClient.REQUEST_FORMAT, format);
		intent.putExtra(TimetableClient.RESPONSE_RECEIVER, result_receiver);
		startService(intent);
	}



	private ResultReceiver result_receiver = new ResultReceiver(new Handler())
	{
		protected void onReceiveResult(int resultCode, Bundle resultData) {

			switch (resultCode) {
			case -1:
				alert.show();
				break;
			case 0:
			case 1:
				resulttxt=resultData.getString(TimetableClient.RESPONSE_MESSAGE);
				setResultCode();
				break;
			case 2:
				nameList=resultData.getStringArrayList(TimetableClient.RESPONSE_NAMELIST);
				codeList=resultData.getStringArrayList(TimetableClient.RESPONSE_CODELIST);
				setResultName();
				break;

			default:
				break;
			}


			mProgress.dismiss();


		}
	};
	public void setResultCode()
	{


		t1.setText(resulttxt);
	}

	public void setResultName()
	{
		/*t1.setVisibility(View.GONE);
		list.setVisibility(View.VISIBLE);*/
		adapter = new PaperlistAdapter(getApplicationContext(),nameList,codeList);
		//list.setAdapter(adapter);
		//((PaperlistAdapter)list.getAdapter()).notifyDataSetChanged();

		AlertDialog.Builder builderSingle = new AlertDialog.Builder(
				context);
		//builderSingle.setIcon(R.drawable.ic_launcher);
		builderSingle.setTitle("Select One Paper");

		builderSingle.setNegativeButton("cancel",
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}
				);

		builderSingle.setAdapter(adapter,
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				data= codeList.get(which);
				format = "code";
				searchPaper();}
		});
		builderSingle.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		final MenuItem  search_menu_item = (MenuItem) menu.findItem(R.id.search);
		SearchView searchView =
				(SearchView) menu.findItem(R.id.search).getActionView();

		searchView.setOnQueryTextListener(this);
		searchView.setOnQueryTextFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					search_menu_item.collapseActionView();
				}

			}
		});

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {


		switch (item.getItemId()) {
		case R.id.search:
			Log.d("ashwini","search");
			return true;
		case R.id.code:
			format="code";
			Log.d("ashwini","code");
			return true;
		case R.id.name:
			format="name";
			Log.d("ashwini","name");
			return true;
		case R.id.menu_overflow:
			Log.d("ashwini","overflow");
			return true;

		default:
			return super.onOptionsItemSelected(item);

		}

	}


	@Override
	public boolean onQueryTextChange(String txt) {
		Log.d("ashwini","search arg0" +txt);
		return true;
	}


	@Override
	public boolean onQueryTextSubmit(String txt) {
		View searchView =findViewById(R.id.search);
		searchView.clearFocus();
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(( searchView).getWindowToken(), 0);

		data = txt;
		if(data.isEmpty())
		{
			new AlertDialog.Builder(MainActivity.this)
			.setTitle("Empty")
			.setMessage("Enter a paper code to search")
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) { 
					// continue with delete
				}
			})

			.setIcon(android.R.drawable.ic_dialog_alert)
			.show();
		}
		else
		{
			if(format.equals("code"))
				searchPaper();
			else if(format.equals("name"))
				searchPaper();
		}




		return true;
	}

}
