package com.example.android.opengl.shapes;

import java.util.ArrayList;

public class ShapeUtil {
    
	private static final float ALIGN_CENTRE = 0f;
	private static final float OFFSET_ALIGN  = 0.7f;
	private static final float SCALE_PADDING = 0.1f;
	private static final float TEXT_MIN_X = -0.3f;
    
    public static ArrayList<Shape> generateNestedShapes(Shape parentShape, float scale, String nestedText) {
    	ArrayList<Shape> nestedShapes = new ArrayList<Shape>();
		if (nestedText == null || nestedText == "") {
			return nestedShapes;
		}
		
		scale = getTextScale(parentShape, scale, nestedText);
		
		for (int i = 0; i < nestedText.length(); i++) {
			nestedShapes.add(getShape(parentShape, 
					scale,
					nestedText.charAt(i),
					getAlignPosition(nestedText.length(),i)
					));
		}
		return nestedShapes;
	}
    
    private static float getTextScale(Shape parentShape, float scale, String nestedText) {
    	float parentPadding = parentShape.getMinX()*SCALE_PADDING;
    	parentPadding = (parentPadding < 0 ? parentPadding*-1 : parentPadding);
		float parentMinX = parentShape.getMinX() + parentPadding;
		float adjustValue = getAlignPosition(nestedText.length(),0);
		//0.3f is the width from centre of a text shape. It's always 0.3f
		float adjustedScale = scale;
		float leftTextMinX = ((adjustedScale*adjustValue) + parentShape.getCentreX()) + (TEXT_MIN_X*adjustedScale);
		
		while (leftTextMinX <= parentMinX) {
			adjustedScale -= 0.01f;
			leftTextMinX = ((adjustedScale*adjustValue) + parentShape.getCentreX()) + (TEXT_MIN_X*adjustedScale);
		};
		
		return adjustedScale;
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
			textColor = Color.NAVY_BLUE;
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
				return new OperatorPlus(scale, centreX, centreY, textColor);
			case '-':
				return new OperatorSubtract(scale, centreX, centreY, textColor);
			case '*':
				return new OperatorMultiply(scale, centreX, centreY, textColor);
			case '/':
				return new OperatorDivide(scale, centreX, centreY, textColor);
			
			//Text
			case '_':
				return new TextUnderscore(scale, centreX, centreY, textColor);
			case ':':
				return new TextColon(scale, centreX, centreY, textColor);
			case 'x':
				return new OperatorMultiply(scale, centreX, centreY, textColor);
			
			//This shouldn't happen 
			default:
				return null;
		}
	}

}
