package com.laytonlabs.android.todotoday;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Task {
	
	private static final String JSON_ID = "id";
	private static final String JSON_TITLE = "title";
	private static final String JSON_COMPLETED = "completed";
	private static final String JSON_DATE = "date";
	private static final String JSON_NOTE = "note";
	
	private UUID mId;
	private String mTitle;
	private boolean mCompleted;
	private Date mDate;
	private String mNote;

	public Task() {
		// Generate Unique identifier
		mId = UUID.randomUUID();
		mDate = new Date();
	}
	
	public Task(JSONObject json) throws JSONException {
		mId = UUID.fromString(json.getString(JSON_ID));
		if (json.has(JSON_TITLE))
			mTitle = json.getString(JSON_TITLE);
		mCompleted = json.getBoolean(JSON_COMPLETED);
		mDate = new Date(json.getLong(JSON_DATE));
		if (json.has(JSON_NOTE))
			mNote = json.getString(JSON_NOTE);
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_ID, mId.toString());
		json.put(JSON_TITLE, mTitle);
		json.put(JSON_COMPLETED, mCompleted);
		json.put(JSON_DATE, mDate.getTime());
		json.put(JSON_NOTE, mNote);
		return json;
	}

	public String getmTitle() {
		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public UUID getmId() {
		return mId;
	}

	public Date getmDate() {
		return mDate;
	}

	public void setmDate(Date mDate) {
		this.mDate = mDate;
	}

	public boolean isCompleted() {
		return mCompleted;
	}

	public void setCompleted(boolean mCompleted) {
		this.mCompleted = mCompleted;
	}
	
	public String getNote() {
		return mNote;
	}

	public void setNote(String note) {
		this.mNote = note;
	}
	
	@Override
	public String toString() {
		return mTitle;
	}
}
