package com.waikato.timetable;

import java.util.ArrayList;


public class AssignmentData {
	private long id;
	private String title,time;
	private int paperid,eventid;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	public int getPaperId() {
		return paperid;
	}

	public void setPaperId(int paperid) {
		this.paperid = paperid;
	}
	public int getEventId() {
		return eventid;
	}

	public void setEventId(int eventid) {
		this.eventid = eventid;
	}

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return title;
	}
}
