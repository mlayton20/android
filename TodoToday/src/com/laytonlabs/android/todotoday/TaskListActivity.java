package com.laytonlabs.android.todotoday;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class TaskListActivity extends FragmentActivity 
	implements TaskListFragment.Callbacks, TaskFragment.Callbacks, NewTaskFragment.Callbacks {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment);
		
		FragmentManager fm = getSupportFragmentManager(); 
		FragmentTransaction t = fm.beginTransaction();
		
		Fragment fragment = fm.findFragmentById(R.id.fragmentNewTaskContainer);
		
		if (fragment == null) {
			fragment = new NewTaskFragment();
			t.add(R.id.fragmentNewTaskContainer, fragment);
		}
		
		fragment = fm.findFragmentById(R.id.fragmentContainer);
		
		if (fragment == null) {
			fragment = new TaskListFragment();
			t.add(R.id.fragmentContainer, fragment);
		}
		t.commit();
	}

	@Override
	public void onTaskSelected(Task task) {
		if (findViewById(R.id.detailFragmentContainer) == null) {
			// Start an instance of CrimePagerActivity
			Intent i = new Intent(this, TaskPagerActivity.class);
			i.putExtra(TaskFragment.EXTRA_TASK_ID, task.getmId());
			startActivity(i);
		} else {
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			
			Fragment oldDetail = fm.findFragmentById(R.id.detailFragmentContainer);
			Fragment newDetail = TaskFragment.newInstance(task.getmId());
			
			if (oldDetail != null) {
				ft.remove(oldDetail);
			}
			ft.add(R.id.detailFragmentContainer, newDetail);
			ft.commit();
		}
		
	}

	@Override
	public void onTaskUpdated(Task crime) {
		updateListContents();
		
	}

	protected void updateListContents() {
		FragmentManager fm = getSupportFragmentManager();
		TaskListFragment listFragment = (TaskListFragment)fm.findFragmentById(R.id.fragmentContainer);
		listFragment.updateUI();
	}

	@Override
	public void onNewTask() {
		updateListContents();
	}

}
