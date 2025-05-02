/**
 * This class is going to be the equivalent of :
 * 
 * ArrayList<ArrayList<HashMap<String, Vector<String>>>> structure = new ArrayList<>()
 * 
 * It will store the overall structure of the project; each folder, its layer and what it contains.
 * In other words :
 * This structure will represent the whole file system of the project starting from src folder.
 */

package org.mql.java.swing.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Structure {

    // Main structure representing the whole file system, designed to hold multiple layers
    private ArrayList<ArrayList<HashMap<String, Vector<String>>>> structure = new ArrayList<>();

    // Layer: holds the current folder's name and files inside it
    private ArrayList<HashMap<String, Vector<String>>> layer = new ArrayList<>();


    private String currentFolderName;
    
//    Hierarchy : will represent the parent-child relation between folders
     ArrayList<ArrayList<String>> hierarchy = new ArrayList<>();
	 ArrayList<String> hierarchyElement = new ArrayList<>();


    private Vector<String> filesNames = new Vector<>();

    public Structure() {}

    // Add a folder with its files to the layer
    public void addFolderWithFilesToLayer(String folderName, Vector<String> filesNames, int layerIndex) {
//    	Given layer's initial size is 0, we set it to 50 by adding null elements
//    	for (int i = 1; i<=50; i++) layer.add(null);
    	
        HashMap<String, Vector<String>> folderFiles = new HashMap<>();
        folderFiles.put(folderName, filesNames);

        // Add the folder and files map to the layer
        layer.add(layerIndex, folderFiles);
        
//       After we're done adding elements, we now remove the null elements
//        for (int i = layer.size() - 1; i>=0; i--) {
//        	if (layer.get(i)== null) {
//        		layer.remove(i);
//        	}
//        }
    }

    // Method to add the current layer to the structure
    public void addLayer() {
//      Adding a whole newArrayList of layer instead of just layer provides the flexibility of indexing 
    	structure.add(new ArrayList<>(layer));
    }


    // Method to retrieve the structure
    public ArrayList<ArrayList<HashMap<String, Vector<String>>>> getStructure() {
        return structure;
    }

    // Getters and setters for folder name and file names
    public String getCurrentFolderName() {
        return currentFolderName;
    }

    public void setCurrentFolderName(String currentFolderName) {
        this.currentFolderName = currentFolderName;
    }

    public Vector<String> getFilesNames() {
        return filesNames;
    }

    public void setFilesNames(Vector<String> filesNames) {
        this.filesNames = filesNames;
    }
    
    public void printStructure() {
        // For each layer in the structure
        for (int i = 0; i < structure.size(); i++) {
            System.out.println("Layer " + i + ":");

            // Get the current layer (which is an ArrayList of HashMaps)
            ArrayList<HashMap<String, Vector<String>>> currentLayer = structure.get(i);

            // Iterate over each HashMap in the current layer
            for (int j = 0; j < currentLayer.size(); j++) {
                HashMap<String, Vector<String>> pair = currentLayer.get(j);
                
                // Entries of our HashMap
                for (Map.Entry<String, Vector<String>> entry : pair.entrySet()) {
                    System.out.println("Folder: " + entry.getKey());
                    System.out.println("Files: " + entry.getValue());
                }
            }
        }
    }

}
