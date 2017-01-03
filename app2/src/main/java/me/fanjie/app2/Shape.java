package me.fanjie.app2;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static me.fanjie.app2.C.fragmentShaderCode;
import static me.fanjie.app2.C.vertexShaderCode;
import static me.fanjie.app2.GLToolbox.createProgram;

/**
 * Created by dell on 2016/12/27.
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
        mProgram = createProgram(vertexShaderCode,fragmentShaderCode);
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

    public void setVertexCoords(float[] coords) {
        count = coords.length / 3;
        vertexBuffer.clear();
        vertexBuffer.put(coords);
        vertexBuffer.position(0);
    }
}
