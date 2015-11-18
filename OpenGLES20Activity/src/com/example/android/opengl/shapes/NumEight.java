package com.example.android.opengl.shapes;

import android.util.Log;

public class NumEight extends Shape {
	
	private static final String TAG = "NumEight";

    private static final float originalCoords[] = {
        -0.225f,  0.5f,   0.0f,   //0
        -0.225f,  0.35f,  0.0f,   //1
         0.225f,  0.35f,  0.0f,   //2
         0.225f,  0.5f,   0.0f,   //3
        -0.3f,    0.425f, 0.0f,   //4
        -0.3f,    0.075f, 0.0f,   //5
        -0.15f,   0.075f, 0.0f,   //6
        -0.15f,   0.425f, 0.0f,   //7
         0.15f,   0.425f, 0.0f,   //8
         0.15f,   0.075f, 0.0f,   //9
         0.3f,    0.075f, 0.0f,   //10
         0.3f,    0.425f, 0.0f,   //11
        -0.225f,  0.425f, 0.0f,   //12
         0.225f,  0.425f, 0.0f,   //13
        -0.225f,  0.075f, 0.0f,   //14
        -0.225f, -0.075f, 0.0f,   //15
         0.225f, -0.075f, 0.0f,   //16
         0.225f,  0.075f, 0.0f,   //17
        -0.225f,  0.0f,   0.0f,   //18
         0.225f,  0.0f,   0.0f,   //19
        -0.3f,   -0.075f, 0.0f,   //20
        -0.3f,   -0.425f, 0.0f,   //21
        -0.15f,  -0.425f, 0.0f,   //22
        -0.15f,  -0.075f, 0.0f,   //23
         0.15f,  -0.075f, 0.0f,   //24
         0.15f,  -0.425f, 0.0f,   //25
         0.3f,   -0.425f, 0.0f,   //26
         0.3f,   -0.075f, 0.0f,   //27
        -0.225f, -0.35f,  0.0f,   //28
        -0.225f, -0.5f,   0.0f,   //29
         0.225f, -0.5f,   0.0f,   //30
         0.225f, -0.35f,  0.0f,   //31
        -0.225f, -0.425f, 0.0f,   //32
         0.225f, -0.425f, 0.0f }; //33
    
    private static final short drawOrder[] = { 0,1,3,3,1,2,4,5,7,7,5,6,8,9,11,11,9,10,0,4,12,3,13,11,14,15,17,17,15,16,5,18,14,18,20,15,17,19,10,19,16,27,20,21,23,23,21,22,24,25,27,27,25,26,28,29,31,31,29,30,21,29,32,33,30,26 }; // order to draw vertices

    //#RGB: white (255, 255, 255)
    private static final float color[] = { 1.0f, 1.0f, 1.0f, 1.0f };

    public NumEight(float scale, float centreX, float centreY) {
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