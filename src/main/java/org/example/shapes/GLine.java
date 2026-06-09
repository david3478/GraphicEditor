package org.example.shapes;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;

public class GLine extends GShape {

    private int startX, startY;

    public GLine() {
        super();
        this.shape = new Line2D.Double();
    }

    @Override
    protected Shape cloneShape() {
        return (Shape) ((Line2D) this.shape).clone();
    }

    @Override
    public void setLocation0(int x, int y) {
        this.startX = x;
        this.startY = y;
        ((Line2D) this.shape).setLine(x, y, x, y);
    }

    @Override
    public void setLocation1(int x, int y) {
        ((Line2D) this.shape).setLine(this.startX, this.startY, x, y);
    }

    @Override
    public EAnchor onShape(int x, int y) {
        // GShape.onShape 시도
        EAnchor anchor = super.onShape(x, y);
        if (anchor != null) {
            return anchor;
        }

        Point p = new Point(x, y);
        try {
            this.affineTransform.inverseTransform(p, p);
        } catch (NoninvertibleTransformException e) {
            throw new RuntimeException(e);
        }

        // onShape 기준: 마우스와 선분 사이의 거리가 5픽셀 이내
        java.awt.geom.Line2D line = (java.awt.geom.Line2D) this.shape;
        if (line.ptSegDist(p) <= 10.0) {
            return EAnchor.eMove;
        }

        return null;
    }
}