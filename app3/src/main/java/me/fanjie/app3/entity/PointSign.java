package me.fanjie.app3.entity;

import android.graphics.Paint;

/**
 * Created by dell on 2017/1/20.
 */

public class PointSign extends HoldableMapEntity {
    private static Paint centerPaint;

    static {
        centerPaint = new Paint(basePaint);
        centerPaint.setStyle(Paint.Style.STROKE);
        centerPaint.setStrokeWidth(5);
    }

    protected Vertex center;

    public PointSign(float x, float y) {
        center = new Vertex(x, y);
    }

    @Override
    public boolean hold(float x, float y) {
        return false;
    }

    @Override
    public void drawHolding() {

    }

    @Override
    public void draw() {
        float l = 30;
        canvas.drawLine(center.x, center.y + l, center.x, center.y - l, centerPaint);
        canvas.drawLine(center.x + l, center.y, center.x - l, center.y, centerPaint);
    }


    public Vertex getCenter() {
        return center;
    }


}
