package me.fanjie.app3.test.mapping;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.fanjie.app3.Panel;
import me.fanjie.app3.test.mapping.Interface.IMapperAngelApi;
import me.fanjie.app3.test.mapping.Interface.IMapperFormApi;
import me.fanjie.app3.test.mapping.Interface.IMapperLabelApi;
import me.fanjie.app3.test.mapping.shape.Shape;

import static me.fanjie.app3.BMath.getL;
import static me.fanjie.app3.BMath.getS;
import static me.fanjie.app3.ToastUtils.showToast;


/**
 * Created by dell on 2016/12/24.
 */

public class Mapper implements IMapperFormApi, IMapperAngelApi, IMapperLabelApi {
//    制图域

    private List<Vertex> vertices;
    private List<Edge> edges;
    private List<Label> vertexLabels;
    private Vertex vertexHolder;
    private Vertex vertexAssistHolder;
    private Edge edgeHolder;

//    渲染域

    private Panel panel;
    private Paint vertexPaint;
    private Paint edgePaint;
    private Paint holderVertexPaint;
    private Paint holderEdgePaint;
    private Paint labelPaint;
    private TextPaint textPaint;


//    配置域

    //    正交
    private boolean orthogonality;
    //    可形变
    private boolean deformable;
    //    顶点hold得住
    private boolean vertexHoldable;
    private boolean vertexAssistHoldable;
    //    边线hold得住
    private boolean edgeHoldable;

//    其他域

    private MappingStep step;

    public Mapper(Panel panel) {
        panel.setMapper(this);
        this.panel = panel;
        edgePaint = new Paint();
        edgePaint.setColor(Color.BLACK);
        edgePaint.setStrokeWidth(5);
        edgePaint.setStyle(Paint.Style.STROKE);
        edgePaint.setAntiAlias(true);
        edgePaint.setDither(true);
        vertexPaint = new Paint(edgePaint);
        vertexPaint.setStyle(Paint.Style.FILL);
        holderEdgePaint = new Paint(edgePaint);
        holderEdgePaint.setColor(Color.RED);
        holderEdgePaint.setStrokeWidth(10);
        holderVertexPaint = new Paint(holderEdgePaint);
        holderVertexPaint.setStyle(Paint.Style.FILL);
        labelPaint = new Paint(edgePaint);
        labelPaint.setStrokeWidth(2);
        textPaint = new TextPaint();
        textPaint.setTextSize(50);
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
        vertexLabels = new ArrayList<>();
    }

    public boolean setStep(MappingStep step) {
        switch (step) {
            case FORM: {
                orthogonality = true;
                deformable = true;
                vertexHoldable = true;
                edgeHoldable = true;
                vertexAssistHoldable = false;
                return true;
            }
            case ANGEL: {
                if (vertices.size() < 3) {
                    return false;
                }
                orthogonality = false;
                deformable = false;
                edgeHoldable = false;
                vertexHoldable = true;
                vertexAssistHoldable = false;
                return true;
            }
            case SIZE_LABEL: {
                orthogonality = false;
                deformable = false;
                edgeHoldable = true;
                vertexHoldable = true;
                vertexAssistHoldable = true;
                return true;
            }

        }
        this.step = step;
        return false;
    }

    @Override
    public void addShape(Shape shape) {
        vertices = shape.getVertices();
        vertexHolder = null;
        edgeHolder = null;
        initMapping();
        initDrawable();
    }

    @Override
    public void addGap() {
//        非正交状态下不可开缺
        if (!orthogonality) {
            showToast("非正交状态下不可开缺");
            return;
        }
        if (vertexHolder != null) {
            addVertexGap();
        } else if (edgeHolder != null) {
            addEdgeGap();
        } else {
            showToast("请选中需要开缺的顶点或者边线");
        }
    }

    @Override
    public void setAngel(int angel, Edge.Direction direction) {
        if (vertexHolder != null) {
            Log.d("XXX", "angel = " + angel + ", direction = " + direction);
            double a = (90 - angel) * (Math.PI / 180);
            if (direction == Edge.Direction.HOR) {
                float ab = Math.abs(vertexHolder.h.x - vertexHolder.x);
                float v = vertexHolder.v.y - vertexHolder.y;
                float pn = v / Math.abs(v);
                double bc = ab * Math.tan(a) * pn;
                vertexHolder.h.y = (float) (vertexHolder.y + bc);
            } else {
                float ab = Math.abs(vertexHolder.v.y - vertexHolder.y);
                float v = vertexHolder.h.x - vertexHolder.x;
                float pn = v / Math.abs(v);
                double bc = ab * Math.tan(a) * pn;
                vertexHolder.v.x = (float) (vertexHolder.x + bc);
            }
            initDrawable();

        }
    }

    @Override
    public void addEdgeLabel() {
        if (edgeHolder != null) {
            edgeHolder.initLabel();
            initDrawable();
        }
    }

    @Override
    public void addVertexLabel(Label.Type type) {
        if(vertexHolder!=null && vertexAssistHolder != null){
            vertexLabels.add(new Label(vertexHolder,vertexAssistHolder,type));
            initDrawable();
        }
    }

    public boolean onPanelTouch(int action, float x, float y) {
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                edgeHolder = null;
                vertexAssistHolder = null;
                if (vertexAssistHoldable && vertexHolder != null) {
                    for (Vertex v : vertices) {
                        holdAssistVertex(v, x, y);
                    }
                }
                if (vertexAssistHolder == null) {
                    vertexHolder = null;
                    if (vertexHoldable) {
                        for (Vertex v : vertices) {
                            holdVertex(v, x, y);
                        }
                    }
                }


                if (edgeHoldable) {
                    if (vertexHolder == null) {
                        for (Edge edge : edges) {
                            holdEdge(edge, x, y);
                        }
                    }
                }
                initDrawable();
                if (vertexHolder != null || edgeHolder != null) {
                    return true;
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (deformable) {
                    if (vertexHolder != null) {
                        vertexHolder.x = x;
                        vertexHolder.y = y;
                        if (orthogonality) {
                            vertexHolder.v.x = x;
                            vertexHolder.h.y = y;
                        }
                    } else if (edgeHolder != null) {
                        if (edgeHolder.getDirection() == Edge.Direction.HOR) {
                            edgeHolder.start.y = y;
                            edgeHolder.stop.y = y;
                        } else if (edgeHolder.getDirection() == Edge.Direction.VER) {
                            edgeHolder.start.x = x;
                            edgeHolder.stop.x = x;
                        }
                    }
                    initDrawable();
                }
                if (vertexHolder != null || edgeHolder != null) {
                    return true;
                }
                break;
            }
        }
        return false;
    }


    public void drawing(Canvas canvas) {
        Log.d("XXX", "vertices  = " + Arrays.toString(vertices.toArray()));
        if (vertices.size() < 3) {
            return;
        }
        for (Edge e : edges) {
            e.drawLine(canvas, edgePaint);
            e.drawLabel(canvas, labelPaint, textPaint);
        }

        if (edgeHolder != null) {
            edgeHolder.drawLine(canvas, holderEdgePaint);
        }

        for (Vertex v : vertices) {
            v.drawCircle(canvas, 15, vertexPaint);
        }

        if (vertexHolder != null) {
            vertexHolder.drawCircle(canvas, 20, holderVertexPaint);
        }

        if (vertexAssistHolder != null) {
            vertexAssistHolder.drawCircle(canvas, 20, holderVertexPaint);
        }

        for (Label l : vertexLabels) {
            l.drawLabel(canvas, labelPaint, textPaint);
        }

    }

    // 初始化制图
    private void initMapping() {
        int size = vertices.size();
        edges.clear();
        for (int i = 0; i < size; i++) {
            Vertex v = vertices.get(i);
            v.position = i;
            if (i == 0) {
                v.setNeighbor(vertices.get(1));
                v.setNeighbor(vertices.get(size - 1));
            } else if (i == size - 1) {
                v.setNeighbor(vertices.get(0));
                v.setNeighbor(vertices.get(size - 2));
                edges.add(new Edge(vertices.get(size - 2), v));
                edges.add(new Edge(v, vertices.get(0)));
            } else {
                v.setNeighbor(vertices.get(i - 1));
                v.setNeighbor(vertices.get(i + 1));
                edges.add(new Edge(vertices.get(i - 1), v));
            }
        }

    }

    //   顶点选择
    private void holdVertex(Vertex v, float x, float y) {
        if (getL(v.x, v.y, x, y) < 40) {
            vertexHolder = v;
        }
    }

    private void holdAssistVertex(Vertex v, float x, float y) {
        if (getL(v.x, v.y, x, y) < 40) {
            vertexAssistHolder = v;
        }
    }

    //    顶点开缺
    private void addVertexGap() {
        //        带方向的偏移量
        double offsetX = vertexHolder.h.x - vertexHolder.x;
        double offsetY = vertexHolder.v.y - vertexHolder.y;
//        开缺后原顶点偏移
        float dX = (float) (vertexHolder.x + offsetX / 3);
        float dY = (float) (vertexHolder.y + offsetY / 3);

        int position = vertexHolder.position;
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
        Vertex nextVertex;
//            判断选中顶点的朝向，如果前一个顶点是水平邻居
        if (prevVertex.equals(vertexHolder.h)) {
            prevVertex = new Vertex(dX, vertexHolder.y, position);
            nextVertex = new Vertex(vertexHolder.x, dY, position + 2);
        } else {
            prevVertex = new Vertex(vertexHolder.x, dY, position);
            nextVertex = new Vertex(dX, vertexHolder.y, position + 2);
        }
        vertices.add(prevVertex.position, prevVertex);
        vertices.add(nextVertex.position, nextVertex);
        vertexHolder.x = dX;
        vertexHolder.y = dY;
        initMapping();
        initDrawable();
        vertexHolder = null;
    }

    // 边线选择
    private void holdEdge(Edge edge, float x, float y) {


        double s = getS(edge.start.x, edge.start.y, edge.stop.x, edge.stop.y, x, y);
        double ac = getL(x, y, edge.start.x, edge.start.y);
        double bc = getL(x, y, edge.stop.x, edge.stop.y);
        double ab = getL(edge.start.x, edge.start.y, edge.stop.x, edge.stop.y);
        boolean select = Math.abs(s) < 40 && ac < ab && bc < ab;
        if (select) {
            edgeHolder = edge;
        }
    }

    // 边线开缺
    private void addEdgeGap() {
        Vertex start = edgeHolder.start;
        Vertex stop = edgeHolder.stop;
        int position = start.position;
        float x1, x2, y1, y2;
        Vertex a, b, c, d;
        if (edgeHolder.getDirection() == Edge.Direction.HOR) {
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

    //    初始化渲染
    private void initDrawable() {
        panel.invalidate();
    }


    //    制图步骤
    public enum MappingStep {
        //        定型
        FORM,
        //        角度
        ANGEL,
        //        尺寸标注
        SIZE_LABEL,
        //        标注定点
        LABEL_DRAG,
        //        标记
    }

}
