package me.fanjie.app3.mapping.mapper;

import me.fanjie.app3.entity.SideWall;

/**
 * Created by dell on 2017/1/19.
 */

public abstract class MapperCallback implements Cloneable{
    public void onEdgeClick() {}

    public void onSignLabelClick(){}

    public void onSideWallSlipClick(SideWall.Type currentType){

    }

    @Override
    protected MapperCallback clone() throws CloneNotSupportedException {
        return (MapperCallback) super.clone();
    }
}
