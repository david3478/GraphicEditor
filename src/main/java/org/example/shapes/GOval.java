package org.example.shapes;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class GOval extends GShape {

    public GOval() {
        this.shape = new Ellipse2D.Double();
    }

    public void draw(Graphics2D graphics) {
        graphics.setColor(Color.BLACK);
        graphics.drawOval(this.x0, this.y0, this.x1 - this.x0, this.y1 - this.y0);
    }
}
