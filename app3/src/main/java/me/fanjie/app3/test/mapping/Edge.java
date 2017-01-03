package me.fanjie.app3.test.mapping;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;

/**
 * Created by dell on 2016/12/24.
 */

public class Edge {

    public Vertex start;
    public Vertex stop;
    public Label label;

    public Edge(Vertex start, Vertex stop) {
        this.start = start;
        this.stop = stop;
    }

    public void drawLine(Canvas canvas,  Paint paint) {
        Path path = new Path();
        path.moveTo(start.x, start.y);
        path.lineTo(stop.x, stop.y);
        canvas.drawPath(path, paint);
    }
    public void drawLabel(Canvas canvas, Paint labelPaint, TextPaint textPaint) {
        if(label != null){
            label.drawLabel(canvas,labelPaint,textPaint);
        }
    }

    public Direction getDirection(){
        if(start.x == stop.x){
            return Direction.VER;
        }else if(start.y == stop.y){
            return Direction.HOR;
        }else {
            return null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        Edge edge = (Edge) obj;
        return this.start.equals(edge.start)&&this.stop.equals(edge.stop);
    }

    @Override
    public String toString() {
        return "ShapeEdge{" +
                "start=" + start.position +
                ", stop=" + stop.position +
                '}';
    }

    public void initLabel() {
        if(label == null) {
            label = new Label(start, stop, Label.Type.EDGE);
        }else {
            label.init();
        }
    }



    //    边线方向，横竖
    public enum Direction {
        HOR,VER
    }

}
