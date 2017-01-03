package me.fanjie.app3.test.mapping.shape;

import android.view.View;

import java.util.List;

import me.fanjie.app3.test.mapping.Vertex;

/**
 * Created by dell on 2016/12/30.
 */

public class UShape extends Shape {
    public UShape(View rootView) {
        super(rootView);
    }

    @Override
    public List<Vertex> getVertices() {
        float x1 = width / 5f;
        float x2 = x1 + x1;
        float x3 = x2 + x1;
        float x4 = width - x1;
        float y1 = height/5;
        float y2 = y1 + x1;
        float y3 = height - y1;
        vertices.add(new Vertex(x1,y1,0));
        vertices.add(new Vertex(x4,y1,1));
        vertices.add(new Vertex(x4,y3,2));
        vertices.add(new Vertex(x3,y3,3));
        vertices.add(new Vertex(x3,y2,4));
        vertices.add(new Vertex(x2,y2,5));
        vertices.add(new Vertex(x2,y3,6));
        vertices.add(new Vertex(x1,y3,7));
        return vertices;
    }
}
