package me.fanjie.app3.mapping;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.text.TextPaint;

/**
 * Created by dell on 2017/1/18.
 */

public class PaintUtils {

    private static Paint angelPaint;
    private static Paint edgePaint;
    private static Paint holderVertexPaint;
    private static Paint setEdgeSizeChoseVertexPaint;
    private static Paint holderEdgePaint;
    private static Paint labelPaint;
    private static Paint leadingLine;
    private static Paint holderLabelPaint;
    private static Paint holderLeadingLine;
    private static TextPaint textPaint;
    private static TextPaint holderTextPaint;

    static {
        edgePaint = new Paint();
        edgePaint.setColor(Color.BLACK);
        edgePaint.setStrokeWidth(5);
        edgePaint.setStyle(Paint.Style.STROKE);
        edgePaint.setAntiAlias(true);
        edgePaint.setDither(true);
        angelPaint = new Paint(edgePaint);
        angelPaint.setColor(Color.GRAY);
        angelPaint.setStrokeWidth(10);
        holderEdgePaint = new Paint(edgePaint);
        holderEdgePaint.setColor(Color.RED);
        holderEdgePaint.setStrokeWidth(10);
        holderVertexPaint = new Paint(holderEdgePaint);
        holderVertexPaint.setStyle(Paint.Style.FILL);
        setEdgeSizeChoseVertexPaint = new Paint(holderVertexPaint);
        setEdgeSizeChoseVertexPaint.setColor(Color.GREEN);
        labelPaint = new Paint(edgePaint);
        labelPaint.setStrokeWidth(2);
        leadingLine = new Paint(labelPaint);
        leadingLine.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        holderLabelPaint = new Paint(labelPaint);
        holderLabelPaint.setColor(Color.RED);
        holderLeadingLine = new Paint(leadingLine);
        holderLeadingLine.setColor(Color.RED);
        textPaint = new TextPaint();
        textPaint.setTextSize(30);
        holderTextPaint = new TextPaint(textPaint);
        holderTextPaint.setColor(Color.RED);
    }

    public static Paint getSetEdgeSizeChoseVertexPaint() {
        return setEdgeSizeChoseVertexPaint;
    }

    public static Paint getAngelPaint() {
        return angelPaint;
    }

    public static Paint getEdgePaint() {
        return edgePaint;
    }

    public static Paint getHolderVertexPaint() {
        return holderVertexPaint;
    }

    public static Paint getHolderEdgePaint() {
        return holderEdgePaint;
    }

    public static Paint getLabelPaint() {
        return labelPaint;
    }

    public static Paint getLeadingLine() {
        return leadingLine;
    }

    public static Paint getHolderLabelPaint() {
        return holderLabelPaint;
    }

    public static Paint getHolderLeadingLine() {
        return holderLeadingLine;
    }

    public static TextPaint getTextPaint() {
        return textPaint;
    }

    public static TextPaint getHolderTextPaint() {
        return holderTextPaint;
    }
}
