package org.mql.java.reflection;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.mql.java.drawing.ClassDiagram;
import org.mql.java.drawing.Frame;
import org.mql.java.drawing.PackageDiagram;
import org.mql.java.drawing.relations.pack.Merge;

public final class UMLDiagramsGenerator {
	private static List<String> fullyQualifiedClassNames = new ArrayList<>();
    private static List<String> packageHierarchy = new ArrayList<>();
    private static List<String> packagesNames = new ArrayList<>();
    
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
	
	public static void generateDiagrams() throws ClassNotFoundException {
		Frame frame = new Frame();
		
		for (int i = 0; i< packagesNames.size(); i++) {
			boolean containsClasses = false;
			PackageDiagram p = new PackageDiagram(packagesNames.get(i));

			for (int j = 0; j < fullyQualifiedClassNames.size(); j++) {
				ClassDiagram c = new ClassDiagram(fullyQualifiedClassNames.get(j));
				
				Class<?> cls = Class.forName(fullyQualifiedClassNames.get(j));
				if (cls.getPackageName().equals(packageHierarchy.get(i))) {
					containsClasses = true;
					p.addToPackageDiagram(c);
				}
			}
			
			if (containsClasses) PackageDiagram.counter++;
			frame.addToContentPanel(p);
		}
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
