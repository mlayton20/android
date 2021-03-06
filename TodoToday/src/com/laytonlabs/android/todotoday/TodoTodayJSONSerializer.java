package com.laytonlabs.android.todotoday;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;

public class TodoTodayJSONSerializer {

	private Context mContext;
	private String mFilename;
	
	public TodoTodayJSONSerializer(Context c, String f) {
		mContext = c;
		mFilename = f;
	}
	
	public void saveTasks(ArrayList<Task> tasks) throws JSONException, IOException {
		//Build an array in JSON
		JSONArray array = new JSONArray();
		for (Task c : tasks)
			array.put(c.toJSON());
		
		writeJSON(array);
	}
	
	public void saveSettings(ArrayList<Setting> settings) throws JSONException, IOException {
		//Build an array in JSON
		JSONArray array = new JSONArray();
		for (Setting c : settings)
			array.put(c.toJSON());
		
		writeJSON(array);
	}

	private void writeJSON(JSONArray array) throws FileNotFoundException,
			IOException {
		//Write the file to disk
		Writer writer = null;
		try {
			OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(out);
			writer.write(array.toString());
		} finally {
			if (writer != null)
				writer.close();
		}
	}
	
	public ArrayList<Task> loadTasks() throws IOException, JSONException {
		ArrayList<Task> tasks = new ArrayList<Task>();
		JSONArray array = readJSON();
		
		//Build the array of crimes from JSONObjects
		for (int i = 0; i < array.length(); i++) {
			tasks.add(new Task(array.getJSONObject(i)));
		}
		
		return tasks;
	}
	
	public ArrayList<Setting> loadSettings() throws IOException, JSONException {
		ArrayList<Setting> settings = new ArrayList<Setting>();
		JSONArray array = readJSON();
		
		//Build the array of crimes from JSONObjects
		for (int i = 0; i < array.length(); i++) {
			settings.add(new Setting(array.getJSONObject(i)));
		}
		
		return settings;
	}

	private JSONArray readJSON() throws IOException, JSONException {
		JSONArray array = new JSONArray();
		BufferedReader reader = null;
		try {
			//Open and read the file into a StringBuilder
			InputStream in = mContext.openFileInput(mFilename);
			reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder jsonString = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				//Line breaks are omitted and irrelevant
				jsonString.append(line);
			}
			//Parse the JSON using JSONTokener
			array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
		} catch (FileNotFoundException e) {
			//Ignore this one; it happens when starting fresh
		} finally {
			if (reader != null)
				reader.close();
		}
		return array;
	}
}
