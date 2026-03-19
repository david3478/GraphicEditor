package org.example.shape;

import java.awt.*;

public class GOval extends GShape {
    public GOval(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void draw(Graphics g) {
        g.drawOval(x, y, width, height);
    }
}

