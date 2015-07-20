package com.laytonlabs.android.todotoday;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

public class TaskLab {
	
	private static final String TAG = "TaskLab";
	private static final String FILENAME = "tasks.json";
	
	private static TaskLab sTaskLab;
	private Context mAppContext;
	
	private ArrayList<Task> mTasks;
	private TodoTodayJSONSerializer mSerializer;
	
	private TaskLab(Context appContext) {
		mAppContext = appContext;
		mSerializer = new TodoTodayJSONSerializer(mAppContext, FILENAME);
		
		try {
			mTasks = mSerializer.loadTasks();
		} catch (Exception e) {
			mTasks = new ArrayList<Task>();
			Log.e(TAG, "Error loading tasks: ", e);
		}
	}
	
	public static TaskLab get(Context c) {
		if (sTaskLab == null) {
			sTaskLab = new TaskLab(c.getApplicationContext());
		}
		return sTaskLab;
	}
	
	public void addTask(Task t) {
		mTasks.add(t);
	}
	
	public void deleteTask(Task t) {
		mTasks.remove(t);
	}
	
	public boolean saveTasks() {
		try {
			mSerializer.saveTasks(mTasks);
			Log.d(TAG, "tasks saved to file");
			return true;
		} catch (Exception e) {
			Log.e(TAG, "Error saving tasks: ", e);
			return false;
		}
	}
	
	public ArrayList<Task> getTasks() {
		return mTasks;
	}
	
	public Task getTask(UUID id) {
		for (Task t : mTasks) {
			if (t.getmId().equals(id))
				return t;
		}
		return null;
	}

}
