package org.mql.java.application;


import org.mql.java.swing.business.ProjectProcessor;
import org.mql.java.swing.business.UMLDiagramsGenerator;
import org.mql.java.swing.ui.Frame;

public class SwingMain {
	public static void main(String[] args) throws ClassNotFoundException {
        Frame frame = new Frame();
        
//       To test another folder, go to "findSrcBasePath" method in Project Processor, then change 'src' to folder name, and change its length in "fullPath.substring(0, srcIndex + 4)"
        
        frame.getButton().addActionListener(e -> {
        	String path = frame.getUserInput().getText();
        	ProjectProcessor.process(path); 
            try {
				UMLDiagramsGenerator.generateDiagrams(frame);
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
    });
        
        
//        -------------------------------------------------------------------------------------
//        You can always test specific methods like this : 
//        
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
