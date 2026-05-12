package org.example.global;

import org.example.shapes.GOval;
import org.example.shapes.GRectangle;
import org.example.shapes.GShape;

public class Constants {
    public enum EDrawingType {
        e2Point,
        eNPoint
    }

    public enum EShapeType {
        eSelect("선택", new GRectangle(), EDrawingType.e2Point),
        eRectangle("네모", new GRectangle(), EDrawingType.e2Point),
        eOval("동그라미", new GOval(), EDrawingType.e2Point),
        eLine("라인", new GRectangle() ,EDrawingType.e2Point),
        ePolygon("폴리곤", new GRectangle() ,EDrawingType.eNPoint);

        private final String name;
        private final GShape shape;
        private final EDrawingType drawingType;

        private EShapeType(String name, GShape shape, EDrawingType drawingType) {
            this.name = name;
            this.drawingType = drawingType;
            this.shape = shape;
        }

        public String getName() {
            return name;
        }

        public EDrawingType getDrawingType() {
            return drawingType;
        }

        public GShape getShape() {
            return shape.clone();
        }
    }
}
