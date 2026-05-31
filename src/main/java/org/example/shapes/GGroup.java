package org.example.shapes;

import java.awt.Graphics2D;
import java.util.Vector;

public class GGroup extends GShape {
    private final Vector<GShape> childShapes;

    public GGroup(Vector<GShape> selectedShapes) {
        super();
        this.childShapes = selectedShapes;
    }

//    public void addShape(GShape shape) {
//        this.childShapes.add(shape);
//    }

    @Override
    public EAnchor onShape(int x, int y) {
        for (GShape shape : childShapes) {
            EAnchor anchor = shape.onShape(x, y);
            if (anchor != null) {
                // 그룹 안에 있는 도형 중 하나라도 클릭되었다면,
                // 일단 다중 선택(그룹) 상태에서는 개별 Resize/Rotate보다 '이동(eMove)' 상태를 반환하는 것이 가장 안전합니다.
                return EAnchor.eMove;
            }
        }
        return null; // 아무 도형도 클릭되지 않음
    }
    @Override
    public void translate(int dx, int dy) {
        for (GShape shape : childShapes) {
            shape.translate(dx, dy);
        }
    }

    // 나중에 확장성을 위한 세팅 (지금은 비워둬도 됨)
    // @Override public void resize(int x, int y) { for(...) child.resize(); }
    // @Override public void rotate(int x, int y) { for(...) child.rotate(); }

    @Override
    public void setLocation0(int x, int y) {}
    @Override
    public void setLocation1(int x, int y) {}

    @Override
    public void draw(Graphics2D graphics) {}
}