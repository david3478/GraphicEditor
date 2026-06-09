package org.example.frames;

import org.example.global.Constants;
import org.example.shapes.GShape;
import org.example.transformer.*;

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
    private Color shapeLineColor;

    // components
    private final Vector<GShape> shapes;
    private final Vector<GShape> selectedShapes;
    private BufferedImage bufferImage;
    private GTransformer transformer;

    // association
    private GShapeToolBar toolBar;

    // constructor
    public GDrawingPanel() {
        // attributes
        this.setBackground(Color.WHITE);
        eDrawingState = EDrawingState.eIdle;
        this.shapeLineColor = Color.BLACK;

        // components
        this.shapes = new Vector<>();
        this.selectedShapes = new Vector<>();
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
    public void setShapeLineColor(Color selectedColor) {
        this.shapeLineColor = selectedColor;

        // 선택된 도형들의 색상을 즉시 변경
        if (!selectedShapes.isEmpty()) {
            for (GShape shape : selectedShapes) {
                shape.setLineColor(selectedColor);
            }
            Graphics2D bufferGraphics = bufferImage.createGraphics();
            bufferGraphics.setColor(this.getBackground());
            bufferGraphics.fillRect(0, 0, getWidth(), getHeight());
            bufferGraphics.setColor(this.getForeground());

            for (GShape s : shapes) {
                s.draw(bufferGraphics);
            }
            bufferGraphics.dispose();
            repaint();
        }
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
    private GShape startNewShape() {
        GShape newShape = toolBar.getEShapeType().getShape();
        newShape.setLineColor(shapeLineColor);
        return newShape;
    }

    private void startTransform(int x, int y) {
        if (toolBar.getEShapeType() == Constants.EShapeType.eSelect) {
            GShape target = getTargetShape(x, y);
            if(target != null) {
                if (!selectedShapes.contains(target)) {
                    initSelectedShapes();
                    selectedShapes.add(target);
                    target.setSelected(true);
                }

                GShape.EAnchor eAnchor = target.onShape(x, y); // 그룹의 onShape가 실행됨
                if (eAnchor == GShape.EAnchor.eMove) { // translate
                    this.transformer = new GTranslator(selectedShapes);
                } else if (eAnchor == GShape.EAnchor.eRotate) { // rotate
                    this.transformer = new GRotate(target, selectedShapes);
                } else { // resize
                    this.transformer = new GScale(target, eAnchor, selectedShapes);
                }
            } else {    // select state not onshape
                this.transformer = new GSelector(startNewShape());
                initSelectedShapes();
            }
            this.transformer.start(x, y);
        } else {
            GShape currentShape = startNewShape();
            this.transformer = new GDrawer(currentShape);
            this.shapes.add(currentShape);
            this.transformer.start(x, y);
            initSelectedShapes();
        }

        prepareDrawing();   // prepare for double buffering
    }

    private GShape getTargetShape(int x, int y) {
        for (GShape shape : shapes) {
            if (shape.onShape(x, y) != null) {
                return shape;
            }
        }
        return null;
    }

    private void initSelectedShapes() {
        for(GShape s : selectedShapes) s.setSelected(false);
        for(GShape s : shapes) s.setSelected(false);
        this.selectedShapes.clear();
    }

    private void keepTransform(int x, int y) {
        this.transformer.keep(x, y);

        Graphics2D bufferGraphics = bufferImage.createGraphics();
        bufferGraphics.setColor(this.getBackground());
        bufferGraphics.fillRect(0, 0, getWidth(), getHeight());
        bufferGraphics.setColor(this.getForeground());

        for (GShape shape : shapes) {
            shape.draw(bufferGraphics);
        }

        if (this.transformer instanceof GSelector selector) {
            selector.draw(bufferGraphics);
        }
        bufferGraphics.dispose();
        repaint();
    }

    private void finishTransform(int x, int y) {
        this.transformer.finish(x, y);

        if (this.transformer instanceof GSelector) {
            initSelectedShapes();
            ((GSelector) this.transformer).processSelection(this.shapes, this.selectedShapes);
        }

        Graphics2D bufferGraphics = bufferImage.createGraphics();
        bufferGraphics.setColor(this.getBackground());
        bufferGraphics.fillRect(0, 0, getWidth(), getHeight());
        bufferGraphics.setColor(this.getForeground());

        for (GShape shape : shapes) {
            shape.draw(bufferGraphics);
        }
        this.transformer = null;

        repaint();
    }

    private void continueDrawing(int x, int y) {
        this.transformer.cont(x, y);

        Graphics2D bufferGraphics = bufferImage.createGraphics();
        bufferGraphics.setColor(this.getBackground());
        bufferGraphics.fillRect(0, 0, getWidth(), getHeight());
        bufferGraphics.setColor(this.getForeground());

        for (GShape shape : shapes) {
            shape.draw(bufferGraphics);
        }
        bufferGraphics.dispose();
        repaint();
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
            if (toolBar.getEShapeType().getDrawingType() == Constants.EDrawingType.eNPoint) {
                if (eDrawingState == EDrawingState.eIdle) {
                    startTransform(e.getX(), e.getY());
                    eDrawingState = EDrawingState.eTransforming;
                } else if (eDrawingState == EDrawingState.eTransforming) {
                    continueDrawing(e.getX(), e.getY());
                }
                prepareDrawing();
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