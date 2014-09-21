package com.waikato.timetable;

import java.util.ArrayList;


public class TimetableData {
	private long id;
	private String name;
	private int total;
	private ArrayList< String> event;
	private ArrayList< String> day;
	private ArrayList< String> start;
	private ArrayList< String> end;
	private ArrayList< String> loc;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	public ArrayList< String> getEvent() {
		return event;
	}

	public void setEvent(ArrayList< String> event) {
		this.event = event;
	}
	public ArrayList< String> getDay() {
		return day;
	}

	public void setDay(ArrayList< String> day) {
		this.day = day;
	}
	public ArrayList< String> getStart() {
		return start;
	}

	public void setStart(ArrayList< String> start) {
		this.start = start;
	}
	public ArrayList< String> getEnd() {
		return end;
	}

	public void setEnd(ArrayList< String> end) {
		this.end = end;
	}
	public ArrayList< String> getLoc() {
		return loc;
	}

	public void setLoc(ArrayList< String> loc) {
		this.loc = loc;
	}

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return name;
	}
}
