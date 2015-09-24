package com.laytonlabs.android.todotoday;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SettingActivity extends Activity {
	
	private CheckBox mDeleteOnNewDay;

	@TargetApi(11)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (NavUtils.getParentActivityName(this) != null) {
				getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
        
        mDeleteOnNewDay = (CheckBox)findViewById(R.id.setting_delete_tasks_new_day);
        mDeleteOnNewDay.setChecked(SettingLab.get(this).getSetting(SettingLab.DELETE_ON_NEW_DAY).equals(SettingLab.ENABLED) ? true : false);
        mDeleteOnNewDay.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SettingLab.get(null).setSetting(SettingLab.DELETE_ON_NEW_DAY, isChecked ? SettingLab.ENABLED : SettingLab.DISABLED);
			}
		});
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				if (NavUtils.getParentActivityName(this) != null) {
					NavUtils.navigateUpFromSameTask(this);
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
