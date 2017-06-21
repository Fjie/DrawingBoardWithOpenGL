package me.fanjie.app3.mapping.Interface.mapperApi;

import me.fanjie.app3.entity.sign.Basin;

/**
 * Created by dell on 2017/1/11.
 */

public interface PointSignApi {

    void addKitchen(int rAngel, int width, int height);
    void addBasin(Basin.Type type, int rAngel, int width, int height);
    boolean addHorSignLabel();
    boolean addVerSignLabel();
    void setDistance(int distance);
    void addHole();
}
