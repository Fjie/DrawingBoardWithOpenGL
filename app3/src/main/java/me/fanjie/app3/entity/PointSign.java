package me.fanjie.app3.entity;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by dell on 2017/1/20.
 */

public class PointSign {
    protected Vertex center;
    private Path centerPath;

    public PointSign(float x,float y) {
        center = new Vertex(x,y);
        float l = 30;
        centerPath = new Path();
        centerPath.moveTo(x, y + l);
        centerPath.lineTo(x, y - l);
        centerPath.moveTo(x - l, y);
        centerPath.lineTo(x + l, y);
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawPath(centerPath, paint);
    }

    public Vertex getCenter() {
        return center;
    }
}
