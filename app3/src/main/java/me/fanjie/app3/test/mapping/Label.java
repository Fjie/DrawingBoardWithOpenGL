package me.fanjie.app3.test.mapping;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;

import static me.fanjie.app3.BMath.getL;

/**
 * Created by dell on 2017/1/3.
 */

public class Label {
    private Vertex start;
    private Vertex stop;
    private Type type;

    private float startX;
    private float startY;
    private float stopX;
    private float stopY;
    private Path mPath;
    private Path startPath;
    private Path stopPath;

    public Label(Vertex start, Vertex stop, Type type) {
        this.start = start;
        this.stop = stop;
        this.type = type;
        init();
    }

    /**
     * 在创建时初始化，在所依附的形状改变后需要调用
     */
    public void init() {
        float dX = 0;
        float dY = -50;
        switch (type) {
            case EDGE: {
                startX = start.x + dX;
                startY = start.y + dY;
                stopX = stop.x + dX;
                stopY = stop.y + dY;
                break;
            }
            case HOR: {
                float minY = start.y < stop.y ? start.y : stop.y;
                startX = start.x;
                startY = minY + dY;
                stopX = stop.x;
                stopY = minY + dY;
                break;
            }
            case VER: {
                startX = start.x + dX;
                startY = start.y + dY;
                stopX = stop.x + dX;
                stopY = stop.y + dY;
                break;
            }

        }
        initPath();
    }

    private void initPath() {
        mPath = new Path();
        mPath.moveTo(startX, startY);
        mPath.lineTo(stopX, stopY);
        startPath = new Path();
        startPath.moveTo(startX, startY - 20);
        startPath.lineTo(startX, startY + 20);
        stopPath = new Path();
        stopPath.moveTo(stopX, stopY - 20);
        stopPath.lineTo(stopX, stopY + 20);
    }


    public void drawLabel(Canvas canvas, Paint labelPaint, TextPaint textPaint) {
        canvas.drawPath(mPath, labelPaint);
        canvas.drawPath(startPath, labelPaint);
        canvas.drawPath(stopPath, labelPaint);
        canvas.drawTextOnPath(String.valueOf(getLength()), mPath, getLength() / 2f - 50, -20, textPaint);
    }

    public int getLength() {
        double length = 0;
        switch (type) {
            case EDGE: {
                length = getL(start.x, start.y, stop.x, stop.y);
                break;
            }
            case HOR: {
                length = Math.abs(start.x - stop.x);
                break;
            }
            case VER: {
                length = Math.abs(start.y - stop.y);
            }
        }
        return (int) length;

    }

    public enum Type {
        EDGE, HOR, VER
    }
}
