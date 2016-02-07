package com.laytonlabs.android.levelup.game;

public class CurrentAnswer {

	private static int currentAnswer = randomWithRange(1,10);

	public static void reset() {
		currentAnswer = randomWithRange(1,10);
	}
	
	public static int get() {
		return currentAnswer;
	}
	
	private static int randomWithRange(int min, int max) {
		int range = (max - min) + 1;     
		return (int)(Math.random() * range) + min;
	}

	public static String getLabel() {
		return Integer.toString(CurrentAnswer.get());
	}

	public static void set(int currentAnswer) {
		CurrentAnswer.currentAnswer = currentAnswer;
	}	
	
}
