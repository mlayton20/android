package com.laytonlabs.android.levelup.shapes;

import android.util.Log;

public class NumFour extends Shape {
	
	private static final String TAG = "NumFour";

    private static final float originalCoords[] = {
         0.075f,  0.5f,  0.0f,   //0
         0.075f, -0.35f, 0.0f,   //1
         0.225f, -0.35f, 0.0f,   //2
         0.225f,  0.5f,  0.0f,   //3
        -0.3f,    0.0f,  0.0f,   //4
        -0.3f,   -0.15f, 0.0f,   //5
         0.3f,   -0.15f, 0.0f,   //6
         0.3f,    0.0f,  0.0f,   //7
         0.025f, -0.35f, 0.0f,   //8
         0.025f, -0.5f,  0.0f,   //9
         0.275f, -0.5f,  0.0f,   //10
         0.275f, -0.35f, 0.0f,   //11
         0.175f,  0.4f,  0.0f,   //12
        -0.2f,   -0.1f,  0.0f }; //13
    
    private static final short drawOrder[] = { 0,1,3,3,1,2,4,5,7,7,5,6,8,9,11,11,9,10,0,4,13,0,13,12 }; // order to draw vertices

    //#RGB: white (255, 255, 255)
    private static final float color[] = { 1.0f, 1.0f, 1.0f, 1.0f };

    public NumFour(float scale, float centreX, float centreY, float[] color) {
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