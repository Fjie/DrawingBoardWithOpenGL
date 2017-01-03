package me.fanjie.drawingboardwithopengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import me.fanjie.drawingboardwithopengl.drawable.DrawLine;

/**
 * Created by dell on 2016/12/13.
 */
public class MyGLSurfaceView extends GLSurfaceView {

    public MyGLRenderer myGLRenderer;
    public DrawLine drawLine;

    public MyGLSurfaceView(Context context) {
        this(context,null);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        myGLRenderer = new MyGLRenderer();
        setRenderer(myGLRenderer);
//        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = toX(event.getX());
        float y = toY(event.getY());
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                myGLRenderer.variableShape.down(x,y);
                myGLRenderer.fontTexDemo.down(x,y);
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                myGLRenderer.variableShape.move(x,y);
                myGLRenderer.fontTexDemo.move(x,y);
                break;
            }
            case MotionEvent.ACTION_UP:{
                myGLRenderer.variableShape.up(x,y);
                myGLRenderer.fontTexDemo.up(x,y);
                break;
            }
        }
        return true;
    }
    public boolean onTouchEventXX(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                if(drawLine == null){
                    drawLine = myGLRenderer.drawLine;
                }
                drawLine.setStart(toX(x),toY(y));
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                drawLine.setStop(toX(x),toY(y));
                myGLRenderer.drawLines.add(drawLine);
                requestRender();
                break;
            }
            case MotionEvent.ACTION_UP:{
                break;
            }

        }
        return true;
    }

    private float toX(float x){
        int w = getWidth() / 2;
        return (x-w)/w;
    }
    private float toY(float y){
        int h = getHeight()/2;
        return (h-y)/h;
    }
}
