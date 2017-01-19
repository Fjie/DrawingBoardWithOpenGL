package me.fanjie.app3.entity;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by dell on 2017/1/13.
 */

public class KitChen {
    public float x;
    public float y;
    private Path path;

    public KitChen(float x, float y) {
        this.x = x;
        this.y = y;

        float l = 30;
        path = new Path();
        path.moveTo(x, y + l);
        path.lineTo(x, y - l);
        path.moveTo(x - l, y);
        path.lineTo(x + l, y);
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawPath(path, paint);
    }
}
