package me.fanjie.app3.mapping.Interface;

import me.fanjie.app3.entity.Edge;
import me.fanjie.app3.entity.Vertex;

/**
 * Created by dell on 2017/1/19.
 */

public interface MapperCallback {
    void onEdgeClick(Edge edge);
    void onEdgeVertexClick(Edge edge,Vertex vertex);
}
