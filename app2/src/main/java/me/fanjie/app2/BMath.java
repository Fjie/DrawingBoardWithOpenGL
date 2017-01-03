package me.fanjie.app2;

/**
 * Created by dell on 2016/12/15.
 */

public class BMath {

    /**
     * 点到直线距离
     */
    public static double getS(float x1, float y1, float x2, float y2, float x3, float y3) {
        return (((y2 - y1) * x3 + (x1 - x2) * y3 + ((x2 * y1) - (x1 * y2)))) / getL(x1, y1, x2, y2);
    }

    /**
     * 两点距离
     */
    public static double getL(float x1, float y1, float x2, float y2) {
        return Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x1 - x2, 2));
    }

}
