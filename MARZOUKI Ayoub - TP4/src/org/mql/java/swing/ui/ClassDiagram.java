package org.mql.java.swing.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.mql.java.util.SwingUtilities;

public class ClassDiagram extends JPanel {
	private String className;
	
	private String fieldsUML;
	private String methodsUML;
	
	public static Graphics2D g2d;
//	x and y coordinates of the name Rectangle should take into account the package's name rectangle's coordinates
	private static int[] nameRectCoordinates = new int[] {0, 0, 500, 50};
//	classContentRectangle's y = classNameRectangle's height, same goes for classMethodsRectangle, also class rect means fields rectangle
	private static int[] classRectCoordinates = new int[] {0, nameRectCoordinates[3], nameRectCoordinates[2], 200};
	private static int[] methodsRectCoordinates = new int[] {0, nameRectCoordinates[3] + classRectCoordinates[3], nameRectCoordinates[2], 400};
	private static int classDiagramHeight = nameRectCoordinates[3] + classRectCoordinates[3] + methodsRectCoordinates[3];
	private static int classDiagramWidth = nameRectCoordinates[2];
	
	
	public ClassDiagram(String className) {
		this.className = className;
		this.setPreferredSize(new Dimension(classDiagramWidth, classDiagramHeight));
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(3));
		
		g2d.drawRect(nameRectCoordinates[0], nameRectCoordinates[1], nameRectCoordinates[2], nameRectCoordinates[3]);
		g2d.drawRect(nameRectCoordinates[0], nameRectCoordinates[1] + nameRectCoordinates[3], nameRectCoordinates[2], classRectCoordinates[3]);
		g2d.drawRect(nameRectCoordinates[0], classRectCoordinates[1] + classRectCoordinates[3], nameRectCoordinates[2], methodsRectCoordinates[3]);
		
		SwingUtilities.drawCenteredText(g2d, className, nameRectCoordinates[0], nameRectCoordinates[1], nameRectCoordinates[2], nameRectCoordinates[3]);
		SwingUtilities.drawCenteredText(g2d, fieldsUML, classRectCoordinates[0], classRectCoordinates[1], classRectCoordinates[2], classRectCoordinates[3]);
		SwingUtilities.drawCenteredText(g2d, methodsUML, methodsRectCoordinates[0], methodsRectCoordinates[1], methodsRectCoordinates[2], methodsRectCoordinates[3]);
	}
//	Maybe whenever you find a relationship between 2 classes, add that class in the next line

	
//	These 2 getters will be used to calculate the width / height of the package diagram
	public static int getClassDiagramHeight() {
		return classDiagramHeight;
	}

	public static int getClassDiagramWidth() {
		return classDiagramWidth;
	}

	public String getFieldsUML() {
		return fieldsUML;
	}

	public void setFieldsUML(String fieldsUML) {
		this.fieldsUML = fieldsUML;
	}

	public String getMethodsUML() {
		return methodsUML;
	}

	public void setMethodsUML(String methodsUML) {
		this.methodsUML = methodsUML;
	}

	public static int[] getClassrectcoordinates() {
		return classRectCoordinates;
	}

	public static int[] getMethodsrectcoordinates() {
		return methodsRectCoordinates;
	}

	public static int[] getNameRectCoordinates() {
		return nameRectCoordinates;
	}

	public static void setNameRectCoordinates(int[] nameRectCoordinates) {
		ClassDiagram.nameRectCoordinates = nameRectCoordinates;
	}

	public static int[] getMethodsRectCoordinates() {
		return methodsRectCoordinates;
	}

	public static void setMethodsRectCoordinates(int[] methodsRectCoordinates) {
		ClassDiagram.methodsRectCoordinates = methodsRectCoordinates;
	}
	
	
	
}
