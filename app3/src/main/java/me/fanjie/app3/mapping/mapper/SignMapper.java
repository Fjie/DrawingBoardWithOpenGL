package me.fanjie.app3.mapping.mapper;

import android.view.MotionEvent;

import me.fanjie.app3.Panel;
import me.fanjie.app3.entity.CMap;
import me.fanjie.app3.entity.PointSignBasin;
import me.fanjie.app3.entity.PointSignKitChen;
import me.fanjie.app3.mapping.Interface.IMapperSignApi;

/**
 * Created by dell on 2017/1/18.
 */

public class SignMapper extends BaseMapper implements IMapperSignApi {


    private boolean addKitChen;
    private boolean addBasin;

    public SignMapper(CMap cMap, Panel panel) {
        super(cMap, panel);
    }

    @Override
    public void addKitchen() {
        addKitChen = true;
    }

    @Override
    public void addBasin() {
        addBasin = true;
    }

    @Override
    public void drawing() {
        super.drawing();
        if (cMap.pointSignKitChen != null) {
            cMap.pointSignKitChen.draw();
        }
        if (cMap.pointSignBasin != null) {
            cMap.pointSignBasin.draw();
        }
    }

    @Override
    public boolean onTouch(int action, float x, float y) {
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                if (addKitChen) {
                    cMap.pointSignKitChen = new PointSignKitChen(x, y);
                    addKitChen = false;
                    initDrawable();
                    break;
                }
                if (addBasin) {
                    cMap.pointSignBasin = new PointSignBasin(x, y, 300, 150, 35);
                    addBasin = false;
                    initDrawable();
                    break;
                }
                initDrawable();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                break;
            }
        }
        return false;
    }
}
