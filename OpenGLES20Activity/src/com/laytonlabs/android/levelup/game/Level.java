package com.laytonlabs.android.levelup.game;

public class Level {

	private static int level = 1;

	public static int get() {
		return level;
	}
	
	public static String getLabel() {
		return Integer.toString(get());
	}

	public static void increment() {
		Level.level++;
	}
}
