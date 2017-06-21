package me.fanjie.app3.entity.label;

import me.fanjie.app3.CPath;
import me.fanjie.app3.ShapeUtils;
import me.fanjie.app3.entity.Vertex;

import static me.fanjie.app3.ShapeUtils.getPerpendicularLine;
import static me.fanjie.app3.entity.CMap.shapePath;

/**
 * Created by dell on 2017/2/6.
 */

public class EdgeLabel extends DragLabel {

    //    边线是否接近垂直
    private boolean edgeVer;
    //    是否已经转换过，防止无限递归
    private boolean changed;


    public EdgeLabel(Vertex start, Vertex stop) {
        super(start, stop);
//        计算方向的数值
        float symbolAge;
//        默认偏移量
        double a = Math.atan((stop.y - start.y) / (stop.x - start.x));
        edgeVer = a < 0 || a > Math.PI / 4;
        if (edgeVer) {
            symbolAge = start.x - start.h.x;
        } else {
            symbolAge = start.y - start.v.y;
        }
        symbol = symbolAge / Math.abs(symbolAge);
        offset = symbol * 50;
        init();
    }

    @Override
    public void drag(float x, float y) {
        if (edgeVer) {
            offset = x - start.x;
        } else {
            offset = y - start.y;
        }
        init();
    }

    @Override
    public void init() {
        if (edgeVer) {
            startX = start.x + offset;
            stopX = stop.x + offset;
            startY = start.y;
            stopY = stop.y;
        } else {
            startX = start.x;
            stopX = stop.x;
            startY = start.y + offset;
            stopY = stop.y + offset;
        }
        if (!changed && ShapeUtils.pointInPath((startX + stopX) / 2, (startY + stopY) / 2, shapePath)) {
            offset = -offset;
            changed = true;
            init();
            return;
        }
        initEdgePath();
    }

    private void initEdgePath() {
        path = new CPath();
        path.moveTo(startX, startY);
        path.lineTo(stopX, stopY);
        startPath = getPerpendicularLine(start,stop,startX,startY,20);
        stopPath = getPerpendicularLine(start,stop,stopX,stopY,20);

    }

    @Override
    public void draw() {
        super.draw();
        canvas.drawPath(startPath, labelPaint);
        canvas.drawPath(stopPath, labelPaint);
    }

    @Override
    public void drawHolding() {
        super.drawHolding();
        canvas.drawPath(startPath, holdenLabelPaint);
        canvas.drawPath(stopPath, holdenLabelPaint);
    }

}
