package me.fanjie.app3.mapping.mapper;

import android.view.MotionEvent;

import me.fanjie.app3.Panel;
import me.fanjie.app3.entity.CMap;
import me.fanjie.app3.entity.Edge;
import me.fanjie.app3.entity.Vertex;
import me.fanjie.app3.mapping.Interface.IMapperFormApi;
import me.fanjie.app3.mapping.initShape.Shape;

import static me.fanjie.app3.ToastUtils.showToast;

/**
 * Created by dell on 2017/1/18.
 */
public class FormMapper extends BaseMapper implements IMapperFormApi {


    public FormMapper(CMap cMap, Panel panel) {
        super(cMap, panel);
    }

    @Override
    public void addGap() {
        if (cMap.vertexHolder != null) {
            addVertexGap();
        } else if (cMap.edgeHolder != null) {
            addEdgeGap();
        } else {
            showToast("请选中需要开缺的顶点或者边线");
        }
    }

    @Override
    public void addShape(Shape shape) {
        cMap.vertices = shape.getVertices();
        cMap.vertexHolder = null;
        cMap.edgeHolder = null;
        initMapping();
        initDrawable();
    }

    @Override
    public boolean onTouch(int action, float x, float y) {
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                cMap.edgeHolder = null;
                cMap.vertexHolder = null;
                for (Vertex v : cMap.vertices) {
                    holdVertex(v, x, y);
                }
                if (cMap.vertexHolder == null) {
                    for (Edge edge : cMap.edges) {
                        holdEdge(edge, x, y);
                    }
                }
                initDrawable();
                if (cMap.vertexHolder != null || cMap.edgeHolder != null || cMap.labelHolder != null) {
                    return true;
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (cMap.vertexHolder != null) {
                    cMap.vertexHolder.x = x;
                    cMap.vertexHolder.y = y;
                    cMap.vertexHolder.v.x = x;
                    cMap.vertexHolder.h.y = y;

                } else if (cMap.edgeHolder != null) {
                    if (cMap.edgeHolder.getDirection() == Edge.Direction.HOR) {
                        cMap.edgeHolder.start.y = y;
                        cMap.edgeHolder.stop.y = y;
                    } else if (cMap.edgeHolder.getDirection() == Edge.Direction.VER) {
                        cMap.edgeHolder.start.x = x;
                        cMap.edgeHolder.stop.x = x;
                    }
                }
                initDrawable();
                if (cMap.vertexHolder != null || cMap.edgeHolder != null || cMap.labelHolder != null) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    private void addVertexGap() {
        //        带方向的偏移量
        double offsetX = cMap.vertexHolder.h.x - cMap.vertexHolder.x;
        double offsetY = cMap.vertexHolder.v.y - cMap.vertexHolder.y;
//        开缺后原顶点偏移
        float dX = (float) (cMap.vertexHolder.x + offsetX / 3);
        float dY = (float) (cMap.vertexHolder.y + offsetY / 3);

        int position = cMap.vertexHolder.position;
        int pervPosition;
        int nextPosition;
        if (position == 0) {
            pervPosition = cMap.vertices.size() - 1;
            nextPosition = 1;
        } else if (position == cMap.vertices.size() - 1) {
            pervPosition = position - 1;
            nextPosition = 0;
        } else {
            pervPosition = position - 1;
            nextPosition = position + 1;
//            前一个顶点
        }
        Vertex prevVertex = cMap.vertices.get(pervPosition);
//            后一个顶点
        Vertex nextVertex;
//            判断选中顶点的朝向，如果前一个顶点是水平邻居
        if (prevVertex.equals(cMap.vertexHolder.h)) {
            prevVertex = new Vertex(dX, cMap.vertexHolder.y, position);
            nextVertex = new Vertex(cMap.vertexHolder.x, dY, position + 2);
        } else {
            prevVertex = new Vertex(cMap.vertexHolder.x, dY, position);
            nextVertex = new Vertex(dX, cMap.vertexHolder.y, position + 2);
        }
        cMap.vertices.add(prevVertex.position, prevVertex);
        cMap.vertices.add(nextVertex.position, nextVertex);
        cMap.vertexHolder.x = dX;
        cMap.vertexHolder.y = dY;
        initMapping();
        initDrawable();
        cMap.vertexHolder = null;
    }

    // 边线开缺
    private void addEdgeGap() {
        Vertex start = cMap.edgeHolder.start;
        Vertex stop = cMap.edgeHolder.stop;
        int position = start.position;
        float x1, x2, y1, y2;
        Vertex a, b, c, d;
        if (cMap.edgeHolder.getDirection() == Edge.Direction.HOR) {
            float hLength = (stop.x - start.x) / 3;
            float v1 = (start.v.y - start.y) / 3;
            float v2 = (stop.v.y - stop.y) / 3;
            float vLength = Math.abs(v1) < Math.abs(v2) ? v1 : v2;
            x1 = start.x + hLength;
            x2 = x1 + hLength;
            y1 = start.y;
            y2 = start.y + vLength;
            a = new Vertex(x1, y1, position + 1);
            b = new Vertex(x1, y2, position + 2);
            c = new Vertex(x2, y2, position + 3);
            d = new Vertex(x2, y1, position + 4);
        } else {
            float vLength = (stop.y - start.y) / 3;
            float v1 = (start.h.x - start.x) / 3;
            float v2 = (stop.h.x - stop.x) / 3;

            float hLength = Math.abs(v1) < Math.abs(v2) ? v1 : v2;
            y1 = start.y + vLength;
            y2 = y1 + vLength;
            x1 = start.x;
            x2 = start.x + hLength;
            a = new Vertex(x1, y1, position + 1);
            b = new Vertex(x2, y1, position + 2);
            c = new Vertex(x2, y2, position + 3);
            d = new Vertex(x1, y2, position + 4);
        }
        cMap.vertices.add(a.position, a);
        cMap.vertices.add(b.position, b);
        cMap.vertices.add(c.position, c);
        cMap.vertices.add(d.position, d);
        initMapping();
        initDrawable();
        cMap.edgeHolder = null;
    }

    private void initMapping() {
        int size = cMap.vertices.size();
        cMap.edges.clear();
        for (int i = 0; i < size; i++) {
            Vertex v = cMap.vertices.get(i);
            v.position = i;
            if (i == 0) {
                v.setNeighbor(cMap.vertices.get(1));
                v.setNeighbor(cMap.vertices.get(size - 1));
            } else if (i == size - 1) {
                v.setNeighbor(cMap.vertices.get(0));
                v.setNeighbor(cMap.vertices.get(size - 2));
                cMap.edges.add(new Edge(cMap.vertices.get(size - 2), v));
                cMap.edges.add(new Edge(v, cMap.vertices.get(0)));
            } else {
                v.setNeighbor(cMap.vertices.get(i - 1));
                v.setNeighbor(cMap.vertices.get(i + 1));
                cMap.edges.add(new Edge(cMap.vertices.get(i - 1), v));
            }
        }

    }
}
