package org.example.transformer;

import org.example.shapes.GShape;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class GRotate extends GTransformer {
    private GShape referenceShape;

    private double startAngle;
    private Point2D globalCenter;
    private Map<GShape, AffineTransform> initialTransforms;

    public GRotate(GShape targetShape, Vector<GShape> targets) {
        super(targets);
        this.referenceShape = targetShape;
        this.initialTransforms = new HashMap<>();
    }

    @Override
    public void start(int x, int y) {
        for (GShape shape : targets) {
            initialTransforms.put(shape, new AffineTransform(shape.getAffineTransform()));
        }

        Rectangle r = referenceShape.getShape().getBounds();
        Point2D localCenter = new Point2D.Double(r.getCenterX(), r.getCenterY());
        globalCenter = new Point2D.Double();
        referenceShape.getAffineTransform().transform(localCenter, globalCenter);
        this.startAngle = Math.atan2(y - globalCenter.getY(), x - globalCenter.getX());
    }

    @Override
    public void keep(int x, int y) {
        double currentAngle = Math.atan2(y - globalCenter.getY(), x - globalCenter.getX());
        double angle = currentAngle - startAngle;
        for (GShape shape : targets) {
            AffineTransform newAt = new AffineTransform(initialTransforms.get(shape));

            Rectangle localBounds = shape.getShape().getBounds();
            newAt.rotate(angle, localBounds.getCenterX(), localBounds.getCenterY());
            shape.setAffineTransform(newAt);
        }
    }

    @Override
    public void finish(int x, int y) {
        initialTransforms.clear();
    }
}
