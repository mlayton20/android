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

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

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
    private static EquationRectangle equationRectangle;
    private ArrayList<Shape> shapes;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mModelMatrix = new float[16];
    private float[] mEquationModelMatrix = new float[16];
    private float[] mTempMatrix = new float[16];

    private float mAngle;
    private String mCurrentAnswer = "11";
    private String mEquationText = mCurrentAnswer;

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        
        equationRectangle = new EquationRectangle();
        shapes = new ArrayList<Shape>();
        buildThreeCells();
        buildFourCells();
    }

	private void buildThreeCells() {
		shapes.add(new Hexagon(0.3f,     0, 0.7f, "+12"));
        shapes.add(new Hexagon(0.3f, -1.0f, 0.7f, "+21"));
        shapes.add(new Hexagon(0.3f,  1.0f, 0.7f, "+2"));
	}
	
	private void buildFourCells() {
		shapes.add(new Hexagon(0.3f, -1.5f, 0, "+11"));
        shapes.add(new Hexagon(0.3f, -0.5f, 0, "+1"));
        shapes.add(new Hexagon(0.3f,  0.5f, 0, "+22"));
        shapes.add(new Hexagon(0.3f,  1.5f, 0, "+12"));
	}

    @Override
    public void onDrawFrame(GL10 unused) {
    	float[] mMVPEquation = new float[16];
        
        Matrix.setIdentityM(mModelMatrix, 0); // initialize to identity matrix

        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        
        //Setup the equation display before we start moving the grid around
    	mEquationModelMatrix = mModelMatrix.clone();
		Matrix.translateM(mEquationModelMatrix, 0, 0, -0.8f, 0);
		mTempMatrix = mMVPMatrix.clone();

        // Create a rotation for the triangle

        // Use the following code to generate constant rotation.
        // Leave this code out when using TouchEvents.
        // long time = SystemClock.uptimeMillis() % 4000L;
        // float angle = 0.090f * ((int) time);
        
        //Start the grid drawing at bottom of screen.
        Matrix.translateM(mModelMatrix, 0, 0, -0.5f, 0);

        //Move the grid down or up the screen depending on touch events.
        Matrix.translateM(mModelMatrix, 0, 0, mAngle, 0);

        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        //Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);
        
        //Add the movement to the matrix
        mTempMatrix = mMVPMatrix.clone();
        Matrix.multiplyMM(mMVPMatrix, 0, mTempMatrix, 0, mModelMatrix, 0);
        
        //Draw all shapes
        for (Shape shape : shapes) {
        	shape.draw(mMVPMatrix);
        	
        	//Don't draw nested shapes if there are none.
        	if (shape.getShapes() == null)
        		continue;
        	
        	//Draw the nested shapes
        	for (Shape nestedShapes : shape.getShapes()) {
        		nestedShapes.draw(mMVPMatrix);
        	}
        }
        
        //Show the equation using the values from the selected cell.
        Matrix.multiplyMM(mMVPEquation, 0, mTempMatrix, 0, mEquationModelMatrix, 0);
        
        equationRectangle.setShapes(mEquationText);
        equationRectangle.draw(mMVPEquation);
        for (Shape nestedShapes : equationRectangle.getShapes()) {
    		nestedShapes.draw(mMVPEquation);
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

    /**
     * Returns the rotation angle of the triangle shape (mTriangle).
     *
     * @return - A float representing the rotation angle.
     */
    public float getAngle() {
        return mAngle;
    }

    /**
     * Sets the rotation angle of the triangle shape (mTriangle).
     */
    public void setAngle(float angle) {
        mAngle = angle;
    }

	public float[] getProjectionMatrix() {
		return mProjectionMatrix;
	}

	public float[] getModelMatrix() {
		return mModelMatrix;
	}
	
	public Shape getTouchedShape(Vec2 touchGLCoords) {
		for (Shape shape : shapes) {
			if (shape.intersects(touchGLCoords)) {
				return shape;
			}
		}
		return null;
	}

	public String getEquationText() {
		return mEquationText;
	}

	public void setEquationText(String mEquationText) {
		this.mEquationText = mEquationText;
	}

	public String getCurrentAnswer() {
		return mCurrentAnswer;
	}

	public void setCurrentAnswer(String mCurrentAnswer) {
		this.mCurrentAnswer = mCurrentAnswer;
	}
}