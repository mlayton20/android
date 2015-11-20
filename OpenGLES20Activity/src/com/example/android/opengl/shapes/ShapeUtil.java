package com.example.android.opengl.shapes;

import java.util.ArrayList;

public class ShapeUtil {
    
	private static final float ALIGN_CENTRE = 0f;
	private static final float OFFSET_ALIGN  = 0.7f;
    
    public static ArrayList<Shape> generateNestedShapes(Shape parentShape, float scale, String nestedText) {
    	ArrayList<Shape> nestedShapes = new ArrayList<Shape>();
		if (nestedText == null || nestedText == "") {
			return nestedShapes;
		}
		
		for (int i = 0; i < nestedText.length(); i++) {
			nestedShapes.add(getShape(parentShape, 
					scale,
					nestedText.charAt(i),
					getAlignPosition(nestedText.length(),i)
					));
		}
		return nestedShapes;
	}

	private static float getAlignPosition(int maxPosition, int position) {
		int centre = maxPosition / 2;
		int distanceFromCentre = position - centre;
		float centreAlign = ALIGN_CENTRE;
		
		//Check if even amount of numbers
		if (maxPosition % 2 == 0) {
			centreAlign = OFFSET_ALIGN/2;
		}
		
		//If its the centre, return centre
		if (distanceFromCentre == 0) {
			return centreAlign;
		//Otherwise return the offset
		} else {
			return centreAlign + (OFFSET_ALIGN*distanceFromCentre);
		}
	}

	private static Shape getShape(Shape parentShape, float scale, char value, float alignValue) {
		float centreX = (scale*alignValue) + parentShape.getCentreX();
		float centreY = parentShape.getCentreY();
		float[] textColor = parentShape.getNestedTextColor();
		
		if (textColor == null) {
			textColor = Color.WHITE;
		}
		
		switch(value) {
			//Numbers
			case '1':
				return new NumOne(scale, centreX, centreY, textColor);
			case '2':
				return new NumTwo(scale, centreX, centreY, textColor);
			case '3':
				return new NumThree(scale, centreX, centreY, textColor);
			case '4':
				return new NumFour(scale, centreX, centreY, textColor);
			case '5':
				return new NumFive(scale, centreX, centreY, textColor);
			case '6':
				return new NumSix(scale, centreX, centreY, textColor);
			case '7':
				return new NumSeven(scale, centreX, centreY, textColor);
			case '8':
				return new NumEight(scale, centreX, centreY, textColor);
			case '9':
				return new NumNine(scale, centreX, centreY, textColor);
			case '0':
				return new NumZero(scale, centreX, centreY, textColor);
				
			//Operators
			case '+':
				return new OperatorPlus(scale, centreX, centreY);
			case '*':
				return new OperatorMultiply(scale, centreX, centreY);
			case '/':
				return new OperatorDivide(scale, centreX, centreY);
			
			//Text
			case '_':
				return new TextUnderscore(scale, centreX, centreY);
			case 'x':
				return new OperatorMultiply(scale, centreX, centreY);
			
			//This shouldn't happen 
			default:
				return null;
		}
	}

}
