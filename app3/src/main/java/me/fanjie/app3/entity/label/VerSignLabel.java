package me.fanjie.app3.entity.label;

import me.fanjie.app3.CPath;
import me.fanjie.app3.entity.Vertex;

/**
 * Created by dell on 2017/2/6.
 */

public class VerSignLabel extends SignLabel {

    public VerSignLabel(Vertex start, Vertex stop) {
        super(start, stop);
        init();
    }

    public VerSignLabel(Vertex assistVertex, Vertex center, int offset) {
        super(assistVertex, center, offset);
        init();
    }

    @Override
    public void setDistance(int distance) {
        float yL = stop.y - start.y;
        stop.y = start.y + distance * (yL / Math.abs(yL));
    }

    @Override
    public void init() {
        startY = start.y;
        stopY = stop.y;
        stopX = startX = stop.x + offset;
        path = new CPath();
        path.moveTo(startX, startY);
        path.lineTo(stopX, stopY);

        startPath = new CPath();
        startPath.moveTo(startX - portOffset, startY);
        startPath.lineTo(startX + portOffset, startY);

        stopPath = new CPath();
        stopPath.moveTo(stopX - portOffset, stopY);
        stopPath.lineTo(stopX + portOffset, stopY);
    }
}
