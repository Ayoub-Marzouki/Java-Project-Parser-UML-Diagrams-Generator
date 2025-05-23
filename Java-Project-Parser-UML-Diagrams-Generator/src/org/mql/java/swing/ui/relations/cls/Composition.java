package org.mql.java.swing.ui.relations.cls;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

import org.mql.java.util.SwingUtilities;


public class Composition {
	 // Change color of each merge relation
    static Random random = new Random();
    static int red = random.nextInt(256);
    static int green = random.nextInt(256);
    static int blue = random.nextInt(256);
    static Color randomColor = new Color(red, green, blue);

	
    /**
     * Draws a straight composition line between two adjacent class diagrams.
     * It includes a composition diamond symbol and a UML label "<<composition>>".
     *
     * @param g               The Graphics context to draw on.
     * @param sourcePosition  The (x, y) coordinates of the source (child) class diagram.
     * @param targetPosition  The (x, y) coordinates of the target (parent) class diagram.
     * @param direction       An integer indicating the direction of the relationship:
     *                        0 = draw composition diamond at the source (child),
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
	    
	    g2d.drawString("<<composition>>", (parentX + childX) /2 , parentY);
	    
	    g2d.drawLine(childX, childY, parentX, parentY);
	    if (direction == 0) {
		    SwingUtilities.drawCompositionSign(g2d, childX, childY, 4);
	    } else {
		    SwingUtilities.drawCompositionSign(g2d, parentX, parentY, 2);
	    }
	}
	
	
	/**
	 * Draws a multi-segment composition line between vertically stacked class diagrams.
	 * The path follows vertical-horizontal-vertical segments and includes a composition diamond and label.
	 *
	 * @param g                  The Graphics context to draw on.
	 * @param sourcePosition     The (x, y) coordinates of the source (child) class (top edge).
	 * @param targetPosition     The (x, y) coordinates of the target (parent) class (bottom edge).
	 * @param direction          An integer indicating the direction of the relationship:
	 *                           0 = draw composition diamond pointing upward,
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
	    
	    g2d.drawString("<<composition>>", parentX, parentY);
	    
	    // Draw vertical line from child to the calculated position
	    g2d.drawLine(childX, childY, childX, childY + verticalLineLength);  // Vertical line to extend to the parent level

	    // Draw horizontal line from the child to the parent class's horizontal position
	    g2d.drawLine(childX, childY + verticalLineLength, parentX, childY + verticalLineLength);  // Horizontal line at the parent level

	    // Draw vertical line from the parent class to the final position
	    g2d.drawLine(parentX, childY + verticalLineLength, parentX, parentY); // Vertical line to the parent
	    
	    if (direction == 0) {
		    SwingUtilities.drawCompositionSign(g2d, childX, childY, 3);
		    
	    } else {
		    SwingUtilities.drawCompositionSign(g2d, childX, childY, 1);
	    }
	}

}
