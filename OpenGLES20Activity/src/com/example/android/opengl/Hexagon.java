package com.example.android.opengl;

public class Hexagon extends Shape {

    private static final float originalCoords[] = {
        0.0f,   0.5f, 0.0f,   // top
       -0.5f,  0.2f, 0.0f,   // top left
       -0.5f, -0.2f, 0.0f,   // bottom left
        0.0f,  -0.5f, 0.0f,   // bottom
        0.5f, -0.2f, 0.0f,   // bottom right
        0.5f,  0.2f, 0.0f }; // top right
    
    private static final short drawOrder[] = { 0, 1, 5, 1, 4, 5, 1, 2, 4, 2, 3, 4 }; // order to draw vertices

    //#RGB: red (229, 51, 72)
    private static final float color[] = { 0.8980392156862745f, 0.2f, 0.2823529411764706f, 1.0f };

    public Hexagon(float scale, float centre) {
    	super(originalCoords, drawOrder, color, scale, centre);
    }
}