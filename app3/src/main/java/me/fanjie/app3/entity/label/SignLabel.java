package me.fanjie.app3.entity.label;

import android.graphics.Paint;

import me.fanjie.app3.entity.Vertex;

/**
 * Created by dell on 2017/2/8.
 */

public abstract class SignLabel extends BaseLabel {

    protected int offset;

//    端口绘制偏移
    protected int portOffset;

    public SignLabel(Vertex start, Vertex stop) {
        super(start, stop);
    }

    public SignLabel(Vertex start, Vertex stop, int offset) {
        super(start, stop);
        this.offset = offset;
    }

    @Override
    public void draw() {
        init();
        super.draw();
        drawEdgeLine(labelPaint);

    }

    @Override
    public void drawHolding() {
        init();
        super.drawHolding();
        drawEdgeLine(holdenLabelPaint);
    }
    private void drawEdgeLine(Paint paint) {
        if(startPath!=null && stopPath!=null){
            canvas.drawPath(startPath,paint);
            canvas.drawPath(stopPath,paint);
        }
    }
    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setPortOffset(int portOffset) {
        this.portOffset = portOffset;
    }

    public abstract void setDistance(int distance);
}
