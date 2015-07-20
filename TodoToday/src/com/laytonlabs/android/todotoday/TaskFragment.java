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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class TaskFragment extends Fragment {
	
	public static final String EXTRA_TASK_ID = 
			"com.laytonlabs.android.todotoday.task_id";
	private Task mTask;
	private EditText mTitleField;
	private CheckBox mSolvedCheckBox;
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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				if (NavUtils.getParentActivityName(getActivity()) != null) {
					NavUtils.navigateUpFromSameTask(getActivity());
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
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
		
		mSolvedCheckBox = (CheckBox)v.findViewById(R.id.task_completed);
		mSolvedCheckBox.setChecked(mTask.isCompleted());
		mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//Set the crime's solved property
				mTask.setCompleted(isChecked);
				TaskLab.get(getActivity()).moveToLast(mTask);
				mCallbacks.onTaskUpdated(mTask);
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
