package me.fanjie.app3.mapping.mapper;

import android.view.MotionEvent;

import me.fanjie.app3.Panel;
import me.fanjie.app3.entity.CMap;
import me.fanjie.app3.entity.Edge;
import me.fanjie.app3.entity.Label;
import me.fanjie.app3.entity.Vertex;
import me.fanjie.app3.mapping.Interface.IMapperLabelApi;
import me.fanjie.app3.mapping.Interface.IMapperSizeApi;
import me.fanjie.app3.mapping.Interface.MapperCallback;

/**
 * Created by dell on 2017/1/18.
 */

public class LabelSizeMapper extends BaseMapper implements IMapperSizeApi, IMapperLabelApi {
    private Edge holdenEdge;
    private Edge settingSizeEdge;
    private Vertex holdenVertex;
    private Vertex assistHoldenVertex;

    private int edgeSize;

    public LabelSizeMapper(CMap cMap, Panel panel, MapperCallback callback) {
        super(cMap, panel, callback);
        for (Edge edge : cMap.edges) {
            edge.initLabel();
        }
        initDrawable();
    }

    @Override
    public void addVertexLabel(Label.Type type) {
        if (holdenVertex != null && assistHoldenVertex != null) {
            cMap.vertexLabels.add(new Label(holdenVertex, assistHoldenVertex, type));
            initDrawable();
        }
    }

    @Override
    public boolean setSize(int length) {
        if (holdenEdge != null) {
            settingSizeEdge = holdenEdge;
            edgeSize = length;
            initDrawable();
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouch(int action, float x, float y) {
        if (action == MotionEvent.ACTION_DOWN) {
            if (settingSizeEdge != null) {
                settingSize(x,y);
            }else {
                holdingEntity(x,y);
            }
            initDrawable();
        }
        return holdenVertex != null || holdenEdge != null;
    }

    private void holdingEntity(float x, float y) {
        holdenEdge = null;
        assistHoldenVertex = null;
        if (holdenVertex != null) {
            for (Vertex v : cMap.vertices) {
                if (v.hold(x, y)) {
                    assistHoldenVertex = v;
                }
            }
        }
        if (assistHoldenVertex == null) {
            holdenVertex = null;
            for (Vertex v : cMap.vertices) {
                if (v.hold(x, y)) {
                    holdenVertex = v;
                }
            }
        }
        if (holdenVertex == null) {
            for (Edge edge : cMap.edges) {
                if (edge.hold(x, y)) {
                    holdenEdge = edge;
                }
            }
            if (holdenEdge != null && callback != null) {
                callback.onEdgeClick(holdenEdge);
            }
        }
    }

    private void settingSize(float x,float y) {
        if (holdenEdge.setSize(edgeSize, x, y)) {
            initLabel();
        }
        settingSizeEdge = null;
        holdenEdge = null;
    }

    @Override
    public void drawing() {
        for (Edge e : cMap.edges) {
            e.draw();
            e.drawLabel();
        }
        for (Vertex v : cMap.vertices) {
            v.draw();
        }
        for (Label l : cMap.vertexLabels) {
            l.draw();
        }
        if (holdenEdge != null) {
            holdenEdge.drawHolding();
        }
        if (holdenVertex != null) {
            holdenVertex.drawHolding();
        }
        if (assistHoldenVertex != null) {
            assistHoldenVertex.drawHolding();
        }
        if (settingSizeEdge != null) {
            settingSizeEdge.drawSettingSize();
        }
    }

    private void initLabel() {
        for (Edge edge : cMap.edges) {
            if (edge.label != null) {
                edge.initLabel();
            }
        }
        for (Label label : cMap.vertexLabels) {
            label.init();
        }
    }

}
