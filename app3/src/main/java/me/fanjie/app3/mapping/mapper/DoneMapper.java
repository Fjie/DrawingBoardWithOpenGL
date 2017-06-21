package me.fanjie.app3.mapping.mapper;

import me.fanjie.app3.Panel;
import me.fanjie.app3.entity.CMap;
import me.fanjie.app3.entity.Edge;
import me.fanjie.app3.entity.Vertex;
import me.fanjie.app3.entity.label.DragVertexLabel;

/**
 * Created by dell on 2017/2/10.
 */

public class DoneMapper extends BaseMapper {

    public DoneMapper(CMap cMap, Panel panel, MapperCallback callback) {
        super(cMap, panel, callback);
    }

    @Override
    public void drawing() {
        if (cMap.hole != null) {
            cMap.hole.draw();
        }
        for (Edge e : cMap.edges) {
            e.draw();
            e.drawLabel();
            e.drawSideWall();
        }
        for (Vertex v : cMap.vertices) {
            v.draw();
        }
        for (DragVertexLabel l : cMap.vertexLabels) {
            l.draw();
        }
        if (cMap.kitChen != null) {
            cMap.kitChen.draw();
        }
        if (cMap.basin != null) {
            cMap.basin.draw();
        }
        if (cMap.divideLine != null) {
            cMap.divideLine.draw();
        }
        if (cMap.breakLine != null) {
            cMap.breakLine.draw();
        }
    }

    @Override
    public boolean onTouch(int action, float x, float y) {
        return false;
    }
}
