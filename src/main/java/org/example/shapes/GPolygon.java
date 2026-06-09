package org.example.shapes;

import java.awt.*;
import java.awt.geom.RectangularShape;

public class GPolygon extends GNPointShape {

    public GPolygon() {
        super();
        this.shape = new Polygon();
    }

    @Override
    protected Shape cloneShape() {
        Polygon p = (Polygon) this.shape;
        return new Polygon(p.xpoints, p.ypoints, p.npoints);
    }

    @Override
    public void setLocation0(int x, int y) {
        Polygon p = (Polygon) this.shape;
        p.addPoint(x, y);
        p.addPoint(x, y);
    }

    @Override
    public void setLocation1(int x, int y) {
        Polygon p = (Polygon) this.shape;
        if (p.npoints > 0) {
            p.xpoints[p.npoints - 1] = x;
            p.ypoints[p.npoints - 1] = y;
            p.invalidate(); // Bounds 재계산을 위해 무효화
        }
    }

    @Override
    public void addPoint(int x, int y) {
        Polygon p = (Polygon) this.shape;
        p.addPoint(x, y);
    }
}