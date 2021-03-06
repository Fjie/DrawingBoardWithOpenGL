package me.fanjie.app3.mapping.mapper;

import android.view.MotionEvent;

import me.fanjie.app3.Panel;
import me.fanjie.app3.entity.CMap;
import me.fanjie.app3.entity.Edge;
import me.fanjie.app3.entity.Vertex;
import me.fanjie.app3.entity.label.DragLabel;
import me.fanjie.app3.entity.label.DragVertexLabel;

/**
 * Created by dell on 2017/1/18.
 */

public class LabelDragMapper extends BaseMapper {

    private DragLabel holdenLabel;

    public LabelDragMapper(CMap cMap, Panel panel) {
        super(cMap, panel);
    }

    @Override
    public void drawing() {
        for (Edge e : cMap.edges) {
            e.draw();
            e.drawLabel();
        }
        for (Vertex v : cMap.vertices) {
            v.draw();
        }
        for (DragVertexLabel l : cMap.vertexLabels) {
            l.draw();
        }
        if (holdenLabel != null) {
            holdenLabel.drawHolding();
        }
    }

    @Override
    protected void onStepChange() {
        holdenLabel = null;
    }

    @Override
    public boolean onTouch(int action, float x, float y) {
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                holdenLabel = null;
                for (Edge edge : cMap.edges) {
                    if (edge.label != null) {
                        if (edge.label.hold(x, y)) {
                            holdenLabel = edge.label;
                        }
                    }
                }
                if (holdenLabel == null) {
                    for (DragVertexLabel label : cMap.vertexLabels) {
                        if (label.hold(x, y)) {
                            holdenLabel = label;
                        }
                    }
                }
                initDrawable();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (holdenLabel != null) {
                    holdenLabel.drag(x, y);
                    beDrag = true;
                    initDrawable();
                }
                break;
            }
            case MotionEvent.ACTION_UP:{
                if(beDrag){
                    done();
                    beDrag = false;
                }
                break;
            }

        }
        return holdenLabel != null;
    }

}
