package me.fanjie.app3.entity;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import me.fanjie.app3.ShapeUtils;
import me.fanjie.app3.mapping.MapHelper;

/**
 * Created by dell on 2017/1/7.
 */

public class SideWall {

    private Type type;


    private float startX;
    private float startY;
    private float stopX;
    private float stopY;

    private Path path;

    public SideWall(Vertex start, Vertex stop, Type type) {

        this.type = type;
        float l = 20;
        float hS = (start.h.x - start.x) / Math.abs(start.h.x - start.x);
        float vS = (start.v.y - start.y) / Math.abs(start.v.y - start.y);

        startX = start.x + l * hS;
        startY = start.y + l * vS;

        if (!ShapeUtils.pointInPath(startX, startY, MapHelper.getInstance().cMap.shapePath)) {
            startX = start.x + -l * hS;
            startY = start.y + -l * vS;
        }

        float hS1 = (stop.h.x - stop.x) / Math.abs(stop.h.x - stop.x);
        float vS1 = (stop.v.y - stop.y) / Math.abs(stop.v.y - stop.y);

        stopX = stop.x + l * hS1;
        stopY = stop.y + l * vS1;

        if (!ShapeUtils.pointInPath(stopX, stopY, MapHelper.getInstance().cMap.shapePath)) {
            stopX = stop.x + -l * hS1;
            stopY = stop.y + -l * vS1;
        }

        path = new Path();
        path.moveTo(startX, startY);
        path.lineTo(stopX, stopY);
    }

    public void draw(Canvas canvas, Paint downPaint, Paint upPaint) {
        if (type == Type.UP) {
            canvas.drawPath(path, upPaint);
        } else {
            canvas.drawPath(path, downPaint);
        }
    }


    public enum Type {
        UP, DOWN, BOTH
    }
}
