package org.example.transformer;

import org.example.shapes.GShape;

import java.awt.*;

public abstract class GTransformer {
    protected GShape shape;
    public GTransformer(GShape shape) {
        this.shape = shape;
    }

    abstract public void start(int x, int y);
    abstract public void keep(int x, int y);
    abstract public void finish(int x, int y);
    public void cont(int x, int y) {};
}
