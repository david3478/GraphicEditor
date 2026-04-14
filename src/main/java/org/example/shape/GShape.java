package org.example.shape;

import java.awt.*;

public class GShape {
    protected int x0;
    protected int y0;
    protected int x1;
    protected int y1;

    public GShape(int x, int y, int width, int height) {
        this.x0 = x;
        this.y0 = y;
        this.x1 = width;
        this.y1 = height;
    }

    public void setLocation0(int x, int y) {
        this.x0 = x;
        this.y0 = y;
    }
    public void setLocation1(int x, int y) {
        this.x1 = x;
        this.y1 = y;
    }

    public void setSize(int width, int height) {
        this.x1 = x0 + width;
        this.y1 = y0 + height;
    }
    public void draw(Graphics2D g) {

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

    // setter
    public void setX1(int x1) {
        this.x1 = x1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }
}
