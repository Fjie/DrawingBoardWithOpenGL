package me.fanjie.app3.mapping.mapper;

import android.view.MotionEvent;

import me.fanjie.app3.Panel;
import me.fanjie.app3.entity.CMap;
import me.fanjie.app3.entity.Edge;
import me.fanjie.app3.entity.Label;

/**
 * Created by dell on 2017/1/18.
 */

public class LabelDragMapper extends BaseMapper {

    public LabelDragMapper(CMap cMap, Panel panel) {
        super(cMap, panel);
    }

    @Override
    public boolean onTouch(int action, float x, float y) {
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                cMap.edgeHolder = null;
                cMap.vertexAssistHolder = null;
                cMap.labelHolder = null;
                cMap.vertexHolder = null;
                for (Edge edge : cMap.edges) {
                    holdLabel(edge.label, x, y);
                }
                if (cMap.labelHolder == null) {
                    for (Label label : cMap.vertexLabels) {
                        holdLabel(label, x, y);
                    }
                }
                initDrawable();
                if (cMap.vertexHolder != null || cMap.edgeHolder != null || cMap.labelHolder != null) {
                    return true;
                }
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                if (cMap.labelHolder != null) {
                    cMap.labelHolder.drag(x, y);
                    initDrawable();
                }
                if (cMap.vertexHolder != null || cMap.edgeHolder != null || cMap.labelHolder != null) {
                    return true;
                }
                break;
            }

        }
        return false;
    }
}
