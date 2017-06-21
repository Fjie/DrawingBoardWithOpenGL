package me.fanjie.app3.entity.sign;

import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by dell on 2017/1/13.
 */

public class Basin extends RangePointSign {
    private static Paint edgePaint;
    static {
        edgePaint = new Paint(basePaint);
        edgePaint.setStyle(Paint.Style.STROKE);
        edgePaint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
    }

    private Type type;

    public Basin(float x, float y, Type type,int rAngel, int width, int height) {
        super(x, y, rAngel, width, height);
        this.type = type;
        super.title = (type == Type.UP ? "台上盆":"台下盆");

    }

    public void draw() {
        super.draw();
        float w = width / 2;
        float h = height / 2;
        RectF rectF = new RectF(center.x - w, center.y - h, center.x + w, center.y + h);
        canvas.drawRoundRect(rectF, rAngel, rAngel, edgePaint);
    }

    public enum Type{
        UP,DOWN
    }
}
