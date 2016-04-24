package com.laytonlabs.android.taptheblue.game;

/**
 * Created by matthewlayton on 24/04/2016.
 */
public class Move {

    private static int move = 0;

    public static void reset() {
        move = 0;
    }

    public static int get() {
        return move;
    }

    public static String getLabel() {
        return Integer.toString(get());
    }

    public static void increment() {
        move++;
    }
}