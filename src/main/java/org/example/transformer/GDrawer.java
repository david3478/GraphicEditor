package org.example.transformer;

import org.example.shapes.GShape;

import java.awt.*;

public class GDrawer extends GTransformer {

    public GDrawer(GShape shape) {
        super(shape);
    }

    @Override
    public void start(int x, int y) {
        getShape0().setLocation0(x, y);
        getShape0().setLocation1(x, y);
    }

    @Override
    public void keep(int x, int y) {
        getShape0().setLocation1(x, y);
    }

    @Override
    public void finish(int x, int y) {
        getShape0().setLocation1(x, y);
        getShape0().setSelected(true);
    }

    @Override
    public void cont(int x, int y) {
        getShape0().addPoint(x, y);
    }
}
