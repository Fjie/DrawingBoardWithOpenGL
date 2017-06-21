package me.fanjie.app3.entity;

import android.graphics.Path;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.fanjie.app3.JLog;
import me.fanjie.app3.entity.label.DragVertexLabel;
import me.fanjie.app3.entity.sign.Basin;
import me.fanjie.app3.entity.sign.Hole;
import me.fanjie.app3.entity.sign.KitChen;
import me.fanjie.app3.entity.sign.LineSign;

/**
 * Created by dell on 2017/1/18. 台面图纸数据,
 */

public class CMap implements Cloneable,Serializable {

    public static Path shapePath = new Path();

    public List<Vertex> vertices;
    public List<Edge> edges;
    public List<DragVertexLabel> vertexLabels;
    public KitChen kitChen;
    public Basin basin;
    public Hole hole;
    public LineSign breakLine;
    public LineSign divideLine;
    public SideWallSlip sideWallSlip;


    public CMap() {
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
        vertexLabels = new ArrayList<>();
    }

    @Override
    public CMap clone() {
        CMap clone = null;
        try {
            clone =  byteClone();
        } catch (IOException | ClassNotFoundException e) {
            JLog.e(e);
        }
        return clone;
    }

    private CMap byteClone() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream obs = new ObjectOutputStream(out);
        obs.writeObject(this);
        obs.close();
        //分配内存，写入原始对象，生成新对象
        CMap clone;
        ByteArrayInputStream ios = new  ByteArrayInputStream(out.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(ios);
        //返回生成的新对象
        clone =  (CMap) ois.readObject();
        ois.close();
        return clone;
    }

//    @Override
//    public String toString() {
//        return "CMap{" +
////                "vertices=" + vertices +
//                ", edges=" + edges +
////                ", vertexLabels=" + vertexLabels +
////                ", kitChen=" + kitChen +
////                ", basin=" + basin +
////                ", hole=" + hole +
////                ", breakLine=" + breakLine +
////                ", divideLine=" + divideLine +
////                ", sideWallSlip=" + sideWallSlip +
//                '}';
//    }
}
