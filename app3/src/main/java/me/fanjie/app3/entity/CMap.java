package me.fanjie.app3.entity;

import android.graphics.Path;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/1/18. 台面图纸数据,
 */

public class CMap {

    public static Path shapePath = new Path();

    public List<Vertex> vertices;
    public List<Edge> edges;
    public List<Label> vertexLabels;
    public PointSignKitChen pointSignKitChen;
    public PointSignBasin pointSignBasin;


    public CMap() {
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
        vertexLabels = new ArrayList<>();
    }
}
