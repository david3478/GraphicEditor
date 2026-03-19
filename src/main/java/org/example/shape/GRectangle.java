package org.example.shape;

import java.awt.*;

public class GRectangle extends GShape {
    public GRectangle(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void draw(Graphics g) {
        g.drawRect(x, y, width, height);
    }
}

