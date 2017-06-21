package me.fanjie.app3.mapping.mapper;

import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import me.fanjie.app3.Panel;
import me.fanjie.app3.entity.CMap;
import me.fanjie.app3.entity.Edge;
import me.fanjie.app3.entity.SideWall;
import me.fanjie.app3.entity.SideWallSlip;
import me.fanjie.app3.entity.Vertex;
import me.fanjie.app3.mapping.Interface.mapperApi.SideWallApi;

import static me.fanjie.app3.ToastUtils.showToast;

/**
 * Created by dell on 2017/1/24.
 */

public class SideWallMapper extends BaseMapper implements SideWallApi {

    private List<Edge> holdenEdges;
    private SideWallSlip holdenWallSlip;

    public SideWallMapper(CMap cMap, Panel panel, MapperCallback callback) {
        super(cMap, panel, callback);
        holdenEdges = new ArrayList<>();
    }

    @Override
    public void addSideWall(SideWall.Type type) {
        if (!holdenEdges.isEmpty()) {
            for (Edge edge : holdenEdges) {
                edge.addSideWall(type);
            }
            holdenEdges.clear();
            done();
            initDrawable();
        }
    }

    @Override
    public void addSideWallSlip() {
        if (holdenEdges.size() == 1) {
            cMap.sideWallSlip = new SideWallSlip(holdenEdges.get(0));
            done();
            initDrawable();
        } else {
            showToast("只需选中一条边线");
        }
    }

    @Override
    public void setSlipSideWall(int size, SideWall.Type type) {
        if (holdenWallSlip != null && holdenWallSlip.setChildSideWall(size, type)) {
            done();
            initDrawable();
        } else {
            showToast("尺寸不能超出中长");
        }
    }

    @Override
    public void setSlipSideWall(SideWall.Type type) {
        if (holdenWallSlip != null && holdenWallSlip.setChildSideWall(type)) {
            done();
            initDrawable();
        }
    }

    @Override
    public void drawing() {
        for (Edge e : cMap.edges) {
            e.draw();
            e.drawSideWall();
        }
        for (Vertex v : cMap.vertices) {
            v.draw();
        }
        if (cMap.sideWallSlip != null) {
            cMap.sideWallSlip.draw();
        }
        if (!holdenEdges.isEmpty()) {
            for (Edge edge : holdenEdges) {
                edge.drawHolding();
            }
        }
        if (holdenWallSlip != null) {
            holdenWallSlip.drawHolding();
        }
    }

    @Override
    public boolean onTouch(int action, float x, float y) {
        if (action == MotionEvent.ACTION_DOWN) {
            holdSideWallSlip(x, y);
            if (holdenWallSlip == null) {
                holdEdge(x, y);
            }
            initDrawable();
        }
        return !holdenEdges.isEmpty();
    }

    private void holdSideWallSlip(float x, float y) {
        holdenWallSlip = null;
        if (cMap.sideWallSlip != null && cMap.sideWallSlip.hold(x, y)) {
            holdenWallSlip = cMap.sideWallSlip;
            holdenEdges.clear();
            callback.onSideWallSlipClick(holdenWallSlip.getType());
        }
    }

    private void holdEdge(float x, float y) {
        Edge holdenEdge = null;
        for (Edge e : cMap.edges) {
            if (e.hold(x, y)) {
                holdenEdge = e;
            }
        }
        if (holdenEdge == null) {
            holdenEdges.clear();
        } else {
            holdenEdges.add(holdenEdge);
        }
    }


}
