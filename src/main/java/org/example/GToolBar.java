package org.example;

import org.example.shape.ShapeType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GToolBar extends JToolBar {
    private JRadioButton rectangleButton;
    private JRadioButton ovalButton;
    private ButtonGroup buttonGroup;
    private ShapeType shapeType;
    // ActionListener를 필드로 분리
    private ActionListener shapeListener;

    public GToolBar() {
        this.rectangleButton = new JRadioButton("Rectangle");
        this.add(this.rectangleButton);

        this.ovalButton = new JRadioButton("Oval");
        this.add(this.ovalButton);

        this.buttonGroup = new ButtonGroup();
        this.buttonGroup.add(this.rectangleButton);
        this.buttonGroup.add(this.ovalButton);

        shapeListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == rectangleButton) {
                    shapeType = ShapeType.RECTANGLE;
                } else if (e.getSource() == ovalButton) {
                    shapeType = ShapeType.OVAL;
                }
            }
        };

        this.rectangleButton.addActionListener(shapeListener);
        this.ovalButton.addActionListener(shapeListener);
    }

    // Getter
    public ShapeType getShapeType() {
        return this.shapeType;
    }
}
