package me.fanjie.app3.entity;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;

import me.fanjie.app3.ShapeUtils;
import me.fanjie.app3.mapping.MapHelper;

import static me.fanjie.app3.BMath.getL;

/**
 * Created by dell on 2017/1/3.
 */

public class Label extends HoldableMapEntity {
    private static Paint labelPaint;
    private static Paint leadingLine;
    private static Paint holdenLabelPaint;
    private static Paint holdenLeadingLine;
    private static TextPaint textPaint;
    private static TextPaint holdenTextPaint;

    static {
        labelPaint= new Paint(basePaint);
        labelPaint.setStyle(Paint.Style.STROKE);
        labelPaint.setStrokeWidth(2);
        leadingLine = new Paint(labelPaint);
        leadingLine.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        holdenLabelPaint = new Paint(labelPaint);
        holdenLabelPaint.setColor(Color.RED);
        holdenLeadingLine = new Paint(leadingLine);
        holdenLeadingLine.setColor(Color.RED);
        textPaint = new TextPaint();
        textPaint.setTextSize(30);
        holdenTextPaint = new TextPaint(textPaint);
        holdenTextPaint.setColor(Color.RED);
    }

    private Vertex start;
    private Vertex stop;
    private Type type;
    //    边线是否接近垂直
    private boolean edgeVer;

    private float startX;
    private float startY;
    private float stopX;
    private float stopY;
    //    标注 与线的偏移量
    private float offset;
    //    表示偏移量正负的符号
    private float symbol;
    private Path mPath;
    private Path startPath;
    private Path stopPath;

    //    是否已经转换过，防止无线递归
    private boolean changed;

    public Label(Vertex start, Vertex stop, Type type) {
        this.start = start;
        this.stop = stop;
        this.type = type;
//        计算方向的数值
        float symbolAge = 0;
//        默认偏移量
        float defaultOffset = 0;
        switch (type) {
            case EDGE: {
                double a = Math.atan((stop.y - start.y) / (stop.x - start.x));
                edgeVer = a < 0 || a > Math.PI / 4;
                if (edgeVer) {
                    symbolAge = start.x - start.h.x;
                } else {
                    symbolAge = start.y - start.v.y;
                }
                defaultOffset = 50;
                break;
            }
            case VER: {
                symbolAge = start.x - start.h.x;
                defaultOffset = 200 + Math.abs(start.x - stop.x) / 2;
                break;
            }
            case HOR: {
                symbolAge = start.y - start.v.y;
                defaultOffset = 200 + Math.abs(start.y - stop.y) / 2;
                break;
            }

        }
        symbol = symbolAge / Math.abs(symbolAge);
        offset = symbol * defaultOffset;
        init();
    }

    /**
     * 在创建时初始化，在所依附的形状改变后需要调用
     */
    public void init() {
        switch (type) {
            case EDGE: {
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
                if (!changed && ShapeUtils.pointInPath((startX + stopX) / 2, (startY + stopY) / 2, MapHelper.getInstance().cMap.shapePath)) {
                    offset = -offset;
                    changed = true;
                    init();
                    break;
                }
                initEdgePath();
                break;
            }
            case HOR: {
//                偏移基点
                float baseY;
                baseY = (start.y + stop.y) / 2;
                startX = start.x;
                startY = baseY + offset;
                stopX = stop.x;
                stopY = baseY + offset;
                initVertexPath();
                break;
            }
            case VER: {
                float baseX;
                baseX = (start.x + stop.x) / 2;
                startX = baseX + offset;
                startY = start.y;
                stopX = baseX + offset;
                stopY = stop.y;
                initVertexPath();
                break;
            }

        }

    }

    public void setLength(int length) {
        double v = Math.atan((stopX - startX) / (stopY - startY));
        v = Math.abs(v);
        float bc = (float) (length * Math.sin(v));
        float ab = (float) (length * Math.cos(v));
//        手动判断方向 几何
        stop.x = stop.x > start.x ? start.x + bc : start.x - bc;
        stop.y = stop.y > start.y ? start.y + ab : start.y - ab;
    }

    private void initEdgePath() {
        mPath = new Path();
        mPath.moveTo(startX, startY);
        mPath.lineTo(stopX, stopY);
        float bc = 20;

        double a;
        if (type == Type.HOR) {
            a = 0;
        } else if (type == Type.VER) {
            a = Math.PI / 2;
        } else {
            a = Math.atan((stop.y - start.y) / (stop.x - start.x));
        }
        double c = Math.PI / 2 - a;
        double b1 = Math.PI / 2 - c;
        double bh = bc * Math.sin(c);
        double ch = bc * Math.sin(b1);
        float cX = (float) (startX - ch);
        float cY = (float) (startY + bh);
        float dX = (float) (startX + ch);
        float dY = (float) (startY - bh);
        startPath = new Path();
        startPath.moveTo(cX, cY);
        startPath.lineTo(dX, dY);
        float c1X = (float) (stopX - ch);
        float c1Y = (float) (stopY + bh);
        float d1X = (float) (stopX + ch);
        float d1Y = (float) (stopY - bh);
        stopPath = new Path();
        stopPath.moveTo(c1X, c1Y);
        stopPath.lineTo(d1X, d1Y);
    }

    private void initVertexPath() {
        mPath = new Path();
        mPath.moveTo(startX, startY);
        mPath.lineTo(stopX, stopY);

        startPath = new Path();
        startPath.moveTo(start.x, start.y);
        startPath.lineTo(startX, startY);

        stopPath = new Path();
        stopPath.moveTo(stop.x, stop.y);
        stopPath.lineTo(stopX, stopY);
    }
    @Override
    public boolean hold(float x, float y) {
        float x1 = startX + (stopX - startX) / 2;
        float y1 = startY + (stopY - startY) / 2;
        return getL(x, y, x1, y1) < 50;
    }

    @Override
    public void drawHolding() {
        canvas.drawPath(mPath, holdenLabelPaint);
        canvas.drawPath(startPath, type == Type.EDGE? holdenLabelPaint : holdenLeadingLine);
        canvas.drawPath(stopPath,  type == Type.EDGE? holdenLabelPaint : holdenLeadingLine);
        canvas.drawTextOnPath(String.valueOf(getLength()), mPath, getLength() / 2f - 50, -20, holdenTextPaint);
    }

    @Override
    public void draw() {
        canvas.drawPath(mPath, labelPaint);
        canvas.drawPath(startPath, type == Type.EDGE?labelPaint:leadingLine);
        canvas.drawPath(stopPath,  type == Type.EDGE?labelPaint:leadingLine);
        canvas.drawTextOnPath(String.valueOf(getLength()), mPath, getLength() / 2f - 50, -20, textPaint);
    }


    public void drag(float x, float y) {
        switch (type) {
            case EDGE: {
                if (edgeVer) {
                    offset = x - start.x;
                } else {
                    offset = y - start.y;
                }
                break;
            }
            case HOR: {
                offset = y - (start.y + stop.y) / 2;
                break;
            }
            case VER: {
                offset = x - (start.x + stop.x) / 2;
                break;
            }
        }
        init();
    }

    public int getLength() {
        double length = 0;
        switch (type) {
            case EDGE: {
                length = getL(start.x, start.y, stop.x, stop.y);
                break;
            }
            case HOR: {
                length = Math.abs(start.x - stop.x);
                break;
            }
            case VER: {
                length = Math.abs(start.y - stop.y);
            }
        }
        return (int) length;

    }

    public boolean inLabel(float x, float y) {
        float x1 = startX + (stopX - startX) / 2;
        float y1 = startY + (stopY - startY) / 2;
        return getL(x, y, x1, y1) < 50;
    }

    public enum Type {
        EDGE, HOR, VER
    }
}
