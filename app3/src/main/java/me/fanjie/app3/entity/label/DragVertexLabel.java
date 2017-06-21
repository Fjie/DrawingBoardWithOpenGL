package me.fanjie.app3.entity.label;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;

import me.fanjie.app3.CPath;
import me.fanjie.app3.entity.Vertex;

/**
 * Created by dell on 2017/2/6.
 */

public abstract class DragVertexLabel extends DragLabel {
    private static Paint leadingLine;
    private static Paint holdenLeadingLine;

    static {
        leadingLine = new Paint(basePaint);
        leadingLine.setStyle(Paint.Style.STROKE);
        leadingLine.setStrokeWidth(2);
        leadingLine.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        holdenLeadingLine = new Paint(leadingLine);
        holdenLeadingLine.setColor(Color.RED);
    }

    public DragVertexLabel(Vertex start, Vertex stop) {
        super(start, stop);
    }

    protected void initVertexPath() {
        path = new CPath();
        path.moveTo(startX, startY);
        path.lineTo(stopX, stopY);

        startPath = new CPath();
        startPath.moveTo(start.x, start.y);
        startPath.lineTo(startX, startY);

        stopPath = new CPath();
        stopPath.moveTo(stop.x, stop.y);
        stopPath.lineTo(stopX, stopY);
    }

    @Override
    public void draw() {
        super.draw();
        canvas.drawPath(startPath,leadingLine);
        canvas.drawPath(stopPath,leadingLine);
    }

    @Override
    public void drawHolding() {
        super.drawHolding();
        canvas.drawPath(startPath,holdenLeadingLine);
        canvas.drawPath(stopPath,holdenLeadingLine);
    }
}
