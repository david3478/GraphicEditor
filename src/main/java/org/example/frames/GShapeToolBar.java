package org.example.frames;

import org.example.global.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Collections;

public class GShapeToolBar extends JToolBar {
    ButtonGroup buttonGroup;

    private Constants.EShapeType eShapeType;
    public Constants.EShapeType getEShapeType() {
        return eShapeType;
    }

    public GShapeToolBar() {
        ActionHandler actionHandler = new ActionHandler();
        this.buttonGroup = new ButtonGroup();
        eShapeType = Constants.EShapeType.eSelect;

        for(Constants.EShapeType type: Constants.EShapeType.values()) {

            // 버튼 UI 개선: 아이콘 사용
            ImageIcon icon = createIcon(type);
            JRadioButton radioButton = new JRadioButton(icon);
            if (icon != null) {
                radioButton.setIcon(icon);
                radioButton.setToolTipText(type.getName());
            } else {
                // 방어 코드: 이미지 파일을 찾지 못했을 때
                radioButton.setText(type.getName());
            }
            radioButton.setFocusPainted(false);
            radioButton.setBorderPainted(false);

            this.add(radioButton);
            buttonGroup.add(radioButton);
            radioButton.addActionListener(actionHandler);
            radioButton.setActionCommand(type.toString());
        }
        ((JRadioButton)(this.getComponentAtIndex(Constants.EShapeType.eSelect.ordinal()))).doClick();
    }

    private ImageIcon createIcon(Constants.EShapeType type) {
        String path = type.getIconFileName();

        URL imgURL = getClass().getResource(path);

        if (imgURL != null) {
            ImageIcon originalIcon = new ImageIcon(imgURL);
            Image scaledImage = originalIcon.getImage()
                    .getScaledInstance(Constants.ICON_WIDTH_SIZE, Constants.ICON_HEIGHT_SIZE, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } else {
            System.err.println("아이콘을 찾을 수 없습니다: " + path);
            return null;
        }
    }

    private class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            eShapeType = Constants.EShapeType.valueOf(e.getActionCommand());
            for(AbstractButton b : Collections.list(buttonGroup.getElements())) {
                b.setBackground(Color.WHITE);
            }
            JRadioButton b = (JRadioButton)e.getSource();
            b.setBackground(Color.lightGray);
            System.out.println(eShapeType);
        }
    }
}
