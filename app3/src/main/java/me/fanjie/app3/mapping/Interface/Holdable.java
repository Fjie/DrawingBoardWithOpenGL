package me.fanjie.app3.mapping.Interface;

/**
 * Created by dell on 2017/1/20.
 * 可hold住接口，有hold状态就一定要有相应的绘制方法
 */

public interface Holdable {
    boolean hold(float x,float y);
    void drawHolding();
}
