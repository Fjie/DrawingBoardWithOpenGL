package me.fanjie.app3.test.mapping.shape;

import android.view.View;

import java.util.List;

import me.fanjie.app3.test.mapping.Vertex;

/**
 * Created by dell on 2016/12/30.
 */

public class BarShape extends Shape {

    public BarShape(View rootView) {
        super(rootView);
    }

    @Override
    public List<Vertex> getVertices() {
        float x1 = width / 5f;
        float x2 = width - x1;
        float y1 = height / 5f;
        float y2 = y1 + y1 ;
        vertices.add(new Vertex(x1, y1, 0));
        vertices.add(new Vertex(x2, y1, 1));
        vertices.add(new Vertex(x2, y2, 2));
        vertices.add(new Vertex(x1, y2, 3));
        return vertices;
    }
}
