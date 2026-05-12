package org.example.frames;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GShapeToolBar extends JToolBar {
    public enum EShapeType {
        eSelect("선택"),
        eRectangle("네모"),
        eOval("동그라미"),
//        eLine("라인"),
        ePolygon("폴리곤");

        private String name;
        private EShapeType(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
    }

    private EShapeType eShapeType;
    public EShapeType getEShapeType() {
        return eShapeType;
    }

    public GShapeToolBar() {
        ActionHandler actionHandler = new ActionHandler();
        ButtonGroup buttonGroup = new ButtonGroup();
        eShapeType = EShapeType.eSelect;

        for(EShapeType type: EShapeType.values()) {
            JRadioButton radioButton = new JRadioButton(type.getName());
            this.add(radioButton);
            buttonGroup.add(radioButton);
            radioButton.addActionListener(actionHandler);
            radioButton.setActionCommand(type.toString());
        }
        ((JRadioButton)(this.getComponentAtIndex(EShapeType.eSelect.ordinal()))).doClick();
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
