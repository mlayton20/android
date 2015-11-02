package com.example.android.opengl;

import java.util.ArrayList;

import android.util.Log;

public class EquationRectangle extends Shape {
	
	private static final String TAG = "EquationRectangle";
	private static final float SCALE_NESTED_TEXT = 0.35f;

    private static final float originalCoords[] = {
        -1.0f,  0.1f,  0.0f,   //0
        -1.0f, -0.1f,  0.0f,   //1
         1.0f, -0.1f,  0.0f,   //2
         1.0f,  0.1f,  0.0f }; //3
    
    private static final short drawOrder[] = { 0,1,3,3,1,2 }; // order to draw vertices

    //#RGB: Light blue
    private static final float color[] = { 0.2f, 0.709803922f, 0.898039216f, 1.0f };
    
    private ArrayList<Shape> shapes;

    public EquationRectangle() {
    	super(originalCoords, drawOrder, color, 1, 0, 0);
    	shapes = new ArrayList<Shape>();
    }
    
    @Override
    public ArrayList<Shape> getShapes() {
		return shapes;
	}
    
    @Override
    public void setShapes(String nestedText) {
    	shapes = ShapeUtil.generateNestedShapes(this, 0.3f*SCALE_NESTED_TEXT, nestedText);
    }

	@Override
	public float getCentreX() {
		return shapeCoords[6] + shapeCoords[0];
	}

	@Override
	public float getCentreY() {
		return shapeCoords[1] + shapeCoords[4];
	}
}