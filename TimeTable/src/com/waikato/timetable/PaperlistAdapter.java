package com.waikato.timetable;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PaperlistAdapter extends BaseAdapter{
	private LayoutInflater mInflater;
	private Context ctx;
	ArrayList< String> nameList = new ArrayList<String>();
	ArrayList< String> codeList = new ArrayList<String>();

	PaperlistAdapter(Context context,ArrayList<String> name,ArrayList<String> code)
	{
		ctx=context;
		nameList.clear();
		codeList.clear();
		nameList = name;
		codeList = code;
	}

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
		mInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

}
