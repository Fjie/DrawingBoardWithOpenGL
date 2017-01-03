package me.fanjie.drawingboardwithopengl.test.renderer;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static me.fanjie.drawingboardwithopengl.C.fragmentShaderCode;
import static me.fanjie.drawingboardwithopengl.C.vertexShaderCode;
import static me.fanjie.drawingboardwithopengl.OpenGLUtlis.loadShader;

/**
 * Created by dell on 2016/12/24.
 */

public class Shape implements IDrawable {
    private FloatBuffer vertexBuffer;
    private int mProgram;
    private float color[] = {1f, 1f, 1f, 1f};
    private int count;

    public Shape() {
        ByteBuffer bb = ByteBuffer.allocateDirect(100 * 3 * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    @Override
    public void drawing() {
        GLES20.glUseProgram(mProgram);
        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glVertexAttribPointer(mPositionHandle, 3,
                GLES20.GL_FLOAT, false,
                3 * 4, vertexBuffer);

        int mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        GLES20.glLineWidth(10);
        GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, count);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    @Override
    public void putVertexCoords(float[] coords) {
        count = coords.length / 3;
        vertexBuffer.put(coords);
        vertexBuffer.position(0);
    }
}
