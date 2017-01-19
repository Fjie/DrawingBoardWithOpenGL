package me.fanjie.app3.mapping.initShape;

import android.view.View;

import java.util.List;

import me.fanjie.app3.entity.Vertex;

/**
 * Created by dell on 2016/12/30.
 */

public class LShape extends Shape {
    public LShape(View rootView) {
        super(rootView);
    }

    @Override
    public List<Vertex> getVertices() {
        float x1 = width / 5f;
        float y1 = height / 5f;
        float x2 = width - x1;
        float y2 = height - y1;
        float x3 = x2 - x1;
        float y3 = y1 + x1;

        vertices.add(new Vertex(x1, y1, 0));
        vertices.add(new Vertex(x2, y1, 1));
        vertices.add(new Vertex(x2, y2, 2));
        vertices.add(new Vertex(x3, y2, 3));
        vertices.add(new Vertex(x3, y3, 4));
        vertices.add(new Vertex(x1, y3, 5));
        return vertices;
    }
}
