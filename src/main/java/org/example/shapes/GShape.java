package org.example.shapes;

import java.awt.*;

public abstract class GShape implements Cloneable {
    protected int x0, y0, x1, y1;
    protected Shape shape;


    public enum EAnchor {
        eRotate,
        eMove,
        eResize
    }

    public GShape() {
    }
    public GShape clone() {
        try {
            return (GShape) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
    public EAnchor onShape(int x, int y) {
        if(this.shape.contains(x, y)) {
            return EAnchor.eMove;
        }
        return null;
    }
    abstract public void setLocation0(int x, int y);

    abstract public void setLocation1(int x, int y);
    public abstract void translate(int dx, int dy);

    public void draw(Graphics2D graphics) {
        graphics.draw(shape);
    }

    // getter
    public int getX0() {
        return x0;
    }
    public int getY0() {
        return y0;
    }
    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }
}
