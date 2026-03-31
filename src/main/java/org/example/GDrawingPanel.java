package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GDrawingPanel extends JPanel {

    public DrawingState drawingState;

    public GDrawingPanel() {
        this.setBackground(Color.WHITE);

        drawingState = DrawingState.NONE;

        MouseHandler mouseHandler = new MouseHandler();
        this.addMouseListener(mouseHandler);
        this.addMouseMotionListener(mouseHandler);
    }

    private int x0,y0;
    private int x1,y1;

    private void startRectangularShape(int x, int y) {
        this.x0 = x;
        this.y0 = y;

        this.drawingState = DrawingState.DRAWING;
    }
    private void keepDrawing(int x, int y) {

    }
    private void finishRectangularShape(int x, int y) {
        this.x1 = x;
        this.y1 = y;

        Graphics2D g2d = (Graphics2D) this.getGraphics();
        g2d.drawRect(this.x0, this.y0, this.x1-this.x0, this.y1-this.y0);

        this.drawingState = DrawingState.NONE;
    }

    private class MouseHandler implements MouseListener,MouseMotionListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getClickCount()==2 && drawingState==DrawingState.DRAWING){
                finishRectangularShape(e.getX(),e.getY());
            } else if (e.getClickCount()==1 && drawingState==DrawingState.NONE) {
                startRectangularShape(e.getX(),e.getY());
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
//            startRectangularShape(e.getX(),e.getY());
        }

        @Override
        public void mouseDragged(MouseEvent e) {
//            finishRectangularShape(e.getX(),e.getY());
        }
        @Override
        public void mouseReleased(MouseEvent e) {
//            finishRectangularShape(e.getX(),e.getY());
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }


        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }
}