package me.fanjie.app2;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by dell on 2016/12/27.
 */

public class MyGLSurface extends GLSurfaceView {
    private MyRenderer myRenderer;
    public MyGLSurface(Context context) {
        this(context,null);
    }

    public MyGLSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
    }

    public void setMyRenderer(MyRenderer myRenderer) {
        this.myRenderer = myRenderer;
        setRenderer(myRenderer);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        myRenderer.onPanelTouch(event.getAction(),toX(event.getX()),toY(event.getY()));
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
