package org.example.global;

import org.example.shapes.*;

public class Constants {
    public enum EDrawingType {
        e2Point,
        eNPoint
    }

    public enum EShapeType {
        eSelect("선택", new GRectangle(), EDrawingType.e2Point, IMAGE_PATH+ICON_SELECT_PATH),
        eRectangle("네모", new GRectangle(), EDrawingType.e2Point, IMAGE_PATH+ICON_RECT_PATH),
        eOval("동그라미", new GOval(), EDrawingType.e2Point, IMAGE_PATH+ICON_OVAL_PATH),
        eLine("라인", new GLine() ,EDrawingType.e2Point, IMAGE_PATH+ICON_LINE_PATH),
        ePolygon("폴리곤", new GPolygon() ,EDrawingType.eNPoint, IMAGE_PATH+ICON_POLYGON_PATH),;

        private final String name;
        private final GShape shape;
        private final EDrawingType drawingType;
        private final String iconFileName;

        private EShapeType(String name, GShape shape, EDrawingType drawingType, String iconFileName) {
            this.name = name;
            this.drawingType = drawingType;
            this.shape = shape;
            this.iconFileName = iconFileName;
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

        public String getIconFileName() {
            return iconFileName;
        }
    }

    // ShapeToolBar Icon
    public static final int ICON_WIDTH_SIZE = 24;
    public static final int ICON_HEIGHT_SIZE = 24;
    public static final String IMAGE_PATH = "/image/";
    public static final String ICON_RECT_PATH = "rectangle.png";
    public static final String ICON_OVAL_PATH = "oval.png";
    public static final String ICON_LINE_PATH = "line.png";
    public static final String ICON_POLYGON_PATH = "polygon.png";
    public static final String ICON_SELECT_PATH = "select.png";

    // ColorToolBar
    public static final String OUTLINE_BUTTON_NAME = "테두리 색";
    public static final String OUTLINE_BUTTON_MENU_NAME = "테두리 색 선택";
    // ANCHOR
    public final static int ANCHOR_WIDTH_SIZE = 15;
    public final static int ANCHOR_HEIGHT_SIZE = 15;
    public final static String ICON_ROTATE_PATH = "/image/rotate.png";
}
