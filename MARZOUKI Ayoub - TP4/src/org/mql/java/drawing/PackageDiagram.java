package org.mql.java.drawing;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class PackageDiagram extends JPanel {
	private String packageName;
	private int[] nameRectCoordinates = new int[] {0, 10, 400, 50};
	private int[] packageRectCoordinates = new int[] {0, 60, 3000, 720};
	private Graphics2D g2d;
	private GridBagConstraints gbc;
	
	public PackageDiagram(String packageName) {
		this.packageName = packageName;
		
//		height = sum(height) of all elements + some space (we need to take into account the stroke's width), same goes for width
		this.setPreferredSize(new Dimension(nameRectCoordinates[2] + packageRectCoordinates[2] + 50, nameRectCoordinates[3] + packageRectCoordinates[3] + 50));
		
//		this.setLayout(new GridBagLayout());
		
/*		The hgap and vgap in what follows are ultra necessary :
 * 		The problem encountered if we do not add these, is that the classes added to the package will keep overlapping with their container, and the lines drawing the package will always be cut and interrupted. Manually pushing the classes to the inside of the package doesn't work either; it overrides the entire space; from the initial position to the position the classdiagram was pushed to.
 * Thus we can either go for JLayeredPane Layout, or we can use hgap and vgap.
 *		
 */
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 100, 200));
		
		gbc = new GridBagConstraints();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(3));
		
		Frame.drawCenteredText(g2d, packageName, nameRectCoordinates[0], nameRectCoordinates[1], nameRectCoordinates[2], nameRectCoordinates[3]);

		
		g2d.drawRect(10, 10, nameRectCoordinates[2], nameRectCoordinates[3]);
		g2d.drawRect(10, 60, packageRectCoordinates[2], packageRectCoordinates[3]);
	}

//	Will be later needed to start putting classes inside the package
	public GridBagConstraints getGbc() {
		return gbc;
	}
	
	public void addToPackageDiagram(Component c) {
		this.add(c);
		
		this.repaint();
		this.revalidate();
	}
	
	
	
//	Maybe add a method to check if size of name's rectangle has enough space for the name
}
