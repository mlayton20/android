package com.laytonlabs.android.todotoday;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Setting {
	private static final String JSON_KEY = "key";
	private static final String JSON_VALUE = "value";
	
	private String key;
	private String value;
	
	public Setting(String key, String value) {
		this.setKey(key);
		this.setValue(value);
	}
	
	public Setting(JSONObject json) throws JSONException {
		key = json.getString(JSON_KEY);
		value = json.getString(JSON_VALUE);
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_KEY, key);
		json.put(JSON_VALUE, value);
		return json;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
