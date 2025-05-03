package org.mql.java.swing.business;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mql.java.swing.ui.ClassDiagram;
import org.mql.java.swing.ui.Frame;
import org.mql.java.swing.ui.PackageDiagram;

public class ProjectProcessor {
    private static List<String> fullyQualifiedClassNames = new ArrayList<>();
    private static List<String> packageHierarchy = new ArrayList<>();
    private static List<String> packagesNames = new ArrayList<>();
    
//  Will be later used to drawMergeRelations in RelationsLayer class
    public static HashMap<String, String> mergeRelations = new HashMap<>();
    
    private static HashMap<String, String> importRelations = new HashMap<>();
    
    public static HashMap<String, String> generalizationRelations = new HashMap<>();
    
    public static HashMap<String, String> aggregationRelations = new HashMap<>();
    
    public static HashMap<String, String> compositionRelations = new HashMap<>();
    
    public static HashMap<String, String> realizationRelations = new HashMap<>();
    
    public static HashMap<String, String> dependencyRelations = new HashMap<>();

    public static void process(String srcFolderPath) {
    	Path pathToSrcFolder = Path.of(srcFolderPath);
        File srcFolder = pathToSrcFolder.toFile();
        
        if (!isValidSrcFolder(srcFolder)) {
        	Frame.setStatus("Invalid src folder path provided.");
    	    Frame.getPopup().setVisible(true);
            return;
        }

        String srcBasePath = findSrcBasePath(srcFolder.getAbsolutePath());
        if (srcBasePath == null) {
        	Frame.setStatus("The provided path does not contain a 'src' folder.");
    	    Frame.getPopup().setVisible(true);
            return;
        }

        File[] srcFolderFiles = srcFolder.listFiles();
        if (srcFolderFiles != null) {
            retrieveAllFilesAndGenerateNames(srcFolderFiles, srcBasePath);
        }
        
        prepareToDraw();
//        printResults();
        processRelations();
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
    	
    	processGeneralizationRelations();
    	processAggregationRelations();
    	processCompositionRelations();
    	processRealizationRelations();
    	processDependencyRelations();
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
    
    public static void processGeneralizationRelations() {
        for (String className : fullyQualifiedClassNames) {
            try {
                Class<?> clazz = Class.forName(className);
                // Get the superclass
                Class<?> superclass = clazz.getSuperclass();

                // Check if the superclass is not Object and is part of the classes we're analyzing
                if (superclass != null && !superclass.equals(Object.class) && fullyQualifiedClassNames.contains(superclass.getName())) {
                    generalizationRelations.put(className, superclass.getName());
                }
            } catch (ClassNotFoundException e) {
                System.err.println("Class not found: " + className);
            }
        }
    }
    
    public static void processAggregationRelations() {
        for (String className : fullyQualifiedClassNames) {
            try {
                Class<?> clazz = Class.forName(className);

                // Get all fields of the class
                Field[] fields = clazz.getDeclaredFields();

                // Check each field to see if it represents an aggregation (i.e., the field is a class type, not a primitive type)
                for (Field field : fields) {
                    // We're looking for object type fields (not primitive types)
                    if (!field.getType().isPrimitive()) {
                        String fieldClassName = field.getType().getName();

                        // Ensure the field's class is one of the classes we're analyzing
                        if (fullyQualifiedClassNames.contains(fieldClassName)) {
                            // Assuming that the class holds a reference to another class (aggregation relation)
                            aggregationRelations.put(className, fieldClassName);
                        }
                    }
                }

            } catch (ClassNotFoundException e) {
                System.err.println("Class not found: " + className);
            }
        }
    }
    
    public static void processCompositionRelations() {
        for (String className : fullyQualifiedClassNames) {
            try {
                Class<?> clazz = Class.forName(className);

                // Get all fields of the class
                Field[] fields = clazz.getDeclaredFields();

                // Check each field to see if it represents a composition
                for (Field field : fields) {
                    // We're looking for object type fields (not primitive types)
                    if (!field.getType().isPrimitive()) {
                        String fieldClassName = field.getType().getName();

                        // Ensure the field's class is one of the classes we're analyzing
                        if (fullyQualifiedClassNames.contains(fieldClassName)) {
                            // Check if the field is initialized inside the container class (composition)
                            if (isCompositionField(clazz, field)) {
                                compositionRelations.put(className, fieldClassName);
                            }
                        }
                    }
                }

            } catch (ClassNotFoundException e) {
                System.err.println("Class not found: " + className);
            }
        }
    }

    /**
     * Checks if a field represents a composition relationship.
     * A field is considered a composition if it is initialized inside the container class.
     *
     * @param clazz The container class.
     * @param field The field to check.
     * @return True if the field represents a composition, false otherwise.
     */
    private static boolean isCompositionField(Class<?> clazz, Field field) {
        try {
            // Check if the field is initialized in the constructor
            for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                // Check if the field is assigned in the constructor
                if (isFieldAssignedInConstructor(constructor, field)) {
                    return true;
                }
            }

            // Check if the field is initialized directly (e.g., private SomeClass obj = new SomeClass())
            field.setAccessible(true);
            Object instance = clazz.getDeclaredConstructor().newInstance();
            Object fieldValue = field.get(instance);
            return fieldValue != null; // If the field is initialized, it's a composition
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if a field is assigned in a constructor.
     *
     * @param constructor The constructor to check.
     * @param field       The field to check.
     * @return True if the field is assigned in the constructor, false otherwise.
     */
    private static boolean isFieldAssignedInConstructor(Constructor<?> constructor, Field field) {
        // This is a simplified check. In a real implementation, you might need to analyze the bytecode or source code.
        // For now, we assume that if the field is final, it's likely assigned in the constructor.
        return java.lang.reflect.Modifier.isFinal(field.getModifiers());
    }
    
    
    public static void processRealizationRelations() {
        for (String className : fullyQualifiedClassNames) {
            try {
                Class<?> clazz = Class.forName(className);

                // Check if the class implements any interfaces
                Class<?>[] interfaces = clazz.getInterfaces();

                // Map each interface to the class
                for (Class<?> interfaceClass : interfaces) {
                    String interfaceName = interfaceClass.getName();

                    // Ensure the interface is one of the classes we're analyzing
                    if (fullyQualifiedClassNames.contains(interfaceName)) {
                        realizationRelations.put(className, interfaceName);
                    }
                }

            } catch (ClassNotFoundException e) {
                System.err.println("Class not found: " + className);
            }
        }
    }
    
    
    public static void processDependencyRelations() {
        // Iterate through the list of fully qualified class names
        for (String className : fullyQualifiedClassNames) {
            try {
                // Load the class
                Class<?> clazz = Class.forName(className);

                // Process methods in the class
                processMethods(clazz);

                // You can also process constructors or fields if you need more dependency information
                // For simplicity, we're focusing on methods for this example.

            } catch (ClassNotFoundException e) {
                System.err.println("Class not found: " + className);
            }
        }
    }

    // Helper method to process methods in a class
    private static void processMethods(Class<?> clazz) {
        // Get all methods in the class
        Method[] methods = clazz.getDeclaredMethods();

        // Check method parameters to find dependencies
        for (Method method : methods) {
            // Process method parameters
            processMethodParameters(method, clazz.getName());

            // Process return type to find dependencies
            processReturnType(method, clazz.getName());
        }
    }

    // Helper method to process method parameters and detect dependencies
    private static void processMethodParameters(Method method, String className) {
        // Get method parameters
        Class<?>[] parameterTypes = method.getParameterTypes();

        // Iterate through each parameter type to check if it's a dependency
        for (Class<?> parameterType : parameterTypes) {
            String parameterClassName = parameterType.getName();

            // Ensure the parameter's class is one of the classes we're analyzing
            if (fullyQualifiedClassNames.contains(parameterClassName)) {
                // Record the dependency (className depends on parameterClassName)
                dependencyRelations.put(className, parameterClassName);
            }
        }
    }

    // Helper method to process return type and detect dependencies
    private static void processReturnType(Method method, String className) {
        // Get method return type
        Class<?> returnType = method.getReturnType();
        String returnClassName = returnType.getName();

        // Ensure the return type's class is one of the classes we're analyzing
        if (fullyQualifiedClassNames.contains(returnClassName)) {
            // Record the dependency (className depends on returnClassName)
            dependencyRelations.put(className, returnClassName);
        }
    }

    // Method to print dependency relations
    public static void printDependencyRelations() {
        for (String key : dependencyRelations.keySet()) {
            System.out.println("Class: " + key + " depends on Class: " + dependencyRelations.get(key));
        }
    }
    
    public static void processImportRelations() throws ClassNotFoundException {
/*    	This will rely on class diagram relations :
 * 		If a class has any kind of relation with another class from another package, then it's a relation of import between the 2 packages
 */
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

	public static HashMap<String, String> getGeneralizationRelations() {
		return generalizationRelations;
	}

	public static void setGeneralizationRelations(HashMap<String, String> generalizationRelations) {
		ProjectProcessor.generalizationRelations = generalizationRelations;
	}
	
	
    
	
    
}
