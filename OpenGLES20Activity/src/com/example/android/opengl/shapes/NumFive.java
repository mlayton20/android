package com.example.android.opengl.shapes;

import android.util.Log;

public class NumFive extends Shape {
	
	private static final String TAG = "NumFive";

    private static final float originalCoords[] = {
        -0.3f,    0.5f,   0.0f,   //0
        -0.3f,    0.35f,  0.0f,   //1
         0.3f,    0.35f,  0.0f,   //2
         0.3f,    0.5f,   0.0f,   //3
        -0.3f,    0.0f,   0.0f,   //4
        -0.15f,   0.0f,   0.0f,   //5
        -0.15f,   0.35f,  0.0f,   //6
        -0.225f,  0.0f,   0.0f,   //7
        -0.225f,  0.075f, 0.0f,   //8
        -0.225f, -0.075f, 0.0f,   //9
         0.225f, -0.075f, 0.0f,   //10
         0.225f,  0.075f, 0.0f,   //11
         0.15f,   0.0f,   0.0f,   //12
         0.15f,  -0.425f, 0.0f,   //13
         0.3f,   -0.425f, 0.0f,   //14
         0.3f,    0.0f,   0.0f,   //15
         0.225f,  0.0f,   0.0f,   //16
        -0.3f,   -0.275f, 0.0f,   //17
        -0.3f,   -0.425f, 0.0f,   //18
        -0.15f,  -0.425f, 0.0f,   //19
        -0.15f,  -0.275f, 0.0f,   //20
        -0.225f, -0.35f,  0.0f,   //21
        -0.225f, -0.5f,   0.0f,   //22
         0.225f, -0.5f,   0.0f,   //23
         0.225f, -0.35f,  0.0f,   //24
        -0.225f, -0.425f, 0.0f,   //25
         0.225f, -0.425f, 0.0f }; //26
    
    private static final short drawOrder[] = { 0,1,3,3,1,2,1,4,6,6,4,5,8,9,11,11,9,10,4,9,7,12,13,15,15,13,14,11,16,15,17,18,20,20,18,19,21,22,24,24,22,23,18,22,25,26,23,14 }; // order to draw vertices

    //#RGB: white (255, 255, 255)
    private static final float color[] = { 1.0f, 1.0f, 1.0f, 1.0f };

    public NumFive(float scale, float centreX, float centreY) {
    	super(originalCoords, drawOrder, color, scale, centreX, centreY);
    }

	@Override
	public float getCentreX() {
		return shapeCoords[17] + shapeCoords[0];
	}

	@Override
	public float getCentreY() {
		// TODO Auto-generated method stub
		return 0;
	}
}