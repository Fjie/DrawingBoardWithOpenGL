package me.fanjie.drawingboardwithopengl.drawable;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import me.fanjie.drawingboardwithopengl.ToastUtils;
import me.fanjie.drawingboardwithopengl.models.ShapeEdge;
import me.fanjie.drawingboardwithopengl.test.mapping.Vertex;
import me.fanjie.drawingboardwithopengl.test.renderer.IDrawable;

import static me.fanjie.drawingboardwithopengl.BMath.getL;
import static me.fanjie.drawingboardwithopengl.BMath.getS;
import static me.fanjie.drawingboardwithopengl.C.fragmentShaderCode;
import static me.fanjie.drawingboardwithopengl.C.vertexShaderCode;
import static me.fanjie.drawingboardwithopengl.OpenGLUtlis.loadShader;

/**
 * Created by dell on 2016/12/17.
 */

public class VariableShape implements IDrawable, IDeformation {
//    绘制相关域
    private FloatBuffer vertexBuffer;
    private int mProgram;
    private float color[] = {1f, 1f, 1f, 1f};
//    图形相关域
    private List<Vertex> vertexList;
    private Vertex holder;
//    逻辑相关域
    private LinkedHashSet<ShapeEdge> shapeEdges;
    private ShapeEdge edgeHolder;
    //    是否正交
    private boolean orthogonality;



    public VariableShape() {
        ByteBuffer bb = ByteBuffer.allocateDirect(100 * 3 * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        initShape();


        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);
        mProgram = GLES20.glCreateProgram();

        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    private void initShape() {
        vertexList = new ArrayList<>();
        vertexList.add(new Vertex(-0.5f, 0.5f, 0));
        vertexList.add(new Vertex(0.5f, 0.5f, 1));
        vertexList.add(new Vertex(0.5f, -0.5f, 2));
        vertexList.add(new Vertex(-0.5f, -0.5f, 3));
        shapeEdges = new LinkedHashSet<>();
        orthogonality = true;
        holder = null;
        edgeHolder = null;
        initNeighbor();
        initVertexBuffer();
    }

    private void initNeighbor() {
        int size = vertexList.size();
        for (int i = 0; i < size; i++) {
            Vertex v = vertexList.get(i);
            if (i == 0) {
                v.setNeighbor(vertexList.get(1));
                v.setNeighbor(vertexList.get(size - 1));
            } else if (i == size - 1) {
                v.setNeighbor(vertexList.get(0));
                v.setNeighbor(vertexList.get(size - 2));
                shapeEdges.add(new ShapeEdge(vertexList.get(size - 2), v));
                shapeEdges.add(new ShapeEdge(v, vertexList.get(0)));
            } else {
                v.setNeighbor(vertexList.get(i - 1));
                v.setNeighbor(vertexList.get(i + 1));
                shapeEdges.add(new ShapeEdge(vertexList.get(i - 1), v));
            }
        }
    }

    private void selectVertex(Vertex v, float x, float y) {
        if (getL(v.x, v.y, x, y) < 0.06) {
            holder = v;
        }
    }

    private void selectEdge(ShapeEdge edge, float x, float y) {
        double s = getS(edge.start.x, edge.start.y, edge.stop.x, edge.stop.y, x, y);
        double ac = getL(x, y, edge.start.x, edge.start.y);
        double bc = getL(x, y, edge.stop.x, edge.stop.y);
        double ab = getL(edge.start.x, edge.start.y, edge.stop.x, edge.stop.y);
        boolean select = Math.abs(s) < 0.06 && ac < ab && bc < ab;
        if (select) {
            edgeHolder = edge;
        }
    }

    public void clear() {
        initShape();
    }

    //    取消正交，不可取消后重新设置为正交
    public void removeOrthogonality() {
        this.orthogonality = false;
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

    private void addEdgeGap() {
        Vertex start = edgeHolder.start;
        Vertex stop = edgeHolder.stop;
        int position = start.position;
        float x1, x2, y1, y2;
        Vertex a, b, c, d;
        if (edgeHolder.getDirection() == ShapeEdge.EdgeDirection.HOR) {
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
        vertexList.add(a.position, a);
        vertexList.add(b.position, b);
        vertexList.add(c.position, c);
        vertexList.add(d.position, d);
        initNeighbor();
        initVertexBuffer();
        edgeHolder = null;
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
            pervPosition = vertexList.size() - 1;
            nextPosition = 1;
        } else if (position == vertexList.size() - 1) {
            pervPosition = position - 1;
            nextPosition = 0;
        } else {
            pervPosition = position - 1;
            nextPosition = position + 1;
//            前一个顶点
        }
        Vertex prevVertex = vertexList.get(pervPosition);
//            后一个顶点
        Vertex nextVertex = vertexList.get(nextPosition);
//            判断选中顶点的朝向，如果前一个顶点是水平邻居
        if (prevVertex.equals(holder.h)) {
            prevVertex = new Vertex(dX, holder.y, position);
            nextVertex = new Vertex(holder.x, dY, position + 2);
        } else {
            prevVertex = new Vertex(holder.x, dY, position);
            nextVertex = new Vertex(dX, holder.y, position + 2);
        }
        vertexList.add(prevVertex.position, prevVertex);
        vertexList.add(nextVertex.position, nextVertex);
        holder.x = dX;
        holder.y = dY;
        initNeighbor();
        initVertexBuffer();
        holder = null;
    }


    @Override
    public void drawing() {
//        Log.d("XXX", "vertexList.size() = " + vertexList.size());
        GLES20.glUseProgram(mProgram);
        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glVertexAttribPointer(mPositionHandle, 3,
                GLES20.GL_FLOAT, false,
                3 * 4, vertexBuffer);

        int mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        GLES20.glLineWidth(10);
        GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, vertexList.size());
        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }

    @Override
    public void putVertexCoords(float[] coords) {

    }

    private void initVertexBuffer() {
        int size = vertexList.size();
        float[] vertexes = new float[size * 3];
        int i = 0;
        Vertex v;
        for (int j = 0; j < size; j++) {
            v = vertexList.get(j);
            v.position = j;
            vertexes[i++] = v.x;
            vertexes[i++] = v.y;
            vertexes[i++] = v.z;
        }
        vertexBuffer.put(vertexes);
        vertexBuffer.position(0);
        Log.d("XXX", "vertexes = " + Arrays.toString(vertexes));
        Log.d("XXX", "shapeEdges = " + shapeEdges.toString());
        Log.d("XXX", "holder = " + holder);
        Log.d("XXX", "edgeHolder = " + edgeHolder);
    }

    @Override
    public void down(float x, float y) {
        holder = null;
        edgeHolder = null;
        for (Vertex v : vertexList) {
            selectVertex(v, x, y);
        }
        if (holder == null) {
            for (ShapeEdge edge : shapeEdges) {
                selectEdge(edge, x, y);
            }
        }
        initVertexBuffer();
    }


    @Override
    public void move(float x, float y) {
        if (holder != null) {
            holder.x = x;
            holder.y = y;
            if (orthogonality) {
                holder.v.x = x;
                holder.h.y = y;
            }
            initVertexBuffer();
        }
    }

    @Override
    public void up(float x, float y) {

    }


}
