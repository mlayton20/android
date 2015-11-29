package com.example.android.opengl;

import android.util.Log;

public class Screen {
	
	private static final String TAG = "Screen";
	public static final float DEFAULT_PORTRAIT_RATIO = 0.64400715f;
	public static final float DEFAULT_LANDSCAPE_RATIO = 2.2299652f;
	
	//TODO - This will be applied to the cell ratio, so the scale, offsetY 
	//and offsetX will be multiplied by this value to scale the shapes.
	private static float ratio = 1;
	private static boolean isPortrait;
	
	public static void rebuild(float ratio) {
		setPortrait(ratio);
		setRatio(ratio);
		
		Log.d(TAG, "Ratio is: " + getRatio());
		//TODO - If ratio is greater than 1 then the screen is in landscape mode, so keep as default values.
	}

	public static float getRatio() {
		return ratio;
	}

	public static void setRatio(float ratio) {
		if (isPortrait()) {
			Screen.ratio = ratio/DEFAULT_PORTRAIT_RATIO;
		} else {
			Screen.ratio = ratio/DEFAULT_LANDSCAPE_RATIO;
		}
	}

	public static boolean isPortrait() {
		return isPortrait;
	}

	private static void setPortrait(float ratio) {
		if (ratio > 1) {
			Screen.isPortrait = false;
		} else {
			Screen.isPortrait = true;
		}
	}

}
