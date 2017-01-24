package me.fanjie.app3.mapping.mapper;

import me.fanjie.app3.Panel;
import me.fanjie.app3.entity.CMap;
import me.fanjie.app3.entity.Edge;
import me.fanjie.app3.entity.Label;
import me.fanjie.app3.entity.Vertex;
import me.fanjie.app3.mapping.Interface.MapperCallback;

import static me.fanjie.app3.BMath.getL;
import static me.fanjie.app3.BMath.getS;

/**
 * Created by dell on 2017/1/18.
 */

public abstract class BaseMapper {
    protected CMap cMap;
    protected Panel panel;
    protected MapperCallback callback;

    public BaseMapper(CMap cMap, Panel panel, MapperCallback callback) {
        this.cMap = cMap;
        this.panel = panel;
        this.callback = callback;
        initDrawable();
    }

    public BaseMapper(CMap cMap, Panel panel) {
        this(cMap,panel,null);
    }
    protected void initDrawable() {
        cMap.shapePath.reset();
        Vertex vertex;
        int size = cMap.vertices.size();
        for (int i = 0; i < size; i++) {
            vertex = cMap.vertices.get(i);
            if (i == 0) {
                cMap.shapePath.moveTo(vertex.x, vertex.y);
            } else {
                cMap.shapePath.lineTo(vertex.x, vertex.y);
            }
        }
        cMap.shapePath.close();
        panel.invalidate();
    }

    public void drawing(){
        for (Edge e : cMap.edges) {
            e.draw();
        }

        for (Vertex v : cMap.vertices) {
            v.draw();
        }
    }

    //   顶点选择
    protected void holdVertex(Vertex v, float x, float y) {
        if (getL(v.x, v.y, x, y) < 40) {
            cMap.vertexHolder = v;
        }
    }

    protected void holdAssistVertex(Vertex v, float x, float y) {
        if (getL(v.x, v.y, x, y) < 40) {
            cMap.vertexAssistHolder = v;
        }
    }

    // 边线选择
    protected void holdEdge(Edge edge, float x, float y) {
        double s = getS(edge.start.x, edge.start.y, edge.stop.x, edge.stop.y, x, y);
        double ac = getL(x, y, edge.start.x, edge.start.y);
        double bc = getL(x, y, edge.stop.x, edge.stop.y);
        double ab = getL(edge.start.x, edge.start.y, edge.stop.x, edge.stop.y);
        boolean select = Math.abs(s) < 40 && ac < ab && bc < ab;
        if (select) {
            cMap.edgeHolder = edge;
        }
    }

    protected void holdLabel(Label label, float x, float y) {
        if (label != null && label.inLabel(x, y)) {
            cMap.labelHolder = label;
        }
    }
    public abstract boolean onTouch(int action, float x, float y);

}
