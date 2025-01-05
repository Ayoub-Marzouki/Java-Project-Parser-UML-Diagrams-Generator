package org.mql.java.drawing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Testing {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		frame.setLayout(flowLayout);
		frame.setSize(1000, 1000);
		frame.setTitle("UML Package and Class Diagrams Generator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		PackageDiagram panel1 = new PackageDiagram();
		
		frame.add(panel1);
		frame.repaint();
		frame.revalidate();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
