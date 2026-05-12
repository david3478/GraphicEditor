package org.example.shapes;

import java.awt.*;

public class GRectangle extends GShape {

    public GRectangle() {
        this.shape = new Rectangle();
    }

    public void draw(Graphics2D graphics) {
        graphics.setColor(Color.BLACK);
        graphics.drawRect(this.x0, this.y0, this.x1 - this.x0, this.y1 - this.y0);
    }
}
