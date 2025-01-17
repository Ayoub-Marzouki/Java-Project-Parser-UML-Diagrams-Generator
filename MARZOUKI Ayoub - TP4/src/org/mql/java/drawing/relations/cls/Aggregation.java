package org.mql.java.drawing.relations.cls;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;


public class Aggregation {
	private Point start;
    private Point end;

    public Aggregation(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawLine(start.x, start.y, end.x, end.y);
        // Add an open diamond shape for aggregation at 'end'
        drawDiamond(g, end);
    }

    private void drawDiamond(Graphics g, Point position) {
        int[] xPoints = {position.x, position.x - 10, position.x, position.x + 10};
        int[] yPoints = {position.y - 10, position.y, position.y + 10, position.y};
        g.drawPolygon(xPoints, yPoints, 4);
    }

}
