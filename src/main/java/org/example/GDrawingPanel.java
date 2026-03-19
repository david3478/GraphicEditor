package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class GDrawingPanel extends JPanel {
    // 도형의 종류를 명시하는 열거형
    public enum ShapeType { RECTANGLE, OVAL, NONE }
    private ShapeType currentShapeType = ShapeType.NONE;

    // 그려진 도형들을 저장할 리스트
    private List<DrawingShape> shapes = new ArrayList<>();

    private Point startPoint;
    private Point currentPoint;

    public GDrawingPanel() {
        super();
        this.setBackground(Color.WHITE);

        MouseHandler mouseHandler = new MouseHandler();
        this.addMouseListener(mouseHandler);
        this.addMouseMotionListener(mouseHandler);
    }

    // 툴바에서 호출할 메서드
    public void setCurrentShape(ShapeType type) {
        this.currentShapeType = type;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 1. 확정되어 저장된 도형들 그리기
        for (DrawingShape shape : shapes) {
            drawShape(g, shape.type, shape.x, shape.y, shape.width, shape.height);
        }

        // 2. 현재 드래그 중인 도형 미리보기
        if (currentShapeType != ShapeType.NONE && startPoint != null && currentPoint != null) {
            int x = Math.min(startPoint.x, currentPoint.x);
            int y = Math.min(startPoint.y, currentPoint.y);
            int width = Math.abs(startPoint.x - currentPoint.x);
            int height = Math.abs(startPoint.y - currentPoint.y);

            g.setColor(Color.RED); // 미리보기는 빨간색으로
            drawShape(g, currentShapeType, x, y, width, height);
            g.setColor(Color.BLACK); // 색상 원상복구
        }
    }

    // 도형 종류에 따라 알맞은 Graphics 메서드를 호출해 주는 헬퍼 메서드
    private void drawShape(Graphics g, ShapeType type, int x, int y, int width, int height) {
        if (type == ShapeType.RECTANGLE) {
            g.drawRect(x, y, width, height);
        } else if (type == ShapeType.OVAL) {
            g.drawOval(x, y, width, height);
        }
    }

    private class MouseHandler extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (currentShapeType != ShapeType.NONE) {
                startPoint = e.getPoint();
                currentPoint = startPoint;
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (currentShapeType != ShapeType.NONE) {
                currentPoint = e.getPoint();
                repaint();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (currentShapeType != ShapeType.NONE && startPoint != null) {
                int x = Math.min(startPoint.x, currentPoint.x);
                int y = Math.min(startPoint.y, currentPoint.y);
                int width = Math.abs(startPoint.x - currentPoint.x);
                int height = Math.abs(startPoint.y - currentPoint.y);

                if (width > 0 && height > 0) {
                    // 리스트에 추가할 때 도형의 타입도 함께 넘겨줍니다.
                    shapes.add(new DrawingShape(currentShapeType, x, y, width, height));
                }

                startPoint = null;
                currentPoint = null;
                repaint();
            }
        }
    }

    // 도형의 타입과 좌표 정보를 묶어서 보관하기 위한 내부 클래스
    private static class DrawingShape {
        ShapeType type;
        int x, y, width, height;

        DrawingShape(ShapeType type, int x, int y, int width, int height) {
            this.type = type;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }
}