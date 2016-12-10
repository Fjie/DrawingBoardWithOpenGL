package me.fanjie.drawingboardwithopengl;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import me.fanjie.drawingboardwithopengl.drawable.Cube;
import me.fanjie.drawingboardwithopengl.drawable.MyDrawable;

/**
 * @Description: TODO 添加类的描述
 * @author: fanjie
 * @date: 2016/12/10 14:58
 */

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private MyDrawable drawable;

    public MyGLRenderer() {
        this.drawable = new Cube();
    }

    public void onMove(float x,float y){

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        drawable.draw(null);
    }
}
