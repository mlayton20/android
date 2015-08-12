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
			.setMessage(R.string.share_label)
			.setPositiveButton(R.string.share_button, 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent i = new Intent(Intent.ACTION_SEND);
							i.setType("text/plain");
							//TODO change the below to point to my play store app when its created
							i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.task_share_subject));
							i.putExtra(Intent.EXTRA_TEXT, getString(R.string.task_share_url));
							startActivity(i);
							
							mShareProgress = true;
							sendResult(Activity.RESULT_OK);
							
						}
					})
			.setNegativeButton(android.R.string.cancel,null)
			.create();
	}
}
