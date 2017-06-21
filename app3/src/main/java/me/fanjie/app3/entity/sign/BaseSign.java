package me.fanjie.app3.entity.sign;

import android.graphics.Paint;
import android.text.TextPaint;

import me.fanjie.app3.entity.HoldableMapEntity;

/**
 * Created by dell on 2017/2/10.
 */

public abstract class BaseSign extends HoldableMapEntity {

    protected static Paint textPaint;
    static {
        textPaint = new TextPaint();
        textPaint.setTextSize(30);
    }
}
