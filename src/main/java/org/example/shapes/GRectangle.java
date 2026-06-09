package org.example.shapes;

import java.awt.*;

public class GRectangle extends G2PointShape {

    public GRectangle() {
        this.shape = new Rectangle();
    }

//    public void setLocation0(int x, int y) {
//        Rectangle e = (Rectangle) shape;
//        e.setFrame(x, y, 0, 0);
//    }
//
//    public void setLocation1(int x, int y) {
//        Rectangle r = (Rectangle) shape;
//        double w = x - r.getX();
//        double h = y - r.getY();
//        r.setFrame(r.getX(), r.getY(), w, h);
//    }

}
