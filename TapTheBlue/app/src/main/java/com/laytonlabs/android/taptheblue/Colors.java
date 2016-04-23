package com.laytonlabs.android.taptheblue;

import android.graphics.Color;

/**
 * Created by matthewlayton on 23/04/2016.
 */
public class Colors {

    private static int maxColorRange = 1;
    public static final int BLUE =  Color.parseColor("#3498DB");
    private static final String[] COLORS_HEX = new String[] {
            "#ECF0F1", //light grey
            "#89D2DC",
            "#6564DB",
            "#FFF689",
            "#CFFFB0",
            "#58355E",
            "#EC7D10",
            "#EC0868",
            "#F0A658",
            "#93BD6A",
            "#E03616",
            "#C33149",
            "#C6D4FF",
            "#307473",
            "#2DC2BD",
            "#7DD181",
            "#4D3F68",
            "#645E9D",
            "#6C969D",
            "#C79EB4",
            "#C5F2F9",
            "#FF98A4",
            "#573280",
            "#574D68",
            "#DAC4F7",
            "#E07A5F",
            "#81B29A",
            "#7CFEF0",
            "#A27E8E",
            "#75485E",
            "#51A3A3",
            "#C3E991"};

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
}
