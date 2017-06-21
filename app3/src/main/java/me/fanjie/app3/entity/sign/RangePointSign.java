package me.fanjie.app3.entity.sign;

import android.graphics.Color;
import android.graphics.Paint;

import me.fanjie.app3.entity.Vertex;
import me.fanjie.app3.entity.label.HorSignLabel;
import me.fanjie.app3.entity.label.SignLabel;
import me.fanjie.app3.entity.label.VerSignLabel;

/**
 * Created by dell on 2017/1/20. 范围标记，自由选点
 */

public abstract class RangePointSign extends PointSign {

    private static final int LABEL_PORT_OFFSET = 12;

    private static Paint centerPaint;
    private static Paint holdenCenterPaint;

    static {
        centerPaint = new Paint(basePaint);
        centerPaint.setStyle(Paint.Style.STROKE);
        centerPaint.setStrokeWidth(5);
        holdenCenterPaint = new Paint(centerPaint);
        holdenCenterPaint.setColor(Color.RED);
    }

    protected HorSignLabel horLabel;
    protected VerSignLabel verLabel;

    protected int rAngel;
    protected int width;
    protected int height;

    public RangePointSign(float x, float y, int rAngel, int width, int height) {
        super(x, y);
        this.rAngel = rAngel;
        this.width = width;
        this.height = height;
    }

    public RangePointSign(float x, float y) {
        super(x, y);
    }

    public void addHorLabel(Vertex assistVertex) {
        horLabel = new HorSignLabel(assistVertex, center, 35 + LABEL_PORT_OFFSET);
        horLabel.setPortOffset(LABEL_PORT_OFFSET);
    }

    public void addVerLabel(Vertex assistVertex) {
        verLabel = new VerSignLabel(assistVertex, center, 35 + LABEL_PORT_OFFSET);
        verLabel.setPortOffset(LABEL_PORT_OFFSET);
    }

    public SignLabel holdLabel(float x, float y) {
        if (verLabel != null && verLabel.hold(x, y)) {
            return verLabel;
        } else if (horLabel != null && horLabel.hold(x, y)) {
            return horLabel;
        } else {
            return null;
        }
    }

    @Override
    public void drag(float x, float y) {
        center.x = x;
        center.y = y;
    }

    @Override
    public void drawHolding() {
        float l = 30;
        canvas.drawLine(center.x, center.y + l, center.x, center.y - l, holdenCenterPaint);
        canvas.drawLine(center.x + l, center.y, center.x - l, center.y, holdenCenterPaint);
    }

    @Override
    public void draw() {
        float l = 30;
        canvas.drawLine(center.x, center.y + l, center.x, center.y - l, centerPaint);
        canvas.drawLine(center.x + l, center.y, center.x - l, center.y, centerPaint);
        canvas.drawText(title, center.x + 10, center.y - 10, textPaint);
        if (horLabel != null) {
            horLabel.draw();
        }
        if (verLabel != null) {
            verLabel.draw();
        }
    }


}
