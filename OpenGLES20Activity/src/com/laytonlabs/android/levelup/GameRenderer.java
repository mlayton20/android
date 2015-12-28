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
package com.laytonlabs.android.levelup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.laytonlabs.android.levelup.game.Cell;
import com.laytonlabs.android.levelup.game.CurrentAnswer;
import com.laytonlabs.android.levelup.game.Equation;
import com.laytonlabs.android.levelup.game.Game;
import com.laytonlabs.android.levelup.game.Level;
import com.laytonlabs.android.levelup.game.Score;
import com.laytonlabs.android.levelup.game.Stage;
import com.laytonlabs.android.levelup.game.Time;
import com.laytonlabs.android.levelup.shapes.Color;
import com.laytonlabs.android.levelup.shapes.EquationRectangle;
import com.laytonlabs.android.levelup.shapes.Hexagon;
import com.laytonlabs.android.levelup.shapes.InputSquare;
import com.laytonlabs.android.levelup.shapes.Shape;
import com.laytonlabs.android.levelup.shapes.StatsRectangle;
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
public class GameRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "GameRenderer";
    private static StatsRectangle levelRectangle;
    private static StatsRectangle timeRectangle;
    private static StatsRectangle scoreRectangle;
    private static EquationRectangle equationRectangle;
    private static EquationRectangle answerRectangle;
    private ArrayList<Shape> gridShapes;
    private ArrayList<Shape> bottomRowShapes;
    private static ArrayList<Shape> inputShapes;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mMVPFixed = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mGridModelMatrix = new float[16];
    private float[] mFixedModelMatrix = new float[16];
    private float[] mTempMatrix = new float[16];

    private float mMovementY;
    private float mBottomRowScale; 
    private static String mAnswerText = "";
    private boolean isCorrectGuess = false;
    private boolean renderCorrectGuess = false;
    private boolean isWrongGuess = false;
    private boolean renderOutput = false;
    private boolean isGameOver = false;
    
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
    
    private int gridLevel = 0; //The cell layout level in the grid.
    private int rowLevel = 1; //The current row in the grid user is selected.
    
    //Constants for grid presentation 
    private final float CELL_SCALE = 0.3f;
    private final float CELL_OFFSET_Y = 0.7f;
    
    private GameEventListener eventListener;

    public GameRenderer(GameEventListener eventListener) {
    	this.eventListener = eventListener;
	}

	@Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(Color.DARK_GREY[0], Color.DARK_GREY[1], Color.DARK_GREY[2], Color.DARK_GREY[3]);
        
        startTime = System.currentTimeMillis();        
        
        //Sets current answer to whatever the current answer is.
        mAnswerText = CurrentAnswer.getLabel();

        //Initialise fixed shapes
        equationRectangle = new EquationRectangle(-0.35f);
        answerRectangle = new EquationRectangle(-0.5f);
        
        equationRectangle.setShapes(0.2f, Equation.get());
        answerRectangle.setShapes(0.3f, mAnswerText);
        
        //TODO - Change the below calculations to be align_left, align_centre, align_right, etc.
        levelRectangle = new StatsRectangle(0 - (Screen.DEFAULT_WIDTH/3), Color.LIGHT_BLUE, Color.LIGHT_BLUE);
        timeRectangle = new StatsRectangle(0, Color.TURQUOISE, Color.TURQUOISE);
        scoreRectangle = new StatsRectangle(0 + (Screen.DEFAULT_WIDTH/3), Color.PURPLE, Color.PURPLE);
        
        levelRectangle.setShapes(-1f, Level.getLabel());
        scoreRectangle.setShapes(-1f, Score.getLabel());
        timeRectangle.setShapes(-1f, Time.getTimeRemainingLabel());
        
        setGridShapes();
        //Complete the bottom row for inputting guesses
        bottomRowShapes = new ArrayList<Shape>();
        setBottomRowShapes();
        buildInputGrid();
        setBottomRowScale(1.0f);
        setFPSBottomRowScale(getBottomRowScale() / FPS_ANIMATION_20);
        setFPSMovementY((CELL_OFFSET_Y*CELL_SCALE) / FPS_ANIMATION_20);
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
        
        //Update the timers by deducting timeRemaining
        Time.update();
        
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
        
        checkGameOver();
    }

	private void checkGameOver() {
		if (Time.isTimeUp() && !isGameOver) {
			eventListener.onGameOver();
			isGameOver = true;
		}
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
        	if (currentFrame >= FPS_ANIMATION_20) {            // if end of sequence
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
        	drawAllShapes(getGridShapes(), mMVPMatrix);
        }
	}
	
	private void drawAllShapes(ArrayList<Shape> shapes, float[] mMVPMatrix) {
		for (Shape shape : shapes) {
        	//Log.d(TAG, "Scale Hexagon ("+shape.toString()+") Aft ("+shape.getCentreX()+", "+shape.getCentreY()+")");
        	drawShapes(shape, mMVPMatrix);
        }
	}
	
	private void drawAllShapesAndShrinkBottomRow(float[] mMVPMatrix) {
		float[] mMVPScaled = mMVPMatrix.clone();
		
		Matrix.scaleM(mMVPScaled, 0, getBottomRowScale(), getBottomRowScale(), 0);
		
		int lastCellIndex = getBottomRowLastCellIndex();
		
		//Apply scaling to the bottom row and just move the other rows.
		for (int i = 0; i < getGridShapes().size(); i++) {
			if (i <= lastCellIndex) {
				drawShapes(getGridShapes().get(i), mMVPScaled);
			} else {
				drawShapes(getGridShapes().get(i), mMVPMatrix);
			}
		}
	}

	private void drawFixedShapes() {
		
		Matrix.multiplyMM(mMVPFixed, 0, mTempMatrix, 0, mFixedModelMatrix, 0);
		
		if (isRenderOutput()) {
			//Show the equation using the values from the selected cell.
	        equationRectangle.setShapes(0.2f, Equation.get());
	        answerRectangle.setShapes(0.3f, mAnswerText);
	        
	        setRenderOutput(false);
		}
		
		//Update the time if time has changed
		if (!timeRectangle.toString().equals(Time.getTimeRemainingLabel())) {
			//If the time remaining is almost up then change text to red.
			if (Time.isTimeAlmostUp()) {
				timeRectangle.setShapes(-1f, Time.getTimeRemainingLabel(), Color.RED);
			} else {
				timeRectangle.setShapes(-1f, Time.getTimeRemainingLabel());
			}
		}
		
		//Animation for changing color of the text
		if (isWrongGuess()) {
			if (outputCurrentFrame == 0) {
				answerRectangle.setShapes(0.3f, mAnswerText, Color.RED);
			}
			outputCurrentFrame++;
			if (outputCurrentFrame >= FPS_ANIMATION_10) {
				outputCurrentFrame = 0;
				setAnswerText("");
				answerRectangle.setShapes(0.3f, mAnswerText);
        		setWrongGuess(false);			
        	}
		} else if (renderCorrectGuess) {
			if (outputCurrentFrame == 0) {
				answerRectangle.setShapes(0.3f, mAnswerText, Color.GREEN);
				levelRectangle.setShapes(-1f, Level.getLabel(), Color.YELLOW);
				scoreRectangle.setShapes(-1f, Score.getLabel(), Color.YELLOW);
				timeRectangle.setShapes(-1f, Time.getTimeRemainingLabel(), Color.YELLOW);
			}
			outputCurrentFrame++;
			if (outputCurrentFrame >= FPS_ANIMATION_20) {
				outputCurrentFrame = 0;
				answerRectangle.setShapes(0.3f, mAnswerText);
				levelRectangle.setShapes(-1f, Level.getLabel());
				scoreRectangle.setShapes(-1f, Score.getLabel());
				timeRectangle.setShapes(-1f, Time.getTimeRemainingLabel());
				setGridShapes();
				renderCorrectGuess = false;		
        	}
		}

        drawShapes(answerRectangle, mMVPFixed);
        drawShapes(equationRectangle, mMVPFixed);
        drawShapes(levelRectangle, mMVPFixed);
        drawShapes(timeRectangle, mMVPFixed);
        drawShapes(scoreRectangle, mMVPFixed);
        
        //Draw all input grid shapess
        drawAllShapes(inputShapes, mMVPFixed);
	}
	
	public static void printStack() {
		Log.e(TAG,"Level: " + Level.getLabel());
		Log.e(TAG,"Score: " + Score.getLabel());
		Log.e(TAG,"Time: " + Time.getTimeRemainingLabel());
		Log.e(TAG,"mAnswerText: " + mAnswerText);
		Log.e(TAG,"Equation: " + Equation.get());
		for (int i = 0; i < inputShapes.size(); i++) {
			Log.e(TAG,"inputShapes[" + i + "]: " + inputShapes.get(i).toString());
		}
    }

	private void drawShapes(Shape parentShape, float[] mMVPMatrix) {
		parentShape.draw(mMVPMatrix);
		
		if (parentShape.getShapes() == null || parentShape.getShapes().isEmpty()) {
			return;
		}
		
        for (Shape nestedShapes : parentShape.getShapes()) {
        	Log.d(TAG,"NestedShape is: " + nestedShapes.toString());
    		nestedShapes.draw(mMVPMatrix);
    	}
	}
	
	private ArrayList<Shape> getGridShapes() {
		return gridShapes;
	}
	
	private int getBottomRowLastCellIndex() {
		int lastCellIndex = 0;
		Shape prevShape = null;
		for (int i = 0; i < getGridShapes().size(); i++) {
			if (prevShape == null || getGridShapes().get(i).getCentreY() == prevShape.getCentreY()) {
				lastCellIndex = i;
				prevShape = getGridShapes().get(i);
			} else {
				return lastCellIndex;
			}
		}
		return lastCellIndex;
	}
	
	private void removeBottomRow() {
		for (int i = getBottomRowLastCellIndex(); i >= 0 ; i--) {
			getGridShapes().remove(i);
		}
		//Reset the bottom row shapes
		setBottomRowShapes();
	}
	
	public ArrayList<Shape> getBottomRowShapes() {
		return bottomRowShapes;
	}
	
	private void setBottomRowShapes() {
		ArrayList<Shape> tempRowShapes = new ArrayList<Shape>();
		
		//Apply scaling to the bottom row and just move the other rows.
		for (int i = 0; i <= getBottomRowLastCellIndex(); i++) {
			tempRowShapes.add(getGridShapes().get(i));
		}
		bottomRowShapes = tempRowShapes;
	}

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);
        
        //Log.d("MyGLRenderer", "Width: " + width + " Height: " + height);

        float ratio = (float) width / height;
        
        Log.d("Screen","Width: "+ width +" - Height: "+ height +" - Ratio: "+ ratio);

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        if (ratio > 1) {
        	ratio = Screen.DEFAULT_LANDSCAPE_RATIO;
        } else {
        	ratio = Screen.DEFAULT_PORTRAIT_RATIO;
        }
        
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
            Log.e(TAG, glOperation + ": 1glError " + error);
            printStack();
            //TODO - Print out the fixed shapes values to see if something wierd is being displayed after a while.
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

	public String getAnswerText() {
		return mAnswerText;
	}

	public void setAnswerText(String guessInput) {
		if (guessInput == "") {
			this.mAnswerText = getInputUnderscores();
			return;
		}
		
		this.mAnswerText = this.mAnswerText.replaceFirst("_", guessInput);
	}
	
	public void resetAnswerText() {
		this.mAnswerText = CurrentAnswer.getLabel();
	}

	private String getInputUnderscores() {
		if (Equation.getExpectedAnswer() <= 0) {
			return "";
		}
		
		return Equation.getExpectedAnswerLabel().replaceAll("[0-9]", "_");
	}

	public ArrayList<Shape> getInputShapes() {
		return inputShapes;
	}
	
	private void setGridShapes() {
		//This needs to create the grid based on the cells in the grid.
		ArrayList<Shape> tempGrid = new ArrayList<Shape>();
		float xOffset;
		int fixedXOffset = 1;
		
		for (Stage stage : Game.getGrid()) {
			if (stage.getStageSize() == 4) {
				xOffset = -1.5f;
			} else {
				xOffset = -1f;
			}
			for (int i = 0; i < stage.getStageSize(); i++) {				
				tempGrid.add(new Hexagon(CELL_SCALE, xOffset+(fixedXOffset*i), CELL_OFFSET_Y*stage.getRow(), stage.getCell(i)));
			}
			
		}
		
		gridShapes = tempGrid;
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