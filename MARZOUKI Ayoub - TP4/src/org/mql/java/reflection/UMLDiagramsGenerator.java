package org.mql.java.reflection;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.mql.java.drawing.ClassDiagram;
import org.mql.java.drawing.Frame;
import org.mql.java.drawing.PackageDiagram;

public class UMLDiagramsGenerator {
	private static List<String> fullyQualifiedClassNames = new ArrayList<>();
    private static List<String> packageHierarchy = new ArrayList<>();
    private static List<String> packagesNames = new ArrayList<>();
	
	public UMLDiagramsGenerator() {}
	
	public static void generateDiagrams() throws ClassNotFoundException {
		Frame frame = new Frame();
		
		for (int i = 0; i< packagesNames.size(); i++) {
			PackageDiagram p = new PackageDiagram(packagesNames.get(i));
			
			for (int j = 0; j < fullyQualifiedClassNames.size(); j++) {
				ClassDiagram c = new ClassDiagram(fullyQualifiedClassNames.get(j));
				
				Class<?> cls = Class.forName(fullyQualifiedClassNames.get(j));
				if (cls.getPackageName().equals(packagesNames.get(i))) p.addToPackageDiagram(c);
			}
			
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
