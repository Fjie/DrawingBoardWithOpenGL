package me.fanjie.app3.mapping.mapper;

import android.view.MotionEvent;

import me.fanjie.app3.Panel;
import me.fanjie.app3.entity.PointSignBasin;
import me.fanjie.app3.entity.CMap;
import me.fanjie.app3.entity.Edge;
import me.fanjie.app3.entity.PointSignKitChen;
import me.fanjie.app3.entity.SideWall;
import me.fanjie.app3.mapping.Interface.IMapperSignApi;

/**
 * Created by dell on 2017/1/18.
 */

public class SignMapper extends BaseMapper implements IMapperSignApi {
    private boolean addKitChen;
    private boolean addBasin;

    public SignMapper(CMap cMap, Panel panel) {
        super(cMap, panel);
    }

    @Override
    public void addSideWall(SideWall.Type type) {
        if (cMap.edgeHolder != null) {
            cMap.edgeHolder.addSideWall(type);
            initDrawable();
        }
    }

    @Override
    public void addKitchen() {
        addKitChen = true;
    }

    @Override
    public void addBasin() {
        addBasin = true;
    }

    @Override
    public boolean onTouch(int action, float x, float y) {
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                cMap.labelHolder = null;
                cMap.edgeHolder = null;
                if (addKitChen) {
                    cMap.pointSignKitChen = new PointSignKitChen(x, y);
                    addKitChen = false;
                    initDrawable();
                    break;
                }
                if (addBasin) {
                    cMap.pointSignBasin = new PointSignBasin(x, y, 300, 150, 35);
                    addBasin = false;
                    initDrawable();
                    break;
                }
                for(Edge e:cMap.edges){
                    holdEdge(e,x,y);
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
