package org.example;

import javax.swing.*;

public class GToolBar extends JToolBar {
    private JRadioButton rectangleButton;
    private JRadioButton ovalButton;

    public GToolBar() {
        this.rectangleButton = new JRadioButton("Rectangle");
        this.add(this.rectangleButton);

        this.ovalButton = new JRadioButton("Oval");
        this.add(this.ovalButton);
    }
}
