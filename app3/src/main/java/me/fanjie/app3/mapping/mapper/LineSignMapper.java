package me.fanjie.app3.mapping.mapper;

import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import me.fanjie.app3.Panel;
import me.fanjie.app3.entity.CMap;
import me.fanjie.app3.entity.Edge;
import me.fanjie.app3.entity.sign.LineSign;
import me.fanjie.app3.entity.sign.UnEvennessException;
import me.fanjie.app3.mapping.Interface.mapperApi.LineSignApi;

import static me.fanjie.app3.ToastUtils.showToast;

/**
 * Created by dell on 2017/2/9.
 */

public class LineSignMapper extends BaseMapper implements LineSignApi {

    private List<Edge> holdenEdges;
    private LineSign holdenSign;

    public LineSignMapper(CMap cMap, Panel panel, MapperCallback callback) {
        super(cMap, panel, callback);
        holdenEdges = new ArrayList<>();
    }


    @Override
    public void addBreakLine() {
        if (holdenEdges.size() == 2) {
            try {
                cMap.breakLine = LineSign.create(holdenEdges.get(0), holdenEdges.get(1), "断开线");
                done();
                initDrawable();
            } catch (UnEvennessException e) {
                showToast("需要选中两条平行线");
            }
        }else {
            showToast("请先选择两天平行边线");
        }
    }

    @Override
    public void addDivideLine() {
        if (holdenEdges.size() == 2) {
            try {
                cMap.divideLine = LineSign.create(holdenEdges.get(0), holdenEdges.get(1), "落差线");
                done();
            } catch (UnEvennessException e) {
                showToast("需要选中两条平行线");
            }
            initDrawable();
        }else {
            showToast("请先选择两天平行边线");
        }
    }

    @Override
    protected void onStepChange() {
        holdenSign = null;
    }

    @Override
    public boolean onTouch(int action, float x, float y) {
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                holdEdge(x, y);
                holdSign(x, y);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (holdenSign != null) {
                    holdenSign.drag(x, y);
                    beDrag = true;
                }
                break;
            }
            case MotionEvent.ACTION_UP:{
                if(beDrag){
                    done();
                    beDrag = false;
                }
            }
        }
        initDrawable();
        return !holdenEdges.isEmpty() || holdenSign != null;
    }

    private void holdSign(float x, float y) {
        holdenSign = null;
        if (cMap.breakLine != null && cMap.breakLine.hold(x, y)) {
            holdenSign = cMap.breakLine;
        }
        if (holdenSign == null && cMap.divideLine != null && cMap.divideLine.hold(x, y)) {
            holdenSign = cMap.divideLine;
        }
        if (holdenSign != null) {
            holdenEdges.clear();
        }
    }

    private void holdEdge(float x, float y) {
        Edge holdEdge = null;
        for (Edge edge : cMap.edges) {
            if (edge.hold(x, y)) {
                holdEdge = edge;
            }
        }
        if (holdEdge == null) {
            holdenEdges.clear();
        } else {
            if (holdenEdges.size() > 1) {
                holdenEdges.remove(1);
            }
            holdenEdges.add(holdEdge);
        }
    }

    @Override
    public void drawing() {
        super.drawing();
        if (cMap.divideLine != null) {
            cMap.divideLine.draw();
        }
        if (cMap.breakLine != null) {
            cMap.breakLine.draw();
        }
        for (Edge edge : holdenEdges) {
            edge.drawHolding();
        }
        if (holdenSign != null) {
            holdenSign.drawHolding();
        }
    }
}
