package org.mql.java.swing.ui.relations.cls;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

import org.mql.java.util.SwingUtilities;


public class Aggregation {
	 // Change color of each merge relation
    static Random random = new Random();
    static int red = random.nextInt(256);
    static int green = random.nextInt(256);
    static int blue = random.nextInt(256);
    static Color randomColor = new Color(red, green, blue);

	
    /**
     * Draws a straight aggregation line between two adjacent class diagrams.
     * It includes an aggregation symbol and a UML label "<<aggregation>>".
     *
     * @param g               The Graphics context to draw on.
     * @param sourcePosition  The (x, y) position of the source (child) class diagram.
     * @param targetPosition  The (x, y) position of the target (parent) class diagram.
     * @param direction       An integer indicating the direction:
     *                        0 = draw aggregation symbol at the source (child),
     *                        otherwise at the target (parent).
     */
	public static void drawAdjacent(Graphics g, int[] sourcePosition, int[] targetPosition, int direction) {
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(2));
	    int childX = sourcePosition[0];
	    int childY = sourcePosition[1];

	    int parentX = targetPosition[0];
	    int parentY = targetPosition[1];
	    g2d.setColor(randomColor);
	    
	    g2d.drawString("<<aggregation>>", (parentX + childX) /2 , parentY);
	    
	    g2d.drawLine(childX, childY, parentX, parentY);
	    if (direction == 0) {
		    SwingUtilities.drawAggregationSign(g2d, childX, childY, 4);
	    } else {
		    SwingUtilities.drawAggregationSign(g2d, parentX, parentY, 2);
	    }
	}
	
	/**
	 * Draws a multi-segment aggregation line between two class diagrams stacked vertically,
	 * with a vertical-horizontal-vertical path. Includes label and aggregation symbol.
	 *
	 * @param g                  The Graphics context to draw on.
	 * @param sourcePosition     The (x, y) position of the source (child) class (top edge).
	 * @param targetPosition     The (x, y) position of the target (parent) class (bottom edge).
	 * @param direction          An integer indicating the direction:
	 *                           0 = draw aggregation symbol pointing upward,
	 *                           otherwise downward.
	 * @param verticalLineLength The vertical distance from the child to the horizontal segment.
	 */
	public static void draw(Graphics g, int[] sourcePosition, int[] targetPosition, int direction, int verticalLineLength) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(2));
		g2d.setColor(randomColor);
		
	    int childX = sourcePosition[0]; // Middle of the child class
	    int childY = sourcePosition[1]; // Top of the child class

	    int parentX = targetPosition[0]; // Middle of the parent class
	    int parentY = targetPosition[1]; // Bottom of the parent class
	    
	    g2d.drawString("<<aggregation>>", parentX, parentY);
	    
	    // Draw vertical line from child to the calculated position
	    g2d.drawLine(childX, childY, childX, childY + verticalLineLength);  // Vertical line to extend to the parent level

	    // Draw horizontal line from the child to the parent class's horizontal position
	    g2d.drawLine(childX, childY + verticalLineLength, parentX, childY + verticalLineLength);  // Horizontal line at the parent level

	    // Draw vertical line from the parent class to the final position
	    g2d.drawLine(parentX, childY + verticalLineLength, parentX, parentY); // Vertical line to the parent
	    
	    if (direction == 0) {
		    SwingUtilities.drawAggregationSign(g2d, childX, childY, 3);
		    
	    } else {
		    SwingUtilities.drawAggregationSign(g2d, childX, childY, 1);
	    }
	}

}
