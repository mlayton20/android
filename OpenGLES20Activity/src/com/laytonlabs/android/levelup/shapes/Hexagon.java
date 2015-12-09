package com.laytonlabs.android.levelup.shapes;

import java.util.ArrayList;

import com.laytonlabs.android.levelup.Vec2;
import com.laytonlabs.android.levelup.game.Cell;

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
    private float scale;
    private Cell cell;

    //This will be the parent cell.
	public Hexagon(float scale, float centreX, float centreY, Cell cell) {
    	super(originalCoords, drawOrder, getBorderColor(cell), scale, scale*centreX, scale*centreY);
    	
    	this.nestedText = cell.toString();
    	this.scale = scale;
    	this.cell = cell;
    	
    	shapes = new ArrayList<Shape>();
    	shapes.add(0, new Hexagon(this));
    	generateNestedShapes(scale, nestedText);
    }
	
	private static float[] getBorderColor(Cell cell) {
		String nestedText = cell.toString();
		float[] color;
		switch(nestedText.charAt(0)) {
			//Operators
			case '+':
				color = Color.LIGHT_BLUE.clone();
				break;
			case '-':
				color = Color.ORANGE.clone();
				break;
			case '*':
				color = Color.PURPLE.clone();
				break;
			case '/':
				color = Color.TURQUOISE.clone();
				break;
			//This shouldn't happen 
			default:
				color = Color.LIGHT_BLUE.clone();
				break;
		}
		
		//Grey out the cell if it's not enabled.
		if (!cell.isEnabled()) {
			//Set the opacity so the cell looks greyed out.
			color[3] = 0.5f;
		}
		
		return color;
	}

	//This is for when we want to add a border to the hexagon.
	private Hexagon(Hexagon parent) {
    	super(originalCoords, drawOrder, 
    			getFillColor(parent.cell), 
    			parent.scale*SCALE_BORDER, 
    			0 + parent.getCentreX(), 
    			0 + parent.getCentreY());
    }
	
	private static float[] getFillColor(Cell cell) {
		float[] color;
		
		//TODO - Add section to determine if a cell is a bonus cell.
		//when its a bonus cell the color is yellow otherwise light_grey
		color = Color.LIGHT_GREY.clone();
		
		//Grey out the cell if it's not enabled.
		if (!cell.isEnabled()) {
			color[3] = 0.5f;
		}
		
		return color;
	}

	public void generateNestedShapes(float parentScale, String nestedText) {
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