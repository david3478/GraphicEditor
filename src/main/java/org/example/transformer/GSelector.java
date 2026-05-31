package org.example.transformer;

import org.example.shapes.GRectangle;
import org.example.shapes.GShape;

import java.awt.*;
import java.util.Vector;

public class GSelector extends GTransformer {

    public GSelector(GShape shape) {
        super(shape);
        // 시각화를 위한 설정
        float[] dash = {5.0f};
        this.shape.setLineColor(Color.BLUE);
        this.shape.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
    }

    @Override
    public void start(int x, int y) {
        this.shape.setLocation0(x, y);
        this.shape.setLocation1(x, y);
    }

    @Override
    public void keep(int x, int y) {
        this.shape.setLocation1(x, y);
    }

    @Override
    public void finish(int x, int y) {
        this.shape.setLocation1(x, y);
    }

    public void draw(Graphics2D graphics) {
        this.shape.draw(graphics);
    }

    public void processSelection(Vector<GShape> allShapes, Vector<GShape> selectedShapes) {
        // 기존 선택 초기화
        selectedShapes.clear();

        Rectangle selectionBox = this.shape.getShape().getBounds();

        for (GShape s : allShapes) {
            if (s.isContained(selectionBox)) {
                selectedShapes.add(s);
            }
        }
        System.out.println("다중 선택된 도형 개수: " + selectedShapes.size());
    }
}