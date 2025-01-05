package org.mql.java.drawing;

import javax.swing.JFrame;

public class Frame extends JFrame {
	public Frame() {
		this.setSize(1000, 1000);
		this.setTitle("UML Package and Class Diagrams Generator");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.add(new PackageDiagram());
		this.repaint();
		this.revalidate();
		this.setVisible(true);
	}
}
