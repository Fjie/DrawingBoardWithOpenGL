package me.fanjie.app3.test.mapping.Interface;

import me.fanjie.app3.test.mapping.Edge;

/**
 * Created by dell on 2016/12/30.
 */

public interface IMapperAngelApi {
    /**
     * 选中顶点后设置角度
     * @param angel 角度值0~360
     * @param direction 选择基于顶点旋转来适应角度的一条边
     */
    void setAngel(int angel, Edge.Direction direction);
}
