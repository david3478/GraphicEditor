package org.example.shapes;

import org.example.global.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.net.URL;

public abstract class GShape implements Cloneable {
    private static Image rotateIconImage;

    static {
        try {
            URL imgURL = GShape.class.getResource(Constants.ICON_ROTATE_PATH);
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                rotateIconImage = icon.getImage();
            } else {
                System.err.println(Constants.ICON_ROTATE_PATH+"  이미지를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public enum EAnchor {
        eNW,
        eNN,
        eNE,
        eEE,
        eSE,
        eSS,
        eSW,
        eWW,
        eRotate,
        eMove,
    }

    protected Shape shape;
    protected AffineTransform affineTransform;
    protected Color lineColor;
    protected Stroke stroke;
    private boolean isSelected;

    // getter and setter
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    public Shape getShape() {
        return shape;
    }
    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    public AffineTransform getAffineTransform() {
        return affineTransform;
    }

    public void setAffineTransform(AffineTransform affineTransform) {
        this.affineTransform = affineTransform;
    }

    public Ellipse2D getAnchor(Point2D p) {
        return new Ellipse2D.Double(p.getX() - Constants.ANCHOR_HEIGHT_SIZE/2.0, p.getY()-Constants.ANCHOR_WIDTH_SIZE/2.0,
                Constants.ANCHOR_WIDTH_SIZE, Constants.ANCHOR_HEIGHT_SIZE);
    }

    // constructor
    public GShape() {
        this.lineColor = Color.BLACK;
        this.stroke = new BasicStroke(1.0f);
        this.isSelected = false;
        this.affineTransform = new AffineTransform();
    }

    protected abstract Shape cloneShape();
    public GShape clone() {
        try {
            GShape cloned = (GShape) super.clone();
            if (this.shape != null) {
                // 2Point, NPoint 구분
                cloned.shape = this.cloneShape();
            }
            if (this.affineTransform != null) {
                cloned.affineTransform = (AffineTransform) this.affineTransform.clone();
            }
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
    public EAnchor onShape(int x, int y) {
        Point p = new Point(x, y);
        EAnchor eAnchor = null;
        if(isSelected) {    // resize anchor
            Rectangle r = this.shape.getBounds();
            int w = r.width;
            int h = r.height;
            int x_ = r.x;
            int y_ = r.y;

            if(getAnchor(transformToGlobal(x_, y_)).contains(p.x, p.y)) return EAnchor.eNW;
            if(getAnchor(transformToGlobal(x_+w/2, y_)).contains(p.x, p.y)) return EAnchor.eNN;
            if(getAnchor(transformToGlobal(x_+w, y_)).contains(p.x, p.y)) return EAnchor.eNE;
            if(getAnchor(transformToGlobal(x_+w, y_+h/2)).contains(p.x, p.y)) return EAnchor.eEE;
            if(getAnchor(transformToGlobal(x_+w, y_+h)).contains(p.x, p.y)) return EAnchor.eSE;
            if(getAnchor(transformToGlobal(x_+w/2, y_+h)).contains(p.x, p.y)) return EAnchor.eSS;
            if(getAnchor(transformToGlobal(x_, y_+h)).contains(p.x, p.y)) return EAnchor.eSW;
            if(getAnchor(transformToGlobal(x_, y_+h/2)).contains(p.x, p.y)) return EAnchor.eWW;

            // Rotate Anchor
            if(getAnchor(transformToGlobal(x_ + w/2, y_-30)).contains(p.x, p.y)) return EAnchor.eRotate;
        }

        try {
            // mouse의 포인트를 틀어버린다
            this.affineTransform.inverseTransform(p, p);
        } catch (NoninvertibleTransformException e) {
            throw new RuntimeException(e);
        }
        if(this.shape.contains(p.getX(), p.getY())) {
            eAnchor = EAnchor.eMove;
        }
        return eAnchor;
    }

    private Point2D transformToGlobal(int x, int y) {
        Point2D.Double point = new Point2D.Double(x, y);
        this.affineTransform.transform(point, point);
        return point;
    }

    public boolean isContained(java.awt.Rectangle selectionRect) {
        Shape transformedShape = this.affineTransform.createTransformedShape(this.shape);
        return selectionRect.contains(transformedShape.getBounds());
    }

    abstract public void setLocation0(int x, int y);

    abstract public void setLocation1(int x, int y);
    public void addPoint(int x, int y) {}

    public void draw(Graphics2D graphics) {
        Shape transformedShape = this.affineTransform.createTransformedShape(this.shape);
        graphics.setColor(this.lineColor);
        graphics.setStroke(this.stroke);

        graphics.draw(transformedShape);
        if(isSelected) {
            this.drawAnchor(graphics);
        }

        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke());
    }

    private void drawAnchor(Graphics2D graphics) {
        Rectangle r = this.shape.getBounds();
        int w = r.width;
        int h = r.height;
        int x = r.x;
        int y = r.y;

        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke(1.0f));

        graphics.draw((getAnchor(transformToGlobal(x, y))));
        graphics.draw((getAnchor(transformToGlobal(x+w/2, y))));
        graphics.draw((getAnchor(transformToGlobal(x+w, y))));
        graphics.draw((getAnchor(transformToGlobal(x+w, y+h/2))));
        graphics.draw((getAnchor(transformToGlobal(x+w, y+h))));
        graphics.draw((getAnchor(transformToGlobal(x+w/2, y+h))));
        graphics.draw((getAnchor(transformToGlobal(x, y+h))));
        graphics.draw((getAnchor(transformToGlobal(x, y+h/2))));

        Point2D rotatePoint = transformToGlobal(x + w/2, y-30);

        if (rotateIconImage != null) {
            int imgW = Constants.ANCHOR_WIDTH_SIZE;
            int imgH = Constants.ANCHOR_HEIGHT_SIZE;
            graphics.drawImage(rotateIconImage,
                    (int)rotatePoint.getX()-imgW/2,
                    (int)rotatePoint.getY()-imgH/2,
                    Constants.ANCHOR_WIDTH_SIZE,Constants.ANCHOR_HEIGHT_SIZE,
                    null);
        } else {
            // 방어 코드: rotate 이미지 미존재
            graphics.draw((getAnchor(rotatePoint)));
        }
    }
}
