package org.example.frames;

import org.example.shapes.GOval;
import org.example.shapes.GPolygon;
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
        eRotating, eSearing,

        ePolygonDrawing
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

    // ----DoubleBuffering 헬퍼 메서드 ---
    private void initBufferedImage() {
        if (this.getWidth() <= 0 || this.getHeight() <= 0 || this.eDrawingState == EDrawingState.eIdle) {
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

    private Graphics2D getGraphics2DByBufferedImage() {
        Graphics2D bufferGraphics = this.bufferImage.createGraphics();
        bufferGraphics.setColor(this.getBackground());
        bufferGraphics.fillRect(0, 0, this.getWidth(), this.getHeight());
        bufferGraphics.setColor(this.getGraphics().getColor());
        return bufferGraphics;
    }

    // --- Rectangular Shape 관련 메서드
    private void startRectangularShape(int x, int y) {
        if(this.eDrawingState == EDrawingState.eIdle) {
            if(this.toolBar.getEShapeType() == GShapeToolBar.EShapeType.eSelect) {
                for(GShape shape : this.shapes) {
                    GShape.EAnchor eAnchor = shape.onShape(x, y);
                    if(eAnchor != null) {
                        this.currentShape = shape;
                        if(eAnchor == GShape.EAnchor.eMove) {
                            eDrawingState = EDrawingState.eMoving;
                            currentShape.setLocationP(x, y);
                        } else if(eAnchor == GShape.EAnchor.eRotate){
                            eDrawingState = EDrawingState.eRotating;
                        } else { // resize
                            eDrawingState = EDrawingState.eResizing;

                        }
                        break;
                    }
                }
            } else { // drawing
                if(this.toolBar.getEShapeType() == GShapeToolBar.EShapeType.eRectangle) {
                    this.currentShape = new GRectangle(x, y, x, y);
                } else if(this.toolBar.getEShapeType() == GShapeToolBar.EShapeType.eOval) {
                    this.currentShape = new GOval(x, y, x, y);
                } else { // rectangular shape이 아님
                    return;
                }
                this.eDrawingState = EDrawingState.eDrawing;
            }
        }

        initBufferedImage();
    }


    private void keepDrawing(int x, int y) {
        if(this.eDrawingState != EDrawingState.eIdle) {
            Graphics2D bufferGraphics = getGraphics2DByBufferedImage();

            if (this.eDrawingState == EDrawingState.eDrawing) {
                this.currentShape.setLocation1(x, y);
                this.currentShape.draw(bufferGraphics);
            } else if (this.eDrawingState == EDrawingState.eMoving) {
                this.currentShape.move(x, y);
            } else if (this.eDrawingState == EDrawingState.eResizing) {
                this.currentShape.resize(x, y);
            } else if (this.eDrawingState == EDrawingState.eRotating) {
                this.currentShape.rotate(x, y);
            }
            for (GShape shape : shapes) {
                shape.draw(bufferGraphics);
            }
            this.currentShape.draw(bufferGraphics);
            bufferGraphics.dispose();
            repaint();
        }
    }

    private void finishRectangularShape(int x, int y) {
        if(this.eDrawingState != EDrawingState.eIdle && this.eDrawingState != EDrawingState.ePolygonDrawing) {
            if(this.toolBar.getEShapeType() != GShapeToolBar.EShapeType.eSelect) {
                addShape();
            }
            this.currentShape = null;
            this.eDrawingState = EDrawingState.eIdle;
        }
    }

    private void addShape() {
//        if(this.eDrawingState == EDrawingState.eDrawing) {
            this.shapes.add(this.currentShape);
//        }
    }

    // Polygon 관련 메서드
    private void polygonKeepAnimation(MouseEvent e) {
        if (eDrawingState == EDrawingState.ePolygonDrawing) {
            Graphics2D bufferGraphics = getGraphics2DByBufferedImage();


            ((GPolygon) currentShape).setLastPoint(e.getX(), e.getY());
            for (GShape shape : shapes) {
                shape.draw(bufferGraphics);
            }
            this.currentShape.draw(bufferGraphics);
            bufferGraphics.dispose();
            repaint();
        }
    }

    private void polygonfinishDrawing() {
        System.out.println(eDrawingState);
        if (eDrawingState == EDrawingState.ePolygonDrawing) {
            ((GPolygon) currentShape).closePolygon();
            addShape();

            currentShape = null;
            eDrawingState = EDrawingState.eIdle;

            repaint();
        }
    }

    private void polygonStartAndKeepDrawing(MouseEvent e) {
        if (toolBar.getEShapeType() == GShapeToolBar.EShapeType.ePolygon) {

            if (eDrawingState == EDrawingState.eIdle) {

                currentShape = new GPolygon();
                ((GPolygon) currentShape).addPoint(e.getX(), e.getY());
                ((GPolygon) currentShape).addPoint(e.getX(), e.getY());

                eDrawingState = EDrawingState.ePolygonDrawing;

                initBufferedImage();
            } else if (eDrawingState == EDrawingState.ePolygonDrawing) {
                Graphics2D bufferGraphics = getGraphics2DByBufferedImage();

                ((GPolygon) currentShape).addPoint(e.getX(), e.getY());

                for (GShape shape : shapes) {
                    shape.draw(bufferGraphics);
                }
                currentShape.draw(bufferGraphics);
            }
        }
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

        @Override
        public void mouseMoved(MouseEvent e) {
            polygonKeepAnimation(e);
        }



        private void mouseLButtonClicked(MouseEvent e) {
            polygonStartAndKeepDrawing(e);
        }

        private void mouseLButton2Clicked(MouseEvent e) {
            polygonfinishDrawing();
        }


        @Override
        public void mousePressed(MouseEvent e) {
//            startRectangularShape(e.getX(),e.getY());
//            if(eDrawingState == EDrawingState.eIdle) {
                startRectangularShape(e.getX(),e.getY());
//                eDrawingState = EDrawingState.eDrawing;
//            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
//            finishRectangularShape(e.getX(),e.getY());
//            if(eDrawingState == EDrawingState.eDrawing) {
                keepDrawing(e.getX(),e.getY());
//            }
        }
        @Override
        public void mouseReleased(MouseEvent e) {
//            finishRectangularShape(e.getX(),e.getY());
//            if(eDrawingState ==EDrawingState.eDrawing) {
                finishRectangularShape(e.getX(),e.getY());
//                eDrawingState = EDrawingState.eIdle;
//            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }


    }



}