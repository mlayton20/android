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
package com.laytonlabs.android.levelup.shapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import android.opengl.GLES20;

import com.laytonlabs.android.levelup.MyGLRenderer;
import com.laytonlabs.android.levelup.Vec2;
import com.laytonlabs.android.levelup.game.Cell;

/**
 * A two-dimensional square for use as a drawn object in OpenGL ES 2.0.
 */
public abstract class Shape {
	
	private final String TAG = "Shape";

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "void main() {" +
            // The matrix must be included as a modifier of gl_Position.
            // Note that the uMVPMatrix factor *must be first* in order
            // for the matrix multiplication product to be correct.
            "  gl_Position = uMVPMatrix * vPosition;" +
            "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}";

    private final FloatBuffer vertexBuffer;
    private final ShortBuffer drawListBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;
    protected float[] shapeCoords;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    
    //These will be initiated by the abstract class
    private final float[] ORIGINAL_COORDS;
    private final short[] DRAW_ORDER; // order to draw vertices
    
    //#RGB: white (255, 255, 255)
    private final float[] COLOR;
    
    //Sets the scale of the shape and where the X centre is.
    private final float SCALE;
    private final float CENTRE_X;
    private final float CENTRE_Y;
    
    public abstract float getCentreX();
    public abstract float getCentreY();
    
    public float[] getNestedTextColor() {
    	return null;
    }
    
    public void setNestedTextColor(float[] textColor) {}
    
    public Cell getCell() {
    	return null;
    }
    
    public ArrayList<Shape> getShapes() {
    	return null;
    }
    
    public void setShapes(float scale, String nestedText) {}
    
    public void setShapes(float scale, String nestedText, float[] textColor) {}
    
    public boolean intersects(Vec2 touchCoords) {
    	return false;
    }
    
    public float getMinX() {return getMin(getArraySubset(0));}
	public float getMaxX() {return getMax(getArraySubset(0));}
    public float getMinY() {return getMin(getArraySubset(1));}
    public float getMaxY() {return getMax(getArraySubset(1));}
    
    private float getMin(float[] values) {
    	float minVal = 1000f;
    	for (float value : values) {
    		if (value < minVal) {
    			minVal = value;
    		}
    	}
		return minVal;
	}
    
    private float getMax(float[] values) {
    	float maxVal = -1000f;
    	for (float value : values) {
    		if (value > maxVal) {
    			maxVal = value;
    		}
    	}
		return maxVal;
	}
    
    private float[] getArraySubset(int offset) {
    	if (shapeCoords == null || shapeCoords.length == 0) {
    		return null;
    	}
    	
    	float[] subsetArray = new float[shapeCoords.length / COORDS_PER_VERTEX];
    	int subsetIndex = 0;
    	
    	for (int i = offset; i < shapeCoords.length; i=(i+COORDS_PER_VERTEX)) {
    		subsetArray[subsetIndex] = shapeCoords[i];
    		subsetIndex++;
    	}
    	return subsetArray;
    }
    
    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public Shape(float[] originalCoords, short[] drawOrder, float[] color, 
    		float scale, float centreX, float centreY) {
    	
    	this.ORIGINAL_COORDS = originalCoords;
    	this.DRAW_ORDER = drawOrder;
    	this.COLOR = color;
    	this.SCALE = scale;
    	this.CENTRE_X = centreX;
    	this.CENTRE_Y = centreY;
    	
    	this.shapeCoords = ORIGINAL_COORDS.clone();
    	
    	adjustShape(scale, centreX, centreY);
    	//Resize based on the scale
    	//adjustSize(scale);
    	
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
        // (# of coordinate values * 4 bytes per float)
                shapeCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(shapeCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                DRAW_ORDER.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(DRAW_ORDER);
        drawListBuffer.position(0);

        // prepare shaders and OpenGL program
        int vertexShader = MyGLRenderer.loadShader(
                GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(
                GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
    }

    //Adjust the original scale of the shape and position
    private void adjustShape(float scale, float centreX, float centreY) {
    	for (int i = 0; i < shapeCoords.length; i++) {
    		//Apply the scale
    		shapeCoords[i] = (ORIGINAL_COORDS[i] * scale);
    		
    		//Apply the x offset
    		shapeCoords[i] += (i % 3 == 0 ? centreX : 0);
    		
    		//Apply the y offset
    		shapeCoords[i] += (i % 3 == 1 ? centreY : 0);
		}
	}

	/**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param mvpMatrix - The Model View Project matrix in which to draw
     * this shape.
     */
    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, COLOR, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyGLRenderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");

        // Draw the square
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, DRAW_ORDER.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}