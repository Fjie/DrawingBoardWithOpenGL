package me.fanjie.app3.entity;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.io.Serializable;

/**
 * Created by dell on 2017/1/20.
 * TODO 需要抽象出一个绘制实体基类或者接口
 */

public abstract class BaseMapEntity implements Serializable {
    protected static Canvas canvas;
    protected static Paint basePaint;


    static {
        basePaint = new Paint();
        basePaint.setAntiAlias(true);
        basePaint.setDither(true);
    }
    public abstract void draw();

    public static void setCanvas(Canvas canvas) {
        BaseMapEntity.canvas = canvas;
    }
}
