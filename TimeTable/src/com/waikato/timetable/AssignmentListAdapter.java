package com.waikato.timetable;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AssignmentListAdapter extends BaseAdapter{

	List<AssignmentData> assignmentData;
	private LayoutInflater mInflater;
	Context context;


	AssignmentListAdapter(Context ctx,List<AssignmentData> timeTableData)
	{
		context = ctx;
		this.assignmentData = timeTableData;
	}

	public void setData(List<AssignmentData> data)
	{
		assignmentData = data;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return assignmentData.size();
	}

	@Override
	public Object getItem(int arg0) {

		return assignmentData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View view = null;
		String name = assignmentData.get(arg0).getTitle();
		String time  = assignmentData.get(arg0).getTime();
		Log.d("ashwin"," title "+ name + " time "+time);
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = mInflater.inflate(R.layout.list_item,null);
		LinearLayout layout = (LinearLayout)view.findViewById(R.id.bg);
		if(arg0%2 != 0 )
			layout.setBackgroundColor(Color.RED);
		TextView papername = (TextView)view.findViewById(R.id.textView1);
		TextView code = (TextView)view.findViewById(R.id.textView2);
		papername.setText(name);
		code.setText(time);
		return view;
	}

}
