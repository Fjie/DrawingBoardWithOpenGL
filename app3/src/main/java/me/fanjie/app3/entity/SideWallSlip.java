package me.fanjie.app3.entity;

import android.graphics.Color;
import android.graphics.Paint;

import static me.fanjie.app3.ShapeUtils.getPerpendicularLine;
import static me.fanjie.app3.ShapeUtils.setLineSize;

/**
 * Created by dell on 2017/2/11.
 */
public class SideWallSlip extends HoldableMapEntity {
    private static Paint paint;
    static {
        paint = new Paint(basePaint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(Color.YELLOW);
    }

    private Edge father;
    private Edge child1;
    private Edge child2;
    private Edge holdenChild;
    private Vertex center;

    public SideWallSlip(Edge father) {
        this.father = father;
        float x = (father.start.x + father.stop.x) / 2;
        float y = (father.start.y + father.stop.y) / 2;
        center = new Vertex(x, y);
        center.h = father.start.h;
        center.v = father.start.v;
        child1 = new Edge(father.start, center);
        child2 = new Edge(father.stop, center);
        child1.initLabel();
        child2.initLabel();
        child1.addSideWall(SideWall.Type.DOWN);
        child2.addSideWall(SideWall.Type.UP);
        father.addSideWall(null);
    }

    public boolean setChildSideWall(int size, SideWall.Type type) {
        if (holdenChild != null && size < father.getLength()) {
            setLineSize(holdenChild.start,center,size);
            return setChildSideWall(type);
        }
        return false;
    }

    public boolean setChildSideWall(SideWall.Type type) {
        if(holdenChild!=null) {
            holdenChild.addSideWall(type);
            child1.initLabel();
            child2.initLabel();
            child1.initSideWall();
            child2.initSideWall();
            return true;
        }
        return false;
    }

    @Override
    public boolean hold(float x, float y) {
        if(child1.hold(x,y)){
            holdenChild = child1;
        }else if(child2.hold(x,y)){
            holdenChild = child2;
        }else {
            return false;
        }
        return true;
    }

    @Override
    public void drawHolding() {
        holdenChild.drawHolding();
    }

    @Override
    public void draw() {
        child1.drawLabel();
        child1.drawSideWall();
        child2.drawLabel();
        child2.drawSideWall();
        canvas.drawPath(getPerpendicularLine(father,center,30),paint);
    }


    public SideWall.Type getType() {
        if(holdenChild!=null){
            return holdenChild.getSideWall().getType();
        }
        return null;
    }
}
