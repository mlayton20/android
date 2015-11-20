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

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.example.android.opengl.shapes.Color;
import com.example.android.opengl.shapes.EquationRectangle;
import com.example.android.opengl.shapes.Hexagon;
import com.example.android.opengl.shapes.InputSquare;
import com.example.android.opengl.shapes.Shape;
import com.example.android.opengl.shapes.StatsRectangle;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

/**
 * Provides drawing instructions for a GLSurfaceView object. This class
 * must override the OpenGL ES drawing lifecycle methods:
 * <ul>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceCreated}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onDrawFrame}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceChanged}</li>
 * </ul>
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";
    private static StatsRectangle levelRectangle;
    private static StatsRectangle timeRectangle;
    private static StatsRectangle scoreRectangle;
    private static EquationRectangle equationRectangle;
    private static EquationRectangle answerRectangle;
    private ArrayList<Shape> shapes;
    private ArrayList<Shape> inputShapes;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mGridModelMatrix = new float[16];
    private float[] mFixedModelMatrix = new float[16];
    private float[] mTempMatrix = new float[16];

    private float mMovementY;
    private float mBottomRowScale; 
    private String mCurrentAnswer = "11";
    private String mEquationText = "";
    private String mAnswerText = mCurrentAnswer;
    private boolean isCorrectGuess = false;
    private boolean renderCorrectGuess = false;
    private boolean isWrongGuess = false;
    private boolean renderOutput = false;
    
    //To limit the number of renders per second
    private long startTime;
    private long endTime;
    private long timeElapsed;
    private int currentFrame = 0;              // active frame
    private int outputCurrentFrame = 0;              // active frame
    private final int FPS = 33;			   	   // Frames per second
    private final int FPS_ANIMATION_10 = 10;
    private final int FPS_ANIMATION_20 = 20;
    
    //Reducer values, these are used for animation scenes
    private float mFPSMovementY;
    private float mFPSBottomRowScale;
    
    private int level; //The current row in the grid.
    
    //Constants for grid presentation 
    private final float CELL_SCALE = 0.3f;
    private final float CELL_OFFSET_Y = 0.7f;

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        
        startTime = System.currentTimeMillis();
        
        //TODO Add code that sets current answer to whatever the current answer is.
        
        //TODO - Set level to be whatever the current level is
        level = 0;
        //Initialise fixed shapes
        equationRectangle = new EquationRectangle(-0.35f);
        answerRectangle = new EquationRectangle(-0.5f);
        
        equationRectangle.setShapes(0.2f, mEquationText);
        answerRectangle.setShapes(0.3f, mAnswerText);
        
        //TODO - Change the below calculations to be align_left, align_centre, align_right, etc.
        levelRectangle = new StatsRectangle(0 - (2.0f/4.6f));
        timeRectangle = new StatsRectangle(0);
        scoreRectangle = new StatsRectangle(0 + (2.0f/4.6f));
        
        levelRectangle.setShapes(-1f, Integer.toString(level));
        scoreRectangle.setShapes(-1f, "22");
        
        shapes = new ArrayList<Shape>();
        buildInputGrid();
        buildGrid();
        setBottomRowScale(1.0f);
        setFPSBottomRowScale(getBottomRowScale() / FPS_ANIMATION_20);
        setFPSMovementY((CELL_OFFSET_Y*CELL_SCALE) / FPS_ANIMATION_20);
    }

	private void buildGrid() {
		buildFourCells(CELL_OFFSET_Y*level);
		level++;
        buildThreeCells(CELL_OFFSET_Y*level);
	}
    
    private void buildInputGrid() {
    	inputShapes = new ArrayList<Shape>();
    	
    	inputShapes.add(new InputSquare(0.16f, -3.0f, 1.15f, "1")); //1
    	inputShapes.add(new InputSquare(0.16f, -1.8f, 1.15f, "2")); //2
    	inputShapes.add(new InputSquare(0.16f, -0.6f, 1.15f, "3")); //3
    	inputShapes.add(new InputSquare(0.16f,  0.6f, 1.15f, "4")); //4
    	inputShapes.add(new InputSquare(0.16f,  1.8f, 1.15f, "5")); //5
    	inputShapes.add(new InputSquare(0.16f,  3.0f, 1.15f, "6")); //6
    	inputShapes.add(new InputSquare(0.16f, -2.4f,     0, "7")); //7
    	inputShapes.add(new InputSquare(0.16f, -1.2f,     0, "8")); //8
    	inputShapes.add(new InputSquare(0.16f,     0,     0, "9")); //9
    	inputShapes.add(new InputSquare(0.16f,  1.2f,     0, "0")); //0
    	inputShapes.add(new InputSquare(0.16f,  2.4f,     0, "x")); //X - This is to clear input
	}

	private void buildThreeCells(float offsetY) {
		shapes.add(new Hexagon(CELL_SCALE,     0, offsetY, "/2"));
        shapes.add(new Hexagon(CELL_SCALE, -1.0f, offsetY, "+24"));
        shapes.add(new Hexagon(CELL_SCALE,  1.0f, offsetY, "*9"));
	}
	
	private void buildFourCells(float offsetY) {
		shapes.add(new Hexagon(CELL_SCALE, -1.5f, offsetY, "+17"));
        shapes.add(new Hexagon(CELL_SCALE, -0.5f, offsetY, "+10"));
        shapes.add(new Hexagon(CELL_SCALE,  0.5f, offsetY, "-3"));
        shapes.add(new Hexagon(CELL_SCALE,  1.5f, offsetY, "+12"));
	}

    @Override
    public void onDrawFrame(GL10 unused) {
    	
    	//We dont need continuous rendering, only needed for animation and time switching
    	endTime = System.currentTimeMillis();
    	timeElapsed = endTime - startTime;
        if (timeElapsed < FPS) {
            try {
            	Log.d(TAG, "Sleeping until "+FPS+" millsecs pass");
				Thread.sleep(FPS - timeElapsed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
        startTime = endTime;
        
        Matrix.setIdentityM(mGridModelMatrix, 0); // initialize to identity matrix
        //Setup the equation display before we start moving the grid around
        Matrix.setIdentityM(mFixedModelMatrix, 0); // initialize to identity matrix

        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        
        // Enable transparency options for colors.
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        
        //Clone this for use with fixed and grid MVPs
        mTempMatrix = mMVPMatrix.clone();

        // Create a rotation for the triangle

        // Use the following code to generate constant rotation.
        // Leave this code out when using TouchEvents.
        // long time = SystemClock.uptimeMillis() % 4000L;
        // float angle = 0.090f * ((int) time);
        
        drawGridShapes();
        
        drawFixedShapes();
    }

	private void drawGridShapes() {    	
		//Start the grid drawing at bottom of screen.
        Matrix.translateM(mGridModelMatrix, 0, 0, -0.1f, 0);

        //Move the grid down or up the screen depending on touch events.
        Matrix.translateM(mGridModelMatrix, 0, 0, mMovementY, 0);

        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        //Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);
        
        //Add the movement to the matrix
        Matrix.multiplyMM(mMVPMatrix, 0, mTempMatrix, 0, mGridModelMatrix, 0);
        
        if (isCorrectGuess()) {
        	renderCorrectGuess = true; //This is to change the answer text to green inside drawFixedShapes
        	currentFrame++;                    // step to next frame
        	setMovementY(getMovementY() - getFPSMovementY());
        	setBottomRowScale(getBottomRowScale() - getFPSBottomRowScale());
        	if (currentFrame > FPS_ANIMATION_20) {            // if end of sequence
        		currentFrame = 0;               // restart sequence
        		setBottomRowScale(1.0f);		// Reset the scale
        		removeBottomRow();
        		setCorrectGuess(false);			//Mark as false so animation stops and user can make new guess
        	}
        }
    	
        //Draw all grid shapes
        if (isCorrectGuess()) {
        	drawAllShapesAndShrinkBottomRow(mMVPMatrix);
        } else {
        	drawAllShapes(shapes, mMVPMatrix);
        }
	}
	
	private void drawAllShapes(ArrayList<Shape> shapes, float[] mMVPMatrix) {
		for (Shape shape : shapes) {
        	Log.d(TAG, "Scale Hexagon ("+shape.toString()+") Aft ("+shape.getCentreX()+", "+shape.getCentreY()+")");
        	drawShapes(shape, mMVPMatrix);
        }
	}
	
	private void drawAllShapesAndShrinkBottomRow(float[] mMVPMatrix) {
		float[] mMVPScaled = mMVPMatrix.clone();
		float[] mScaleModelMatrix = mGridModelMatrix.clone();
		
		Matrix.scaleM(mScaleModelMatrix, 0, getBottomRowScale(), getBottomRowScale(), 0);
		Matrix.multiplyMM(mMVPScaled, 0, mTempMatrix, 0, mScaleModelMatrix, 0);
		
		int lastCellIndex = getBottomRowLastCellIndex();
		
		//Apply scaling to the bottom row and just move the other rows.
		for (int i = 0; i < shapes.size(); i++) {
			if (i <= lastCellIndex) {
				drawShapes(shapes.get(i), mMVPScaled);
			} else {
				drawShapes(shapes.get(i), mMVPMatrix);
			}
		}
	}

	private void drawFixedShapes() {
		float[] mMVPFixed = new float[16];
		
		Matrix.multiplyMM(mMVPFixed, 0, mTempMatrix, 0, mFixedModelMatrix, 0);
		
		if (isRenderOutput()) {
			//Show the equation using the values from the selected cell.
	        equationRectangle.setShapes(0.2f, mEquationText);
	        answerRectangle.setShapes(0.3f, mAnswerText);
	        
	        //Update the stats
	        levelRectangle.setShapes(-1f, Integer.toString(level));
	        scoreRectangle.setShapes(-1f, "22");
	        setRenderOutput(false);
		}
		
		//Animation for changing color of the text
		if (isWrongGuess()) {
			if (outputCurrentFrame == 0) {
				answerRectangle.setShapes(0.3f, mAnswerText, Color.RED);
			}
			outputCurrentFrame++;
			if (outputCurrentFrame > FPS_ANIMATION_10) {
				outputCurrentFrame = 0;
				//TODO - Change the input to be underscores instead of blank.
				setAnswerText("");
				answerRectangle.setShapes(0.3f, mAnswerText);
        		setWrongGuess(false);			
        	}
		} else if (renderCorrectGuess) {
			if (outputCurrentFrame == 0) {
				answerRectangle.setShapes(0.3f, mAnswerText, Color.GREEN);
			}
			outputCurrentFrame++;
			if (outputCurrentFrame > FPS_ANIMATION_20) {
				outputCurrentFrame = 0;
				answerRectangle.setShapes(0.3f, mAnswerText);
				renderCorrectGuess = false;		
        	}
		}
		
		timeRectangle.setShapes(-1f, "12");

        drawShapes(answerRectangle, mMVPFixed);
        drawShapes(equationRectangle, mMVPFixed);
        drawShapes(levelRectangle, mMVPFixed);
        drawShapes(timeRectangle, mMVPFixed);
        drawShapes(scoreRectangle, mMVPFixed);
        
        //Draw all input grid shapess
        drawAllShapes(inputShapes, mMVPFixed);
	}

	private void drawShapes(Shape parentShape, float[] mMVPMatrix) {
		parentShape.draw(mMVPMatrix);
		
		if (parentShape.getShapes() == null) {
			return;
		}
		
        for (Shape nestedShapes : parentShape.getShapes()) {
    		nestedShapes.draw(mMVPMatrix);
    	}
	}
	
	private int getBottomRowLastCellIndex() {
		int lastCellIndex = 0;
		Shape prevShape = null;
		for (int i = 0; i < shapes.size(); i++) {
			if (prevShape == null || shapes.get(i).getCentreY() == prevShape.getCentreY()) {
				lastCellIndex = i;
				prevShape = shapes.get(i);
			} else {
				return lastCellIndex;
			}
		}
		return lastCellIndex;
	}
	
	private void removeBottomRow() {
		for (int i = getBottomRowLastCellIndex(); i >= 0 ; i--) {
			shapes.remove(i);
		}
	}

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);
        
        //Log.d("MyGLRenderer", "Width: " + width + " Height: " + height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        
        //TODO Store the current answer, the current answer Text and the current Equation. 
    }

    /**
     * Utility method for compiling a OpenGL shader.
     *
     * <p><strong>Note:</strong> When developing shaders, use the checkGlError()
     * method to debug shader coding errors.</p>
     *
     * @param type - Vertex or fragment shader type.
     * @param shaderCode - String containing the shader code.
     * @return - Returns an id for the shader.
     */
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    /**
    * Utility method for debugging OpenGL calls. Provide the name of the call
    * just after making it:
    *
    * <pre>
    * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
    * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
    *
    * If the operation is not successful, the check throws an error.
    *
    * @param glOperation - Name of the OpenGL call to check.
    */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    public float getMovementY() {
        return mMovementY;
    }

    public void setMovementY(float movementY) {
        mMovementY = movementY;
    }

	public float[] getProjectionMatrix() {
		return mProjectionMatrix;
	}

	public float[] getGridModelMatrix() {
		return mGridModelMatrix;
	}
	
	public float[] getFixedModelMatrix() {
		return mFixedModelMatrix;
	}

	public String getEquationText() {
		return mEquationText;
	}

	public void setEquationText(String mEquationText) {
		this.mEquationText = mEquationText;
	}

	public String getAnswerText() {
		return mAnswerText;
	}

	public void setAnswerText(String mAnswerText) {
		this.mAnswerText = mAnswerText;
	}

	public String getCurrentAnswer() {
		return mCurrentAnswer;
	}

	public void setCurrentAnswer(String mCurrentAnswer) {
		this.mCurrentAnswer = mCurrentAnswer;
	}

	public ArrayList<Shape> getShapes() {
		return shapes;
	}

	public ArrayList<Shape> getInputShapes() {
		return inputShapes;
	}

	public boolean isCorrectGuess() {
		return isCorrectGuess;
	}

	public void setCorrectGuess(boolean isCorrectGuess) {
		this.isCorrectGuess = isCorrectGuess;
	}

	public float getBottomRowScale() {
		return mBottomRowScale;
	}

	public void setBottomRowScale(float mBottomRowScale) {
		if (mBottomRowScale < 0) {
			return;
		}
		this.mBottomRowScale = mBottomRowScale;
	}

	public float getFPSMovementY() {
		return mFPSMovementY;
	}

	public void setFPSMovementY(float mFPSMovementY) {
		this.mFPSMovementY = mFPSMovementY;
	}

	public float getFPSBottomRowScale() {
		return mFPSBottomRowScale;
	}

	public void setFPSBottomRowScale(float mFPSBottomRowScale) {
		this.mFPSBottomRowScale = mFPSBottomRowScale;
	}

	public boolean isWrongGuess() {
		return isWrongGuess;
	}

	public void setWrongGuess(boolean isWrongGuess) {
		this.isWrongGuess = isWrongGuess;
	}

	public boolean isRenderOutput() {
		return renderOutput;
	}

	public void setRenderOutput(boolean renderOutput) {
		this.renderOutput = renderOutput;
	}
}