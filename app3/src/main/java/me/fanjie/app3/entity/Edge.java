package me.fanjie.app3.entity;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextPaint;

import me.fanjie.app3.mapping.PaintUtils;

/**
 * Created by dell on 2016/12/24.
 */

public class Edge {

    public Vertex start;
    public Vertex stop;
    public Label label;
    private SideWall sideWall;
    private boolean initSize;

    public Edge(Vertex start, Vertex stop) {
        this.start = start;
        this.stop = stop;
    }

    public void initLabel() {
        if (label == null) {
            label = new Label(start, stop, Label.Type.EDGE);
        } else {
            label.init();
        }
    }

    public void addSideWall(SideWall.Type type) {
        sideWall = new SideWall(start, stop, type);
    }

    public void drawLine(Canvas canvas, Paint paint) {
        Path path = new Path();
        path.moveTo(start.x, start.y);
        path.lineTo(stop.x, stop.y);
        canvas.drawPath(path, paint);
        if(initSize){
            int l = 20;
            RectF startRectF = new RectF(start.x - l,start.y - l,start.x + l,start.y+l);
            RectF stopRectF = new RectF(stop.x - l,stop.y - l,stop.x + l,stop.y+l);
            canvas.drawRect(startRectF,PaintUtils.getSetEdgeSizeChoseVertexPaint());
            canvas.drawRect(stopRectF,PaintUtils.getSetEdgeSizeChoseVertexPaint());
        }
    }

    public void drawLabel(Canvas canvas, Paint labelPaint, TextPaint textPaint) {
        if (label != null) {
            label.drawLabel(canvas, labelPaint, textPaint);
        }
    }

    public void drawSideWall(Canvas canvas, Paint downPaint, Paint upPaint) {
        if (sideWall != null) {
            sideWall.draw(canvas, downPaint, upPaint);
        }
    }

    public Direction getDirection() {
        if (start.x == stop.x) {
            return Direction.VER;
        } else if (start.y == stop.y) {
            return Direction.HOR;
        } else {
            return null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        Edge edge = (Edge) obj;
        return this.start.equals(edge.start) && this.stop.equals(edge.stop);
    }

    @Override
    public String toString() {
        return "ShapeEdge{" +
                "start=" + start.position +
                ", stop=" + stop.position +
                '}';
    }

    public void setSize(int size, float x, float y) {

    }

    public void setInitSize(boolean initSize) {
        this.initSize = initSize;
    }


    //    边线方向，横竖
    public enum Direction {
        HOR, VER
    }

}
