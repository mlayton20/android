package com.laytonlabs.android.todotoday;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

public class SettingLab {
	public static final String DELETE_ON_NEW_DAY = "DeleteOnNewDay";
	public static final String DISABLED = "0";
	public static final String ENABLED = "1";
	
	private static final String TAG = "SettingLab";
	private static final String FILENAME = "settings.json";
	
	private static SettingLab sSettingLab;
	private Context mAppContext;
	
	private ArrayList<Setting> mSettings;
	private TodoTodayJSONSerializer mSerializer;
	
	private SettingLab(Context appContext) {
		mAppContext = appContext;
		mSerializer = new TodoTodayJSONSerializer(mAppContext, FILENAME);
		
		try {
			mSettings = mSerializer.loadSettings();
		} catch (Exception e) {
			mSettings = new ArrayList<Setting>();
			Log.e(TAG, "Error loading settings: ", e);
		}
		
		if (mSettings.isEmpty()) {
			initDefaultSettings();
		}
	}
	
	private void initDefaultSettings() {
		addSetting(new Setting(DELETE_ON_NEW_DAY,ENABLED));
	}

	public static SettingLab get(Context c) {
		if (sSettingLab == null) {
			sSettingLab = new SettingLab(c.getApplicationContext());
		}
		return sSettingLab;
	}

	public void addSetting(Setting s) {
		mSettings.add(s);
	}
	
	public boolean saveSettings() {
		try {
			mSerializer.saveSettings(mSettings);
			Log.d(TAG, "Settings saved to file");
			return true;
		} catch (Exception e) {
			Log.e(TAG, "Error saving settings: ", e);
			return false;
		}
	}
	
	public ArrayList<Setting> getSettings() {
		return mSettings;
	}
	
	public String getSetting(String key) {
		for (Setting s : mSettings) {
			if (s.getKey().equals(key))
				return s.getValue();
		}
		return null;
	}
}
