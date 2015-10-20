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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

/**
 * A two-dimensional square for use as a drawn object in OpenGL ES 2.0.
 */
public class NumTwo {
	
	private final String TAG = "NumTwo";

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
    
    private final float SCALE;
    private final float CENTRE;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float originalCoords[] = {
        -0.225f,  0.5f,   0.0f,   //0
        -0.225f,  0.35f,  0.0f,   //1
         0.225f,  0.35f,  0.0f,   //2
         0.225f,  0.5f,   0.0f,   //3
        -0.3f,    0.425f, 0.0f,   //4
        -0.3f,    0.275f, 0.0f,   //5
        -0.15f,   0.275f, 0.0f,   //6
        -0.225f,  0.425f, 0.0f,   //7
         0.225f,  0.425f, 0.0f,   //8
        -0.15f,   0.425f, 0.0f,   //9
         0.15f,   0.425f, 0.0f,   //10
         0.15f,   0.0f,   0.0f,   //11
         0.3f,    0.0f,   0.0f,   //12
         0.3f,    0.425f, 0.0f,   //13
        -0.225f,  0.075f, 0.0f,   //14
        -0.225f, -0.075f, 0.0f,   //15
         0.225f, -0.075f, 0.0f,   //16
         0.225f,  0.075f, 0.0f,   //17
         0.225f,  0.0f,   0.0f,   //18
        -0.225f,  0.0f,   0.0f,   //19
        -0.3f,    0.0f,   0.0f,   //20
        -0.3f,   -0.35f,  0.0f,   //21
        -0.15f,  -0.35f,  0.0f,   //22
        -0.15f,   0.0f,   0.0f,   //23
        -0.3f,   -0.5f,   0.0f,   //24
         0.3f,   -0.5f,   0.0f,   //25
         0.3f,   -0.35f,  0.0f,   //26
         0.15f,  -0.275f, 0.0f,   //27
         0.15f,  -0.35f,  0.0f,   //28
         0.3f,   -0.275f, 0.0f }; //29
    static float[] shapeCoords = originalCoords.clone();

    private final short drawOrder[] = { 0,1,3,3,1,2,0,4,7,4,5,9,9,5,6,3,8,13,10,11,13,13,11,12,18,16,12,14,15,17,17,15,16,14,20,19,20,21,23,23,21,22,21,24,26,26,24,25,27,28,29,29,28,26 }; // order to draw vertices

    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    //#RGB: white (255, 255, 255)
    float color[] = { 1.0f, 1.0f, 1.0f, 1.0f };

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public NumTwo(float scale, float centre) {
    	this.SCALE = scale;
    	this.CENTRE = centre;
    	
    	adjustShape(scale, centre);
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
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
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
    private void adjustShape(float scale, float centre) {
    	for (int i = 0; i < shapeCoords.length; i++) {
    		shapeCoords[i] = (originalCoords[i] * scale) + (i % 3 == 0 ? centre * scale : 0);
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
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyGLRenderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");

        // Draw the square
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

}