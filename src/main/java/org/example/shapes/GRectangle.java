package org.example.shapes;

import java.awt.*;

public class GRectangle extends GShape {

    public GRectangle(int x0, int y0, int x1, int y1) {
        super(x0, y0, x1, y1);
    }

    public void draw(Graphics2D graphics) {
        graphics.setColor(Color.BLACK);
        graphics.drawRect(this.x0, this.y0, this.x1 - this.x0, this.y1 - this.y0);
    }
}
