package me.fanjie.app3.mapping;

import android.graphics.Canvas;

import me.fanjie.app3.Panel;
import me.fanjie.app3.entity.CMap;
import me.fanjie.app3.entity.HoldableMapEntity;
import me.fanjie.app3.mapping.Interface.IMapperAngelApi;
import me.fanjie.app3.mapping.Interface.IMapperFormApi;
import me.fanjie.app3.mapping.Interface.IMapperLabelApi;
import me.fanjie.app3.mapping.Interface.IMapperSideWallApi;
import me.fanjie.app3.mapping.Interface.IMapperSignApi;
import me.fanjie.app3.mapping.Interface.IMapperSizeApi;
import me.fanjie.app3.mapping.Interface.MapperCallback;
import me.fanjie.app3.mapping.mapper.AngelMapper;
import me.fanjie.app3.mapping.mapper.BaseMapper;
import me.fanjie.app3.mapping.mapper.FormMapper;
import me.fanjie.app3.mapping.mapper.LabelDragMapper;
import me.fanjie.app3.mapping.mapper.LabelSizeMapper;
import me.fanjie.app3.mapping.mapper.SideWallMapper;
import me.fanjie.app3.mapping.mapper.SignMapper;


/**
 * Created by dell on 2016/12/24.
 * 制图帮助类，根据设置步骤方法，动态配置代理，好像也没别的卵用
 * TODO 考虑是不是把绘制的任务弄到CMap模型里面去，这个类为什么要绘制？
 */

public class MapHelper {

    private static MapHelper mapHelper;

    public CMap cMap;
    private BaseMapper mapper;
    private Panel panel;
    private MapperCallback callback;

    public static MapHelper getInstance() {
        return mapHelper;
    }

    public MapHelper(Panel panel, MapperCallback callback) {
        this.panel = panel;
        this.callback = callback;
        mapHelper = this;
        panel.setMapHelper(this);
        cMap = new CMap();
    }

    public boolean setStep(MappingStep step) {
        switch (step) {
            case FORM: {
                mapper = new FormMapper(cMap, panel);
                return true;
            }
            case ANGEL: {
                if (cMap.vertices.size() < 3) {
                    return false;
                }
                mapper = new AngelMapper(cMap, panel);
                return true;
            }
            case LABEL_SIZE: {
                mapper = new LabelSizeMapper(cMap, panel, callback);
                return true;
            }
            case LABEL_DRAG: {
                mapper = new LabelDragMapper(cMap, panel);
                return true;
            }
            case SIDE_WALL:{
                mapper = new SideWallMapper(cMap,panel,callback);
                return true;
            }
            case ADD_SIGN: {
                mapper = new SignMapper(cMap, panel);
                return true;
            }

        }
        return false;
    }

    public boolean onPanelTouch(int action, float x, float y) {
        return mapper.onTouch(action, x, y);
    }

    public void drawing(Canvas canvas) {
        int size = cMap.vertices.size();
        if (size < 3) {
            return;
        }
        HoldableMapEntity.setCanvas(canvas);
        mapper.drawing();
    }

    public IMapperFormApi getFormApi() {
        if (mapper == null || !(mapper instanceof IMapperFormApi)) {
            throw new RuntimeException("兄弟你的步骤不对");
        }
        return (IMapperFormApi) mapper;
    }

    public IMapperAngelApi getAngelApi() {
        if (mapper == null || !(mapper instanceof IMapperAngelApi)) {
            throw new RuntimeException("兄弟你的步骤不对");
        }
        return (IMapperAngelApi) mapper;
    }

    public IMapperLabelApi getLabelApi() {
        if (mapper == null || !(mapper instanceof IMapperLabelApi)) {
            throw new RuntimeException("兄弟你的步骤不对");
        }
        return (IMapperLabelApi) mapper;
    }

    public IMapperSizeApi getSizeApi() {
        if (mapper == null || !(mapper instanceof IMapperSizeApi)) {
            throw new RuntimeException("兄弟你的步骤不对");
        }
        return (IMapperSizeApi) mapper;
    }

    public IMapperSideWallApi getSideWallApi() {
        if (mapper == null || !(mapper instanceof IMapperSideWallApi)) {
            throw new RuntimeException("兄弟你的步骤不对");
        }
        return (IMapperSideWallApi) mapper;
    }

    public IMapperSignApi getSignApi() {
        if (mapper == null || !(mapper instanceof IMapperSignApi)) {
            throw new RuntimeException("兄弟你的步骤不对");
        }
        return (IMapperSignApi) mapper;
    }

    //    制图步骤
    public enum MappingStep {
        //        定型
        FORM,
        //        角度
        ANGEL,
        //        尺寸标注
        LABEL_SIZE,
        //        标注定点
        LABEL_DRAG,
        //        添加挡水下垂
        SIDE_WALL,
        //        添加标记
        ADD_SIGN
    }

}
