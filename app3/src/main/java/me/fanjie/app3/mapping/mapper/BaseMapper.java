package me.fanjie.app3.mapping.mapper;

import java.util.ArrayList;
import java.util.List;

import me.fanjie.app3.JLog;
import me.fanjie.app3.Panel;
import me.fanjie.app3.entity.CMap;
import me.fanjie.app3.entity.Edge;
import me.fanjie.app3.entity.Vertex;

import static me.fanjie.app3.entity.CMap.shapePath;

/**
 * Created by dell on 2017/1/18.
 */

public abstract class BaseMapper implements Cloneable{
    protected CMap cMap;
    protected Panel panel;
    protected MapperCallback callback;
    protected boolean beDrag;

    private List<CMap> mapSteps;
    private int stepPosition;

    public BaseMapper(CMap cMap, Panel panel, MapperCallback callback) {
        this.cMap = cMap;
        this.panel = panel;
        this.callback = callback;
        mapSteps = new ArrayList<>();
        done();
        initDrawable();
    }

    public BaseMapper(CMap cMap, Panel panel) {
        this(cMap, panel, null);
    }

    public boolean redo() {
        if (stepPosition < mapSteps.size() - 1) {
            cMap = mapSteps.get(++stepPosition).clone();
            onStepChange();
            initDrawable();
            return true;
        }
        return false;
    }

    public boolean undo() {
        if (stepPosition > 0) {
            cMap = mapSteps.get(--stepPosition).clone();
            onStepChange();
            initDrawable();
            return true;
        }
        return false;
    }

    protected void done() {
        int size = mapSteps.size();
        if (stepPosition < size-1) {
            mapSteps = mapSteps.subList(0,stepPosition+1);
        }
        mapSteps.add(cMap.clone());
        stepPosition = mapSteps.size() - 1;
        JLog.d(mapSteps);
    }

    protected void onStepChange(){}
//    恢复
    public void recover(){
        JLog.d(getClass().getSimpleName());
        stepPosition = mapSteps.size()-1;
        initDrawable();
    }


    protected void initDrawable() {
        shapePath.reset();
        Vertex vertex;
        int size = cMap.vertices.size();
        for (int i = 0; i < size; i++) {
            vertex = cMap.vertices.get(i);
            if (i == 0) {
                shapePath.moveTo(vertex.x, vertex.y);
            } else {
                shapePath.lineTo(vertex.x, vertex.y);
            }
        }
        shapePath.close();
        panel.invalidate();
    }

    public void drawing() {
        for (Edge e : cMap.edges) {
            e.draw();
        }
        for (Vertex v : cMap.vertices) {
            v.draw();
        }
    }

    public CMap getCMap() {
        return cMap.clone();
    }

    public abstract boolean onTouch(int action, float x, float y);

    @Override
    public BaseMapper clone() throws CloneNotSupportedException {
        return (BaseMapper) super.clone();
    }
}
