package com.laytonlabs.android.taptheblue.game;

/**
 * Created by matthewlayton on 24/04/2016.
 */
public class Score {

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

    public static void increment() {
        score++;
    }
}
