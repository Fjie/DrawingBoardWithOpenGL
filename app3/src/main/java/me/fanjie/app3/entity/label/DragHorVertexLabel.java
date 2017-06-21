package me.fanjie.app3.entity.label;

import me.fanjie.app3.entity.Vertex;

/**
 * Created by dell on 2017/2/6.
 */

public class DragHorVertexLabel extends DragVertexLabel {
    public DragHorVertexLabel(Vertex start, Vertex stop) {
        super(start, stop);
        //        计算方向的数值
        float symbolAge = 0;
//        默认偏移量
        float defaultOffset = 0;
        symbolAge = start.y - start.v.y;
        defaultOffset = 200 + Math.abs(start.y - stop.y) / 2;
        symbol = symbolAge / Math.abs(symbolAge);
        offset = symbol * defaultOffset;
        init();
    }

    @Override
    public void drag(float x, float y) {
        offset = y - (start.y + stop.y) / 2;
        init();
    }

    @Override
    public void init() {
//                偏移基点
        float baseY;
        baseY = (start.y + stop.y) / 2;
        startX = start.x;
        startY = baseY + offset;
        stopX = stop.x;
        stopY = baseY + offset;
        initVertexPath();
    }
}
