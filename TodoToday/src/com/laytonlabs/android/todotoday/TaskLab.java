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
	
	public void addTaskToFirst(Task t) {
		mTasks.add(0, t);
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
	
	private void moveToLast(Task t) {
		Task tempTask = getTask(t.getmId());
		this.deleteTask(t);
		this.addTask(tempTask);
	}

	public void move(Task t) {
		//No need to move. Only 1 item in list 
		if (mTasks.size() == 1) 
			return;
		
		if (t.isCompleted()) 
			this.moveToLast(t);
		else 
			this.moveToActive(t);
	}

	private void moveToActive(Task t) {
		this.deleteTask(t);
		for (int i = 0; i < mTasks.size(); i++) {
			if (mTasks.get(i).isCompleted()) {
				mTasks.add(i, t);
				return;
			}
		}
		this.addTask(t);
	}

	public boolean allTasksCompleted() {
		if (mTasks.isEmpty())
			return false;
		
		for (Task t : mTasks) {
			if (!t.isCompleted()) { 
				return false;
			}
		}
		return true;
	}

}
