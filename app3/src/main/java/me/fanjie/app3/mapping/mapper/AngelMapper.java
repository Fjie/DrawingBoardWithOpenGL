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
    private Vertex holdenVertex;
    public AngelMapper(CMap cMap, Panel panel) {
        super(cMap, panel);
    }

    @Override
    public boolean setAngel(int angel, Edge.Direction direction) {
        if (holdenVertex != null) {
            double a = (90 - angel) * (Math.PI / 180);
            if (direction == Edge.Direction.HOR) {
                float ab = Math.abs(holdenVertex.h.x - holdenVertex.x);
                float v = holdenVertex.v.y - holdenVertex.y;
                float pn = v / Math.abs(v);
                double bc = ab * Math.tan(a) * pn;
                holdenVertex.h.y = (float) (holdenVertex.y + bc);
            } else {
                float ab = Math.abs(holdenVertex.v.y - holdenVertex.y);
                float v = holdenVertex.h.x - holdenVertex.x;
                float pn = v / Math.abs(v);
                double bc = ab * Math.tan(a) * pn;
                holdenVertex.v.x = (float) (holdenVertex.x + bc);
            }
            holdenVertex.setAngel(angel);
            initDrawable();
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouch(int action, float x, float y) {
            if(action == MotionEvent.ACTION_DOWN){
                holdenVertex = null;
                for (Vertex v : cMap.vertices) {
                    if(v.hold(x,y)){
                        holdenVertex = v;
                    }
                }
                initDrawable();
            }
        return holdenVertex != null;
    }

    @Override
    public void drawing() {
        super.drawing();
        if(holdenVertex != null){
            holdenVertex.drawHolding();
        }
    }
}
