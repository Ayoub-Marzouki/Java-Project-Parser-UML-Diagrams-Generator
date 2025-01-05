package org.mql.java.drawing;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class PackageDiagram extends JPanel {
	public PackageDiagram() {
		this.setPreferredSize(new Dimension(700, 700));
	}
	
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(3));
		
		g2d.drawRect(10, 10, 150, 50);
		g2d.drawRect(10, 60, 500, 500);
	}
}
