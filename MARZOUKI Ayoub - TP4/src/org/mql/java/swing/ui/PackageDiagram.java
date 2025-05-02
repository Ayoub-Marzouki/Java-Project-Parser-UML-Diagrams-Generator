package org.mql.java.swing.ui;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.mql.java.swing.business.UMLDiagramsGenerator;
import org.mql.java.util.SwingUtilities;

public class PackageDiagram extends JPanel {
	private String packageName;
	
    private int numberOfPackageDiagrams = UMLDiagramsGenerator.getPackagesNames().size();
//  max horizontal space between the very first class of a package and the package
	private int maxLeftMargin = 200 * numberOfPackageDiagrams; 
//	max Number of Class Diagrams in a single package
	private int maxNumberOfClassDiagrams = UMLDiagramsGenerator.calculateNumberOfClassDiagrams();
	
	
	private int[] nameRectCoordinates = new int[] {5, 5, 400, 50};
//	Not sure if package diagram width's formula is 100% correct. to check later
//	PackageRect's y = nameRect's y + nameRect's height
	private int[] packageRectCoordinates = new int[] {nameRectCoordinates[0], nameRectCoordinates[1] + nameRectCoordinates[3], maxLeftMargin + 200 * numberOfPackageDiagrams * maxNumberOfClassDiagrams, ClassDiagram.getClassDiagramHeight() + 200}; // 200 being the vGap of flowLayout
//	Don't know why we added namerect[2], but deleting it causes problems
	private int packageDiagramWidth = packageRectCoordinates[2] + nameRectCoordinates[2];
//	height = sum(height) of all elements (composing the package diagram) + some space (we need to take into account the stroke's width), same goes for width
	private int packageDiagramHeight = nameRectCoordinates[3] + packageRectCoordinates[3] + 10;
	
	private Graphics2D g2d;
	
	public static int counter = 0;
	
	// This will be used to track the position of a certain class (first usage : generalization relations)
	private int leftMargin;
	
	public PackageDiagram(String packageName) {
		this.packageName = packageName;
		
		this.setPreferredSize(new Dimension(packageDiagramWidth, packageDiagramHeight));
		
		
/*		The hgap and vgap in what follows are ultra necessary :
 * 		The problem encountered if we do not add these, is that the classes added to the package will keep overlapping with their container, and the lines drawing the package will always be cut and interrupted. Manually pushing the classes to the inside of the package doesn't work either; it overrides the entire space; from the initial position to the position the classdiagram was pushed to.
 * Thus we can either go for JLayeredPane Layout, or we can use hgap and vgap.
 *		
 */
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 13, 200));
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(3));
		
		SwingUtilities.drawCenteredText(g2d, packageName, nameRectCoordinates[0], nameRectCoordinates[1], nameRectCoordinates[2], nameRectCoordinates[3]);

		
		g2d.drawRect(nameRectCoordinates[0], nameRectCoordinates[1], nameRectCoordinates[2], nameRectCoordinates[3]);
		g2d.drawRect(packageRectCoordinates[0], packageRectCoordinates[1], packageRectCoordinates[2], packageRectCoordinates[3]);
	}
	
	private boolean firstClassAdded = true;
	// This map will be used for generalization relations
	private HashMap<ClassDiagram, PackageDiagram> classParents = new HashMap<>();
	public void addToPackageDiagram(JComponent c) {
		/*
		 * This JPanel is mandatory, and without it the empty border added to these classes will never be taken into account : 
		 * The FlowLayout calculates the position based solely on the added component's preferred size, ignoring the border.
		 * If we first add that component to the panel first, then the panel will take into account the added border, and so the flowLayout will now take into account the jpanel's preferred size which already takes into account the added border of the component.
		 */
	    JPanel panel = new JPanel();
	    if (firstClassAdded) {
	    	int leftMargin = 500 * counter;
	    	this.leftMargin = leftMargin;
	    	panel.setBorder(new EmptyBorder(0, leftMargin, 0, 0)); // First class of each package will have a variable left margin; formula : left margin = width(class) * package's number (starting from 0)
	    	firstClassAdded = false;
	    } else {
	        panel.setBorder(new EmptyBorder(0, 200 * numberOfPackageDiagrams, 0, 0)); // Rest of classes will have a constant space between them
	    }
	    panel.add(c);
	    this.add(panel);
	    
	    this.revalidate();
	    this.repaint();
	}

	
//	These getters will be used in drawing merge relations
	public int getPackageDiagramWidth() {
		return packageDiagramWidth;
	}


	public int getPackageDiagramHeight() {
		return packageDiagramHeight;
	}

	public int getLeftMargin() {
		return leftMargin;
	}

	public int getMaxNumberOfClassDiagrams() {
		return maxNumberOfClassDiagrams;
	}
	
	public int getNumberOfPackageDiagrams() {
		return numberOfPackageDiagrams;
	}
		
}
