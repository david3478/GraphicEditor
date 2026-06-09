package org.example.shapes;

import java.awt.Shape;
import java.awt.geom.RectangularShape;

public abstract class G2PointShape extends GShape {
    protected int startX, startY;

    @Override
    protected Shape cloneShape() {
        return (Shape) ((RectangularShape) this.shape).clone();
    }

    @Override
    public void setLocation0(int x, int y) {
        this.startX = x;
        this.startY = y;

        ((RectangularShape) this.shape).setFrame(x, y, 0, 0);
    }

    @Override
    public void setLocation1(int x, int y) {
        int minX = Math.min(x, startX);
        int minY = Math.min(y, startY);
        int width = Math.abs(x - startX);
        int height = Math.abs(y - startY);

        ((RectangularShape) this.shape).setFrame(minX, minY, width, height);
    }
}