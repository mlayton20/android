package com.laytonlabs.android.todotoday;

import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class TaskFragment extends Fragment {
	
	public static final String EXTRA_TASK_ID = 
			"com.laytonlabs.android.todotoday.task_id";
	private Task mTask;
	private EditText mTitleField;
	private Callbacks mCallbacks;
	private EditText mNoteField;
	
	public interface Callbacks {
		void onTaskUpdated(Task task);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mCallbacks = (Callbacks)activity;
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}
	
	public static TaskFragment newInstance(UUID taskId) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_TASK_ID, taskId);
		
		TaskFragment fragment = new TaskFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UUID taskId = (UUID)getArguments().getSerializable(EXTRA_TASK_ID);
		mTask = TaskLab.get(getActivity()).getTask(taskId);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	   inflater.inflate(R.menu.fragment_task, menu);
	   if (mTask.isCompleted()) {
		   MenuItem completeItem = menu.findItem(R.id.menu_item_complete_task);
		   completeItem.setVisible(false);
		   completeItem.setEnabled(false);
	   }
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				navigateToParent();
				return true;
			case R.id.menu_item_complete_task:
				if (!mTask.isCompleted()) {
					mTask.setCompleted(true);
					TaskLab.get(getActivity()).move(mTask);
					Toast.makeText(getActivity(), R.string.task_marked_as_complete, Toast.LENGTH_SHORT).show();
					mCallbacks.onTaskUpdated(mTask);
				}
				navigateToParent();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void navigateToParent() {
		if (NavUtils.getParentActivityName(getActivity()) != null) {
			NavUtils.navigateUpFromSameTask(getActivity());
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		TaskLab.get(getActivity()).saveTasks();
	}
	
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_task, parent, false);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		
		mTitleField = (EditText)v.findViewById(R.id.task_title);
		mTitleField.setText(mTask.getmTitle());
		mTitleField.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(
					CharSequence c, int start, int before, int count) {
				mTask.setmTitle(c.toString());
				mCallbacks.onTaskUpdated(mTask);
				getActivity().setTitle(mTask.getmTitle());
			}
			
			@Override
			public void beforeTextChanged(
					CharSequence c, int start, int count, int after) {
				//This space is intentionally left blank
			}
			
			@Override
			public void afterTextChanged(Editable c) {
				//This one too
			}
		});
		
		mNoteField = (EditText)v.findViewById(R.id.task_note);
		mNoteField.setText(mTask.getNote());
		mNoteField.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(
					CharSequence c, int start, int before, int count) {
				mTask.setNote(c.toString());
				mCallbacks.onTaskUpdated(mTask);
			}
			
			@Override
			public void beforeTextChanged(
					CharSequence c, int start, int count, int after) {
				//This space is intentionally left blank
			}
			
			@Override
			public void afterTextChanged(Editable c) {
				//This one too
			}
		});
		
		return v;
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	@Override
	public void onStop() {
		super.onStop();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) 
			return;
	}

}
