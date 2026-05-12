package org.example.shapes;

import java.awt.*;

public abstract class GShape {
    protected int x0, y0, x1, y1;
    protected int px, py;

    public enum EAnchor {
        eRotate,
        eMove,
        eResize
    }

    public GShape(int x0, int y0, int x1, int y1) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
    }

    public void move(int x, int y) {
        int dx = x - this.px;
        int dy = y - this.py;

        setLocation0(this.x0+dx, this.y0+dy);
        setLocation1(this.x1+dx, this.y1+dy);
        setLocationP(x, y);
//        this.x1 += dx;
//        this.y1 += dy;
//        this.x0 += dx;
//        this.y0 += dy;
//
//        this.px = x;
//        this.py = y;

    }
    public void resize(int x, int y) {

    }
    public void rotate(int x, int y) {

    }
    public EAnchor onShape(int x, int y) {
        if(x>this.x0 && y>this.y0 && x<this.x1 && y<this.y1) { // mouse가 도형 안에 있음
            return EAnchor.eMove;
        }
        return null;
    }
    public void setLocationP(int x, int y) {
        this.px=x;
        this.py=y;
    }
    public void setLocation0(int x, int y) {
        this.x0 = x;
        this.y0 = y;
    }

    public void setLocation1(int x, int y) {
        this.x1 = x;
        this.y1 = y;
    }

    abstract public void draw(Graphics2D graphics);
}
