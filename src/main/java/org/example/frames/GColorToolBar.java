package org.example.frames;

import org.example.global.Constants;

import javax.swing.*;
import java.awt.*;

public class GColorToolBar extends JToolBar {
    // attributes
    private Color currentLineColor;
    private ColorIcon colorIcon;

    // association
    private GDrawingPanel drawingPanel;
    public GColorToolBar() {
        // attributes
        currentLineColor = Color.BLACK;
        colorIcon = new ColorIcon(16, 16, currentLineColor);

        JButton lineColorButton = new JButton(Constants.OUTLINE_BUTTON_NAME, colorIcon);

        lineColorButton.addActionListener(e -> {
            Color selectedColor = JColorChooser.showDialog(GColorToolBar.this, Constants.OUTLINE_BUTTON_MENU_NAME, currentLineColor);

            if (selectedColor != null) {
                currentLineColor = selectedColor;


                colorIcon.setColor(currentLineColor);
//                repaint();
                    lineColorButton.repaint();

                if (drawingPanel != null) {
                    drawingPanel.setShapeLineColor(currentLineColor);
                }
            }
        });
        this.add(lineColorButton);
    }

    public void associateWith(GDrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }

    private class ColorIcon implements Icon {
        private int width;
        private int height;
        private Color color;

        public ColorIcon(int width, int height, Color color) {
            this.width = width;
            this.height = height;
            this.color = color;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(this.color);
            g.fillRect(x, y, width, height);

            g.setColor(Color.DARK_GRAY);
            g.drawRect(x, y, width, height);
        }

        @Override
        public int getIconWidth() {
            return width;
        }

        @Override
        public int getIconHeight() {
            return height;
        }
    }
}