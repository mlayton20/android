package com.example.android.opengl;

import android.util.Log;

public class Stats {
	
	private static final String TAG = "Stats";

	private static int score = 0;

	public static int getScore() {
		return score;
	}
	
	public static String getScoreLabel() {
		return Integer.toString(score);
	}

	public static void setScore(String nestedText) {
		int tempScore = calculateScore(nestedText);
		//TODO - If the cell is a bonus cell, multiply the tempScore by 2.
		score += tempScore;
	}

	private static int calculateScore(String nestedText) {
		if (nestedText == null || nestedText == "") {
			return 0;
		}
		
		char operator = nestedText.charAt(0);
		int cellValue = Integer.parseInt(nestedText.substring(1));
		
		int tempScore = 0;
		
		if (operator == '+' || operator == '-') {
			tempScore = 1;
		} else if (operator == '*' || operator == '/') {
			tempScore = 3;
		}
		
		if (cellValue > 10) {
			tempScore *= 2;
		} 
		
		return tempScore;
	}

}
