package me.fanjie.app3.entity.label;

import me.fanjie.app3.CPath;
import me.fanjie.app3.entity.Vertex;

/**
 * Created by dell on 2017/2/6.
 */

public class HorSignLabel extends SignLabel {

    public HorSignLabel(Vertex start, Vertex stop) {
        super(start, stop);
        init();
    }

    public HorSignLabel(Vertex assistVertex, Vertex center, int offset) {
        super(assistVertex, center, offset);
        init();
    }

    @Override
    public void setDistance(int distance) {
        float xL = stop.x - start.x;
        stop.x = start.x + distance * (xL /Math.abs(xL));
    }

    @Override
    public void init() {
        startX = start.x;
        stopY = startY = stop.y + offset;
        stopX = stop.x;
        path = new CPath();
        path.moveTo(startX, startY);
        path.lineTo(stopX, stopY);

        startPath = new CPath();
        startPath.moveTo(startX,startY - 10);
        startPath.lineTo(startX,startY + 10);

        stopPath = new CPath();
        stopPath.moveTo(stopX,stopY - 10);
        stopPath.lineTo(stopX,stopY + 10);

    }
}
