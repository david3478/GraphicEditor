package org.example;

import org.example.shape.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class GDrawingPanel extends JPanel {
    private enum EDrawingState {
        eIdle,
        eDrawing,
        eMoving,

        eResizing,
        eShearing
    }

    private EDrawingState eDrawingState;

    private BufferedImage bufferImage;
    private Vector<GShape> shapes;
    private GShape currentShape;

    private GToolBar toolBar;

    public void setToolBar(GToolBar toolBar) {
        this.toolBar =  toolBar;
    }

    public GDrawingPanel() {
        super();
        this.setBackground(Color.WHITE);
        this.eDrawingState = EDrawingState.eIdle;

        shapes = new Vector<>();
        currentShape = null;

        MouseHandler mouseHandler = new MouseHandler();
        this.addMouseListener(mouseHandler);
        this.addMouseMotionListener(mouseHandler);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D panelGraphics = (Graphics2D) g;
        // 확정되어 저장된 도형들 그리기
        for (GShape shape : shapes) {
            shape.draw(panelGraphics);
        }

    }

    private void makeNewCurrentShape(int x, int y) {
        if(toolBar.getShapeType() == ShapeType.RECTANGLE) {
            this.currentShape = new GRectangle(x, y, x, y);
        } else if(toolBar.getShapeType() == ShapeType.OVAL) {
            this.currentShape = new GOval(x, y, x, y);
        } else if(toolBar.getShapeType() == ShapeType.SELECT) {
            // select
            this.currentShape = new GRectangle(x, y, x, y);
        }
    }

    private void startRectangularShape(int x, int y) {
        makeNewCurrentShape(x,y);

        if (this.getWidth() <= 0 || this.getHeight() <= 0) {
            return;
        }

        if (this.bufferImage == null
                || this.bufferImage.getWidth() != this.getWidth()
                || this.bufferImage.getHeight() != this.getHeight()) {
            this.bufferImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D bufferGraphics = this.bufferImage.createGraphics();
            bufferGraphics.setColor(this.getBackground());
            bufferGraphics.fillRect(0, 0, this.getWidth(), this.getHeight());
            bufferGraphics.dispose();
        }
    }

    private void keepRectangularShape(int x, int y) {
        this.currentShape.setLocation1(x, y);

        Graphics2D bufferGraphics = this.bufferImage.createGraphics();
        bufferGraphics.setColor(this.getBackground());
        bufferGraphics.fillRect(0, 0, this.getWidth(), this.getHeight());
        bufferGraphics.setColor(Color.BLACK);
        for (GShape shape : this.shapes) {
            shape.draw(bufferGraphics);
        }
        this.currentShape.draw(bufferGraphics);
        bufferGraphics.dispose();

        Graphics2D panelGraphics = (Graphics2D) this.getGraphics();
        if (panelGraphics != null) {
            panelGraphics.drawImage(this.bufferImage, 0, 0, null);
            panelGraphics.dispose();
        }
    }

    private void finishRectangularShape(int x, int y) {
        this.currentShape.setLocation1(x, y);
        addShape();
        this.currentShape = null;
        repaint();
    }

    private void addShape() {
        this.shapes.add(this.currentShape);
    }

    private class MouseHandler implements MouseListener, MouseMotionListener {

        @Override
        public void mousePressed(MouseEvent e) {
            if (eDrawingState == EDrawingState.eIdle) {
                startRectangularShape(e.getX(), e.getY());
                eDrawingState = EDrawingState.eDrawing;
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (eDrawingState == EDrawingState.eDrawing) {
                keepRectangularShape(e.getX(), e.getY());
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (eDrawingState == EDrawingState.eDrawing) {
                finishRectangularShape(e.getX(), e.getY());
                eDrawingState = EDrawingState.eIdle;
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}