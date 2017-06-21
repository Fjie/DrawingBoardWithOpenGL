package me.fanjie.app3.entity.sign;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;

import me.fanjie.app3.entity.Edge;
import me.fanjie.app3.entity.Vertex;

import static me.fanjie.app3.BMath.getL;

/**
 * Created by dell on 2017/2/8.
 */

public class LineSign extends BaseSign {

    private static Paint paint;
    private static Paint holdenPaint;

    static {
        paint = new Paint(basePaint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setPathEffect(new DashPathEffect(new float[]{8, 8}, 0));
        holdenPaint = new Paint(paint);
        holdenPaint.setColor(Color.RED);
    }

    private Vertex start;
    private Vertex stop;
    private Type type;
    private String title;
    private int hOffset;

    public static LineSign create(Edge edge1, Edge edge2, String title) {
        Edge.Direction direction1 = edge1.getDirection();
        Edge.Direction direction2 = edge2.getDirection();
//        不平行
        if (direction1 != direction2 || direction1 == null || edge1.equals(edge2)) {
            throw new UnEvennessException();
        }
        Vertex start;
        Vertex stop;
        Type type;
        if (direction1 == Edge.Direction.VER) {
            type = Type.HOR;
            float y;
            if (edge1.getLength() < edge2.getLength()) {
                y = (edge1.start.y + edge1.stop.y) / 2;
            } else {
                y = (edge2.start.y + edge2.stop.y) / 2;
            }
            start = new Vertex(edge1.start.x, y);
            stop = new Vertex(edge2.start.x, y);
        } else {
            type = Type.VER;
            float x;
            if (edge1.getLength() < edge2.getLength()) {
                x = (edge1.start.x + edge1.stop.x) / 2;
            } else {
                x = (edge2.start.x + edge2.stop.x) / 2;
            }
            start = new Vertex(x, edge1.start.y);
            stop = new Vertex(x, edge2.start.y);
        }
        return new LineSign(start, stop, type, title);
    }

    private LineSign(Vertex start, Vertex stop, Type type, String title) {
        this.start = start;
        this.stop = stop;
        this.type = type;
        this.title = title;
        Rect rect = new Rect();
        textPaint.getTextBounds(title, 0, title.length(), rect);
        hOffset = getLength() / 2 - rect.width() / 2;
    }

    @Override
    public boolean hold(float x, float y) {
        float x1 = start.x + (stop.x - start.x) / 2;
        float y1 = start.y + (stop.y - start.y) / 2;
        return getL(x, y, x1, y1) < 50;
    }

    @Override
    public void draw() {
        drawing(paint);
    }

    @Override
    public void drawHolding() {
        drawing(holdenPaint);
    }

    private void drawing(Paint paint) {
        Path path = new Path();
        path.moveTo(start.x, start.y);
        path.lineTo(stop.x, stop.y);

        canvas.drawPath(path, paint);
        canvas.drawTextOnPath(title, path, hOffset, -10, textPaint);
    }

    public void drag(float x, float y) {
        if(type == Type.HOR){
            start.y = stop.y = y;
        }else {
            start.x = stop.x = x;
        }
    }


    public enum Type {
        HOR, VER
    }

    private int getLength() {
        return (int) getL(start.x, start.y, stop.x, stop.y);
    }
}
