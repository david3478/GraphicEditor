package org.example;

import org.example.shape.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class GDrawingPanel extends JPanel {

    public void setToolBar(GToolBar toolBar) {
        this.toolBar =  toolBar;
    }

    private GToolBar toolBar;

    // 그려진 도형들을 저장할 리스트 (부모 클래스 타입으로 참조)
    private List<GShape> shapes;
    private GShape currentShape;

    public GDrawingPanel() {
        super();
        this.setBackground(Color.WHITE);

        shapes = new ArrayList<>();
        currentShape = null;

        MouseHandler mouseHandler = new MouseHandler();
        this.addMouseListener(mouseHandler);
        this.addMouseMotionListener(mouseHandler);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 확정되어 저장된 도형들 그리기
        for (GShape shape : shapes) {
            shape.draw(g);
        }

        // 2. 현재 드래그 중인 도형 미리보기
        if (toolBar.getShapeType() != ShapeType.NONE && currentShape != null) {
            g.setColor(Color.RED); // 미리보기는 빨간색으로
            currentShape.draw(g);
            g.setColor(Color.BLACK); // 색상 원상복구
        }
    }

    private class MouseHandler extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (toolBar.getShapeType() == ShapeType.RECTANGLE) {
                currentShape = new GRectangle(e.getX(), e.getY(), 0, 0);
            } else if (toolBar.getShapeType() == ShapeType.OVAL) {
                currentShape = new GOval(e.getX(), e.getY(), 0, 0);
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (toolBar.getShapeType() != ShapeType.NONE && currentShape != null) {
                int width = Math.abs(currentShape.getX() - e.getX());
                int height = Math.abs(currentShape.getY() - e.getY());
                currentShape.setWidth(width);
                currentShape.setHeight(height);
                repaint();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (toolBar.getShapeType() != ShapeType.NONE && currentShape != null) {
                Point startPoint = new Point(currentShape.getX(), currentShape.getY());
                Point currentPoint = new Point(e.getX(), e.getY());
                int x = Math.min(startPoint.x, currentPoint.x);
                int y = Math.min(startPoint.y, currentPoint.y);
                int width = Math.abs(startPoint.x - currentPoint.x);
                int height = Math.abs(startPoint.y - currentPoint.y);

                if (width > 0 && height > 0) {
                    if (toolBar.getShapeType() == ShapeType.RECTANGLE) {
                        shapes.add(new GRectangle(x, y, width, height));
                    } else if (toolBar.getShapeType() == ShapeType.OVAL) {
                        shapes.add(new GOval(x, y, width, height));
                    }
                }

                currentShape = null;
                repaint();
            }
        }
    }
}