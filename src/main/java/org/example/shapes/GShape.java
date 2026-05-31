package org.example.shapes;

import java.awt.*;

public abstract class GShape implements Cloneable {
    protected Shape shape;

    protected Color lineColor;
    protected Stroke stroke;

    public enum EAnchor {
        eRotate,
        eMove,
        eResize
    }

    public GShape() {
        this.lineColor = Color.BLACK;
        this.stroke = new BasicStroke(1.0f);
    }

    public GShape clone() {
//        try {
//            return (GShape) super.clone();
//        } catch (CloneNotSupportedException e) {
//            throw new RuntimeException(e);
//        }
        try {
            // awt Shape 속성까지 복사하기 위함
            return this.getClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("도형 복제(생성) 중 오류 발생", e);
        }
    }
    public EAnchor onShape(int x, int y) {
        if(this.shape.contains(x, y)) {
            return EAnchor.eMove;
        }
        return null;
    }

    public boolean isContained(java.awt.Rectangle selectionRect) {
        return selectionRect.contains(this.shape.getBounds());
    }

    abstract public void setLocation0(int x, int y);

    abstract public void setLocation1(int x, int y);
    public abstract void translate(int dx, int dy);

    public void draw(Graphics2D graphics) {
        graphics.setColor(this.lineColor);
        graphics.setStroke(this.stroke);
        graphics.draw(shape);
        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke());
    }

    // getter
    public Shape getShape() {
        return shape;
    }

    // setter
    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }
}
