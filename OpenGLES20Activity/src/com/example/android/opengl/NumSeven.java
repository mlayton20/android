package com.example.android.opengl;

import android.util.Log;

public class NumSeven extends Shape {
	
	private static final String TAG = "NumSeven";

    private static final float originalCoords[] = {
        -0.3f,   0.5f,   0.0f,   //0
        -0.3f,   0.35f,  0.0f,   //1
         0.3f,   0.35f,  0.0f,   //2
         0.3f,   0.5f,   0.0f,   //3
        -0.3f,   0.275f, 0.0f,   //4
        -0.15f,  0.275f, 0.0f,   //5
        -0.15f,  0.35f,  0.0f,   //6
         0.2f,   0.45f,  0.0f,   //7
        -0.05f, -0.35f,  0.0f,   //8
         0.05f, -0.45f,  0.0f,   //9
        -0.1f,  -0.35f,  0.0f,   //10
        -0.1f,  -0.5f,   0.0f,   //11
         0.15f, -0.5f,   0.0f,   //12
         0.15f, -0.35f,  0.0f }; //13
    
    private static final short drawOrder[] = { 0,1,3,3,1,2,1,4,6,6,4,5,7,8,9,7,9,2,10,11,13,13,11,12 }; // order to draw vertices

    //#RGB: white (255, 255, 255)
    private static final float color[] = { 1.0f, 1.0f, 1.0f, 1.0f };

    public NumSeven(float scale, float centreX, float centreY) {
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