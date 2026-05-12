package org.example.frames;

import org.example.global.Constants;
import org.example.shapes.GOval;
import org.example.shapes.GRectangle;
import org.example.shapes.GShape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class GDrawingPanel extends JPanel {

    private BufferedImage bufferImage;
    private Vector<GShape> shapes;
    private GShape currentShape;

    public EDrawingState eDrawingState;

    private GShapeToolBar toolBar;
    public void associateWith(GShapeToolBar toolBar) {
        this.toolBar = toolBar;
    }

    private enum EDrawingState {
        eIdle,
        eDrawing,
        eMoving,
        eResizing,
        eRotating, eSearing
    }
    public GDrawingPanel() {
        // attributes
        this.setBackground(Color.WHITE);
        eDrawingState = EDrawingState.eIdle;

        this.shapes = new Vector<>();
        MouseHandler mouseHandler = new MouseHandler();
        this.addMouseListener(mouseHandler);
        this.addMouseMotionListener(mouseHandler);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D panelGraphics = (Graphics2D) g;

        if (panelGraphics != null) {
            panelGraphics.drawImage(this.bufferImage, 0, 0, null);
            panelGraphics.dispose();
        }
    }

    private void startDrawing(int x, int y) {
        if (getWidth() <= 0 || getHeight() <= 0) {
            return;
        }

        if (bufferImage == null
                || bufferImage.getWidth() != getWidth()
                || bufferImage.getHeight() != getHeight()) {
            bufferImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D bufferGraphics = bufferImage.createGraphics();
            bufferGraphics.setColor(getBackground());
            bufferGraphics.fillRect(0, 0, getWidth(), getHeight());
            bufferGraphics.dispose();
        }
    }
    private void startNewShape(int x, int y) {
        currentShape = toolBar.getEShapeType().getShape();
        currentShape.setLocation0(x, y);
        currentShape.setLocation1(x, y);
//        if(toolBar.getEShapeType() == Constants.EShapeType.eRectangle) {
//            currentShape = new GRectangle(x, y, x, y);
//        } else if(toolBar.getEShapeType() == Constants.EShapeType.eOval) {
//
//        }
    }
    private void startTransform(int x, int y) {
        for(GShape shape : shapes) { // operation
            GShape.EAnchor eAnchor = shape.onShape(x, y);
            if(eAnchor != null) {
                currentShape = shape;
                if(eAnchor == GShape.EAnchor.eMove) {
                    eDrawingState = EDrawingState.eMoving;
                } else if(eAnchor == GShape.EAnchor.eRotate){
                    eDrawingState = EDrawingState.eRotating;
                } else { // resize
                    eDrawingState = EDrawingState.eResizing;

                }
                currentShape = shape;
                break;
            }
        }
    }

    private void keepTransform(int x, int y) {
        Graphics2D bufferGraphics = bufferImage.createGraphics();
        bufferGraphics.setColor(this.getBackground());
        bufferGraphics.fillRect(0, 0, getWidth(), getHeight());
        bufferGraphics.setColor(this.getForeground());

        if (eDrawingState == EDrawingState.eDrawing) {
            currentShape.setLocation1(x, y);
            currentShape.draw(bufferGraphics);
        } else if (eDrawingState == EDrawingState.eMoving) {
            currentShape.move(x, y);
        } else if (eDrawingState == EDrawingState.eResizing) {
            currentShape.resize(x, y);
        } else if (eDrawingState == EDrawingState.eRotating) {
            currentShape.rotate(x, y);
        }
        for (GShape shape : shapes) {
            shape.draw(bufferGraphics);
        }
        currentShape.draw(bufferGraphics);
        bufferGraphics.dispose();
        repaint();
    }

    private void finishTransform(int x, int y) {
        if(this.eDrawingState == EDrawingState.eDrawing) {
            if(this.toolBar.getEShapeType() != Constants.EShapeType.eSelect) {
                addShape();
            }
            this.currentShape = null;
        }
    }

    private void continueDrawing(int x, int y) {

    }

    private void addShape() {
        this.shapes.add(this.currentShape);
    }
    private class MouseHandler implements MouseListener,MouseMotionListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getButton() == 1) { // left button
                if (e.getClickCount()==1) {
                    mouseLButtonClicked(e);
                } else if(e.getClickCount()==2){
                    mouseLButton2Clicked(e);
                }
            }
        }
        private void mouseLButtonClicked(MouseEvent e) {
            if(eDrawingState == EDrawingState.eIdle) { // target state
                if(toolBar.getEShapeType().getDrawingType() == Constants.EDrawingType.eNPoint) { // context
                    startNewShape(e.getX(), e.getY());
                } else {
                    if (toolBar.getEShapeType().getDrawingType() == Constants.EDrawingType.eNPoint) {
                        continueDrawing(e.getX(), e.getY());
                    }
                }
                startDrawing(e.getX(), e.getY()); // prepare for double buffering
            }
        }
        @Override
        public void mouseMoved(MouseEvent e) {
            if(eDrawingState != EDrawingState.eIdle) {
                if (toolBar.getEShapeType().getDrawingType() == Constants.EDrawingType.eNPoint) { // context
                    keepTransform(e.getX(), e.getY());
                }
            }
        }

        private void mouseLButton2Clicked(MouseEvent e) {
            if(eDrawingState != EDrawingState.eIdle) {
                if (toolBar.getEShapeType().getDrawingType() == Constants.EDrawingType.eNPoint) {
                    finishTransform(e.getX(), e.getY());
                    eDrawingState = EDrawingState.eIdle;
                }
            }
        }


        @Override
        public void mousePressed(MouseEvent e) {
            if(eDrawingState == EDrawingState.eIdle) { // target state
                if(toolBar.getEShapeType().getDrawingType() == Constants.EDrawingType.e2Point) {
                    if(toolBar.getEShapeType() == Constants.EShapeType.eSelect) { // context
                        startTransform(e.getX(), e.getY());
                    } else { // drawing
                        startNewShape(e.getX(), e.getY());
                        eDrawingState = EDrawingState.eDrawing;
                    }
                    startDrawing(e.getX(), e.getY()); // prepare for double buffering
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if(eDrawingState != EDrawingState.eIdle) {
                if (toolBar.getEShapeType().getDrawingType() == Constants.EDrawingType.e2Point) {
                    keepTransform(e.getX(), e.getY());
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(eDrawingState != EDrawingState.eIdle) {
                if (toolBar.getEShapeType().getDrawingType() == Constants.EDrawingType.e2Point) {
                    finishTransform(e.getX(), e.getY());
                    eDrawingState = EDrawingState.eIdle;
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }


    }



}