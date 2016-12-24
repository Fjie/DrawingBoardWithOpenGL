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

/**
 * @Description: TODO 添加类的描述
 * @author: fanjie
 * @date: 2016/12/10 14:58
 */

public class MyGLRenderer implements GLSurfaceView.Renderer {
    public Set<DrawLine> drawLines;
    public DrawLine drawLine;
    public VariableShape shape;
    public FontTexDemo fontTexDemo;
    public MyGLRenderer() {

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        drawLines = new HashSet<>();
        drawLine = new DrawLine();
        shape = new VariableShape();
        fontTexDemo = new FontTexDemo();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        if(fontTexDemo != null){
            fontTexDemo.updateViewSize(width,height);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        fontTexDemo.drawing();
//        shape.drawing();
//        for (DrawLine l : drawLines) {
//            l.draw();
//        }
//        MLog.d("drawLines = "+drawLines.size());
    }
}
