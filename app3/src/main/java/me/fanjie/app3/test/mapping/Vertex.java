package me.fanjie.app3.test.mapping;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by dell on 2016/12/17.
 */

public class Vertex{

    public float x;
    public float y;
    public float z;

    public Vertex v;
    public Vertex h;

    public int position;

    public Vertex(float x, float y, int position) {
        this.x = x;
        this.y = y;
        this.position = position;
    }

    public void drawCircle(Canvas canvas, float radius, Paint paint){
        canvas.drawCircle(x, y, radius, paint);
    }


    public void setNeighbor(Vertex v) {
        if (v.x == x) {
            this.v = v;
        } else if (v.y == y) {
            this.h = v;
        }
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "x=" + x +
                ", y=" + y +
                ", position=" + position +
                '}';
    }

}
