package me.fanjie.app3.mapping.mapper;

import me.fanjie.app3.Panel;
import me.fanjie.app3.entity.CMap;
import me.fanjie.app3.entity.Edge;
import me.fanjie.app3.entity.Vertex;
import me.fanjie.app3.mapping.Interface.MapperCallback;

import static me.fanjie.app3.entity.CMap.shapePath;

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

    public void drawing(){
        for (Edge e : cMap.edges) {
            e.draw();
        }
        for (Vertex v : cMap.vertices) {
            v.draw();
        }
    }

    public abstract boolean onTouch(int action, float x, float y);

}
