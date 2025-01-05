package org.mql.java.reflection;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Vector;

public class UMLDiagramsGenerator {
//	These will store all files and folders so that we can apply reflection on them later
	private static Structure structure = new Structure();
	
	private static Vector<String> foldersNames = new Vector<String>();
	
	
	public static void generate(Path srcFolderPath) {
		File srcFolder = srcFolderPath.toFile();
		File[] srcFolderFiles = srcFolder.listFiles();
		retrieveAllFilesNames(srcFolderFiles);		
	}
	
	private static int layerIndex = 0;
	private static StringBuilder fullQualifiedName;
	private static String currentFolderName;
	private static Vector<String> filesNames = new Vector<String>();
	
	static Vector<String> currentFilesNames = new Vector<String>();
	
//		This will store every file, every folder and every file in every folder
	public static void retrieveAllFilesNames(File[] srcFolderFiles) {
		for (File f : srcFolderFiles) {
			if (f.getName().endsWith(".java")) {
				System.out.println(f.getName() +"\n");
				filesNames.add(f.getName());
				currentFilesNames.add(f.getName());
			} 
			else if (f.isDirectory() && !f.getName().startsWith(".")) {
				foldersNames.add(f.getName());
				
				currentFolderName = f.getName();
				structure.addLayer();
				
				System.out.println("Layer " + layerIndex);
				System.out.println(f.getName() + "\n");
				
				structure.addFolderWithFilesToLayer(currentFolderName, new Vector<>(currentFilesNames), layerIndex);				

//				Clear current files to prepare for the next folder
				currentFilesNames.clear();
				
				if (f.listFiles() != null) {
					layerIndex++; // Go 1 level deeper into folder hiearchy
					retrieveAllFilesNames(f.listFiles());
					layerIndex--; // Go back when done processing that sub folder
				}
				structure.hierarchy.add(new ArrayList<>(structure.hierarchyElement));
	            structure.hierarchyElement.remove(currentFolderName);
			}  
		}
	}
	
	public static void structure() {
		// Iterate through each layer in the hierarchy
		for (ArrayList<String> layer : structure.hierarchy) {
		    System.out.println("Layer:");
		    
		    // Iterate through each folder name in the current layer
		    for (String folderName : layer) {
		        System.out.println(" - " + folderName); // Print folder name with indentation
		    }
		    
		    System.out.println(); // Add an empty line for better readability between layers
		}

		structure.printStructure();
	}
	
}
