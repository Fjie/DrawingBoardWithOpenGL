package me.fanjie.app2;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static me.fanjie.app2.BMath.getL;
import static me.fanjie.app2.BMath.getS;

/**
 * Created by dell on 2016/12/27.
 */

public class MyRenderer implements GLSurfaceView.Renderer {

    private List<Vertex> vertices;
    private HashSet<Edge> edges;

    //    图形渲染类
    private Shape shape;

    private Vertex holder;
    private Edge edgeHolder;
    //    是否正交
    private boolean orthogonality;
    private boolean initLabel;

    public MyRenderer(MyGLSurface panel) {
        panel.setMyRenderer(this);
        initShape();
    }

    private void initShape() {
        vertices = new ArrayList<>();
        vertices.add(new Vertex(-0.5f, 0.5f, 0));
        vertices.add(new Vertex(0.5f, 0.5f, 1));
        vertices.add(new Vertex(0.5f, -0.5f, 2));
        vertices.add(new Vertex(-0.5f, -0.5f, 3));
        edges = new LinkedHashSet<>();
        initMapping();
        initDrawable();
        orthogonality = true;
        holder = null;
        edgeHolder = null;
    }

    private void initMapping() {
//        考虑到GLThread的异步操作，加个锁，么么哒
        Set<Edge> edgeSet = Collections.synchronizedSet(Collections.synchronizedSet(edges));
        synchronized (edges) {
            int size = vertices.size();
            for (int i = 0; i < size; i++) {
                Vertex v = vertices.get(i);
                if (i == 0) {
                    v.setNeighbor(vertices.get(1));
                    v.setNeighbor(vertices.get(size - 1));
                } else if (i == size - 1) {
                    v.setNeighbor(vertices.get(0));
                    v.setNeighbor(vertices.get(size - 2));
                    edgeSet.add(new Edge(vertices.get(size - 2), v));
                    edgeSet.add(new Edge(v, vertices.get(0)));
                } else {
                    v.setNeighbor(vertices.get(i - 1));
                    v.setNeighbor(vertices.get(i + 1));
                    edgeSet.add(new Edge(vertices.get(i - 1), v));
                }
            }
        }

        initLabel = true;

    }

    private void initDrawable() {
        int size = vertices.size();
        float[] coords = new float[size * 3];
        int i = 0;
        Vertex v;
        for (int j = 0; j < size; j++) {
            v = vertices.get(j);
            v.position = j;
            coords[i++] = v.x;
            coords[i++] = v.y;
            coords[i++] = v.z;
        }
        if (shape != null) {
            shape.setVertexCoords(coords);
        }
        Log.d("XXX", "coords = " + Arrays.toString(coords));
        Log.d("XXX", "edges = " + edges.toString());
    }

    public void test() {

    }

    public void onPanelTouch(int action, float x, float y) {
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                holder = null;
                edgeHolder = null;
                for (Vertex v : vertices) {
                    selectVertex(v, x, y);
                }
                if (holder == null) {
                    for (Edge edge : edges) {
                        selectEdge(edge, x, y);
                    }
                }
                initDrawable();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (holder != null) {
                    holder.x = x;
                    holder.y = y;
                    if (orthogonality) {
                        holder.v.x = x;
                        holder.h.y = y;
                    }
                    initDrawable();
                }
                break;
            }
        }
    }

    public void addGap() {
//        非正交状态下不可开缺
        if (!orthogonality) {
            ToastUtils.showToast("非正交状态下不可开缺");
            return;
        }
        if (holder != null) {
            addVertexGap();
        } else if (edgeHolder != null) {
            addEdgeGap();
        } else {
            ToastUtils.showToast("请选中需要开缺的顶点或者边线");
        }
    }

    public void removeOrthogonality() {
        this.orthogonality = false;
    }

    private void addEdgeGap() {
        Vertex start = edgeHolder.start;
        Vertex stop = edgeHolder.stop;
        int position = start.position;
        float x1, x2, y1, y2;
        Vertex a, b, c, d;
        if (edgeHolder.getDirection() == Edge.EdgeDirection.HOR) {
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
        vertices.add(a.position, a);
        vertices.add(b.position, b);
        vertices.add(c.position, c);
        vertices.add(d.position, d);
        initMapping();
        initDrawable();
        edgeHolder = null;
    }

    public void clear() {
        initShape();
    }

    //    顶点开缺
    private void addVertexGap() {

        //        带方向的偏移量
        double offsetX = holder.h.x - holder.x;
        double offsetY = holder.v.y - holder.y;
//        开缺后原顶点偏移
        float dX = (float) (holder.x + offsetX / 3);
        float dY = (float) (holder.y + offsetY / 3);

        int position = holder.position;
        int pervPosition;
        int nextPosition;
        if (position == 0) {
            pervPosition = vertices.size() - 1;
            nextPosition = 1;
        } else if (position == vertices.size() - 1) {
            pervPosition = position - 1;
            nextPosition = 0;
        } else {
            pervPosition = position - 1;
            nextPosition = position + 1;
//            前一个顶点
        }
        Vertex prevVertex = vertices.get(pervPosition);
//            后一个顶点
        Vertex nextVertex = vertices.get(nextPosition);
//            判断选中顶点的朝向，如果前一个顶点是水平邻居
        if (prevVertex.equals(holder.h)) {
            prevVertex = new Vertex(dX, holder.y, position);
            nextVertex = new Vertex(holder.x, dY, position + 2);
        } else {
            prevVertex = new Vertex(holder.x, dY, position);
            nextVertex = new Vertex(dX, holder.y, position + 2);
        }
        vertices.add(prevVertex.position, prevVertex);
        vertices.add(nextVertex.position, nextVertex);
        holder.x = dX;
        holder.y = dY;
        initMapping();
        initDrawable();
        holder = null;
    }

    private void selectVertex(Vertex v, float x, float y) {
        if (getL(v.x, v.y, x, y) < 0.06) {
            holder = v;
        }
    }

    private void selectEdge(Edge edge, float x, float y) {
        double s = getS(edge.start.x, edge.start.y, edge.stop.x, edge.stop.y, x, y);
        double ac = getL(x, y, edge.start.x, edge.start.y);
        double bc = getL(x, y, edge.stop.x, edge.stop.y);
        double ab = getL(edge.start.x, edge.start.y, edge.stop.x, edge.stop.y);
        boolean select = Math.abs(s) < 0.06 && ac < ab && bc < ab;
        if (select) {
            edgeHolder = edge;
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        shape = new Shape();
        initShape();
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        if (initLabel) {
            for (Edge e : edges) {
                e.initLabel();
            }
            initLabel = false;
        }
//        异步操作加个锁么么哒
        Set<Edge> edgeSet = Collections.synchronizedSet(Collections.synchronizedSet(edges));
        synchronized (edges) {
            for (Edge e : edgeSet) {
                e.drawLabel();
            }
        }

        shape.drawing();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        GLES20.glViewport(0, 0, i, i1);
    }


}
