package org.example.transformer;

import org.example.shapes.GShape;

import java.awt.geom.AffineTransform;
import java.util.Vector;

public class GTranslator extends GTransformer {
    private int x0, y0;
    public GTranslator(Vector<GShape> targets) {
        super(targets);
    }

    @Override
    public void start(int x, int y) {
        this.x0 = x;
        this.y0 = y;
    }

    @Override
    public void keep(int x, int y) {
        int dx = x-x0;
        int dy = y-y0;

        AffineTransform globalTx = AffineTransform.getTranslateInstance(dx, dy);
        for (GShape shape : targets) {
            shape.getAffineTransform().preConcatenate(globalTx);
        }
        this.x0 = x;
        this.y0 = y;
    }

    @Override
    public void finish(int x, int y) {
        keep(x, y);
    }
}
