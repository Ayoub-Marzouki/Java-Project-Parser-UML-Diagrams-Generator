package org.mql.java.reflection;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProjectProcessor {
    private static List<String> fullyQualifiedClassNames = new ArrayList<>();
    private static List<String> packageHierarchy = new ArrayList<>();
    private static List<String> packagesNames = new ArrayList<>();
    private static HashMap<String, String> mergeRelations = new HashMap<>();

    public static void process(String srcFolderPath) {
    	Path pathToSrcFolder = Path.of(srcFolderPath);
        File srcFolder = pathToSrcFolder.toFile();
        
        if (!isValidSrcFolder(srcFolder)) {
            System.out.println("Invalid src folder path provided.");
            return;
        }

        String srcBasePath = findSrcBasePath(srcFolder.getAbsolutePath());
        if (srcBasePath == null) {
            System.out.println("The provided path does not contain a 'src' folder.");
            return;
        }

        File[] srcFolderFiles = srcFolder.listFiles();
        if (srcFolderFiles != null) {
            retrieveAllFilesAndGenerateNames(srcFolderFiles, srcBasePath);
        }
        
        prepareToDraw();
//        printResults();
    }

    private static boolean isValidSrcFolder(File srcFolder) {
        return srcFolder.exists() && srcFolder.isDirectory();
    }

    private static String findSrcBasePath(String fullPath) {
        int srcIndex = fullPath.indexOf(File.separator + "src");
        if (srcIndex == -1) {
            return null;
        }
        return fullPath.substring(0, srcIndex + 4); // Include 'src' in the base path
    }

    private static void retrieveAllFilesAndGenerateNames(File[] srcFolderFiles, String srcBasePath) {
        for (File f : srcFolderFiles) {
            String relativePath = f.getAbsolutePath().replace(srcBasePath, "").replace(File.separator, ".");

            if (relativePath.startsWith(".")) {
                relativePath = relativePath.substring(1);
            }

            if (f.isFile() && f.getName().endsWith(".java")) {
            	// Only replace '.java' at the end of the relative path, if it's a folder or a package, it won't be removed
                String qualifiedClassName = relativePath.substring(0, relativePath.length() - 5); // Remove the last 5 characters (".java")
                fullyQualifiedClassNames.add(qualifiedClassName);
            } else if (f.isDirectory() && !f.getName().startsWith(".")) {
                packageHierarchy.add(relativePath);
                packagesNames.add(f.getName());
                retrieveAllFilesAndGenerateNames(f.listFiles(), srcBasePath);
            }
        }
    }
    
    private static void prepareToDraw() {
    	UMLDiagramsGenerator.setFullyQualifiedClassNames(ProjectProcessor.getFullyQualifiedClassNames());
        UMLDiagramsGenerator.setPackageHierarchy(ProjectProcessor.getPackageHierarchy());
        UMLDiagramsGenerator.setPackagesNames(ProjectProcessor.getPackagesNames());
    }

    public static void printResults() {
        System.out.println("\nFully Qualified Class Names:");
        for (String className : fullyQualifiedClassNames) {
            System.out.println(className);
        }

        System.out.println("\nPackage Hierarchy:");
        for (String packageName : packageHierarchy) {
            System.out.println(packageName);
        }
        
        System.out.println("\nPackages names : ");
        for (String packageName : packagesNames) {
        	System.out.println(packageName);
        }
    }
    
    
    public static void processRelations() {
    	processMergeRelations();
    }
    
    
    public static void processMergeRelations() {
    	for (String child : packageHierarchy) {
    	    String immediateParent = null; // will hold the closest ancestor of the current child (org and org.mql are both ancestors of org.mql.java, but org.mql is closer)
    	    for (String potentialParent : packageHierarchy) {
    	        if (child.startsWith(potentialParent) && !child.equals(potentialParent)) {
    	            // Check if it's a direct parent (longest matching prefix) : org, org.mql and org.mql.java are all parents of org.mql.java.drawing, but only org.mql.java is the immediate direct parent
    	            if (immediateParent == null || potentialParent.length() > immediateParent.length()) {
    	                immediateParent = potentialParent;
    	            }
    	        }
    	    }
    	    if (immediateParent != null) {
    	        mergeRelations.put(child, immediateParent);
    	    }
    	}

    }

	public static List<String> getFullyQualifiedClassNames() {
		return fullyQualifiedClassNames;
	}

	public static void setFullyQualifiedClassNames(List<String> fullyQualifiedClassNames) {
		ProjectProcessor.fullyQualifiedClassNames = fullyQualifiedClassNames;
	}

	public static List<String> getPackageHierarchy() {
		return packageHierarchy;
	}

	public static void setPackageHierarchy(List<String> packageHierarchy) {
		ProjectProcessor.packageHierarchy = packageHierarchy;
	}

	public static List<String> getPackagesNames() {
		return packagesNames;
	}

	public static void setPackagesNames(List<String> packagesNames) {
		ProjectProcessor.packagesNames = packagesNames;
	}
    
	
    
}
