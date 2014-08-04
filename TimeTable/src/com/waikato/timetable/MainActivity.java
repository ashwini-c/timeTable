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
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
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
	ListView list;
	ArrayList< String> nameList = new ArrayList<String>();
	ArrayList< String> codeList = new ArrayList<String>();
	ListAdapter adapter;
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
						new searchTimeTableByCode().execute();
					else if(format.equals("name"))
						new searchTimeTableByName().execute();
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
					new searchTimeTableByName().execute();
				}
			}
		});

		list = (ListView)findViewById(R.id.listView1);

		adapter = new ListAdapter() {
			private LayoutInflater mInflater;

			@Override
			public void unregisterDataSetObserver(DataSetObserver arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void registerDataSetObserver(DataSetObserver arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean isEmpty() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean hasStableIds() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public int getViewTypeCount() {
				// TODO Auto-generated method stub
				return nameList.size();
			}

			@Override
			public View getView(int pos, View convertView, ViewGroup parent) {
				View view = null;
				mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = mInflater.inflate(R.layout.list_item,null);
				TextView name = (TextView)view.findViewById(R.id.textView1);
				TextView code = (TextView)view.findViewById(R.id.textView2);
				name.setText(nameList.get(pos));
				code.setText(codeList.get(pos));
				return view;

			}

			@Override
			public int getItemViewType(int arg0) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return nameList.get(arg0);
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return nameList.size();
			}

			@Override
			public boolean isEnabled(int position) {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public boolean areAllItemsEnabled() {
				// TODO Auto-generated method stub
				return true;
			}
		};
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				data= codeList.get(pos);
				format = "code";
				new searchTimeTableByCode().execute();

			}
		});


	}

	private class searchTimeTableByCode extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try
			{
				Document doc = Jsoup.connect(urlCode+data+url1+format+url2).get();

				Elements err = doc.select("div[class=error]");
				Log.d("ashwini"," err "+ err.toString());
				if(!(err.toString().isEmpty()))
				{
					resulttxt = "Sorry, there were no timetable paper events found for "+data+". Try again with correct code";
					return null;
				}
				//Elements elm = doc.select("table[class=results table]");
				Elements elm = doc.select("tr[class=odd]");
				resulttxt = elm.toString();
				Elements elm1 = doc.select("tr[class=even]");
				resulttxt = resulttxt+elm1.toString();

			}
			catch(Exception e)
			{
				Log.d("ashwini "," "+ e.getMessage());


				context.runOnUiThread(new Runnable() 

				{
					public void run()
					{
						//Do your UI operations like dialog opening or Toast here
						alert.show();
					}
				});



			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			t1.setVisibility(View.VISIBLE);

			t1.setText(resulttxt);
			list.setVisibility(View.GONE);
			mProgress.dismiss();
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgress  = new ProgressDialog(MainActivity.this);
			mProgress.setTitle("WAIKATO TIMETABLE");
			mProgress.setIndeterminate(false);
			mProgress.setCancelable(false);
			mProgress.setMessage("Loading..........");
			mProgress.show();
		}

	}

	private class searchTimeTableByName extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try
			{
				Document doc = Jsoup.connect(urlCode+data+url1+format+url2).get();

				Elements err = doc.select("div[class=error]");
				Log.d("ashwini"," err "+ err.toString());
				if(!(err.toString().isEmpty()))
				{
					resulttxt = "Sorry, there were no timetable paper events found for "+data+". Try again with correct code";
					return null;
				}
				Log.d("ashwini", "url "+urlCode+data+url1+format+url2);

				Elements elm = doc.select("table[class=results table]");
				Element fir=  elm.get(0);

				//t1.setText(elm.toString());
				resulttxt = fir.toString();
				Log.d("ashwini", "sizee "+fir.children().get(0).children().size());
				Elements table = fir.children().get(0).children();
				int tableSize = table.size();
				nameList.clear();
				codeList.clear();
				for(int i = 1;i<tableSize;i++)
				{
					String name  = table.get(i).children().get(0).children().get(1).text();
					String code = table.get(i).children().get(1).text();
					Log.d("ashwini", "name "+ name);
					Log.d("ashwini", "code "+ code);

					nameList.add(name);
					codeList.add(code);

				}

			}
			catch(Exception e)
			{
				context.runOnUiThread(new Runnable() 

				{
					public void run()
					{
						//Do your UI operations like dialog opening or Toast here
						alert.show();
					}
				});
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			t1.setVisibility(View.GONE);
			list.setVisibility(View.VISIBLE);
			list.setAdapter(adapter);
			mProgress.dismiss();
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgress  = new ProgressDialog(MainActivity.this);
			mProgress.setTitle("WAIKATO TIMETABLE");
			mProgress.setIndeterminate(false);
			mProgress.setCancelable(false);
			mProgress.setMessage("Loading..........");
			mProgress.show();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
