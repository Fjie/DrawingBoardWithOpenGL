package me.fanjie.drawingboardwithopengl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.util.HashSet;
import java.util.Set;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import me.fanjie.drawingboardwithopengl.drawable.DrawLine;
import me.fanjie.drawingboardwithopengl.drawable.FontTexDemo;
import me.fanjie.drawingboardwithopengl.drawable.VariableShape;
import me.fanjie.drawingboardwithopengl.test.renderer.Shape;

/**
 * @Description: TODO 添加类的描述
 * @author: fanjie
 * @date: 2016/12/10 14:58
 */

public class MyGLRenderer implements GLSurfaceView.Renderer {

    public Set<DrawLine> drawLines;
    public DrawLine drawLine;
    public VariableShape variableShape;
    public FontTexDemo fontTexDemo;
    public Shape shape;


    public MyGLRenderer() {

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        drawLines = new HashSet<>();
        drawLine = new DrawLine();
        variableShape = new VariableShape();
        fontTexDemo = new FontTexDemo();
        shape = new Shape();
    }



    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        fontTexDemo.drawing();
        variableShape.drawing();
//        for (DrawLine l : drawLines) {
//            l.draw();
//        }
//        MLog.d("drawLines = "+drawLines.size());
    }
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        if (fontTexDemo != null) {
            fontTexDemo.updateViewSize(width, height);
        }
    }
}
