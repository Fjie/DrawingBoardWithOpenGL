package me.fanjie.app3.entity;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;

import static me.fanjie.app3.BMath.getL;

/**
 * Created by dell on 2016/12/17.
 */

public class Vertex extends HoldableMapEntity {
    private static final float ANGEL_RADIUS = 40;
    private static final float RADIUS = 15;

    private static Paint holdenPaint;
    private static Paint angelPaint;
    private static TextPaint angelTextPaint;

    static {
        holdenPaint = new Paint(basePaint);
        holdenPaint.setColor(Color.RED);
        angelTextPaint = new TextPaint();
        angelTextPaint.setTextSize(25);
        angelPaint = new Paint(basePaint);
        angelPaint.setColor(Color.GRAY);
        angelPaint.setStyle(Paint.Style.STROKE);
        angelPaint.setStrokeWidth(8);
    }

    public float x;
    public float y;
    public float z;

    public Vertex v;
    public Vertex h;

    public int position;
    private Integer angel;

    public Vertex(float x, float y, int position) {
        this.x = x;
        this.y = y;
        this.position = position;
    }

    public Vertex(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setAngel(Integer angel) {
        this.angel = angel;
    }

    public void setNeighbor(Vertex v) {
        if (v.x == x) {
            this.v = v;
        } else if (v.y == y) {
            this.h = v;
        }
    }
    @Override
    public boolean hold(float x, float y) {
        return getL(this.x, this.y, x, y) < 40;
    }

    @Override
    public void drawHolding() {
        canvas.drawCircle(x, y, RADIUS, holdenPaint);
    }

    @Override
    public void draw() {
        if (angel != null) {
            drawAngel();
        }
    }

    private void drawAngel() {
        Path path = new Path();
        double hS = ANGEL_RADIUS / getL(x, y, h.x, h.y);
        double vS = ANGEL_RADIUS / getL(x, y, v.x, v.y);
        float startX = (float) (x + (h.x - x) * hS);
        float startY = (float) (y + (h.y - y) * hS);
        float stopX = (float) (x + (v.x - x) * vS);
        float stopY = (float) (y + (v.y - y) * vS);
        float assesX = x + (startX - stopX);
        float assesY = y + (stopY - startY);
        path.moveTo(startX, startY);
        path.quadTo(assesX, assesY, stopX, stopY);
        canvas.drawPath(path, angelPaint);
        canvas.drawText(angel + "°", assesX, assesY, angelTextPaint);
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "x=" + x +
                ", y=" + y +
                ", position=" + position +
                '}';
    }
}
