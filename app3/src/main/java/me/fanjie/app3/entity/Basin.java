package me.fanjie.app3.entity;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

/**
 * Created by dell on 2017/1/13.
 */

public class Basin {
    public float x;
    public float y;
    public float width;
    public float height;
    public float round;
    private Path centerPath;

    public Basin(float x, float y, float width, float height, float round) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.round = round;
        float l = 30;
        centerPath = new Path();
        centerPath.moveTo(x, y + l);
        centerPath.lineTo(x, y - l);
        centerPath.moveTo(x - l, y);
        centerPath.lineTo(x + l, y);
    }


    public void draw(Canvas canvas, Paint centerPaint, Paint edgePaint){
        canvas.drawPath(centerPath,centerPaint);
        float w = width / 2;
        float h = height / 2;
        RectF rectF = new RectF(x- w,y- h,x+ w,y+ h);
        canvas.drawRoundRect(rectF,round,round,edgePaint);
    }
}
