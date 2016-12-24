package me.fanjie.drawingboardwithopengl.drawable;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;

import me.fanjie.drawingboardwithopengl.MLog;

import static me.fanjie.drawingboardwithopengl.C.fragmentShaderCode;
import static me.fanjie.drawingboardwithopengl.C.vertexShaderCode;
import static me.fanjie.drawingboardwithopengl.OpenGLUtlis.loadShader;

/**
 * Created by dell on 2016/12/12.
 */

public class DrawLine {

    private FloatBuffer vertexBuffer;
    public static float lineCoords[] ={
            -1,  0, 0.0f,
            1, 0, 0.0f
    };
    float color[] = { 1f, 1f, 1f, 1f };
    private int mProgram;
    static final int COORDS_PER_VERTEX = 3;

    int vertexShader;
    int fragmentShader;

    public DrawLine() {
        ByteBuffer bb = ByteBuffer.allocateDirect(lineCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(lineCoords);
        vertexBuffer.position(0);

        vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    private int mPositionHandle;
    private int mColorHandle;

    private final int vertexCount = lineCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    public void draw() {
        MLog.d(Arrays.toString(lineCoords));
        GLES20.glUseProgram(mProgram);
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        GLES20.glLineWidth(10);
        GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, vertexCount);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    public void setStart(float x,float y){
        lineCoords[0] = x;
        lineCoords[1] = y;
        lineCoords[2] = 0;
    }
    public void setStop(float x,float y){
        lineCoords[3] = x;
        lineCoords[4] = y;
        lineCoords[5] = 0;

        vertexBuffer.put(lineCoords);
        vertexBuffer.position(0);
    }


}
