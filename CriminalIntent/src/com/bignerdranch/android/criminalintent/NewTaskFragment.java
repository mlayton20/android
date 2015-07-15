package com.bignerdranch.android.criminalintent;

import com.bignerdranch.android.criminalintent.CrimeFragment.Callbacks;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

public class NewTaskFragment extends Fragment {
	private EditText mTaskField;
	private ImageButton mTaskButton;
	private Callbacks mCallbacks;
	
	public interface Callbacks {
		void onNewTask();
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

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        View v = inflater.inflate(R.layout.activity_newtask, container, false);
        
        mTaskField = (EditText)v.findViewById(R.id.task_new_task_input);
		mTaskButton = (ImageButton)v.findViewById(R.id.task_new_task_button);
		mTaskButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mTaskField.getText().toString() != "") {
					Crime crime = new Crime();
					crime.setmTitle(mTaskField.getText().toString());
					CrimeLab.get(getActivity()).getCrimes().add(crime);
					mTaskField.setText("");
					mCallbacks.onNewTask();
				}
			}
		});


        return v;
    }
}
