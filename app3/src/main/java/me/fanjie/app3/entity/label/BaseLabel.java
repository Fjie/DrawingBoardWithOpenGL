package me.fanjie.app3.entity.label;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;

import me.fanjie.app3.CPath;
import me.fanjie.app3.entity.HoldableMapEntity;
import me.fanjie.app3.entity.Vertex;

import static me.fanjie.app3.BMath.getL;

/**
 * Created by dell on 2017/2/6.
 */

public abstract class BaseLabel extends HoldableMapEntity {

    protected static Paint labelPaint;
    protected static Paint holdenLabelPaint;
    protected static TextPaint textPaint;
    protected static TextPaint holdenTextPaint;

    static {
        labelPaint = new Paint(basePaint);
        labelPaint.setStyle(Paint.Style.STROKE);
        labelPaint.setStrokeWidth(2);
        holdenLabelPaint = new Paint(labelPaint);
        holdenLabelPaint.setColor(Color.RED);
        textPaint = new TextPaint();
        textPaint.setTextSize(30);
        holdenTextPaint = new TextPaint(textPaint);
        holdenTextPaint.setColor(Color.RED);
    }

    protected Vertex start;
    protected Vertex stop;

    protected float startX;
    protected float startY;
    protected float stopX;
    protected float stopY;

    protected CPath path;
    protected CPath startPath;
    protected CPath stopPath;

    private int hOffset;

    public BaseLabel(Vertex start, Vertex stop) {
        this.start = start;
        this.stop = stop;
    }

    public abstract void init();

    @Override
    public boolean hold(float x, float y) {
        float x1 = startX + (stopX - startX) / 2;
        float y1 = startY + (stopY - startY) / 2;
        return getL(x, y, x1, y1) < 50;
    }

    @Override
    public void draw() {
        canvas.drawPath(path, labelPaint);
        String length = String.valueOf(getLength());
        Rect rect = new Rect();
        textPaint.getTextBounds(length,0,length.length(),rect);
        hOffset = getLength()/2 - rect.width()/2;
        canvas.drawTextOnPath(length, path, hOffset, -15, textPaint);
    }


    @Override
    public void drawHolding() {
        canvas.drawPath(path, holdenLabelPaint);
        canvas.drawTextOnPath(String.valueOf(getLength()), path, hOffset, -15, holdenTextPaint);
    }

    protected int getLength(){
        return (int) getL(startX,startY,stopX,stopY);
    }
}
