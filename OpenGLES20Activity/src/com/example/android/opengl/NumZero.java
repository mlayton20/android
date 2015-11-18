package com.example.android.opengl;

public class NumZero extends Shape {

    private static final float originalCoords[] = {
        -0.225f,  0.5f,   0.0f,   //0
        -0.225f,  0.35f,  0.0f,   //1
         0.225f,  0.35f,  0.0f,   //2
         0.225f,  0.5f,   0.0f,   //3
        -0.3f,    0.425f, 0.0f,   //4
        -0.3f,   -0.425f, 0.0f,   //5
        -0.15f,  -0.425f, 0.0f,   //6
        -0.15f,   0.425f, 0.0f,   //7
         0.15f,   0.425f, 0.0f,   //8
         0.15f,   0.275f, 0.0f,   //9
         0.3f,    0.275f, 0.0f,   //10
         0.3f,    0.425f, 0.0f,   //11
        -0.225f,  0.425f, 0.0f,   //12
         0.225f,  0.425f, 0.0f,   //13
        -0.225f,  0.075f, 0.0f,   //14
        -0.225f, -0.075f, 0.0f,   //15
         0.225f, -0.075f, 0.0f,   //16
         0.225f,  0.075f, 0.0f,   //17
         0.15f,   0.0f,   0.0f,   //18
         0.15f,  -0.425f, 0.0f,   //19
         0.3f,   -0.425f, 0.0f,   //20
         0.3f,    0.0f,   0.0f,   //21
         0.225f,  0.0f,   0.0f,   //22
        -0.225f, -0.35f,  0.0f,   //23
        -0.225f, -0.5f,   0.0f,   //24
         0.225f, -0.5f,   0.0f,   //25
         0.225f, -0.35f,  0.0f,   //26
        -0.225f, -0.425f, 0.0f,   //27
         0.225f, -0.425f, 0.0f }; //28
    
    private static final short drawOrder[] = { 0,1,3,3,1,2,4,5,7,7,5,6,0,4,12,8,19,11,11,19,20,3,13,11,23,24,26,26,24,25,28,25,20,5,24,27 }; // order to draw vertices

    //#RGB: white (255, 255, 255)
    private static final float color[] = { 1.0f, 1.0f, 1.0f, 1.0f };

    public NumZero(float scale, float centreX, float centreY) {
    	super(originalCoords, drawOrder, color, scale, centreX, centreY);
    }

	@Override
	public float getCentreX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getCentreY() {
		// TODO Auto-generated method stub
		return 0;
	}
}