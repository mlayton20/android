package com.laytonlabs.android.todotoday;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class TasksCompleteDFragment extends DialogFragment {

	public static final String EXTRA_SHARE = 
			"com.laytonlabs.android.todotoday.share";
	private boolean mShareProgress;
	
	private void sendResult(int resultCode) {
		if (getTargetFragment() == null)
			return;
		
		Intent i = new Intent();
		i.putExtra(EXTRA_SHARE, mShareProgress);
		
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity())
			.setTitle(R.string.date_picker_title)
			.setPositiveButton(android.R.string.ok, 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							mShareProgress = true;
							sendResult(Activity.RESULT_OK);
							
						}
					})
			.create();
	}
}
