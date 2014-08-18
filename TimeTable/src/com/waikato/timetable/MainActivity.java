package com.waikato.timetable;


import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Build;

public class MainActivity extends Activity {

	String urlCode = "http://timetable.waikato.ac.nz/perl-bin/timetable.pl?term=";
	String url1 = "&by=";
	String url2 = "&year=2014#results";
	EditText e1,e2;
	Button b1,b2;
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
	Spinner dropdown;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context= MainActivity.this;
		setContentView(R.layout.activity_main);

		dropdown = (Spinner)findViewById(R.id.spinner1);
		String[] items = new String[]{"By Code", "By Name"};
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
		dropdown.setAdapter(adapter1);
		dropdown.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				// TODO Auto-generated method stub
				if(pos == 0)
				{
					format="code";
				}
				else if( pos == 1)
				{
					format = "name";
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		e1 = (EditText)findViewById(R.id.ed1);
		e2 = (EditText)findViewById(R.id.ed2);
		b1 =(Button)findViewById(R.id.b1);
		b2 = (Button)findViewById(R.id.b2);
		t1 = (TextView)findViewById(R.id.textview);
		e2.setVisibility(View.GONE);
		b2.setVisibility(View.GONE);

		alert = new AlertDialog.Builder(MainActivity.this)
		.setTitle("Connection failed")
		.setMessage("Check your internet connection and try again.")
		.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) { 
				// continue with delete
			}
		})

		.setIcon(android.R.drawable.ic_dialog_alert);
		b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				data = e1.getText().toString();
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


			}
		});

		b2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				data = e2.getText().toString();
				if(data.isEmpty())
				{
					new AlertDialog.Builder(MainActivity.this)
					.setTitle("Empty")
					.setMessage("Enter a paper name to search")
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
					format = "name";
					searchPaper();
				}
			}
		});

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
		builderSingle.setIcon(R.drawable.ic_launcher);
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

}
