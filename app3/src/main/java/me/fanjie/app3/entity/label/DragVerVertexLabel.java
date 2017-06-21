package me.fanjie.app3.entity.label;

import me.fanjie.app3.entity.Vertex;

/**
 * Created by dell on 2017/2/6.
 */

public class DragVerVertexLabel extends DragVertexLabel {
    public DragVerVertexLabel(Vertex start, Vertex stop) {
        super(start, stop);
//        计算方向的数值
        float symbolAge = 0;
//        默认偏移量
        float defaultOffset = 0;
        symbolAge = start.x - start.h.x;
        defaultOffset = 200 + Math.abs(start.x - stop.x) / 2;
        symbol = symbolAge / Math.abs(symbolAge);
        offset = symbol * defaultOffset;
        init();
    }

    @Override
    public void drag(float x, float y) {
        offset = x - (start.x + stop.x) / 2;
        init();
    }

    @Override
    public void init() {
        float baseX;
        baseX = (start.x + stop.x) / 2;
        startX = baseX + offset;
        startY = start.y;
        stopX = baseX + offset;
        stopY = stop.y;
        initVertexPath();
    }
}
