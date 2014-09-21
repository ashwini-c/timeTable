package com.waikato.timetable;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

public class TimetableClient extends IntentService {


	static String TAG = "TimetableClient";
	public static final String REQUEST_DATA = "myRequestdata";
	public static final String REQUEST_FORMAT = "myRequestformat";
	public static final String RESPONSE_NAMELIST = "myResponseName";
	public static final String RESPONSE_CODELIST = "myResponseCode";
	public static final String RESPONSE_RECEIVER = "myResponseReceiver";
	public static final String RESPONSE_MESSAGE = "myResponseMessage";
	public static final String RESPONSE_TOTAL = "myResponseMessageTOTAL";
	public static final String RESPONSE_eventList = "myResponseMessageeventList";
	public static final String RESPONSE_dayList = "myResponseMessagedayList";
	public static final String RESPONSE_startList = "myResponseMessagestartList";
	public static final String RESPONSE_endList = "myResponseMessageendList";
	public static final String RESPONSE_locList = "myResponseMessagelocList";
	public static final String urlCode = "http://timetable.waikato.ac.nz/perl-bin/timetable.pl?term=";
	public static final String url1 = "&by=";
	public static final String url2 = "&year=2014#results";
	ArrayList< String> nameList = new ArrayList<String>();
	ArrayList< String> codeList = new ArrayList<String>();
	ArrayList< String> eventList = new ArrayList<String>();
	ArrayList< String> dayList = new ArrayList<String>();
	ArrayList< String> startList = new ArrayList<String>();
	ArrayList< String> endList = new ArrayList<String>();
	ArrayList< String> locList = new ArrayList<String>();
	String resulttxt;
	ResultReceiver receiver;


	public TimetableClient() {
		super(TAG);

	}

	@Override
	protected void onHandleIntent(Intent intent) {

		String data = intent.getStringExtra(REQUEST_DATA);

		String format = intent.getStringExtra(REQUEST_FORMAT);
		receiver = intent.getParcelableExtra(RESPONSE_RECEIVER);
		resulttxt="";
		Bundle b = new Bundle();
		try{
			Document doc = Jsoup.connect(urlCode+data+url1+format+url2).get();

			Elements err = doc.select("div[class=error]");
			Log.d("ashwini"," err "+ err.toString());
			if(!(err.toString().isEmpty()))
			{
				resulttxt = "Sorry, there were no timetable paper events found for "+data+". Try again with correct code";
				b.putString(RESPONSE_MESSAGE, resulttxt);
				receiver.send(0, b);
				return;

			}

			if(format.equals("code"))
			{
				Elements root = doc.select("table[class=results table]");
				Elements elm = root.select("tr");
				Log.d("ashwini","size "+elm.size());
				int total = elm.size() -1;
				Elements caption = root.select("caption");
				String name  = caption.get(0).text();
				Log.d("ashwini","name "+name);
				eventList.clear();
				dayList.clear();
				startList.clear();
				endList.clear();
				locList.clear();
				for(int i=1;i<=total;i++)
				{
					Element e = elm.get(i);
					Elements nodes = e.select("td");
					eventList.add(nodes.get(5).text());
					dayList.add(nodes.get(1).text());
					startList.add(nodes.get(2).text());
					endList.add(nodes.get(3).text());
					locList.add(nodes.get(4).text());

				}
				b.putString(RESPONSE_MESSAGE, name);
				b.putInt(RESPONSE_TOTAL, total);
				b.putStringArrayList(RESPONSE_eventList, eventList);
				b.putStringArrayList(RESPONSE_dayList, dayList);
				b.putStringArrayList(RESPONSE_startList, startList);
				b.putStringArrayList(RESPONSE_endList, endList);
				b.putStringArrayList(RESPONSE_locList, locList);
				receiver.send(1, b);
				return;
			}
			else if(format.equals("name"))
			{
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

				b.putStringArrayList(RESPONSE_NAMELIST, nameList);
				b.putStringArrayList(RESPONSE_CODELIST, codeList);
				receiver.send(2, b);
				return;
			}
		}
		catch(Exception e)
		{
			receiver.send(-1, b);
			return;
		}


	}

}
