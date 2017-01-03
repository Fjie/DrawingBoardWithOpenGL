package me.fanjie.app2;

import android.util.Log;

import static me.fanjie.app2.BMath.getL;

/**
 * Created by dell on 2016/12/24.
 */

public class Edge {

    public Vertex start;
    public Vertex stop;

    private Label label;

    public Edge(Vertex start, Vertex stop) {
        this.start = start;
        this.stop = stop;
    }

    public void initLabel() {
        if(label == null) {
            this.label = new Label();
        }
    }

    public void drawLabel() {
        Log.d("XXX","drawLabel()  " + this.toString());
        if(label == null){
            return;
        }
        float x = (start.x + stop.x)/2;
        float y = (start.y + stop.y)/2;
        float l = 0.1f;
        float[] coords = new float[]{x-l,y-l,x+l,y-l,x-l,y+l,x+l,y+l};
        label.subTexture(String.format("%.2f",getLength()));
        label.setVertexCoords(coords);
        label.drawing();
    }


    public void drawSelected(){

    }

    public Edge.EdgeDirection getDirection(){
        if(start.x == stop.x){
            return Edge.EdgeDirection.VER;
        }else if(start.y == stop.y){
            return Edge.EdgeDirection.HOR;
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

    public double getLength() {
        return getL(start.x, start.y, stop.x, stop.y);
    }

    //    边线方向，横竖
    public enum EdgeDirection{
        HOR,VER
    }

}
