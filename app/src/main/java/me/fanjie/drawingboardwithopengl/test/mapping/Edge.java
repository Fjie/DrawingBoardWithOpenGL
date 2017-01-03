package me.fanjie.drawingboardwithopengl.test.mapping;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import me.fanjie.drawingboardwithopengl.test.renderer.Label;

import static me.fanjie.drawingboardwithopengl.BMath.getL;

/**
 * Created by dell on 2016/12/24.
 */

public class Edge {

    public Vertex start;
    public Vertex stop;

    private Label label;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint;

    public Edge(Vertex start, Vertex stop) {
        this.start = start;
        this.stop = stop;
        initBitmap();
    }

    public void initLabel() {
        if(label == null) {
            this.label = new Label(bitmap);
        }
    }

    private void initBitmap() {
        // FIXME: 2016/12/26 画布大小需调试
        bitmap = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setTextSize(15);
    }

    public void drawLabel() {
//        Log.d("XXX","drawLabel()  " + this.toString());
        if(label == null){
            return;
        }
        float x = (start.x + stop.x)/2;
        float y = (start.y + stop.y)/2;
        float l = 0.1f;
        float[] coords = new float[]{x-l,y-l,x+l,y-l,x-l,y+l,x+l,y+l};
        initLabelTex();
        label.putVertexCoords(coords);
        label.drawing();
    }

    private void initLabelTex() {
        canvas.drawText(getLength()+"",0,15,paint);
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
