package com.laytonlabs.android.levelup.game;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Time {
	
	private static final SimpleDateFormat HH_DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
	private static final SimpleDateFormat MM_DATE_FORMAT = new SimpleDateFormat("mm:ss");
	private static final long MILLISECOND = 1000;
	private static final long HOUR = 3600 * MILLISECOND; //3600 seconds in an hour
	private static final long STARTING_TIME = 120 * MILLISECOND; //120 seconds
	private static final long CORRECT_ANSWER_TIME = 10 * MILLISECOND; //10 seconds
	private static final long TIME_ALMOST_UP_THRESH = 10 * MILLISECOND; //10 seconds
	
	private static long previousTime = System.currentTimeMillis();
	private static long timeElapsed = 0;
	private static long timeRemaining = 0;
	
	public static void initialise() {
		//Need to set the timezone otherwise it will mess up the hours
		HH_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
		MM_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		timeRemaining = STARTING_TIME;
		previousTime = System.currentTimeMillis();
		timeElapsed = 0;
	}
	
	public static void update() {
		long currentTime = System.currentTimeMillis();
		if (currentTime - previousTime < MILLISECOND) {
			return;
		}
		
		Time.updateTimeElapsed();
		Time.decreaseTimeRemaining();
		previousTime = currentTime;
	}
	
	public static long getTimeElapsed() {
		return timeElapsed;
	}
	
	public static String getTimeElapsedLabel() {
		Date timeElapsed = new Date(getTimeElapsed());
		return (getTimeElapsed() < HOUR ? MM_DATE_FORMAT : HH_DATE_FORMAT).format(timeElapsed);
	}
	
	public static void updateTimeElapsed() {
		Time.timeElapsed += MILLISECOND;
	}
	
	public static long getTimeRemaining() {
		return timeRemaining;
	}
	
	public static String getTimeRemainingLabel() {
		Date timeLeft = new Date(getTimeRemaining());
		return (getTimeRemaining() < HOUR ? MM_DATE_FORMAT : HH_DATE_FORMAT).format(timeLeft);
	}
	
	public static void increaseTimeRemaining() {
		Time.timeRemaining += CORRECT_ANSWER_TIME;
	}
	
	public static void decreaseTimeRemaining() {
		if (isTimeUp()) {
			return;
		}
		Time.timeRemaining -= MILLISECOND;
	}
	
	public static boolean isTimeAlmostUp() {
		return Time.timeRemaining <= TIME_ALMOST_UP_THRESH;
	}
	
	public static boolean isTimeUp() {
		return Time.timeRemaining == 0;
	}

}
