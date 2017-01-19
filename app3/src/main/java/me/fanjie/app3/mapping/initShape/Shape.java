package me.fanjie.app3.mapping.initShape;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import me.fanjie.app3.entity.Vertex;

/**
 * Created by dell on 2016/12/30.
 */

public abstract class Shape {

    protected List<Vertex> vertices;
    protected float width;
    protected float height;

    public Shape(View rootView) {
        width = rootView.getWidth();
        height = rootView.getHeight();
        vertices = new ArrayList<>();
    }

    public abstract List<Vertex> getVertices();
}
