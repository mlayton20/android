package com.laytonlabs.android.levelup.shapes;

public class NumThree extends Shape {

    private static final float originalCoords[] = {
        -0.225f,  0.5f,   0.0f,   //0
        -0.225f,  0.35f,  0.0f,   //1
         0.225f,  0.35f,  0.0f,   //2
         0.225f,  0.5f,   0.0f,   //3
        -0.225f,  0.425f, 0.0f,   //4
         0.225f,  0.425f, 0.0f,   //5
        -0.3f,    0.425f, 0.0f,   //6
        -0.3f,    0.275f, 0.0f,   //7
        -0.15f,   0.275f, 0.0f,   //8
        -0.15f,   0.425f, 0.0f,   //9
         0.15f,   0.425f, 0.0f,   //10
         0.15f,   0.075f, 0.0f,   //11
         0.3f,    0.075f, 0.0f,   //12
         0.3f,    0.425f, 0.0f,   //13
        -0.05f,   0.075f, 0.0f,   //14
        -0.05f,  -0.075f, 0.0f,   //15
         0.225f, -0.075f, 0.0f,   //16
         0.225f,  0.075f, 0.0f,   //17
         0.225f,  0.0f,   0.0f,   //18
         0.15f,  -0.075f, 0.0f,   //19
         0.15f,  -0.425f, 0.0f,   //20
         0.3f,   -0.425f, 0.0f,   //21
         0.3f,   -0.075f, 0.0f,   //22
        -0.3f,   -0.275f, 0.0f,   //23
        -0.3f,   -0.425f, 0.0f,   //24
        -0.15f,  -0.425f, 0.0f,   //25
        -0.15f,  -0.275f, 0.0f,   //26
        -0.225f, -0.35f,  0.0f,   //27
        -0.225f, -0.5f,   0.0f,   //28
         0.225f, -0.5f,   0.0f,   //29
         0.225f, -0.35f,  0.0f,   //30
        -0.225f, -0.425f, 0.0f,   //31
         0.225f, -0.425f, 0.0f }; //32
    
    private static final short drawOrder[] = { 0,1,3,3,1,2,0,6,4,6,7,9,9,7,8,3,5,13,10,11,13,13,11,12,14,15,17,17,15,16,17,18,12,18,16,22,19,20,22,22,20,21,23,24,26,26,24,25,24,31,28,27,28,30,30,28,29,32,29,21 }; // order to draw vertices

    //#RGB: white (255, 255, 255)
    private static final float color[] = { 1.0f, 1.0f, 1.0f, 1.0f };

    public NumThree(float scale, float centreX, float centreY, float[] color) {
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