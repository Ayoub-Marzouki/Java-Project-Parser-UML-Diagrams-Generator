package org.mql.java.drawing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.mql.java.reflection.ProjectProcessor;
import org.mql.java.reflection.UMLDiagramsGenerator;

public class Testing {
	public static void main(String[] args) {
		Frame frame = new Frame();
		
		PackageDiagram panel1 = new PackageDiagram("package 1 long name n stuff");
		PackageDiagram panel2 = new PackageDiagram("package 2");
				
		ClassDiagram class1 = new ClassDiagram("class 1");
		ClassDiagram class2 = new ClassDiagram("class 2");
		panel1.addToPackageDiagram(class1);
		panel1.addToPackageDiagram(class2);

//		frame.addToContentPanel(class1);
		frame.addToContentPanel(panel1);
		frame.addToContentPanel(panel2);
		
		
//		frame.setVisible(false);
//		
//		JFrame jframe = new JFrame("Testing JPanel inside a JPanel");
//		jframe.setLayout(new FlowLayout());
//		
//		JPanel jpanel1 = new JPanel();
//		jpanel1.setBorder(BorderFactory.createLineBorder(Color.RED));
//		
//		JLabel label1 = new JLabel("This one is inside outer JPanel");
//		jpanel1.add(label1);
//		
//		
//		JPanel jpanel2 = new JPanel();
//		jpanel2.setBorder(BorderFactory.createLineBorder(Color.BLUE));
//		
//		JLabel label2 = new JLabel("This one is inside inner JPanel");
//		jpanel2.add(label2);
//		
//		jpanel1.add(jpanel2);
//		
//		jframe.setVisible(true);
//		jframe.setSize(500, 500);
//		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		
//		jframe.add(jpanel1);
		
		
		
//		ProjectProcessor.process("C:\\Users\\ayoub\\git\\repository\\MARZOUKI Ayoub - TP4\\src");
//		
//		for (int i = 0; i< UMLDiagramsGenerator.getPackagesNames().size(); i++) {
//			PackageDiagram p = new PackageDiagram(UMLDiagramsGenerator.getPackagesNames().get(i));
//			
//			for (int j = 0; j < UMLDiagramsGenerator.getFullyQualifiedClassNames().size(); j++) {
//				ClassDiagram c = new ClassDiagram(UMLDiagramsGenerator.getFullyQualifiedClassNames().get(j));
//				p.add(c);
//			}
//			
//			frame.addToContentPanel(p);
//		}
		
	}
}
