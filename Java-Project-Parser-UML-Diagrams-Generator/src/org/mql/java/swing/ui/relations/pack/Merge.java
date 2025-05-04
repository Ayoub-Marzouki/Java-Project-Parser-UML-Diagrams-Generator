package org.mql.java.swing.ui.relations.pack;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

import org.mql.java.swing.ui.PackageDiagram;
import org.mql.java.util.SwingUtilities;

public class Merge {
    // Horizontal extension length for the arrow
    static int horizontalLineLength = 30;
    // Initial horizontal and vertical gaps before the first package (Origins :diagramsLayer.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20)) + nameRectCoordinates[1] ( = 5) )
    public static int diagramsLayerHGap = 25;
    public static int diagramsLayerVGap = 25; 
    public static int gapBetweenPackages = 10; // Vertical gap between consecutive packages (Origins : packageDiagramHeight = nameRectCoordinates[3] + packageRectCoordinates[3] + 10;)
    
    // For the dashed stroke we'll be using to create dashed lines
    private static float[] dashPattern = {8, 8};  // 10 pixels for the dash, 8 pixels for the gap
    
    
    /**
     * Draws a <<merge>> relationship between two packages in a package diagram.
     * It uses a dashed routed line (right → down → left) and a hollow triangle arrowhead pointing from source to target.
     *
     * @param g            The Graphics context to draw on.
     * @param sourcePackage The source PackageDiagram containing both packages.
     * @param sourceIndex  The vertical index of the source package.
     * @param targetIndex  The vertical index of the target package.
     */
    public static void draw(Graphics g, PackageDiagram sourcePackage, int sourceIndex, int targetIndex) {
    	Graphics2D g2d = (Graphics2D) g;
    	// Package height + 10 (I think 10 is coming from nameRect[1] + packrect[1]) + 3 (size of stroke used there)
        int packageHeight = sourcePackage.getPackageDiagramHeight() + 13;

        // Source package middle-right boundary coordinates
        // 30 is the additional visual offset caused by some coordinates while drawing the package, minus 400 (the additional space from nameRect[2])
        int sourcePackageX = sourcePackage.getPackageDiagramWidth() - 400 + diagramsLayerHGap;
        int sourcePackageY = diagramsLayerVGap + (packageHeight + gapBetweenPackages) * sourceIndex + packageHeight / 2;

        // Target package middle-right boundary coordinates
        int targetPackageX = sourcePackageX;
        int targetPackageY = diagramsLayerVGap + (packageHeight + gapBetweenPackages) * targetIndex + packageHeight / 2;

        
        g2d.setStroke(SwingUtilities.createDashedStroke(dashPattern, 2));

        // Change color of each merge relation
        Random random = new Random();
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);

        Color randomColor = new Color(red, green, blue);
        g2d.setColor(randomColor);
        
        g2d.drawLine(sourcePackageX, sourcePackageY, sourcePackageX + horizontalLineLength, sourcePackageY); // Move right
        g2d.drawLine(sourcePackageX + horizontalLineLength, sourcePackageY, sourcePackageX + horizontalLineLength, targetPackageY); // Move down
        g2d.drawLine(sourcePackageX + horizontalLineLength, targetPackageY, targetPackageX, targetPackageY); // Move left
        
        SwingUtilities.drawArrow(g2d, sourcePackageX, sourcePackageY, targetPackageX, targetPackageY, 4);
        
        g2d.setColor(Color.black);
        g2d.drawString("<<merge>>", targetPackageX, 2 * targetPackageY);
    }
}
