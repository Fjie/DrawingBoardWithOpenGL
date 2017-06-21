package me.fanjie.app3.entity.label;

import me.fanjie.app3.entity.Vertex;

/**
 * Created by dell on 2017/2/6.
 */

public abstract class DragLabel extends BaseLabel {

    //    标注 与线的偏移量
    protected float offset;
    //    表示偏移量正负的符号
    protected float symbol;

    public DragLabel(Vertex start, Vertex stop) {
        super(start, stop);
    }

    public abstract void drag(float x, float y);
}
