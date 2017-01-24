package me.fanjie.app3.mapping.mapper;

import android.view.MotionEvent;

import me.fanjie.app3.Panel;
import me.fanjie.app3.entity.CMap;
import me.fanjie.app3.entity.Edge;
import me.fanjie.app3.entity.SideWall;
import me.fanjie.app3.entity.Vertex;
import me.fanjie.app3.mapping.Interface.IMapperSideWallApi;
import me.fanjie.app3.mapping.Interface.MapperCallback;

/**
 * Created by dell on 2017/1/24.
 */

public class SideWallMapper extends BaseMapper implements IMapperSideWallApi {

    private Edge holdenEdge;

    public SideWallMapper(CMap cMap, Panel panel, MapperCallback callback) {
        super(cMap, panel, callback);
    }

    @Override
    public void addSideWall(SideWall.Type type) {
        if (holdenEdge != null) {
            holdenEdge.addSideWall(type);
            initDrawable();
        }
    }

    @Override
    public void drawing() {
        for (Edge e : cMap.edges) {
            e.draw();
            e.drawSideWall();
        }
        for (Vertex v : cMap.vertices) {
            v.draw();
        }
        if (holdenEdge != null) {
            holdenEdge.drawHolding();
        }
    }

    @Override
    public boolean onTouch(int action, float x, float y) {
        if (action == MotionEvent.ACTION_DOWN) {
            holdenEdge = null;
            for (Edge e : cMap.edges) {
                if (e.hold(x, y)) {
                    holdenEdge = e;
                }
            }
            initDrawable();
        }
        return holdenEdge != null;
    }


}
