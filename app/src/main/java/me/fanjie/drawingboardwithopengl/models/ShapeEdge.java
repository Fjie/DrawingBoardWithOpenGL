package me.fanjie.drawingboardwithopengl.models;

import me.fanjie.drawingboardwithopengl.drawable.IDrawable;

import static me.fanjie.drawingboardwithopengl.BMath.getL;

/**
 * Created by dell on 2016/12/21.
 */

public class ShapeEdge implements IDrawable {
    public Vertex start;
    public Vertex stop;

    public ShapeEdge(Vertex start, Vertex stop) {
        this.start = start;
        this.stop = stop;
    }

    public EdgeDirection getDirection(){
        if(start.x == stop.x){
            return EdgeDirection.VER;
        }else if(start.y == stop.y){
            return EdgeDirection.HOR;
        }else {
            return null;
        }
    }

    @Override
    public void drawing() {

    }

    @Override
    public boolean equals(Object obj) {
        ShapeEdge shapeEdge = (ShapeEdge) obj;
        return this.start.equals(shapeEdge.start)&&this.stop.equals(shapeEdge.stop);
    }

    @Override
    public String toString() {
        return "ShapeEdge{" +
                "start=" + start.position +
                ", stop=" + stop.position +
                '}';
    }

    public double getLength() {
        return getL(start.x, start.y, stop.x, stop.y);
    }

    //    边线方向，横竖
public enum EdgeDirection{
        HOR,VER
    }
}
