package me.fanjie.app3.mapping.mapper;

import android.view.MotionEvent;

import me.fanjie.app3.Panel;
import me.fanjie.app3.entity.CMap;
import me.fanjie.app3.entity.Edge;
import me.fanjie.app3.entity.Vertex;
import me.fanjie.app3.mapping.Interface.IMapperAngelApi;

/**
 * Created by dell on 2017/1/18.
 */
public class AngelMapper extends BaseMapper implements IMapperAngelApi{
    public AngelMapper(CMap cMap, Panel panel) {
        super(cMap, panel);
    }

    @Override
    public boolean setAngel(int angel, Edge.Direction direction) {
        if (cMap.vertexHolder != null) {
            double a = (90 - angel) * (Math.PI / 180);
            if (direction == Edge.Direction.HOR) {
                float ab = Math.abs(cMap.vertexHolder.h.x - cMap.vertexHolder.x);
                float v = cMap.vertexHolder.v.y - cMap.vertexHolder.y;
                float pn = v / Math.abs(v);
                double bc = ab * Math.tan(a) * pn;
                cMap.vertexHolder.h.y = (float) (cMap.vertexHolder.y + bc);
            } else {
                float ab = Math.abs(cMap.vertexHolder.v.y - cMap.vertexHolder.y);
                float v = cMap.vertexHolder.h.x - cMap.vertexHolder.x;
                float pn = v / Math.abs(v);
                double bc = ab * Math.tan(a) * pn;
                cMap.vertexHolder.v.x = (float) (cMap.vertexHolder.x + bc);
            }
            cMap.vertexHolder.setAngel(angel);
            initDrawable();
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouch(int action, float x, float y) {
        switch (action){
            case MotionEvent.ACTION_DOWN: {
                cMap.edgeHolder = null;
                cMap.vertexHolder = null;
                for (Vertex v : cMap.vertices) {
                    holdVertex(v, x, y);
                }
                initDrawable();
                if (cMap.vertexHolder != null || cMap.edgeHolder != null || cMap.labelHolder != null) {
                    return true;
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (cMap.vertexHolder != null || cMap.edgeHolder != null || cMap.labelHolder != null) {
                    return true;
                }
                break;
            }
        }
        return false;
    }
}
