package com.example.android.opengl;

public class TextUnderscore extends Shape {

	private static final float originalCoords[] = {
        -0.3f, -0.4f,  0.0f,   //0
        -0.3f, -0.5f,  0.0f,   //1
         0.3f, -0.5f,  0.0f,   //2
         0.3f, -0.4f,  0.0f }; //3
    
    private static final short drawOrder[] = { 0,1,3,3,1,2 }; // order to draw vertices

    //#RGB: white (255, 255, 255)
    private static final float color[] = { 1.0f, 1.0f, 1.0f, 1.0f };

    public TextUnderscore(float scale, float centreX, float centreY) {
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