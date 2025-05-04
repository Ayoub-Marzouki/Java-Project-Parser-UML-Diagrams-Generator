package org.mql.java.util;

import java.awt.BasicStroke;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;

public class SwingUtilities {

	// Enum for directions
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    /**
     * Rotates the graphics context to the specified direction.
     * 
     * @param g2d The Graphics2D object used for drawing.
     * @param direction The desired direction (UP, DOWN, LEFT, RIGHT).
     * @param centerX The x-coordinate of the rotation center.
     * @param centerY The y-coordinate of the rotation center.
     */
    public static void rotate(Graphics2D g2d, Direction direction, int centerX, int centerY) {
        double angleInRadians = 0;

        // Determine the angle based on the specified direction
        switch (direction) {
            case UP:
                angleInRadians = Math.toRadians(-90); // Rotate 90 degrees counterclockwise
                break;
            case DOWN:
                angleInRadians = Math.toRadians(90); // Rotate 90 degrees clockwise
                break;
            case LEFT:
                angleInRadians = Math.toRadians(180); // Rotate 180 degrees
                break;
            case RIGHT:
                angleInRadians = 0; // No rotation (default is right)
                break;
        }

        // Apply rotation
        AffineTransform transform = new AffineTransform();
        transform.rotate(angleInRadians, centerX, centerY);
        g2d.setTransform(transform);
    }
	
    /**
     * Draws a directional triangular arrowhead at a given endpoint (x2, y2),
     * pointing in one of four cardinal directions (UP, RIGHT, DOWN, LEFT).
     *
     * The arrow is drawn as a triangle using trigonometric calculations.
     *
     * @param g         The Graphics context to draw on.
     * @param x1        Starting x-coordinate (not used in current implementation).
     * @param y1        Starting y-coordinate (not used in current implementation).
     * @param x2        x-coordinate of the arrow tip (where the arrowhead is drawn).
     * @param y2        y-coordinate of the arrow tip (where the arrowhead is drawn).
     * @param direction Direction of the arrow:
     *                  1 = UP, 2 = RIGHT, 3 = DOWN, 4 = LEFT.
     *
     * @throws IllegalArgumentException if direction is not in [1, 4].
     */
    public static void drawArrow(Graphics g, int x1, int y1, int x2, int y2, int direction) {
        Graphics2D g2d = (Graphics2D) g;

        // Set the color for the arrow
        g2d.setStroke(new BasicStroke(2));  // Line thickness

        // Arrowhead points
        int arrowSize = 10;  // Size of the arrowhead

        double angle;
        switch (direction) {
        case 1: // point UP
            angle = Math.PI / 2; // 90 degree
            break;
        case 2: // Point RIGHT
        	angle = 0;  // 0 degrees
            break;
        case 3: // Point DOWN
            angle = -Math.PI / 2;  // -90 degrees
            break;
        case 4: // Point LEFT
        	angle = Math.PI; // 180 degrees
            break;
        default:
            throw new IllegalArgumentException("Invalid direction: " + direction);
        }

        // Calculate the points for the arrowhead
        int xArrow1 = (int) (x2 - arrowSize * Math.cos(angle - Math.PI / 6));
        int yArrow1 = (int) (y2 - arrowSize * Math.sin(angle - Math.PI / 6));

        int xArrow2 = (int) (x2 - arrowSize * Math.cos(angle + Math.PI / 6));
        int yArrow2 = (int) (y2 - arrowSize * Math.sin(angle + Math.PI / 6));

        // Draw the arrowhead (triangle)
        g2d.drawPolygon(new int[] { x2, xArrow1, xArrow2 }, new int[] { y2, yArrow1, yArrow2 }, 3);
    }
    
    /**
     * Draws an open directional arrowhead (two lines forming a 'V') at the endpoint (x2, y2),
     * pointing in one of four cardinal directions (UP, RIGHT, DOWN, LEFT).
     *
     * This method only draws the arrowhead and does not include the shaft/line from (x1, y1).
     *
     * @param g         The Graphics context to draw on.
     * @param x1        Starting x-coordinate (not used in this method).
     * @param y1        Starting y-coordinate (not used in this method).
     * @param x2        x-coordinate of the arrow tip (where the head points).
     * @param y2        y-coordinate of the arrow tip (where the head points).
     * @param direction Direction of the arrow:
     *                  1 = UP, 2 = RIGHT, 3 = DOWN, 4 = LEFT.
     *
     * @throws IllegalArgumentException if direction is not in [1, 4].
     */
    public static void drawSimpleArrow(Graphics g, int x1, int y1, int x2, int y2, int direction) {
        Graphics2D g2d = (Graphics2D) g;

        // Set the color and stroke for the arrow
        g2d.setStroke(new BasicStroke(2));  // Line thickness

        // Arrowhead points
        int arrowSize = 10;  // Size of the arrowhead

        // Calculate the angle based on the direction
        double angle;
        switch (direction) {
            case 1: // Point UP
                angle = Math.PI / 2; // 90 degrees
                break;
            case 2: // Point RIGHT
                angle = 0;  // 0 degrees
                break;
            case 3: // Point DOWN
                angle = -Math.PI / 2;  // -90 degrees
                break;
            case 4: // Point LEFT
                angle = Math.PI; // 180 degrees
                break;
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction);
        }

        // Calculate the points for the arrowhead
        int xArrow1 = (int) (x2 - arrowSize * Math.cos(angle - Math.PI / 6));
        int yArrow1 = (int) (y2 - arrowSize * Math.sin(angle - Math.PI / 6));

        int xArrow2 = (int) (x2 - arrowSize * Math.cos(angle + Math.PI / 6));
        int yArrow2 = (int) (y2 - arrowSize * Math.sin(angle + Math.PI / 6));

        // Draw the arrowhead as two lines (open arrowhead)
        g2d.drawLine(x2, y2, xArrow1, yArrow1); // First line of the arrowhead
        g2d.drawLine(x2, y2, xArrow2, yArrow2); // Second line of the arrowhead
    }
    
    
    /**
     * Draws an aggregation sign (diamond) at the specified position and direction.
     *
     * @param g2d       The Graphics2D object to draw on.
     * @param x         The x-coordinate of the center of the diamond.
     * @param y         The y-coordinate of the center of the diamond.
     * @param size      The size of the diamond (width and height).
     * @param direction The direction of the diamond (1 = up, 2 = right, 3 = down, 4 = left).
     */
    public static void drawAggregationSign(Graphics2D g2d, int x, int y, int direction) {
        int[] xPoints;
        int[] yPoints;
        
        int size = 10;

        switch (direction) {
            case 1: // Point UP
                xPoints = new int[] { x, x + size / 2, x, x - size / 2 };
                yPoints = new int[] { y - size / 2, y, y + size / 2, y };
                break;
            case 2: // Point RIGHT
                xPoints = new int[] { x + size / 2, x, x - size / 2, x };
                yPoints = new int[] { y, y - size / 2, y, y + size / 2 };
                break;
            case 3: // Point DOWN
                xPoints = new int[] { x, x - size / 2, x, x + size / 2 };
                yPoints = new int[] { y + size / 2, y, y - size / 2, y };
                break;
            case 4: // Point LEFT
                xPoints = new int[] { x - size / 2, x, x + size / 2, x };
                yPoints = new int[] { y, y + size / 2, y, y - size / 2 };
                break;
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction);
        }

        // Create a polygon with the four points
        Polygon diamond = new Polygon(xPoints, yPoints, 4);

        // Draw the diamond
        g2d.fill(diamond);
    }
    
    
    /**
     * Draws an unfilled diamond (composition symbol) at the specified coordinates and direction.
     * This is commonly used in UML diagrams to indicate a composition relationship.
     *
     * @param g2d       The Graphics2D context to draw on.
     * @param x         The x-coordinate of the center of the diamond.
     * @param y         The y-coordinate of the center of the diamond.
     * @param direction The direction the diamond should face:
     *                  1 = UP, 2 = RIGHT, 3 = DOWN, 4 = LEFT.
     *
     * @throws IllegalArgumentException if direction is not between 1 and 4.
     */
    public static void drawCompositionSign(Graphics2D g2d, int x, int y, int direction) {
        int[] xPoints;
        int[] yPoints;
        
        int size = 10;

        switch (direction) {
            case 1: // Point UP
                xPoints = new int[] { x, x + size / 2, x, x - size / 2 };
                yPoints = new int[] { y - size / 2, y, y + size / 2, y };
                break;
            case 2: // Point RIGHT
                xPoints = new int[] { x + size / 2, x, x - size / 2, x };
                yPoints = new int[] { y, y - size / 2, y, y + size / 2 };
                break;
            case 3: // Point DOWN
                xPoints = new int[] { x, x - size / 2, x, x + size / 2 };
                yPoints = new int[] { y + size / 2, y, y - size / 2, y };
                break;
            case 4: // Point LEFT
                xPoints = new int[] { x - size / 2, x, x + size / 2, x };
                yPoints = new int[] { y, y + size / 2, y, y - size / 2 };
                break;
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction);
        }

        // Create a polygon with the four points
        Polygon diamond = new Polygon(xPoints, yPoints, 4);

        // Draw the diamond
        g2d.draw(diamond);
    }
    
    /**
     * Creates a custom dashed stroke for drawing dashed lines.
     *
     * @param dashPattern  An array of floats specifying the dash pattern.
     *                     For example: {5f, 5f} creates equal dashes and spaces.
     * @param strokeWidth  The thickness of the stroke.
     *
     * @return A BasicStroke object configured with the given dash pattern and width.
     */
    public static BasicStroke createDashedStroke(float[] dashPattern, float strokeWidth) {
        return new BasicStroke(strokeWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, dashPattern, 0f);
    }
    
    
    
    /**	<h1>Centers multi-lines text inside a given rectangle. We calculate each of the horizontal and vertical centering based on the following :</h1> </br>
	 * <strong>Horizontal centering :</strong> </br>
	 * + Width of string (obtained via FontMetrics class) </br> 
	 * + Space left after inserting the string inside the rect (width(rect) - width(string)) </br>
	 * + starting position of the string x :  </br>
	 * 		we want the center, so free space / 2 </br>
	 * 		we then add the x of rectangle </br>
	 * --> Final formula : </br>
	 * x = x(rectangle) + (width(rect) - with(string)) / 2 </br> </br>
	 * 
	 * <strong>Vertical Centering :</strong> </br>
	 * y = y(rectangle) + (height(rect) - height(string)) / 2 + ascent </br>
	 * 
	 * ascent : the distance from the baseline to the highest position of a character (like T, H...)
	 * 
	 */
	public static void drawCenteredText(Graphics2D g2d, String text, int rectX, int rectY, int rectWidth, int rectHeight) {
	    FontMetrics metrics = g2d.getFontMetrics();
	    int lineHeight = metrics.getHeight(); // Height of a single line of text

	    // Split the text into lines
	    String[] lines = text.split("\n");

	    // Calculate the total height of the text block
	    int totalTextHeight = lines.length * lineHeight;

	    // Calculate the starting Y position to center the text block vertically
	    int startY = rectY + (rectHeight - totalTextHeight) / 2 + metrics.getAscent();

	    // Draw each line
	    for (int i = 0; i < lines.length; i++) {
	        String line = lines[i];
	        int textWidth = metrics.stringWidth(line);

	        // Calculate the X position to center the line horizontally
	        int stringX = rectX + (rectWidth - textWidth) / 2;

	        // Calculate the Y position for this line
	        int stringY = startY + (i * lineHeight);

	        // Draw the line
	        g2d.drawString(line, stringX, stringY);
	    }
	}



}
