package org.mql.java.reflection;

import java.awt.Container;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.mql.java.drawing.ClassDiagram;
import org.mql.java.drawing.Frame;
import org.mql.java.drawing.PackageDiagram;
import org.mql.java.drawing.RelationsLayer;
import org.mql.java.drawing.relations.pack.Merge;

public final class UMLDiagramsGenerator {
	private static List<String> fullyQualifiedClassNames = new ArrayList<>();
    private static List<String> packageHierarchy = new ArrayList<>();
    private static List<String> packagesNames = new ArrayList<>();
    
//  Will be later used to draw Relations in RelationsLayer class :
    // Merge Relations
    public static HashMap<String, PackageDiagram> packageNameToDiagramMap = new HashMap<>();
    // Generalization relations
    public static HashMap<String, ClassDiagram> parentChildClasses = new HashMap<>();
    // Aggregration relations
    public static HashMap<String, ClassDiagram> classAggregationMappings = new HashMap<>();
    // Composition relations
    public static HashMap<String, ClassDiagram> classCompositionMappings = new HashMap<>();
    
    public static HashMap<String, ClassDiagram> classRealizationMappings = new HashMap<>();
    
    public static HashMap<String, ClassDiagram> classDependencyMappings = new HashMap<>();


	
    public UMLDiagramsGenerator() {}
	
	
	/**
	 * <h1>Checks which package has the highest amount of classes amidst all others, and returns their number</h1>
	 * 
	 */
	public static int calculateNumberOfClassDiagrams() {
		int maxNumberOfClasses = 0 ;
		for (int i = 0; i< packagesNames.size(); i++) {
			int numberOfClasses = 0;
			
			for (int j = 0; j < fullyQualifiedClassNames.size(); j++) {				
				Class<?> cls;
				try {
					cls = Class.forName(fullyQualifiedClassNames.get(j));
					if (cls.getPackageName().equals(packageHierarchy.get(i))) {
						numberOfClasses++;
						
						if (numberOfClasses > maxNumberOfClasses) maxNumberOfClasses = numberOfClasses;
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return maxNumberOfClasses;
	}
	
	public static void generateDiagrams(Frame frame) throws ClassNotFoundException {
//		Frame frame = new Frame();
		
		for (int i = 0; i< packagesNames.size(); i++) {
			boolean containsClasses = false;
			PackageDiagram p = new PackageDiagram(packagesNames.get(i));
			packageNameToDiagramMap.put(packageHierarchy.get(i), p);

			for (int j = 0; j < fullyQualifiedClassNames.size(); j++) {
				ClassDiagram c = new ClassDiagram(fullyQualifiedClassNames.get(j));

				
				Class<?> cls = Class.forName(fullyQualifiedClassNames.get(j));
				
				UMLClassMetadataExtractor extractor = new UMLClassMetadataExtractor(cls);
				c.setFieldsUML(extractor.getFieldsUML()); 
				c.setMethodsUML(extractor.getMethodsUML());
				
				if (cls.getPackageName().equals(packageHierarchy.get(i))) {
					
					if (ProjectProcessor.generalizationRelations.containsKey(fullyQualifiedClassNames.get(j)) || 
					        ProjectProcessor.generalizationRelations.containsValue(fullyQualifiedClassNames.get(j))) {
					        parentChildClasses.put(fullyQualifiedClassNames.get(j), c);
					}
					
					if (ProjectProcessor.aggregationRelations.containsKey(fullyQualifiedClassNames.get(j)) || 
	                        ProjectProcessor.aggregationRelations.containsValue(fullyQualifiedClassNames.get(j))) {
	                    classAggregationMappings.put(fullyQualifiedClassNames.get(j), c);
	                }
					
					if (ProjectProcessor.compositionRelations.containsKey(fullyQualifiedClassNames.get(j)) || 
	                        ProjectProcessor.compositionRelations.containsValue(fullyQualifiedClassNames.get(j))) {
	                    classCompositionMappings.put(fullyQualifiedClassNames.get(j), c);
	                }
					
					if (ProjectProcessor.realizationRelations.containsKey(fullyQualifiedClassNames.get(j)) || 
	                        ProjectProcessor.realizationRelations.containsValue(fullyQualifiedClassNames.get(j))) {
						classRealizationMappings.put(fullyQualifiedClassNames.get(j), c);
	                }
					
					if (ProjectProcessor.dependencyRelations.containsKey(fullyQualifiedClassNames.get(j)) || 
	                        ProjectProcessor.dependencyRelations.containsValue(fullyQualifiedClassNames.get(j))) {
						classDependencyMappings.put(fullyQualifiedClassNames.get(j), c);
	                }
					
					
					
					containsClasses = true;
					p.addToPackageDiagram(c);
				}
			}
			
			if (containsClasses) PackageDiagram.counter++;
			frame.addToDiagramsLayer(p);
		}
//		The reason we're creating relations layer only until now and not sooner (or as a field in Frame class) is because the data needed to create it will not be available until all diagrams are drawn
		RelationsLayer rl = new RelationsLayer();
		frame.addRelationsLayer(rl);
	}
	
	

	public static List<String> getFullyQualifiedClassNames() {
		return fullyQualifiedClassNames;
	}

	public static void setFullyQualifiedClassNames(List<String> fullyQualifiedClassNames) {
		UMLDiagramsGenerator.fullyQualifiedClassNames = fullyQualifiedClassNames;
	}

	public static List<String> getPackageHierarchy() {
		return packageHierarchy;
	}

	public static void setPackageHierarchy(List<String> packageHierarchy) {
		UMLDiagramsGenerator.packageHierarchy = packageHierarchy;
	}

	public static List<String> getPackagesNames() {
		return packagesNames;
	}

	public static void setPackagesNames(List<String> packagesNames) {
		UMLDiagramsGenerator.packagesNames = packagesNames;
	}
	
	
	
}
