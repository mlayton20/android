package com.example.android.opengl.shapes;

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
    //private static final float color[] = { 0.2f, 0.709803922f, 0.898039216f, 1.0f };
    
    //#RGB: Transparent (Black)
    private static final float color[] = { 0, 0, 0, 0 };
    
    private ArrayList<Shape> shapes;
    
    private float[] nestedTextColor = Color.WHITE;
    
    private String nestedText;

    public EquationRectangle(float centreY) {
    	super(originalCoords, drawOrder, color, 1, 0, centreY);
    	shapes = new ArrayList<Shape>();
    }
    
    @Override
    public ArrayList<Shape> getShapes() {
		return shapes;
	}
    
    @Override
    public void setShapes(float scale, String nestedText) {
    	this.nestedText = nestedText;
    	nestedTextColor = Color.WHITE;
    	shapes = ShapeUtil.generateNestedShapes(this, scale*SCALE_NESTED_TEXT, nestedText);
    }
    
    @Override
    public void setShapes(float scale, String nestedText, float[] color) {
    	this.nestedText = nestedText;
    	nestedTextColor = color;
    	shapes = ShapeUtil.generateNestedShapes(this, scale*SCALE_NESTED_TEXT, nestedText);
    }
    
    @Override
    public float[] getNestedTextColor() {
    	return nestedTextColor;
    }

	@Override
	public float getCentreX() {
		return (shapeCoords[6] + shapeCoords[0])/2;
	}

	@Override
	public float getCentreY() {
		return (shapeCoords[1] + shapeCoords[4])/2;
	}
	
	@Override
	public String toString() {
		return nestedText;
	}
}