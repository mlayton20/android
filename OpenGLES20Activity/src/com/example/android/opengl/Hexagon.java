package com.example.android.opengl;

import java.util.ArrayList;

import android.util.Log;

public class Hexagon extends Shape {
	
	private static final String TAG = "Hexagon";
	
	private static final float ALIGN_LEFT   = -0.75f;
	private static final float ALIGN_CENTRE = -0.05f;
	private static final float ALIGN_RIGHT  =  0.65f;
	private static final float SCALE_NESTED_TEXT = 0.35f;

    private static final float originalCoords[] = {
        0.0f,   0.5f, 0.0f,   // top
       -0.5f,  0.2f, 0.0f,   // top left
       -0.5f, -0.2f, 0.0f,   // bottom left
        0.0f,  -0.5f, 0.0f,   // bottom
        0.5f, -0.2f, 0.0f,   // bottom right
        0.5f,  0.2f, 0.0f }; // top right
    
    private static final short drawOrder[] = { 0, 1, 5, 1, 4, 5, 1, 2, 4, 2, 3, 4 }; // order to draw vertices

    //#RGB: red (229, 51, 72)
    private static final float color[] = { 0.8980392156862745f, 0.2f, 0.2823529411764706f, 1.0f };
    
    private ArrayList<Shape> shapes;

    //This will be the parent cell.
	public Hexagon(float scale, float centre, String nestedText) {
    	super(originalCoords, drawOrder, color, scale, scale*centre);
    	
    	generateNestedShapes(scale, nestedText);
    }
	
	//This is for when we want to add a border to the hexagon.
	public Hexagon(float scale, float centre) {
    	super(originalCoords, drawOrder, color, scale, scale*centre);
    }

	private void generateNestedShapes(float parentScale, String nestedText) {
		if (nestedText == null || nestedText == "") {
			return;
		}
		
		shapes = new ArrayList<Shape>();
		float nestedScale = parentScale*SCALE_NESTED_TEXT;
		
		for (int i = 0; i < nestedText.length(); i++) {
			shapes.add(getShape(nestedScale,
					nestedText.charAt(i),
					getAlignPosition(nestedText.length(),i)
					));
		}
	}

	private float getAlignPosition(int maxPosition, int position) {
		if (position == maxPosition) {
			return ALIGN_CENTRE;
		}
		
		switch(position) {
			case 0:
				return ALIGN_LEFT;
			case 1:
				return ALIGN_CENTRE;
			case 2:
				return ALIGN_RIGHT;
			default:
				return ALIGN_CENTRE;
		}
	}

	private Shape getShape(float scale, char value, float alignValue) {
		switch(value) {
		
			//Numbers
			case '1':
				return new NumOne(scale, alignValue, getXCentre());
			case '2':
				return new NumTwo(scale, alignValue, getXCentre());
				
			//Operators
			case '+':
				return new OperatorPlus(scale, alignValue, getXCentre());
			
			//This shouldn't happen 
			default:
				return null;
		}
	}

	@Override
    public ArrayList<Shape> getShapes() {
		return shapes;
	}

	@Override
	public float getXCentre() {
		return (shapeCoords[12] + shapeCoords[3])/2;
	}
}