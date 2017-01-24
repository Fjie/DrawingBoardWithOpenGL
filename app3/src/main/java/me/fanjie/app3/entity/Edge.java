package me.fanjie.app3.entity;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import static me.fanjie.app3.BMath.getL;
import static me.fanjie.app3.BMath.getS;

/**
 * Created by dell on 2016/12/24.
 */

public class Edge extends HoldableMapEntity {

    private static Paint edgePaint;
    private static Paint holdenEdgePaint;
    private static Paint settingSizeVertexPaint;

    static {
        edgePaint = new Paint(basePaint);
        edgePaint.setStyle(Paint.Style.STROKE);
        edgePaint.setColor(Color.BLACK);
        edgePaint.setStrokeWidth(5);
        holdenEdgePaint = new Paint(edgePaint);
        holdenEdgePaint.setColor(Color.RED);
        holdenEdgePaint.setStrokeWidth(10);
        settingSizeVertexPaint = new Paint(basePaint);
        settingSizeVertexPaint.setColor(Color.GREEN);
    }

    public Vertex start;
    public Vertex stop;

    public Label label;
    private SideWall sideWall;

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

    @Override
    public void draw() {
        canvas.drawLine(start.x, start.y, stop.x, stop.y, edgePaint);
    }
    public void drawLabel() {
        if (label != null) {
            label.draw();
        }
    }

    public void drawSideWall() {
        if(sideWall!=null){
            sideWall.draw();
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

    public boolean setSize(int size, float x, float y) {
        if (getL(start.x, start.y, x, y) < 40) {
            double v = Math.atan((start.x - stop.x) / (start.y - stop.y));
            v = Math.abs(v);
            float bc = (float) (size * Math.sin(v));
            float ab = (float) (size * Math.cos(v));
            start.x = start.x > stop.x ? stop.x + bc : stop.x - bc;
            start.y = start.y > stop.y ? stop.y + ab : stop.y - ab;
        } else if (getL(stop.x, stop.y, x, y) < 40) {
            double v = Math.atan((stop.x - start.x) / (stop.y - start.y));
            v = Math.abs(v);
            float bc = (float) (size * Math.sin(v));
            float ab = (float) (size * Math.cos(v));
            stop.x = stop.x > start.x ? start.x + bc : start.x - bc;
            stop.y = stop.y > start.y ? start.y + ab : start.y - ab;
        } else {
            return false;
        }
        return true;

    }

    @Override
    public boolean hold(float x, float y) {
        double s = getS(start.x, start.y, stop.x, stop.y, x, y);
        double ac = getL(x, y, start.x, start.y);
        double bc = getL(x, y, stop.x, stop.y);
        double ab = getL(start.x, start.y, stop.x, stop.y);
        return Math.abs(s) < 40 && ac < ab && bc < ab;
    }

    @Override
    public void drawHolding() {
        canvas.drawLine(start.x, start.y, stop.x, stop.y, holdenEdgePaint);
    }

    public void drawSettingSize(){
        int l = 20;
        RectF startRectF = new RectF(start.x - l, start.y - l, start.x + l, start.y + l);
        RectF stopRectF = new RectF(stop.x - l, stop.y - l, stop.x + l, stop.y + l);
        canvas.drawRect(startRectF, settingSizeVertexPaint);
        canvas.drawRect(stopRectF, settingSizeVertexPaint);
    }

    //    边线方向，横竖
    public enum Direction {
        HOR, VER
    }

}
