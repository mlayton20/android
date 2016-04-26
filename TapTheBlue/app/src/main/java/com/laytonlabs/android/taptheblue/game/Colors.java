package com.laytonlabs.android.taptheblue.game;

import android.graphics.Color;

/**
 * Created by matthewlayton on 23/04/2016.
 */
public class Colors {

    private static int maxColorRange = 1;
    public static final int BLUE =  Color.parseColor("#3498DB");
    private static final String[] COLORS_HEX = new String[] {
            "#ECF0F1", //light grey
            "#F0A658",
            "#EC0868",
            "#2DC2BD",
            "#89D2DC",
            "#6564DB",
            "#7DD181",
            "#5863F8",
            "#51A3A3",
            "#7CFEF0",
            "#DAC4F7",
            "#A3D5FF",
            "#6154A1",
            "#FE5F55",
            "#009FB7",
            "#6154A1"};

    private static final int[] COLORS = initColor(COLORS_HEX);

    private static int[] initColor(String[] colorsHex) {
        int[] colors = new int[colorsHex.length];
        for (int i = 0; i < colorsHex.length; i++) {
            colors[i] = Color.parseColor(colorsHex[i]);
        }
        return colors;
    }

    public static int getColor(int index) {
        return COLORS[index];
    }

    public static int getRandomColor() {
        return COLORS[(int)(Math.random() * maxColorRange)];
    }

    private static int getMaxColorRange() {
        return maxColorRange;
    }

    public static void setMaxColorRange(int maxColorRange) {
        Colors.maxColorRange = maxColorRange > COLORS.length ? COLORS.length : maxColorRange;
    }

    public static void incrementMaxColorRange() {
        //Only want to add a new color every 5 moves.
        if (Move.get() % 5 == 0) {
            Colors.setMaxColorRange(Colors.getMaxColorRange() + 1);
        }
    }

    public static void reset() {
        maxColorRange = 1;
    }
}
