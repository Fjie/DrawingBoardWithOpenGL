package me.fanjie.app3.entity;

import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by dell on 2017/1/13.
 */

public class PointSignBasin extends PointSign {
    private static Paint edgePaint;
    static {
        edgePaint = new Paint(basePaint);
        edgePaint.setStyle(Paint.Style.STROKE);
        edgePaint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
    }
    private float width;
    private float height;
    private float round;

    public PointSignBasin(float x, float y, float width, float height, float round) {
        super(x, y);
        this.width = width;
        this.height = height;
        this.round = round;
    }

    public void draw() {
        super.draw();
        float w = width / 2;
        float h = height / 2;
        RectF rectF = new RectF(center.x - w, center.y - h, center.x + w, center.y + h);
        canvas.drawRoundRect(rectF, round, round, edgePaint);
    }
}
