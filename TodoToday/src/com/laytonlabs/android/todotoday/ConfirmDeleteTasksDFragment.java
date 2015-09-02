package com.laytonlabs.android.todotoday;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ConfirmDeleteTasksDFragment extends DialogFragment {
	
	private void sendResult(int resultCode) {
		if (getTargetFragment() == null)
			return;
		
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, null);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity())
			.setMessage(R.string.delete_all_tasks_warning)
			.setPositiveButton(R.string.delete_text, 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							sendResult(Activity.RESULT_OK);
						}
					})
			.setNegativeButton(android.R.string.cancel,null)
			.create();
	}
}
