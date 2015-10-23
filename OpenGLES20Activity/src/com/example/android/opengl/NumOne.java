package com.example.android.opengl;

public class NumOne extends Shape {

    private static final float originalCoords[] = {
        -0.075f,  0.5f,  0.0f,   //0 
        -0.125f,   0.425f, 0.0f,   //1
        -0.075f,  0.425f, 0.0f,   //2
        -0.125f,   0.375f,  0.0f,   //3
        -0.075f,  0.375f,  0.0f,   //4
        -0.075f, -0.375f,  0.0f,   //5
         0.075f, -0.375f,  0.0f,   //6
         0.075f,  0.5f,  0.0f,   //7
        -0.125f,  -0.375f,  0.0f,   //8
        -0.125f,  -0.5f,  0.0f,   //9
         0.125f,  -0.5f,  0.0f,   //10
         0.125f,  -0.375f,  0.0f }; //11
    
    private static final short drawOrder[] = { 0,1,2,1,3,2,2,3,4,0,5,7,7,5,6,8,9,11,11,9,10 }; // order to draw vertices

    //#RGB: white (255, 255, 255)
    private static final float color[] = { 1.0f, 1.0f, 1.0f, 1.0f };

    public NumOne(float scale, float centre) {
    	super(originalCoords, drawOrder, color, scale, centre);
    }
}