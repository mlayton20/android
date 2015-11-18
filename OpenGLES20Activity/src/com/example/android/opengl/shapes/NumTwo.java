package com.example.android.opengl.shapes;

public class NumTwo extends Shape {

    private static final float originalCoords[] = {
        -0.225f,  0.5f,   0.0f,   //0
        -0.225f,  0.35f,  0.0f,   //1
         0.225f,  0.35f,  0.0f,   //2
         0.225f,  0.5f,   0.0f,   //3
        -0.3f,    0.425f, 0.0f,   //4
        -0.3f,    0.275f, 0.0f,   //5
        -0.15f,   0.275f, 0.0f,   //6
        -0.225f,  0.425f, 0.0f,   //7
         0.225f,  0.425f, 0.0f,   //8
        -0.15f,   0.425f, 0.0f,   //9
         0.15f,   0.425f, 0.0f,   //10
         0.15f,   0.0f,   0.0f,   //11
         0.3f,    0.0f,   0.0f,   //12
         0.3f,    0.425f, 0.0f,   //13
        -0.225f,  0.075f, 0.0f,   //14
        -0.225f, -0.075f, 0.0f,   //15
         0.225f, -0.075f, 0.0f,   //16
         0.225f,  0.075f, 0.0f,   //17
         0.225f,  0.0f,   0.0f,   //18
        -0.225f,  0.0f,   0.0f,   //19
        -0.3f,    0.0f,   0.0f,   //20
        -0.3f,   -0.35f,  0.0f,   //21
        -0.15f,  -0.35f,  0.0f,   //22
        -0.15f,   0.0f,   0.0f,   //23
        -0.3f,   -0.5f,   0.0f,   //24
         0.3f,   -0.5f,   0.0f,   //25
         0.3f,   -0.35f,  0.0f,   //26
         0.15f,  -0.275f, 0.0f,   //27
         0.15f,  -0.35f,  0.0f,   //28
         0.3f,   -0.275f, 0.0f }; //29
    
    private static final short drawOrder[] = { 0,1,3,3,1,2,0,4,7,4,5,9,9,5,6,3,8,13,10,11,13,13,11,12,18,16,12,14,15,17,17,15,16,14,20,19,20,21,23,23,21,22,21,24,26,26,24,25,27,28,29,29,28,26 }; // order to draw vertices

    //#RGB: white (255, 255, 255)
    private static final float color[] = { 1.0f, 1.0f, 1.0f, 1.0f };

    public NumTwo(float scale, float centreX, float centreY) {
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