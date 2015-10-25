package com.example.android.opengl;

public class OperatorPlus extends Shape {

    private static final float originalCoords[] = {
        -0.075f,  0.3f,   0.0f,   //0
        -0.075f, -0.3f,   0.0f,   //1
         0.075f, -0.3f,   0.0f,   //2
         0.075f,  0.3f,   0.0f,   //3
        -0.3f,    0.075f, 0.0f,   //4
        -0.3f,   -0.075f, 0.0f,   //5
         0.3f,   -0.075f, 0.0f,   //6
         0.3f,    0.075f, 0.0f }; //7
    
    private static final short drawOrder[] = { 0,1,3,3,1,2,4,5,7,7,5,6 }; // order to draw vertices

    //#RGB: white (255, 255, 255)
    private static final float color[] = { 1.0f, 1.0f, 1.0f, 1.0f };

    public OperatorPlus(float scale, float centreX) {
    	super(originalCoords, drawOrder, color, scale, centreX);
    }

	@Override
	public float getCentreX() {
		// TODO Auto-generated method stub
		return 0;
	}
}