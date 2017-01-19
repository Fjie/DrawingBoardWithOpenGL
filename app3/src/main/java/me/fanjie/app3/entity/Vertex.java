package me.fanjie.app3.entity;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.Log;

import static me.fanjie.app3.BMath.getL;

/**
 * Created by dell on 2016/12/17.
 */

public class Vertex {

    public float x;
    public float y;
    public float z;

    public Vertex v;
    public Vertex h;

    public int position;
    private Integer angel;
    private int startAngel;

    public Vertex(float x, float y, int position) {
        this.x = x;
        this.y = y;
        this.position = position;
    }



    public void setAngel(Integer angel) {
        this.angel = angel;
    }

    public void drawCircle(Canvas canvas, float radius, Paint paint) {
        canvas.drawCircle(x, y, radius, paint);
    }

    public void drawAngel(Canvas canvas, float radius, Paint paint, TextPaint textPaint) {
        if(angel != null) {
            Path path = new Path();
            double hS = radius / getL(x, y, h.x, h.y);
            double vS = radius / getL(x, y, v.x, v.y);
            float startX = (float) (x + (h.x - x) * hS);
            float startY = (float) (y + (h.y - y) * hS);
            float stopX = (float) (x + (v.x - x) * vS);
            float stopY = (float) (y + (v.y - y) * vS);
            float assesX = x + (startX - stopX);
            float assesY = y + (stopY - startY);
            path.moveTo(startX, startY);
            path.quadTo(assesX, assesY, stopX, stopY);
            canvas.drawPath(path, paint);
            double start = Math.atan((h.y - y) / (h.x - x));
//            start = Math.abs(start);
            double stop = Math.atan((v.y - y) / (v.x - x)) - start;
            long round = Math.round(Math.toDegrees(stop));
            canvas.drawText(angel + "°", x + radius, y - radius, textPaint);
//            canvas.drawTextOnPath(angel.toString(), path, 0, 0, textPaint);
        }
    }

    public void drawAngel2(Canvas canvas, float radius, Paint paint, TextPaint textPaint) {
        Path path = new Path();
        double a = Math.atan((v.y - y) / (v.x - x));
        float ac = (float) (Math.sin(a) * radius);
        float bc = (float) (Math.cos(a) * radius);
        path.moveTo(x + ac, y + bc);
        double a1 = Math.atan((h.y - y) / (h.x - x));
        float a1c1 = (float) (Math.sin(a1) * radius);
        float b1c1 = (float) (Math.cos(a1) * radius);
        path.lineTo(x + a1c1, y + b1c1);
        canvas.drawPath(path, paint);
        canvas.drawTextOnPath("xxx", path, 0, 0, textPaint);
    }

    public void drawAngel3(Canvas canvas, float radius, Paint paint, TextPaint textPaint) {
//        if (angel != null) {
        if (true) {
            double start = Math.toDegrees(Math.atan((h.y - y) / (h.x - x)));
            double stop =  Math.toDegrees(Math.atan((v.y - y) / (v.x - x)));
            Log.d("XXX", "position = "+position+",start = " + start + ",stop = " + stop);
            if("-0.0".equals(start+"") ){
                stop = -stop;
                start = 180 - start;
            }
            float assess = (float) (stop - start);
            Log.d("XXX", "position = "+position+",start = " + start + ",stop = " + stop);
            RectF rectF = new RectF(x - radius, y - radius, x + radius, y + radius);
            canvas.drawArc(rectF, (float) start, assess, true, paint);
            canvas.drawText(angel + "°", x + radius, y - radius, textPaint);
        }
    }


    public void setNeighbor(Vertex v) {
        if (v.x == x) {
            this.v = v;
        } else if (v.y == y) {
            this.h = v;
        }
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "x=" + x +
                ", y=" + y +
                ", position=" + position +
                '}';
    }


    public void setStartAngel(int startAngel) {
        this.startAngel = startAngel;
    }
}
