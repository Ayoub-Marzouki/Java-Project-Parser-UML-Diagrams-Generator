package org.mql.java.drawing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ClassDiagram extends JPanel {
	private String className;
	private Graphics2D g2d;
//	x and y coordinates of the name Rectangle should take into account the package's name rectangle's coordinates
	private static final int[] nameRectCoordinates = new int[] {0, 0, 200, 50};
//	classContentRectangle's y = classNameRectangle's height, same goes for classMethodsRectangle
	private static final int[] classRectCoordinates = new int[] {0, nameRectCoordinates[3], nameRectCoordinates[2], 300};
	private static final int[] methodsRectCoordinates = new int[] {0, nameRectCoordinates[3] + classRectCoordinates[3], nameRectCoordinates[2], 100};
	private static final int classDiagramHeight = nameRectCoordinates[3] + classRectCoordinates[3] + methodsRectCoordinates[3];
	private static final int classDiagramWidth = nameRectCoordinates[2];
	
	public ClassDiagram(String className) {
		this.className = className;
		this.setPreferredSize(new Dimension(classDiagramWidth, classDiagramHeight));
		this.setBackground(Color.RED);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(3));
		
		g2d.drawRect(nameRectCoordinates[0], nameRectCoordinates[1], nameRectCoordinates[2], nameRectCoordinates[3]);
		Frame.drawCenteredText(g2d, className, nameRectCoordinates[0], nameRectCoordinates[1], nameRectCoordinates[2], nameRectCoordinates[3]);
		g2d.drawRect(nameRectCoordinates[0], nameRectCoordinates[1] + nameRectCoordinates[3], nameRectCoordinates[2], classRectCoordinates[3]);
		g2d.drawRect(nameRectCoordinates[0], classRectCoordinates[1] + classRectCoordinates[3], nameRectCoordinates[2], methodsRectCoordinates[3]);
	}
//	Maybe whenever you find a relationship between 2 classes, add that class in the next line

	
//	These 2 getters will be used to calculate the width / height of the package diagram
	public static int getClassDiagramHeight() {
		return classDiagramHeight;
	}

	public static int getClassDiagramWidth() {
		return classDiagramWidth;
	}
	
	
}
