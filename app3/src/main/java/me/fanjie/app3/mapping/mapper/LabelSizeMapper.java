package me.fanjie.app3.mapping.mapper;

import android.view.MotionEvent;

import me.fanjie.app3.Panel;
import me.fanjie.app3.ToastUtils;
import me.fanjie.app3.entity.CMap;
import me.fanjie.app3.entity.Edge;
import me.fanjie.app3.entity.Label;
import me.fanjie.app3.entity.Vertex;
import me.fanjie.app3.mapping.Interface.IMapperLabelApi;
import me.fanjie.app3.mapping.Interface.IMapperSizeApi;
import me.fanjie.app3.mapping.Interface.MapperCallback;

/**
 * Created by dell on 2017/1/18.
 * TODO 设置边线长度的时候 不改变与临边的角度 类似正♂交拖动的算法
 * TODO 添加点击回调；将设置长度放到边线里面。比较符合操作习惯
 */

public class LabelSizeMapper extends BaseMapper implements IMapperSizeApi, IMapperLabelApi {
    private Integer edgeSize;

    public LabelSizeMapper(CMap cMap, Panel panel, MapperCallback callback) {
        super(cMap, panel, callback);
        for (Edge edge : cMap.edges) {
            edge.initLabel();
        }
        initDrawable();
    }

    @Override
    public void addEdgeLabel() {
        if (cMap.edgeHolder != null) {
            cMap.edgeHolder.initLabel();
            initDrawable();
        }
    }

    @Override
    public void addVertexLabel(Label.Type type) {
        if (cMap.vertexHolder != null && cMap.vertexAssistHolder != null) {
            cMap.vertexLabels.add(new Label(cMap.vertexHolder, cMap.vertexAssistHolder, type));
            initDrawable();
        }
    }

    @Override
    public boolean setSize(int length) {
        if (cMap.edgeHolder != null) {
            edgeSize = length;
            cMap.edgeHolder.setInitSize(true);
            return true;
        }
        return false;
    }

    public boolean setSizeOld(int length) {

        if (cMap.labelHolder != null) {
            cMap.labelHolder.setLength(length);
            for (Edge edge : cMap.edges) {
                if (edge.label != null) {
                    edge.initLabel();
                }
            }
            for (Label label : cMap.vertexLabels) {
                label.init();
            }
            initDrawable();
            return true;
        } else {
            ToastUtils.showToast("您需要先选择一个标注再进行设置");
            return false;
        }
    }

    @Override
    public boolean onTouch(int action, float x, float y) {
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                if (edgeSize != null) {
                    cMap.edgeHolder.setSize(edgeSize, x, y);
                    edgeSize = null;
                }
                cMap.edgeHolder = null;
                cMap.vertexAssistHolder = null;
                cMap.labelHolder = null;
                if (cMap.vertexHolder != null) {
                    for (Vertex v : cMap.vertices) {
                        holdAssistVertex(v, x, y);
                    }
                }
                if (cMap.vertexAssistHolder == null) {
                    cMap.vertexHolder = null;
                    for (Vertex v : cMap.vertices) {
                        holdVertex(v, x, y);
                    }
                }

                if (cMap.vertexHolder == null) {
                    for (Edge edge : cMap.edges) {
                        holdEdge(edge, x, y);
                    }
                    if (cMap.edgeHolder != null && callback != null) {
                        callback.onEdgeClick(cMap.edgeHolder);
                    }
                }
                initDrawable();
                if (cMap.vertexHolder != null || cMap.edgeHolder != null || cMap.labelHolder != null) {
                    return true;
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (cMap.vertexHolder != null || cMap.edgeHolder != null || cMap.labelHolder != null) {
                    return true;
                }
                break;
            }
        }


        return false;
    }
}
