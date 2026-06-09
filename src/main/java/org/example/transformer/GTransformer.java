package org.example.transformer;

import org.example.shapes.GShape;

import java.util.Vector;

public abstract class GTransformer {
    protected Vector<GShape> targets;
    public GTransformer(Vector<GShape> targets) {
        this.targets = targets;
    }

    // GDrawer, GSelector
    public GTransformer(GShape shape) {
        this.targets = new Vector<>();
        if (shape != null) this.targets.add(shape);
    }

    public GShape getShape0() {
        if (targets != null && !targets.isEmpty()) return targets.get(0);
        return null;
    }

    abstract public void start(int x, int y);
    abstract public void keep(int x, int y);
    abstract public void finish(int x, int y);
    public void cont(int x, int y) {};
}
