package me.fanjie.app3.mapping.Interface.mapperApi;

import me.fanjie.app3.entity.SideWall;

/**
 * Created by dell on 2017/1/24.
 */

public interface SideWallApi {
    void addSideWall(SideWall.Type type);
    void addSideWallSlip();

    void setSlipSideWall(int input, SideWall.Type type);
    void setSlipSideWall(SideWall.Type type);
}
