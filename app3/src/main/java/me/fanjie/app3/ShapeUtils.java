package me.fanjie.app3;

import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;

import me.fanjie.app3.entity.CMap;
import me.fanjie.app3.entity.Edge;
import me.fanjie.app3.entity.Vertex;

/**
 * Created by dell on 2017/1/11.
 */

public class ShapeUtils {
    /**
     * 已知直线和点做垂直线
     */
    public static CPath getPerpendicularLine(Vertex start,Vertex stop,float x,float y,int size){
        double a = Math.atan((stop.y - start.y) / (stop.x - start.x));
        double c = Math.PI / 2 - a;
        double b1 = Math.PI / 2 - c;
        double bh = size * Math.sin(c);
        double ch = size * Math.sin(b1);
        float cX = (float) (x - ch);
        float cY = (float) (y + bh);
        float dX = (float) (x + ch);
        float dY = (float) (y - bh);
        CPath path = new CPath();
        path.moveTo(cX, cY);
        path.lineTo(dX, dY);
        return path;
    }
    public static Path getPerpendicularLine(Vertex start,Vertex stop,Vertex v,int size){
        return getPerpendicularLine(start,stop,v.x,v.y,size);
    }
    public static Path getPerpendicularLine(Edge edge,Vertex v,int size){
        return getPerpendicularLine(edge.start,edge.stop,v,size);
    }



    /**
     * 设置直线长度，变化stop方向
     */
    public static void setLineSize(Vertex start,Vertex stop,int size){
        double v = Math.atan((stop.x - start.x) / (stop.y - start.y));
        v = Math.abs(v);
        float bc = (float) (size * Math.sin(v));
        float ab = (float) (size * Math.cos(v));
        stop.x = stop.x > start.x ? start.x + bc : start.x - bc;
        stop.y = stop.y > start.y ? start.y + ab : start.y - ab;
    }

    /**
     * 判断点是否在path内
     */
    public static boolean pointInPath(float x, float y, Path path){
        if (path == null){
            return false;
        }
        Region re = new Region();
        RectF r = new RectF();
        //计算控制点的边界
        path.computeBounds(r, true);
        //设置区域路径和剪辑描述的区域
        re.setPath(path, new Region((int) r.left, (int) r.top, (int) r.right, (int) r.bottom));
        return re.contains((int) x, (int) y);
    }

    /**
     * 判断点是否在CMap的path内
     */
    public static boolean inMap(float x, float y){
        return pointInPath(x,y, CMap.shapePath);
    }
}
