package org.mql.java.main;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.mql.java.drawing.ClassDiagram;
import org.mql.java.drawing.Frame;
import org.mql.java.drawing.PackageDiagram;
import org.mql.java.drawing.RelationsLayer;
import org.mql.java.drawing.relations.pack.Merge;
import org.mql.java.reflection.ProjectProcessor;
import org.mql.java.reflection.UMLDiagramsGenerator;

public class SwingMain {
	public static void main(String[] args) throws ClassNotFoundException {
        Frame frame = new Frame();
        
        frame.getButton().addActionListener(e -> {
        	String path = frame.getUserInput().getText();
        	ProjectProcessor.process(path); 
            try {
				UMLDiagramsGenerator.generateDiagrams(frame);
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    });
        
        
        
//        Class<?> cls = UMLClassMetadataExtractor.class;
//        UMLClassMetadataExtractor c = new UMLClassMetadataExtractor(cls);
//        c.printFieldsUML();
//        c.printMethodsUML();
        
//        Component[] components = Frame.getDiagramsLayer().getComponents();
//        for (Component c : components) {
//        	if (c instanceof PackageDiagram) {
//	        	System.out.println(c);
//	        	Component[] comp = ((Container) c).getComponents();
//	        	for (Component component : comp) {
//	        		System.out.println(component);
//	        	}
//        	}
//        }
        
//        System.out.println("Generalization Relations:");
//        for (Map.Entry<String, String> entry : ProjectProcessor.generalizationRelations.entrySet()) {
//            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
//        }
//
//        // Print parentChildClasses HashMap
//        System.out.println("\nParent-Child Classes:");
//        for (Map.Entry<String, ClassDiagram> entry : UMLDiagramsGenerator.parentChildClasses.entrySet()) {
//            System.out.println("Key: " + entry.getKey() + ", ClassDiagram: " + entry.getValue());
//        }
        
//        System.out.println("Aggregation Relations:");
//        for (Map.Entry<String, String> entry : ProjectProcessor.aggregationRelations.entrySet()) {
//            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
//        }
//
//        System.out.println("\nClass Aggregation Mappings:");
//        for (Map.Entry<String, ClassDiagram> entry : UMLDiagramsGenerator.classAggregationMappings.entrySet()) {
//            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
//        }
        
        
    }
}
