package org.example.frames;

import org.example.global.Constants;
import org.example.shapes.GShape;
import org.example.transformer.GDrawer;
import org.example.transformer.GTranslator;
import org.example.transformer.GTransformer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class GDrawingPanel extends JPanel {
    // declaration
    private enum EDrawingState {
        eIdle,
        eTransforming
    }

    // attributes
    private EDrawingState eDrawingState;

    // components
    private final Vector<GShape> shapes;
    private BufferedImage bufferImage;
    private GTransformer transformer;

    // association
    private GShapeToolBar toolBar;

    // working objects
//    private GShape currentShape;

    // constructor
    public GDrawingPanel() {
        // attributes
        this.setBackground(Color.WHITE);
        eDrawingState = EDrawingState.eIdle;

        // components
        this.shapes = new Vector<>();
        this.bufferImage = null;
        this.transformer = null;

        MouseHandler mouseHandler = new MouseHandler();
        this.addMouseListener(mouseHandler);
        this.addMouseMotionListener(mouseHandler);
    }

    // setters and getters
    public void associateWith(GShapeToolBar toolBar) {
        this.toolBar = toolBar;
    }

    // methods
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D panelGraphics = (Graphics2D) g;

        if (panelGraphics != null) {
            panelGraphics.drawImage(this.bufferImage, 0, 0, null);
            panelGraphics.dispose();
        }
    }

    private void prepareDrawing() {
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
    private GShape startNewShape(int x, int y) {
        GShape currentShape = toolBar.getEShapeType().getShape();
//        currentShape.setLocation0(x, y);
//        currentShape.setLocation1(x, y);
        return currentShape;
    }
    private void startTransform(int x, int y) {
        if (toolBar.getEShapeType() == Constants.EShapeType.eSelect) {
            for(GShape shape : shapes) { // operation
                GShape.EAnchor eAnchor = shape.onShape(x, y);
                if(eAnchor != null) {
                    if(eAnchor == GShape.EAnchor.eMove) {
                        this.transformer = new GTranslator(shape);
                    } else if(eAnchor == GShape.EAnchor.eRotate){
                        this.transformer = new GDrawer(shape);
                    } else { // resize
                        this.transformer = new GDrawer(shape);
                    }
                    this.transformer.start(x, y);
                    break;
                }
            }
            if (this.transformer == null) {    // select state not onshape
                this.transformer = new GDrawer(startNewShape(x, y));
                this.transformer.start(x, y);
            }
        } else {
            GShape currentShape = startNewShape(x, y);
            this.transformer = new GDrawer(currentShape);
            this.shapes.add(currentShape);
            this.transformer.start(x, y);
        }

        prepareDrawing();   // prepare for double buffering
    }

    private void keepTransform(int x, int y) {
        Graphics2D bufferGraphics = bufferImage.createGraphics();
        bufferGraphics.setColor(this.getBackground());
        bufferGraphics.fillRect(0, 0, getWidth(), getHeight());
        bufferGraphics.setColor(this.getForeground());

        this.transformer.keep(x, y);
//        if (eDrawingState == EDrawingState.eDrawing) {
//            currentShape.setLocation1(x, y);
//            currentShape.draw(bufferGraphics);
//        } else if (eDrawingState == EDrawingState.eMoving) {
//            currentShape.move(x, y);
//        } else if (eDrawingState == EDrawingState.eResizing) {
//            currentShape.resize(x, y);
//        } else if (eDrawingState == EDrawingState.eRotating) {
//            currentShape.rotate(x, y);
//        }
        for (GShape shape : shapes) {
            shape.draw(bufferGraphics);
        }
        bufferGraphics.dispose();
        repaint();
    }

    private void finishTransform(int x, int y) {
        this.transformer.finish(x, y);
        this.transformer = null;
//        eDrawingState = EDrawingState.eIdle;
    }

    private void continueDrawing(int x, int y) {

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
                    eDrawingState = EDrawingState.eTransforming;
                } else {
                    if (toolBar.getEShapeType().getDrawingType() == Constants.EDrawingType.eNPoint) {
                        continueDrawing(e.getX(), e.getY());
                    }
                }
                prepareDrawing(); // prepare for double buffering
            }
        }
        @Override
        public void mouseMoved(MouseEvent e) {
            if(eDrawingState == EDrawingState.eTransforming) {
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
            if(toolBar.getEShapeType().getDrawingType() == Constants.EDrawingType.e2Point) {
                if(eDrawingState == EDrawingState.eIdle) { // target state
                    startTransform(e.getX(), e.getY());
                    eDrawingState = EDrawingState.eTransforming;
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (toolBar.getEShapeType().getDrawingType() == Constants.EDrawingType.e2Point) {
                if(eDrawingState == EDrawingState.eTransforming) {
                    keepTransform(e.getX(), e.getY());
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (toolBar.getEShapeType().getDrawingType() == Constants.EDrawingType.e2Point) {
                if(eDrawingState == EDrawingState.eTransforming) {
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