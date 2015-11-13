package com.example.android.opengl;

import java.util.ArrayList;

import android.util.Log;

public class StatsRectangle extends Shape {
	
	private static final String TAG = "StatsRectangle";
	private static final float SCALE_BORDER = 0.92f;
	private static final float SCALE_NESTED_TEXT = 0.07f;
	private static final float FIXED_OFFSET_Y = 0.9f;

    private static final float originalCoords[] = {
        -1.0f/4.6f,  0.1f,  0.0f,   //0
        -1.0f/4.6f, -0.1f,  0.0f,   //1
         1.0f/4.6f, -0.1f,  0.0f,   //2
         1.0f/4.6f,  0.1f,  0.0f }; //3
    
    private static final short drawOrder[] = { 0,1,3,3,1,2 }; // order to draw vertices

    //#RGB: red (229, 51, 72)
    private static final float borderColor[] = { 0.8980392156862745f, 0.2f, 0.2823529411764706f, 1.0f };
    //#RGB: blue (48, 141, 212)
    private static final float fillColor[] = { 0.1882352941176471f, 0.5529411764705882f, 0.8313725490196078f, 1.0f };
    
    private ArrayList<Shape> shapes;

    public StatsRectangle(float offsetX) {
    	super(originalCoords, drawOrder, borderColor, 1, offsetX, FIXED_OFFSET_Y);
    	shapes = new ArrayList<Shape>();
    	shapes.add(0, new StatsRectangle(SCALE_BORDER, 0 + getCentreX(), 0 + getCentreY()));
    }
    
    //This is for when we want to add a border
  	private StatsRectangle(float scale, float centreX, float centreY) {
      	super(originalCoords, drawOrder, fillColor, scale, centreX, centreY);
    }
    
    @Override
    public ArrayList<Shape> getShapes() {
		return shapes;
	}
    
    @Override
    public void setShapes(float scale, String nestedText) {
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