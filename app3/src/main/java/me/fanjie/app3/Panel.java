package me.fanjie.app3;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import me.fanjie.app3.test.mapping.Mapper;

/**
 * Created by dell on 2016/12/29.
 */

public class Panel extends View {

    public Panel(Context context) {
        this(context, null);
    }

    public Panel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Mapper mapper;


    public void setMapper(@NonNull Mapper mapper) {
        this.mapper = mapper;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(dx2 + dx, dy2 + dy);
        if (scale > 0) {
            Log.d("XXX", "scale = " + scale);
            Log.d("XXX", "px = " + px + ",py = " + py);
            canvas.scale(scale, scale, px, py);
        }
        if (mapper != null) {
            mapper.drawing(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_UP) {
            scaling = false;
        }
//        事件先分配给制图
        if (event.getPointerCount() == 1 && !scaling) {
            float x = (event.getX() - dx2 - dx - px) / scale + px;
            float y = (event.getY() - dy2 - dy - py) / scale + py;
            if (mapper.onPanelTouch(event.getAction(), x, y)) {
                return true;
            }
        }
//            若未被return则开始画布操作
        if (event.getPointerCount() == 1) {
            if (scaling) {
                return true;
            }
//            单指平移
            translateTouch(event);
        } else {
//            多指缩放
            scaleTouch(event);
        }
        return true;
    }


    private double startFingerDir;
    private float scale = 1;
    private float px;
    private float py;
    private boolean scaling;

    private void scaleTouch(MotionEvent event) {
        scaling = true;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_DOWN: {
                if (event.getPointerCount() == 2) {
                    startFingerDir = BMath.getL(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
                    px = event.getX(0) + (event.getX(1) - event.getX(0)) / 2;
                    py = event.getY(0) + (event.getY(1) - event.getY(0)) / 2;
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                double stopFingerDir = BMath.getL(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
                double v = stopFingerDir / startFingerDir;
                scale *= (float) v;
                scale = (float) Math.sqrt(scale);
                if (scale > 4) {
                    scale = 4;
                } else if (scale < 0.25) {
                    scale = 0.25f;
                }
                invalidate();
                break;
            }
        }
    }

    private float dx;
    private float dy;
    private float dx2;
    private float dy2;
    private float startX;
    private float startY;

    private void translateTouch(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                startX = x;
                startY = y;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                dx = x - startX;
                dy = y - startY;
                invalidate();
                break;
            }
            case MotionEvent.ACTION_UP: {
                dx2 += dx;
                dy2 += dy;
                dx = 0;
                dy = 0;
                break;
            }

        }
    }

}
