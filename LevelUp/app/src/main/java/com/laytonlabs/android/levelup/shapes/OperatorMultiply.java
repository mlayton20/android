package com.laytonlabs.android.levelup.shapes;

public class OperatorMultiply extends Shape {

    private static final float originalCoords[] = {
        -0.2f,  0.3f, 0.0f,   //0
        -0.3f,  0.2f, 0.0f,   //1
         0.2f, -0.3f, 0.0f,   //2
         0.3f, -0.2f, 0.0f,   //3
         0.2f,  0.3f, 0.0f,   //4
        -0.3f, -0.2f, 0.0f,   //5
        -0.2f, -0.3f, 0.0f,   //6
         0.3f,  0.2f, 0.0f }; //7
    
    private static final short drawOrder[] = { 0,1,2,0,2,3,4,5,6,4,6,7 }; // order to draw vertices

    public OperatorMultiply(float scale, float centreX, float centreY, float[] color) {
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