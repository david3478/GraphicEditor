package org.example.transformer;

import org.example.shapes.GShape;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class GScale extends GTransformer {
    private Point2D point;
    private GShape.EAnchor eAnchor;

    private GShape referenceShape;
    private Map<GShape, AffineTransform> initialTransforms;

    public GScale(GShape targetShape, GShape.EAnchor anchor, Vector<GShape> targets) {
        super(targets);
        this.referenceShape = targetShape;
        this.eAnchor = anchor;
        this.initialTransforms = new HashMap<>();
    }

    @Override
    public void start(int x, int y) {
        this.point = new Point2D.Double(x, y);

        // 드래그 시작 시점의 타겟들 각자의 행렬을 복제해서 저장해 둠
        for (GShape shape : targets) {
            initialTransforms.put(shape, new AffineTransform(shape.getAffineTransform()));
        }
    }

    @Override
    public void keep(int x, int y) {
        Point2D pCurrent = new Point2D.Double(x,y);

        Rectangle refBounds = referenceShape.getShape().getBounds();
        double w = refBounds.getWidth();
        double h = refBounds.getHeight();

        double sx = 1.0;
        double sy = 1.0;

        Point2D p0 = new Point2D.Double();
        Point2D p1 = new Point2D.Double();
        try {
            AffineTransform initialTranform = initialTransforms.get(referenceShape);
            initialTranform.inverseTransform(point, p0);
            initialTranform.inverseTransform(pCurrent, p1);
        } catch (NoninvertibleTransformException e) {
            throw new RuntimeException(e);
        }

        double dx = p1.getX() - p0.getX();
        double dy = p1.getY() - p0.getY();

        switch(eAnchor) {
            case eSE: sx = (w+dx)/w; sy = (h+dy)/h; break;
            case eNW: sx = (w-dx)/w; sy = (h-dy)/h; break;
            case eNE: sx = (w+dx)/w; sy = (h-dy)/h; break;
            case eSW: sx = (w-dx)/w; sy = (h+dy)/h; break;
            case eEE: sx = (w+dx)/w; break;
            case eWW: sx = (w-dx)/w; break;
            case eSS: sy = (h+dy)/h; break;
            case eNN: sy = (h-dy)/h; break;
            default: break;
        }
        if(sx != 0 && sy != 0) {
            for (GShape shape : targets) {
                Rectangle r = shape.getShape().getBounds();
                double tx = 0, ty = 0;

                switch(eAnchor) {
                    case eSE: tx = r.getX(); ty = r.getY(); break;
                    case eNW: tx = r.getX()+r.getWidth(); ty = r.getY()+r.getHeight(); break;
                    case eNE: tx = r.getX(); ty = r.getY()+r.getHeight(); break;
                    case eSW: tx = r.getX()+r.getWidth(); ty = r.getY(); break;
                    case eEE: tx = r.getX(); ty = r.getY(); break;
                    case eWW: tx = r.getX()+r.getWidth(); ty = r.getY(); break;
                    case eSS: tx = r.getX(); ty = r.getY(); break;
                    case eNN: tx = r.getX(); ty = r.getY()+r.getHeight(); break;
                    default: break;
                }

                AffineTransform newAt = new AffineTransform(initialTransforms.get(shape));

                newAt.translate(tx, ty);
                newAt.scale(sx, sy);
                newAt.translate(-tx, -ty);

                shape.setAffineTransform(newAt);
            }
        }
    }

    @Override
    public void finish(int x, int y) {
        initialTransforms.clear();
    }
}
