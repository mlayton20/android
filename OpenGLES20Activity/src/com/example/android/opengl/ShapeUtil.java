package com.example.android.opengl;

import java.util.ArrayList;

public class ShapeUtil {
    
    private static final float ALIGN_LEFT   = -0.7f;
	private static final float ALIGN_CENTRE = 0f;
	private static final float ALIGN_RIGHT  = 0.7f;
	private static final float OFFSET_ALIGN = 0.35f;
    
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
		switch(position) {
			case 0:
				return ALIGN_LEFT + (maxPosition == 2 ? OFFSET_ALIGN : 0) + (maxPosition == 1 ? OFFSET_ALIGN*2 : 0);
			case 1:
				return ALIGN_CENTRE + (maxPosition == 2 ? OFFSET_ALIGN : 0);
			case 2:
				return ALIGN_RIGHT;
			default:
				return ALIGN_CENTRE;
		}
	}

	private static Shape getShape(Shape parentShape, float scale, char value, float alignValue) {
		float centreX = (scale*alignValue) + parentShape.getCentreX();
		float centreY = parentShape.getCentreY();
		switch(value) {
			//Numbers
			case '1':
				return new NumOne(scale, centreX, centreY);
			case '2':
				return new NumTwo(scale, centreX, centreY);
				
			//Operators
			case '+':
				return new OperatorPlus(scale, centreX, centreY);
			
			//This shouldn't happen 
			default:
				return null;
		}
	}

}
