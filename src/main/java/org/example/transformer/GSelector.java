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
        getShape0().setLineColor(Color.BLUE);
        getShape0().setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
    }

    @Override
    public void start(int x, int y) {
        getShape0().setLocation0(x, y);
        getShape0().setLocation1(x, y);
    }

    @Override
    public void keep(int x, int y) {
        getShape0().setLocation1(x, y);
    }

    @Override
    public void finish(int x, int y) {
        getShape0().setLocation1(x, y);
    }

    public void draw(Graphics2D graphics) {
        getShape0().draw(graphics);
    }

    public void processSelection(Vector<GShape> allShapes, Vector<GShape> selectedShapes) {
        // 기존 선택 초기화
        selectedShapes.clear();

        Rectangle selectionBox = getShape0().getShape().getBounds();

        for (GShape shape : allShapes) {
            if (shape.isContained(selectionBox)) {
                selectedShapes.add(shape);
                shape.setSelected(true);
            }
        }
        System.out.println("다중 선택된 도형 개수: " + selectedShapes.size());
    }
}