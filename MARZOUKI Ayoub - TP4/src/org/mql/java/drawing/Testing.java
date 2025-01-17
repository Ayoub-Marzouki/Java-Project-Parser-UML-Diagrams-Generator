package org.mql.java.drawing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.mql.java.drawing.relations.cls.Aggregation;
import org.mql.java.reflection.ProjectProcessor;
import org.mql.java.reflection.UMLDiagramsGenerator;

public class Testing {
	public static void main(String[] args) {
		Frame frame = new Frame();
		
		String path = "C:\\Users\\ayoub\\git\\repository\\MARZOUKI Ayoub - TP4\\src";
		ProjectProcessor.process(path); 
		
		PackageDiagram panel1 = new PackageDiagram("package 1 long name n stuff");
				
		ClassDiagram class1 = new ClassDiagram("class 1");
		ClassDiagram class2 = new ClassDiagram("class 2");
		ClassDiagram class3 = new ClassDiagram("class 3");
		ClassDiagram class4 = new ClassDiagram("class 4");
		ClassDiagram class5 = new ClassDiagram("class 5");
		ClassDiagram class6 = new ClassDiagram("class 6");
		ClassDiagram class7 = new ClassDiagram("class 7");
		ClassDiagram class8 = new ClassDiagram("class 8");
		
		panel1.addToPackageDiagram(class1);
		panel1.addToPackageDiagram(class2);
		panel1.addToPackageDiagram(class3);
		panel1.addToPackageDiagram(class4);
		
		PackageDiagram panel2 = new PackageDiagram("package 2");
		
		panel2.addToPackageDiagram(class5);
		panel2.addToPackageDiagram(class6);
		panel2.addToPackageDiagram(class7);
		panel2.addToPackageDiagram(class8);
		
		PackageDiagram panel3 = new PackageDiagram("package 3");
		
		ClassDiagram class9 = new ClassDiagram("class 9");
		ClassDiagram class10 = new ClassDiagram("class 10");
		ClassDiagram class11 = new ClassDiagram("class 11");
		ClassDiagram class12 = new ClassDiagram("class 12");
		
		panel3.addToPackageDiagram(class9);
		panel3.addToPackageDiagram(class10);
		panel3.addToPackageDiagram(class11);
		panel3.addToPackageDiagram(class12);
		

//		frame.addToContentPanel(class1);
		frame.addToContentPanel(panel1);
		frame.addToContentPanel(panel2);
		frame.addToContentPanel(panel3);
		
		
//		frame.setVisible(false);
//		
//		JFrame jframe = new JFrame("Testing JPanel inside a JPanel");
//		jframe.setLayout(new FlowLayout());
//		
//		JPanel jpanel1 = new JPanel();
//		jpanel1.setBorder(BorderFactory.createLineBorder(Color.RED));
//		jpanel1.setPreferredSize(new Dimension(500, 500));
//		jpanel1.setPreferredSize(new Dimension(200, 200));
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
//		jframe.setSize(800, 800);
//		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		
//		jframe.add(jpanel1);
//		jframe.setVisible(true);
		
		
		
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
