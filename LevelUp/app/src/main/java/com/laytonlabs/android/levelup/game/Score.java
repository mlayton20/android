package com.laytonlabs.android.levelup.game;

import android.util.Log;

public class Score {
	
	private static final String TAG = "Score";

	private static int score = 0;

	public static void reset() {
		score = 0;
	}
	
	public static int get() {
		return score;
	}
	
	public static String getLabel() {
		return Integer.toString(score);
	}

	public static void set(Cell touchedCell) {
		int tempScore = calculateScore(touchedCell.toString());
		
		//If the cell is a bonus cell, multiply the tempScore by 2.
		if (touchedCell.isBonusCell()) {
			tempScore *= 2;
		}
		
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
