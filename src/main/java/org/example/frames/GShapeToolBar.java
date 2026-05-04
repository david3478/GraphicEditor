package org.example.frames;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GShapeToolBar extends JToolBar {
    public enum EShapeType {
        eSelect, eRectangle, eOval, eLine, ePolygon
    }

    private EShapeType eShapeType;
    public EShapeType getEShapeType() {
        return eShapeType;
    }

    public GShapeToolBar() {
        ActionHandler actionHandler = new ActionHandler();
        ButtonGroup buttonGroup = new ButtonGroup();
        eShapeType = EShapeType.eSelect;

        JRadioButton selectButton = new JRadioButton("select");
        this.add(selectButton);
        buttonGroup.add(selectButton);
        selectButton.addActionListener(actionHandler);
        selectButton.setActionCommand(EShapeType.eSelect.toString());

        JRadioButton rectangleButton = new JRadioButton("Rectangle");
        this.add(rectangleButton);
        buttonGroup.add(rectangleButton);
        rectangleButton.addActionListener(actionHandler);
        rectangleButton.setActionCommand(EShapeType.eRectangle.toString());

        JRadioButton ovalButton = new JRadioButton("Oval");
        this.add(ovalButton);
        buttonGroup.add(ovalButton);
        ovalButton.addActionListener(actionHandler);
        ovalButton.setActionCommand(EShapeType.eOval.toString());
    }

    private class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            eShapeType = EShapeType.valueOf(e.getActionCommand());
            System.out.println(eShapeType);
//            if(e.getActionCommand().equals("select")) {
//                eShapeType = EShapeType.eSelect;
//            } else if(e.getActionCommand().equals("Rectangle")) {
//                eShapeType = EShapeType.eRectangle;
//            } else if(e.getActionCommand().equals("Oval")) {
//                eShapeType = EShapeType.eOval;
//            }
        }
    }
}
