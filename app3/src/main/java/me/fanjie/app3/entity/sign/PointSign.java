package me.fanjie.app3.entity.sign;

import me.fanjie.app3.entity.Vertex;

/**
 * Created by dell on 2017/2/8.
 */

public abstract class PointSign extends BaseSign {
    protected Vertex center;
    protected String title;

    public PointSign(float x, float y) {
        center = new Vertex(x, y);
    }
    @Override
    public boolean hold(float x, float y) {
        return center.hold(x,y);
    }
    public abstract void drag(float x,float y);
}
