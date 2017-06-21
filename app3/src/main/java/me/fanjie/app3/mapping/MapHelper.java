package me.fanjie.app3.mapping;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

import me.fanjie.app3.JLog;
import me.fanjie.app3.Panel;
import me.fanjie.app3.entity.BaseMapEntity;
import me.fanjie.app3.entity.CMap;
import me.fanjie.app3.mapping.Interface.mapperApi.AngelApi;
import me.fanjie.app3.mapping.Interface.mapperApi.FormApi;
import me.fanjie.app3.mapping.Interface.mapperApi.LabelApi;
import me.fanjie.app3.mapping.Interface.mapperApi.LineSignApi;
import me.fanjie.app3.mapping.Interface.mapperApi.PointSignApi;
import me.fanjie.app3.mapping.Interface.mapperApi.SideWallApi;
import me.fanjie.app3.mapping.Interface.mapperApi.SizeApi;
import me.fanjie.app3.mapping.mapper.AngelMapper;
import me.fanjie.app3.mapping.mapper.BaseMapper;
import me.fanjie.app3.mapping.mapper.DoneMapper;
import me.fanjie.app3.mapping.mapper.FormMapper;
import me.fanjie.app3.mapping.mapper.LabelDragMapper;
import me.fanjie.app3.mapping.mapper.LabelSizeMapper;
import me.fanjie.app3.mapping.mapper.LineSignMapper;
import me.fanjie.app3.mapping.mapper.MapperCallback;
import me.fanjie.app3.mapping.mapper.PointSignMapper;
import me.fanjie.app3.mapping.mapper.SideWallMapper;


/**
 * Created by dell on 2016/12/24.
 * 制图帮助类，根据设置步骤方法，动态配置代理，好像也没别的卵用
 */

public class MapHelper {

    private BaseMapper mapper;
    private Panel panel;
    private MapperCallback callback;
    private List<BaseMapper> mapperList;

    public MapHelper(Panel panel, MapperCallback callback) {
        this.panel = panel;
        this.callback = callback;
        mapperList = new ArrayList<>();
        panel.setMapHelper(this);
    }

    public boolean nextStep(MappingStep step) {
        CMap cMap;
        if(mapper!=null){
//            把爱心传递下去！
            cMap = mapper.getCMap();
        }else {
            cMap = new CMap();
        }
        switch (step) {
            case FORM: {
                mapper = new FormMapper(cMap, panel);
                break;
            }
            case ANGEL: {
                if (cMap.vertices.size() < 3) {
                    return false;
                }
                mapper = new AngelMapper(cMap, panel);
                break;
            }
            case LABEL_SIZE: {
                mapper = new LabelSizeMapper(cMap, panel, callback);
                break;
            }
            case LABEL_DRAG: {
                mapper = new LabelDragMapper(cMap, panel);
                break;
            }
            case SIDE_WALL: {
                mapper = new SideWallMapper(cMap, panel, callback);
                break;
            }
            case ADD_POINT_SIGN: {
                mapper = new PointSignMapper(cMap, panel, callback);
                break;
            }
            case ADD_LINE_SIGN: {
                mapper = new LineSignMapper(cMap, panel, callback);
                break;
            }
            case DONE:{
                mapper = new DoneMapper(cMap,panel,callback);
                break;
            }
            default:{
                return false;
            }

        }
        try {
            mapperList.add(mapper.clone());
            JLog.d(mapperList);
        }catch (CloneNotSupportedException e) {
            JLog.e(e);
        }
        return true;
    }
    public boolean preStep(){
        int size = mapperList.size();
        if(size >1){
            mapper = mapperList.get(size-2);
            mapper.recover();
            mapperList.remove(size-1);
        }
        return false;
    }

    public boolean onPanelTouch(int action, float x, float y) {
        return mapper.onTouch(action, x, y);
    }

    public void drawing(Canvas canvas) {
        BaseMapEntity.setCanvas(canvas);
        mapper.drawing();
    }

    public FormApi getFormApi() {
        if (mapper == null || !(mapper instanceof FormApi)) {
            throw new RuntimeException("兄弟你的步骤不对");
        }
        return (FormApi) mapper;
    }

    public AngelApi getAngelApi() {
        if (mapper == null || !(mapper instanceof AngelApi)) {
            throw new RuntimeException("兄弟你的步骤不对");
        }
        return (AngelApi) mapper;
    }

    public LabelApi getLabelApi() {
        if (mapper == null || !(mapper instanceof LabelApi)) {
            throw new RuntimeException("兄弟你的步骤不对");
        }
        return (LabelApi) mapper;
    }

    public SizeApi getSizeApi() {
        if (mapper == null || !(mapper instanceof SizeApi)) {
            throw new RuntimeException("兄弟你的步骤不对");
        }
        return (SizeApi) mapper;
    }

    public SideWallApi getSideWallApi() {
        if (mapper == null || !(mapper instanceof SideWallApi)) {
            throw new RuntimeException("兄弟你的步骤不对");
        }
        return (SideWallApi) mapper;
    }

    public PointSignApi getPointSignApi() {
        if (mapper == null || !(mapper instanceof PointSignApi)) {
            throw new RuntimeException("兄弟你的步骤不对");
        }
        return (PointSignApi) mapper;
    }

    public LineSignApi getLineSignApi() {
        if (mapper == null || !(mapper instanceof LineSignApi)) {
            throw new RuntimeException("兄弟你的步骤不对");
        }
        return (LineSignApi) mapper;
    }

    public BaseMapper getMapper() {
        return mapper;
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
        //        添加点型标记
        ADD_POINT_SIGN,
        //        添加线型标记
        ADD_LINE_SIGN,
        //        完成
        DONE
    }

}
