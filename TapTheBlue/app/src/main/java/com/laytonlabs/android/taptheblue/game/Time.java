package com.laytonlabs.android.taptheblue.game;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by matthewlayton on 24/04/2016.
 */
public class Time {

    private static final SimpleDateFormat HH_DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat MM_DATE_FORMAT = new SimpleDateFormat("mm:ss");
    private static final long MILLISECOND = 1000;
    private static final long CENTISECOND = 10;
    public static final long MASTERSECOND = CENTISECOND;
    private static final long HOUR = 3600 * MILLISECOND; //3600 seconds in an hour
    private static final long CORRECT_ANSWER_TIME = 10 * MILLISECOND; //10 seconds
    private static final long TIME_ALMOST_UP_THRESH = 3 * MILLISECOND; //3 seconds

    private static long previousTime = System.currentTimeMillis();
    private static long timeStarted = 0;
    //TODO - This needs to be called once when the game has ended.
    private static long timeElapsed = 0;
    private static long timeRemaining = 0;

    public static void initialise() {
        //Need to set the timezone otherwise it will mess up the hours
        HH_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
        MM_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));

        timeStarted = System.currentTimeMillis();
        resetTimeRemaining();
        previousTime = System.currentTimeMillis();
        timeElapsed = 0;
    }

    public static void update() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - previousTime < MASTERSECOND) {
            return;
        }
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
        Time.timeElapsed = MASTERSECOND;
    }

    public static long getTimeRemaining() {
        return timeRemaining - previousTime;
    }

    public static String getTimeRemainingLabel() {
        long timeLeft = getTimeRemaining();
        if (isTimeUp()) return "0";
        int second = (int) (timeLeft / MILLISECOND);
        long milliseconds = (timeLeft % MILLISECOND);
        return String.format("%d.%03d", second, milliseconds);
        //This is for when I want to add milliseconds.
        //String.format("%d:%02d", minutes, seconds)
    }

    public static void resetTimeRemaining() {
        Time.timeRemaining = System.currentTimeMillis() + CORRECT_ANSWER_TIME;
    }

    public static boolean isTimeAlmostUp() {
        return getTimeRemaining() <= TIME_ALMOST_UP_THRESH;
    }

    public static boolean isTimeUp() {
        return getTimeRemaining() < 0;
    }

    public static void onResume() {
        //When the game starts up again, we need to update the time remaining so that its not in the past.
        timeRemaining = System.currentTimeMillis() + getTimeRemaining();
    }
}
