package me.fanjie.app3.entity;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import static android.R.attr.x;
import static android.R.attr.y;

/**
 * Created by dell on 2017/1/13.
 */

public class PointSignBasin extends PointSign{
    private float width;
    private float height;
    private float round;

    public PointSignBasin(float x, float y, float width, float height, float round) {
        super(x,y);
        this.width = width;
        this.height = height;
        this.round = round;
    }

    public void draw(Canvas canvas, Paint centerPaint, Paint edgePaint){
        super.draw(canvas,centerPaint);
        float w = width / 2;
        float h = height / 2;
        RectF rectF = new RectF(x- w,y- h,x+ w,y+ h);
        canvas.drawRoundRect(rectF,round,round,edgePaint);
    }
}
