package com.laytonlabs.android.todotoday;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class TaskPagerActivity extends FragmentActivity
	implements TaskFragment.Callbacks {
	private ViewPager mViewPager;
	private ArrayList<Task> mTasks;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		
		mTasks = TaskLab.get(this).getTasks();
		
		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			
			@Override
			public int getCount() {
				return mTasks.size();
			}
			
			@Override
			public Fragment getItem(int pos) {
				Task task = mTasks.get(pos);
				return TaskFragment.newInstance(task.getmId());
			}
		});
		
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				Task task = mTasks.get(arg0);
				if (task.getmTitle() != null) {
					setTitle(task.getmTitle());
				}
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		UUID taskId = (UUID)getIntent().getSerializableExtra(TaskFragment.EXTRA_TASK_ID);
		for (int i = 0; i < mTasks.size(); i++) {
			if (mTasks.get(i).getmId().equals(taskId)) {
				mViewPager.setCurrentItem(i);
				break;
			}
		}
	}

	@Override
	public void onTaskUpdated(Task task) {
		//Do nothing
	}

}
