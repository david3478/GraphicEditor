package org.example.frames;

import org.example.global.Constants;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GShapeToolBar extends JToolBar {


    private Constants.EShapeType eShapeType;
    public Constants.EShapeType getEShapeType() {
        return eShapeType;
    }

    public GShapeToolBar() {
        ActionHandler actionHandler = new ActionHandler();
        ButtonGroup buttonGroup = new ButtonGroup();
        eShapeType = Constants.EShapeType.eSelect;

        for(Constants.EShapeType type: Constants.EShapeType.values()) {
            JRadioButton radioButton = new JRadioButton(type.getName());
            this.add(radioButton);
            buttonGroup.add(radioButton);
            radioButton.addActionListener(actionHandler);
            radioButton.setActionCommand(type.toString());
        }
        ((JRadioButton)(this.getComponentAtIndex(Constants.EShapeType.eSelect.ordinal()))).doClick();
    }

    private class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            eShapeType = Constants.EShapeType.valueOf(e.getActionCommand());
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
