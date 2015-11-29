package com.example.android.opengl.shapes;

import java.util.ArrayList;

import com.example.android.opengl.Screen;

import android.util.Log;

public class StatsRectangle extends Shape {
	
	private static final String TAG = "StatsRectangle";
	private static final float SCALE_BORDER = 0.92f;
	private static final float SCALE_NESTED_TEXT = 0.07f;
	private static final float FIXED_OFFSET_Y = 0.9f;

    private static final float originalCoords[] = {
        -Screen.DEFAULT_PORTRAIT_RATIO/3,  0.1f,  0.0f,   //0
        -Screen.DEFAULT_PORTRAIT_RATIO/3, -0.1f,  0.0f,   //1
         Screen.DEFAULT_PORTRAIT_RATIO/3, -0.1f,  0.0f,   //2
         Screen.DEFAULT_PORTRAIT_RATIO/3,  0.1f,  0.0f }; //3
    
    private static final short drawOrder[] = { 0,1,3,3,1,2 }; // order to draw vertices
    
    private ArrayList<Shape> shapes;
    
    private float[] nestedTextColor = Color.WHITE;

    public StatsRectangle(float offsetX, float[] borderColor, float[] fillColor) {
    	super(originalCoords, drawOrder, borderColor, 1, offsetX, FIXED_OFFSET_Y);
    	shapes = new ArrayList<Shape>();
    	shapes.add(0, new StatsRectangle(SCALE_BORDER, 0 + getCentreX(), 0 + getCentreY(), fillColor));
    }
    
    //This is for when we want to add a border
  	private StatsRectangle(float scale, float centreX, float centreY, float[] fillColor) {
      	super(originalCoords, drawOrder, fillColor, scale, centreX, centreY);
    }
    
    @Override
    public ArrayList<Shape> getShapes() {
		return shapes;
	}
    
    @Override
    public float[] getNestedTextColor() {
    	return nestedTextColor;
    }
    
    @Override
    public void setShapes(float scale, String nestedText) {
    	nestedTextColor = Color.WHITE;
    	//Need to remove current text in the shape, if there is any text already.
    	removeNestedTextShapes();
    	
    	ArrayList<Shape> nestedShapes = ShapeUtil.generateNestedShapes(this, SCALE_NESTED_TEXT, nestedText);
    	
    	for (Shape shape : nestedShapes) {
    		shapes.add(shape);
    	}
    }
    
    @Override
    public void setShapes(float scale, String nestedText, float[] color) {
    	nestedTextColor = color;
    	//Need to remove current text in the shape, if there is any text already.
    	removeNestedTextShapes();
    	
    	ArrayList<Shape> nestedShapes = ShapeUtil.generateNestedShapes(this, SCALE_NESTED_TEXT, nestedText);
    	
    	for (Shape shape : nestedShapes) {
    		shapes.add(shape);
    	}
    }

	private void removeNestedTextShapes() {
		if (shapes.size() <= 1) {
			return;
		}
		
		for (int i = shapes.size()-1; i > 0; i--) {
			shapes.remove(i);
		}
	}

	@Override
	public float getCentreX() {
		return (shapeCoords[6] + shapeCoords[0])/2;
	}

	@Override
	public float getCentreY() {
		return (shapeCoords[1] + shapeCoords[4])/2;
	}
}