package com.waikato.timetable;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TimetableListAdapter extends BaseAdapter{

	List<TimetableData> timeTableData;
	private LayoutInflater mInflater;
	Context context;


	TimetableListAdapter(Context ctx,List<TimetableData> timeTableData)
	{
		context = ctx;
		this.timeTableData = timeTableData;
	}

	public void setData(List<TimetableData> data)
	{
		timeTableData = data;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return timeTableData.size();
	}

	@Override
	public Object getItem(int arg0) {

		return timeTableData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View view = null;
		String name = timeTableData.get(arg0).getName();
		int total  = timeTableData.get(arg0).getTotal();
		List< String> event  = timeTableData.get(arg0).getEvent();
		ArrayList< String> day = timeTableData.get(arg0).getDay();
		ArrayList< String> start = timeTableData.get(arg0).getStart();
		ArrayList< String> end = timeTableData.get(arg0).getEnd();
		ArrayList< String> loc = timeTableData.get(arg0).getLoc();
		String text = "";
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = mInflater.inflate(R.layout.list_item,null);
		LinearLayout layout = (LinearLayout)view.findViewById(R.id.bg);
		if(arg0%2 != 0 )
			layout.setBackgroundColor(Color.RED);
		TextView papername = (TextView)view.findViewById(R.id.textView1);
		TextView code = (TextView)view.findViewById(R.id.textView2);
		papername.setText(name);
		for(int i =0;i<total;i++)
		{
			text = text+event.get(i)+" "+ day.get(i)+" "+start.get(i)+" "+ end.get(i)+" "+loc.get(i)+" \n";
		}
		code.setText(text);
		return view;
	}

}
