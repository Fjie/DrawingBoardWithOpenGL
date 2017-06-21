package me.fanjie.app3.mapping.mapper;

import android.view.MotionEvent;

import me.fanjie.app3.Panel;
import me.fanjie.app3.ShapeUtils;
import me.fanjie.app3.entity.CMap;
import me.fanjie.app3.entity.Edge;
import me.fanjie.app3.entity.Vertex;
import me.fanjie.app3.entity.label.SignLabel;
import me.fanjie.app3.entity.sign.Basin;
import me.fanjie.app3.entity.sign.Hole;
import me.fanjie.app3.entity.sign.KitChen;
import me.fanjie.app3.entity.sign.RangePointSign;
import me.fanjie.app3.mapping.Interface.mapperApi.PointSignApi;

import static me.fanjie.app3.ToastUtils.showToast;

/**
 * Created by dell on 2017/1/18.
 */

public class PointSignMapper extends BaseMapper implements PointSignApi {


    private boolean addKitChen;
    private boolean addBasin;
    private boolean addHole;

    private RangePointSign holdenPointSign;
    private Hole holdenHole;
    private Vertex holdenVertex;
    private Edge holdenEdge;
    private SignLabel holdenLabel;

    private int rAngel;
    private int width;
    private int height;
    private Basin.Type type;

    public PointSignMapper(CMap cMap, Panel panel, MapperCallback callback) {
        super(cMap, panel, callback);
    }


    @Override
    public void addKitchen(int rAngel, int width, int height) {
        addKitChen = true;
        this.rAngel = rAngel;
        this.width = width;
        this.height = height;
    }

    @Override
    public void addBasin(Basin.Type type, int rAngel, int width, int height) {
        addBasin = true;
        this.type = type;
        this.rAngel = rAngel;
        this.width = width;
        this.height = height;
    }

    @Override
    public void addHole() {
        addHole = true;
    }

    @Override
    public boolean addHorSignLabel() {
        if (holdenPointSign != null) {
            if (holdenVertex != null) {
                holdenPointSign.addHorLabel(holdenVertex);
                done();
            } else if (holdenEdge != null && holdenEdge.getDirection() == Edge.Direction.VER) {
                holdenPointSign.addHorLabel(holdenEdge.start);
                done();
            } else {
                showToast("需要选择一个顶点或者垂直边线");
            }
            initDrawable();
        }
        return false;
    }

    @Override
    public boolean addVerSignLabel() {
        if (holdenPointSign != null) {
            if (holdenVertex != null) {
                holdenPointSign.addVerLabel(holdenVertex);
                done();
            } else if (holdenEdge != null && holdenEdge.getDirection() == Edge.Direction.HOR) {
                holdenPointSign.addVerLabel(holdenEdge.start);
                done();
            } else {
                showToast("需要选择一个顶点或者水平边线");
            }
            initDrawable();
        }
        return false;
    }

    @Override
    public void setDistance(int distance) {
        if (holdenLabel != null) {
            holdenLabel.setDistance(distance);
            holdenLabel = null;
            done();
            initDrawable();
        }
    }


    @Override
    public void drawing() {

        if (cMap.hole != null) {
            cMap.hole.draw();
        }

        if (holdenHole != null) {
            holdenHole.drawHolding();
        }

        for (Edge e : cMap.edges) {
            e.draw();
        }
        for (Vertex v : cMap.vertices) {
            v.draw();
        }

        if (cMap.kitChen != null) {
            cMap.kitChen.draw();
        }
        if (cMap.basin != null) {
            cMap.basin.draw();
        }


        if (holdenPointSign != null) {
            holdenPointSign.drawHolding();
        }
        if (holdenVertex != null) {
            holdenVertex.drawHolding();
        }
        if (holdenEdge != null) {
            holdenEdge.drawHolding();
        }


    }

    @Override
    public boolean onTouch(int action, float x, float y) {
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                holdenHole(x, y);
                holdLabel(x, y);
                holdVertex(x, y);
                if (holdenVertex == null && holdenEdge == null) {
                    holdenPointSign = null;
                    if (cMap.kitChen != null && cMap.kitChen.hold(x, y)) {
                        holdenPointSign = cMap.kitChen;
                    } else if (cMap.basin != null && cMap.basin.hold(x, y)) {
                        holdenPointSign = cMap.basin;
                    }
                    if (addKitChen && addKitChenDone(x, y)) {
                        initDrawable();
                        break;
                    }
                    if (addBasin && addBasinDone(x, y)) {
                        initDrawable();
                        break;
                    }
                    if (addHole && addHoleDone(x, y)) {
                        initDrawable();
                        break;
                    }
                }
                initDrawable();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (holdenPointSign != null && holdenVertex == null && holdenEdge == null) {
                    holdenPointSign.drag(x, y);
                    beDrag = true;
                }
                if (holdenHole != null) {
                    holdenHole.drag(x, y);
                    beDrag = true;
                }
                initDrawable();
                break;
            }
            case MotionEvent.ACTION_UP:{
                if(beDrag){
                    done();
                    beDrag = false;
                }
            }
        }
        return holdenPointSign != null || holdenVertex != null || holdenEdge != null || holdenHole != null;
    }

    private boolean addHoleDone(float x, float y) {
        for (Vertex v : cMap.vertices) {
            if (v.hold(x, y)) {
                holdenHole = cMap.hole = new Hole(v);
                addHole = false;
                done();
                return true;
            }
        }

        for (Edge edge : cMap.edges) {
            if (edge.hold(x, y)) {
                holdenHole = cMap.hole = new Hole(x, y, edge);
                addHole = false;
                done();
                return true;
            }
        }

        showToast("包管位需要在边线或顶点上");
        addHole = false;
        return false;
    }

    private boolean addBasinDone(float x, float y) {
        if (ShapeUtils.inMap(x, y)) {
            holdenPointSign = cMap.basin = new Basin(x, y, type,rAngel,width,height);
            addBasin = false;
            done();
            return true;
        } else {
            showToast("选点须在图形内");
            return false;
        }
    }

    private boolean addKitChenDone(float x, float y) {
        if (ShapeUtils.inMap(x, y)) {
            holdenPointSign = cMap.kitChen = new KitChen(x, y,rAngel,width,height);
            addKitChen = false;
            done();
            return true;
        } else {
            showToast("选点须在图形内");
            return false;
        }
    }

    private void holdenHole(float x, float y) {
        holdenHole = null;
        if (holdenPointSign == null && cMap.hole != null && cMap.hole.hold(x, y)) {
            holdenHole = cMap.hole;
        }
    }

    private void holdLabel(float x, float y) {
        holdenLabel = null;
        if (cMap.basin != null) {
            holdenLabel = cMap.basin.holdLabel(x, y);
        }
        if (holdenLabel == null && cMap.kitChen != null) {
            holdenLabel = cMap.kitChen.holdLabel(x, y);
        }
        if (holdenLabel == null && cMap.hole != null) {
            holdenLabel = cMap.hole.holdLabel(x, y);
        }
        if (holdenLabel != null) {
            callback.onSignLabelClick();
        }
    }

    private void holdVertex(float x, float y) {
        holdenVertex = null;
        holdenEdge = null;
        if (holdenPointSign != null) {
            for (Vertex v : cMap.vertices) {
                if (v.hold(x, y)) {
                    holdenVertex = v;
                }
            }
            if (holdenVertex == null) {
                for (Edge e : cMap.edges) {
                    if (e.hold(x, y)) {
                        holdenEdge = e;
                    }
                }
            }
        }
    }

    @Override
    protected void onStepChange() {
        holdenHole = null;
        holdenPointSign = null;
        holdenLabel = null;
    }
}
