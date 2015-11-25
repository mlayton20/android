/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.opengl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.android.opengl.shapes.Shape;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;

/**
 * A view container where OpenGL ES graphics can be drawn on screen.
 * This view can also be used to capture touch events, such as a user
 * interacting with drawn objects.
 */
public class MyGLSurfaceView extends GLSurfaceView {

	private static final String TAG = "MyGLSurfaceView";
    private final MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context) {
        super(context);
        
        //Initialise the Time for the Game to start
        Time.initialise();

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new MyGLRenderer();
        setRenderer(mRenderer);
    }
    
    private Shape mPreviousTouchedCell;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.
    	
        Vec2 touchCoords = new Vec2(e.getX(),e.getY());

        switch (e.getAction()) {
            case MotionEvent.ACTION_UP:                
            	Shape touchedShape = null;
            	//Check if input grid is selected.
            	Vec2 touchGLCoords = getWorldCoords(mRenderer.getFixedModelMatrix(), touchCoords);
            	Log.d("TouchedInputShape", "Fixed GL Coords: " + touchGLCoords.toString());
                touchedShape = getTouchedShape(mRenderer.getInputShapes(), touchGLCoords, true);
                
                //If TouchedInput was selected, only show if a cell has been touched previously.
            	if (touchedShape != null) {
            		
            		//Only show the input if a cell has been selected previously
            		if (mPreviousTouchedCell != null) {
            			processGuess(touchedShape);
            		}
            		mRenderer.setRenderOutput(true);
            		break;
                }
                
                //Check if a cell was selected.
            	touchGLCoords = getWorldCoords(mRenderer.getGridModelMatrix(), touchCoords);
            	Log.d("TouchedInputShape", "Grid GL Coords: " + touchGLCoords.toString());
            	touchedShape = getTouchedShape(mRenderer.getShapes(), touchGLCoords, false);
                
            	//If a cell has been touched, start making the equation
                if (touchedShape != null) {
                	mRenderer.setEquationText(mRenderer.getCurrentAnswer() + touchedShape.toString());
                	mRenderer.setExpectedAnswer(mRenderer.getEquationText());
                	mRenderer.setAnswerText("");
                	mPreviousTouchedCell = touchedShape;
                //If nothing has been pressed, reset the output shapes.
        		} else {
        			resetOutput();
        		}
                mRenderer.setRenderOutput(true);
                break;
        }
        return true;
    }

	private void resetOutput() {
		mRenderer.setEquationText("");
		mRenderer.resetAnswerText();
		mPreviousTouchedCell = null;
	}
	
	public void processGuess(Shape touchedShape) {
		//If the x was pressed we need to clear the current guess to start again.
		if (touchedShape.toString() == "x") {
			mRenderer.setAnswerText("");
			return;
		}
		
		mRenderer.setAnswerText(touchedShape.toString());
		
		//If length of guess is not the same as expected answer, ignore it
		if (mRenderer.getAnswerText().length() != mRenderer.getExpectedAnswer().length()) {
			return;
		}
		
		//If user still has to make inputs, ignore it
		if (mRenderer.getAnswerText().indexOf('_') > -1) {
			return;
		}
		
		//If guess matches answer then clear equation and set answer as expected Answer
		if (mRenderer.getAnswerText().equals(mRenderer.getExpectedAnswer())) {
			processCorrectGuess();
			return;
		//If guess does not match answer then clear answer text so someone can make new guess
		} else {
			processWrongGuess();
			return;
		}
	}

	private void processWrongGuess() {
		mRenderer.setWrongGuess(true);
	}

	private void processCorrectGuess() {
		mRenderer.setCurrentAnswer(mRenderer.getExpectedAnswer());
		mRenderer.incrementRowLevel();
		Score.setScore(mPreviousTouchedCell.toString());
		resetOutput();
		mRenderer.setCorrectGuess(true);
	}

	public Shape getTouchedShape(ArrayList<Shape> shapes, Vec2 touchGLCoords, boolean findClosest) {
		int index = 0;
		for (Shape shape : shapes) {
			index++;
			if (shape.intersects(touchGLCoords)) {
				Log.d("TouchedInputShape", "Shape touched is " + index + " value: " + shape.toString());
				return shape;
			}
		}
		
		if (findClosest) {
			return getClosestShape(shapes, touchGLCoords);
		}
		
		return null;
	}

	private Shape getClosestShape(ArrayList<Shape> shapes, Vec2 touchGLCoords) {
		int index;
		float minX = 1000f, maxX = -1000f;
		float minY = 1000f, maxY = -1000f;
		float distanceToCentre = 1000f;
		
		//Get the GL ranges of the shapes
		for (Shape shape : shapes) {
			minX = Math.min(shape.getMinX(), minX);
			maxX = Math.max(shape.getMaxX(), maxX);
			minY = Math.min(shape.getMinY(), minY);
			maxY = Math.max(shape.getMaxY(), maxY);
		}
		
		Log.d("TouchedInputShape", "Range bef: x (" + minX + " - " + maxX + ") y (" + minY + " - " + maxY + ")");
		//Increase the range a bit around the grid.
		minX += -0.1;
		maxX += 0.1;
		minY += -0.1;
		maxY += 0.1;
		Log.d("TouchedInputShape", "Range aft: x (" + minX + " - " + maxX + ") y (" + minY + " - " + maxY + ")");
		
		//Check if the touch was within the range of the shapes.
		if (touchGLCoords.getX() >= minX && touchGLCoords.getX() <= maxX
				&& touchGLCoords.getY() >= minY && touchGLCoords.getY() <= maxY) {
			
			Shape closestShape = null;
			float tempDistanceToCentre = 0;
			index = 0;
			int touchedIndex = 0;
			
			for (Shape shape : shapes) {
				tempDistanceToCentre = (float) Math.sqrt(Math.pow((touchGLCoords.getX() - shape.getCentreX()), 2) 
						+ Math.pow((touchGLCoords.getY() - shape.getCentreY()), 2));
				index++;
				if (tempDistanceToCentre < distanceToCentre) {
					closestShape = shape;
					distanceToCentre = tempDistanceToCentre;
					touchedIndex = index;
				}
			}
			Log.d("TouchedInputShape", "Closest Shape touched is " + touchedIndex + " value: " + closestShape.toString());
			return closestShape;
		}
		return null;
	}
    
    /**
     * Calculates the transform from screen coordinate
     * system to world coordinate system coordinates
     * for a specific point, given a camera position.
     *
     * @param touch Vec2 point of screen touch, the
       actual position on physical screen (ej: 160, 240)
     * @return position in WCS.
     */
    public Vec2 getWorldCoords(float[] modelMatrix, Vec2 touch)
    {  
    	Log.d("TouchedInputShape", "Model Matrix for coords: " + Arrays.toString(modelMatrix));
        // SCREEN height & width (ej: 320 x 480)
        float screenW = (float) getWidth();
        float screenH = (float) getHeight();

        // Auxiliary matrix and vectors
        // to deal with ogl.
        float[] invertedMatrix, transformMatrix,
            normalizedInPoint, outPoint;
        invertedMatrix = new float[16];
        transformMatrix = new float[16];
        normalizedInPoint = new float[4];
        outPoint = new float[4];

        // Invert y coordinate, as android uses
        // top-left, and ogl bottom-left.
        int oglTouchY = (int) (screenH - touch.getY());

        /* Transform the screen point to clip
        space in ogl (-1,1) */       
        normalizedInPoint[0] =
         (float) ((touch.getX()) * 2.0f / screenW - 1.0);
        normalizedInPoint[1] =
         (float) ((oglTouchY) * 2.0f / screenH - 1.0);
        normalizedInPoint[2] = - 1.0f;
        normalizedInPoint[3] = 1.0f;

        /* Obtain the transform matrix and
        then the inverse. */
        Matrix.multiplyMM(
            transformMatrix, 0,
            mRenderer.getProjectionMatrix(), 0,
            modelMatrix, 0);
        Matrix.invertM(invertedMatrix, 0,
            transformMatrix, 0);       

        /* Apply the inverse to the point
        in clip space */
        Matrix.multiplyMV(
            outPoint, 0,
            invertedMatrix, 0,
            normalizedInPoint, 0);

        if (outPoint[3] == 0.0)
        {
            // Avoid /0 error.
            Log.e("World coords", "ERROR!");
            return null;
        }

        Log.d("GLPosition", "Touch: " + (outPoint[0] / outPoint[3]) + ", " + (outPoint[1] / outPoint[3]));
        // Divide by the 3rd component to find
        // out the real position.
        return new Vec2(outPoint[0] / outPoint[3], outPoint[1] / outPoint[3]);       
    }

}
