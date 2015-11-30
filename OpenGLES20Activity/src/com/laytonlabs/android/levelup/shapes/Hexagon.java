package com.laytonlabs.android.levelup.shapes;

import java.util.ArrayList;

import com.laytonlabs.android.levelup.Vec2;

import android.util.Log;

public class Hexagon extends Shape {
	
	private static final String TAG = "Hexagon";
	
	private static final float SCALE_BORDER = 0.92f;
	private static final float SCALE_NESTED_TEXT = 0.35f;

    private static final float originalCoords[] = {
        0.0f,   0.5f, 0.0f,   // top
       -0.5f,  0.2f, 0.0f,   // top left
       -0.5f, -0.2f, 0.0f,   // bottom left
        0.0f,  -0.5f, 0.0f,   // bottom
        0.5f, -0.2f, 0.0f,   // bottom right
        0.5f,  0.2f, 0.0f }; // top right
    
    private static final short drawOrder[] = { 0, 1, 5, 1, 4, 5, 1, 2, 4, 2, 3, 4 }; // order to draw vertices

    private static final float fillColor[] = Color.LIGHT_GREY;
    
    private ArrayList<Shape> shapes;
    
    private String nestedText;

    //This will be the parent cell.
	public Hexagon(float scale, float centreX, float centreY, String nestedText) {
    	super(originalCoords, drawOrder, getBorderColor(nestedText), scale, scale*centreX, scale*centreY);
    	
    	this.nestedText = nestedText;
    	
    	shapes = generateNestedShapes(scale, nestedText);
    	Log.d("GLPosition", "Hexagon: (" + nestedText + ")" + getCentreX() + ", " + getCentreY());
    }
	
	private static float[] getBorderColor(String nestedText) {
		switch(nestedText.charAt(0)) {
			//Operators
			case '+':
				return Color.LIGHT_BLUE;
			case '-':
				return Color.ORANGE;
			case '*':
				return Color.PURPLE;
			case '/':
				return Color.TURQUOISE;
			
			//This shouldn't happen 
			default:
				return Color.LIGHT_BLUE;
		}
	}

	//This is for when we want to add a border to the hexagon.
	private Hexagon(float scale, float centreX, float centreY) {
		//TODO - make getFillColor function that when its a bonus cell the color is yellow otherwise light_grey
    	super(originalCoords, drawOrder, fillColor, scale, centreX, centreY);
    }

	public ArrayList<Shape> generateNestedShapes(float parentScale, String nestedText) {
		ArrayList<Shape> nestedShapes = ShapeUtil.generateNestedShapes(this, parentScale*SCALE_NESTED_TEXT, nestedText);
		
		//Add the border hexagon
		nestedShapes.add(0, new Hexagon(parentScale*SCALE_BORDER, 0 + getCentreX(), 0 + getCentreY()));
		
		return nestedShapes;
	}
	
	@Override
    public ArrayList<Shape> getShapes() {
		return shapes;
	}

	@Override
	public float getCentreX() {
		return (shapeCoords[12] + shapeCoords[3])/2;
	}

	@Override
	public float getCentreY() {
		return (shapeCoords[1] + shapeCoords[10])/2;
	}

	public String getNestedText() {
		return nestedText;
	}
	
	@Override
	public String toString() {
    	return getNestedText();
    }
	
	@Override
	public boolean intersects(Vec2 touchCoords) {
		if (touchCoords.getX() >= shapeCoords[3] && touchCoords.getX() <= shapeCoords[12]
				&& touchCoords.getY() >= shapeCoords[7] && touchCoords.getY() <= shapeCoords[4]) {
			return true;
		}
    	return false;
    }
}