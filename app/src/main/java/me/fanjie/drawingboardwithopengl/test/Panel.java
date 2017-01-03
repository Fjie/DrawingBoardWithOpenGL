package me.fanjie.drawingboardwithopengl.test;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;

import me.fanjie.drawingboardwithopengl.test.mapping.Mapper;

/**
 * Created by dell on 2016/12/24.
 */

public class Panel extends GLSurfaceView {

    private Mapper mapper;

    public Panel(Context context) {
        this(context,null);
    }

    public Panel(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        setDebugFlags(GLSurfaceView.DEBUG_CHECK_GL_ERROR
                | GLSurfaceView.DEBUG_LOG_GL_CALLS);
    }

    public void setMapper(@NonNull Mapper mapper) {
        this.mapper = mapper;
        setRenderer(mapper.getRenderer());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mapper.onPanelTouch(event.getAction(),toX(event.getX()),toY(event.getY()));
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
