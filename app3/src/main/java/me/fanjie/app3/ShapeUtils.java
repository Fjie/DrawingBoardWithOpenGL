package me.fanjie.app3;

import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;

/**
 * Created by dell on 2017/1/11.
 */

public class ShapeUtils {

    public static boolean pointInPath(float x, float y, Path path){
        Region re = new Region();
        RectF r = new RectF();
        //计算控制点的边界
        path.computeBounds(r, true);
        //设置区域路径和剪辑描述的区域
        re.setPath(path, new Region((int) r.left, (int) r.top, (int) r.right, (int) r.bottom));
        return re.contains((int) x, (int) y);
    }
}
