package org.example.shapes;

import java.awt.*;

import java.util.ArrayList;

public class GPolygon extends GShape {

    private ArrayList<Integer> xList;
    private ArrayList<Integer> yList;

    public GPolygon() {
        super(0,0,0,0);  // GShape 생성자 호출용
        xList = new ArrayList<>();
        yList = new ArrayList<>();
    }

    public void addPoint(int x, int y) {
        xList.add(x);
        yList.add(y);
    }

    // 마지막 점 업데이트 (마우스 move 미리보기용)
    public void setLastPoint(int x, int y) {
        if (!xList.isEmpty()) {
            int lastIndex = xList.size() - 1;
            xList.set(lastIndex, x);
            yList.set(lastIndex, y);
        }
    }

    @Override
    public void draw(Graphics2D g) {

        if (xList.size() < 2) return;

        int nPoints = xList.size();

        int[] xPoints = new int[nPoints];
        int[] yPoints = new int[nPoints];

        for (int i = 0; i < nPoints; i++) {
            xPoints[i] = xList.get(i);
            yPoints[i] = yList.get(i);
        }

        g.drawPolygon(xPoints, yPoints, nPoints);
    }

    @Override
    public void move(int x, int y) {

        int dx = x - this.px;
        int dy = y - this.py;

        for (int i = 0; i < xList.size(); i++) {
            xList.set(i, xList.get(i) + dx);
            yList.set(i, yList.get(i) + dy);
        }

        super.move(x, y);
    }
    // 다각형 완성
    public void closePolygon() {
        if (xList.size() > 2) {
            xList.add(xList.get(0));
            yList.add(yList.get(0));
        }

        updateBoundingBox();
    }

    // x0, x1, y0, y1 설정
    private void updateBoundingBox() {

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (int i = 0; i < xList.size(); i++) {

            int x = xList.get(i);
            int y = yList.get(i);

            if (x < minX) minX = x;
            if (y < minY) minY = y;

            if (x > maxX) maxX = x;
            if (y > maxY) maxY = y;
        }

        setLocation0(minX, minY);
        setLocation1(maxX, maxY);
    }
}