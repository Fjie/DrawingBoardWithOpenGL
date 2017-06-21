package me.fanjie.app3.entity.sign;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import me.fanjie.app3.CPath;
import me.fanjie.app3.ShapeUtils;
import me.fanjie.app3.entity.CMap;
import me.fanjie.app3.entity.Edge;
import me.fanjie.app3.entity.Vertex;
import me.fanjie.app3.entity.label.HorSignLabel;
import me.fanjie.app3.entity.label.SignLabel;
import me.fanjie.app3.entity.label.VerSignLabel;

import static me.fanjie.app3.ShapeUtils.pointInPath;

/**
 * Created by dell on 2017/2/8.
 */

public class Hole extends PointSign {
    private static final int LABEL_PORT_OFFSET = 8;

    private static Paint shapePaint;
    private static Paint holdenShapePaint;

    static {
        shapePaint = new Paint(basePaint);
        shapePaint.setStyle(Paint.Style.STROKE);
        shapePaint.setStrokeWidth(4);
        shapePaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));
        holdenShapePaint = new Paint(shapePaint);
        holdenShapePaint.setColor(Color.RED);
    }

    private Type type;
    //    高 宽
    private int height;
    private int width;
    private Edge edge;
    private SignLabel label;
    private CPath path;

    public Hole(float x, float y) {
        super(x, y);
        height = 50;
        width = 50;
    }

    public Hole(float x, float y, Edge edge) {
        this(x, y);
        this.edge = edge;
        if (edge.getDirection() == Edge.Direction.HOR) {
            y = center.y = edge.start.y;
            Vertex vertex = edge.start.x < edge.stop.x ? edge.start : edge.stop;
            label = new HorSignLabel(vertex, center);
            label.setPortOffset(LABEL_PORT_OFFSET);
            if (pointInPath(x, y + 5, CMap.shapePath)) {
                type = Type.U_UP;
                label.setOffset(height + LABEL_PORT_OFFSET + 10);
            } else {
                type = Type.U_DOWN;
                label.setOffset(-(LABEL_PORT_OFFSET + height + 10));
            }
        } else if (edge.getDirection() == Edge.Direction.VER) {
            x = center.x = edge.start.x;
            Vertex vertex = edge.start.y < edge.stop.y ? edge.start : edge.stop;
            label = new VerSignLabel(vertex, center);
            label.setPortOffset(LABEL_PORT_OFFSET);
            if (pointInPath(x + 5, y, CMap.shapePath)) {
                type = Type.U_LEFT;
                label.setOffset(width + LABEL_PORT_OFFSET + 10);
            } else {
                type = Type.U_RIGHT;
                label.setOffset(-(LABEL_PORT_OFFSET + width + 10));
            }
        } else {
            type = Type.U_UN_KNOW;
        }
    }

    public Hole(Vertex v) {
        this(v.x, v.y);
        if (pointInPath(v.x + 5, v.y + 5, CMap.shapePath)) {
            type = Type.L_UP_LEFT;
        } else if (pointInPath(v.x - 5, v.y + 5, CMap.shapePath)) {
            type = Type.L_UP_RIGHT;
        } else if (pointInPath(v.x - 5, v.y - 5, CMap.shapePath)) {
            type = Type.L_DOWN_RIGHT;
        } else if (pointInPath(v.x + 5, v.y - 5, CMap.shapePath)) {
            type = Type.L_DOWN_LEFT;
        } else {
            type = Type.L_UN_KNOW;
        }
    }

    @Override
    public boolean hold(float x, float y) {
        return ShapeUtils.pointInPath(x, y, path);
    }

    @Override
    public void drag(float x, float y) {
        if (type == Type.U_UP || type == Type.U_DOWN) {
            horDrag(x);
        } else if (type == Type.U_LEFT || type == Type.U_RIGHT) {
            verDrag(y);
        }
    }

    private void horDrag(float x) {
        float min = Math.min(edge.start.x, edge.stop.x) + width / 2;
        float max = Math.max(edge.start.x, edge.stop.x) - width / 2;
        if (x < min) {
            x = min;
        } else if (x > max) {
            x = max;
        }
        center.x = x;
    }

    private void verDrag(float y) {
        float min = Math.min(edge.start.y, edge.stop.y) + height / 2;
        float max = Math.max(edge.start.y, edge.stop.y) - height / 2;
        if (y < min) {
            y = min;
        } else if (y > max) {
            y = max;
        }
        center.y = y;
    }

    @Override
    public void draw() {
        drawHole(shapePaint);
    }

    @Override
    public void drawHolding() {
        drawHole(holdenShapePaint);
    }

    private void drawHole(Paint paint) {
        if (label != null) {
            label.draw();
        }
        int w = width / 2;
        int h = height / 2;
        RectF rectF;
        switch (type) {
            case U_UP:
                rectF = new RectF(center.x - w, center.y, center.x + w, center.y + height);
                break;
            case U_DOWN:
                rectF = new RectF(center.x - w, center.y - height, center.x + w, center.y);
                break;
            case U_RIGHT:
                rectF = new RectF(center.x - width, center.y + h, center.x, center.y - h);
                break;
            case U_LEFT:
                rectF = new RectF(center.x, center.y + h, center.x + width, center.y - h);
                break;
            case L_UP_RIGHT:
                rectF = new RectF(center.x - width, center.y, center.x, center.y + height);
                break;
            case L_UP_LEFT:
                rectF = new RectF(center.x + width, center.y, center.x, center.y + height);
                break;
            case L_DOWN_RIGHT:
                rectF = new RectF(center.x - width, center.y - height, center.x, center.y);
                break;
            case L_DOWN_LEFT:
                rectF = new RectF(center.x, center.y - height, center.x + width, center.y);
                break;
            default: {
                rectF = new RectF(center.x - w, center.y - h, center.x + w, center.y + h);
            }
        }
        if(path == null) {
            path = new CPath();
        }
        path.reset();
        path.addRect(rectF, Path.Direction.CW);
        canvas.drawPath(path, paint);
    }

    public SignLabel holdLabel(float x, float y) {
        if (label != null && label.hold(x, y)) {
            return label;
        }
        return null;
    }


    public enum Type {
        U_UP, U_DOWN, U_RIGHT, U_LEFT,
        L_UP_RIGHT, L_UP_LEFT, L_DOWN_RIGHT, L_DOWN_LEFT,
        U_UN_KNOW, L_UN_KNOW
    }
}
