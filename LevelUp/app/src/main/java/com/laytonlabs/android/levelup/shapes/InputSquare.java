package com.laytonlabs.android.levelup.shapes;

import java.util.ArrayList;

import com.laytonlabs.android.levelup.Vec2;

import android.util.Log;

public class InputSquare extends Shape {
	
	private static final String TAG = "InputSquare";

    public static final float SCALE_NORMAL = 0.16f;
    public static final float SCALE_LARGE = 0.19f;
    private static final float FIXED_OFFSET_Y = 0.9f;
	private static final float SCALE_BORDER = 0.92f;
	private static final float SCALE_NESTED_TEXT = 0.6f;

    private static final float originalCoords[] = {
        -0.5f,  0.5f,  0.0f,   //0
        -0.5f, -0.5f,  0.0f,   //1
         0.5f, -0.5f,  0.0f,   //2
         0.5f,  0.5f,  0.0f }; //3
    
    private static final short drawOrder[] = { 0,1,3,3,1,2 }; // order to draw vertices
    
    private static final float borderColor[] = Color.GREY;
    private static final float fillColor[] = Color.LIGHT_GREY;
    
    private ArrayList<Shape> shapes;
    
    private String nestedText;
    private float scale;

    //This will be the parent cell.
	public InputSquare(float scale, float centreX, float centreY, String nestedText) {
    	super(originalCoords, drawOrder, borderColor, scale, scale*centreX, (scale*centreY)-FIXED_OFFSET_Y);
    	
    	this.nestedText = nestedText;
        this.scale = scale;

        shapes = new ArrayList<Shape>();
        shapes.add(0, new InputSquare(this));
        generateNestedShapes(scale, nestedText);
    }
	
	//This is for when we want to add a border.
	private InputSquare(InputSquare parent) {
    	super(originalCoords, drawOrder,
                fillColor,
                parent.scale * SCALE_BORDER,
                0 + parent.getCentreX(),
                0 + parent.getCentreY());
    }

    private void generateNestedShapes(float parentScale, String nestedText) {
        //Need to remove current text in the shape, if there is any text already.
        removeNestedTextShapes();

        ArrayList<Shape> nestedShapes = ShapeUtil.generateNestedShapes(this, parentScale*SCALE_NESTED_TEXT, nestedText);

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
    public void setShapes(float scale, String nestedText) {
        generateNestedShapes(scale, this.nestedText);
    }
	
	@Override
    public ArrayList<Shape> getShapes() {
		return shapes;
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
	public float getMinX() {
		return shapeCoords[0];
	}
	
	@Override
    public float getMaxX() {
    	return shapeCoords[6];
    }
    
	@Override
    public float getMinY() {
		return shapeCoords[4];
	}
	
	@Override
    public float getMaxY() {
		return shapeCoords[1];
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
		if (touchCoords.getX() >= shapeCoords[0] && touchCoords.getX() <= shapeCoords[6]
				&& touchCoords.getY() >= shapeCoords[4] && touchCoords.getY() <= shapeCoords[1]) {
			return true;
		}
    	return false;
    }
}