package org.mql.java.swing.business;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mql.java.swing.ui.Frame;

public class ProjectProcessor {
    private static List<String> fullyQualifiedClassNames = new ArrayList<>();
    private static List<String> packageHierarchy = new ArrayList<>();
    private static List<String> packagesNames = new ArrayList<>();
    
//  Will be later used to drawMergeRelations in RelationsLayer class
    public static HashMap<String, String> mergeRelations = new HashMap<>();
        
    public static HashMap<String, String> generalizationRelations = new HashMap<>();
    
    public static HashMap<String, String> aggregationRelations = new HashMap<>();
    
    public static HashMap<String, String> compositionRelations = new HashMap<>();
    
    public static HashMap<String, String> realizationRelations = new HashMap<>();
    
    public static HashMap<String, String> dependencyRelations = new HashMap<>();

    
    /**
     * <h1>Processes the Java source project located at the specified path.</h1>
     * <p>
     * This method performs the following steps:
     * <ol>
     *     <li>Validates the provided source folder path.</li>
     *     <li>Ensures the folder path contains a 'src' directory.</li>
     *     <li>Recursively retrieves all Java files and generates their fully qualified class names.</li>
     *     <li>Stores package names and package hierarchy information.</li>
     *     <li>Prepares the data structures required for drawing visual representations (e.g., class diagrams).</li>
     *     <li>Extracts and categorizes class relationships (e.g., generalization, aggregation, realization).</li>
     * </ol>
     * If the provided path is invalid or does not contain a 'src' folder, an error popup is shown to the user.
     *
     * @param srcFolderPath the absolute or relative path to the project's source folder
     */
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

    
    /**
     * Checks whether the provided file path is a valid directory.
     *
     * @param srcFolder the file object representing the source folder
     * @return true if the folder exists and is a directory, false otherwise
     */
    private static boolean isValidSrcFolder(File srcFolder) {
        return srcFolder.exists() && srcFolder.isDirectory();
    }

    
    /**
     * Finds and returns the base path ending with 'src' from the given full path.
     * <p>
     * This method looks for the first occurrence of "/src" in the path and returns
     * the substring from the beginning up to and including the 'src' directory.
     *
     * @param fullPath the absolute path to search within
     * @return the base path ending in 'src' if found; otherwise, null
     */
    private static String findSrcBasePath(String fullPath) {
        int srcIndex = fullPath.indexOf(File.separator + "src");
        if (srcIndex == -1) {
            return null;
        }
        return fullPath.substring(0, srcIndex + 4); // Include 'src' in the base path
    }

    /**
     * Recursively traverses the given source files to extract fully qualified class names,
     * package names, and the package hierarchy.
     * <p>
     * For Java files, it removes the '.java' extension and generates the fully qualified class name
     * using the relative path. For directories, it adds their names and relative paths to the
     * packages lists, and continues traversal.
     *
     * @param srcFolderFiles array of files and directories in the source folder
     * @param srcBasePath the base path to which relative paths will be computed
     */
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
    
    
    /**
     * Transfers the collected class and package data to the UMLDiagramsGenerator class
     * to prepare for diagram generation.
     */
    private static void prepareToDraw() {
    	UMLDiagramsGenerator.setFullyQualifiedClassNames(ProjectProcessor.getFullyQualifiedClassNames());
        UMLDiagramsGenerator.setPackageHierarchy(ProjectProcessor.getPackageHierarchy());
        UMLDiagramsGenerator.setPackagesNames(ProjectProcessor.getPackagesNames());
    }

    
    /**
     * Prints the results of the processed project structure to the console,
     * including fully qualified class names, package hierarchy, and individual package names.
     */
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
    
    /**
     * Orchestrates the processing of all types of relationships between packages and classes.
     * <p>
     * This method triggers the detection and registration of:
     * <ul>
     *   <li>Merge relations (package nesting)</li>
     *   <li>Generalization (inheritance)</li>
     *   <li>Aggregation (has-a relationships)</li>
     *   <li>Composition (strong ownership relationships)</li>
     *   <li>Realization (interface implementation)</li>
     *   <li>Dependency (uses-a relationships)</li>
     * </ul>
     */
    public static void processRelations() {
    	processMergeRelations();
    	processGeneralizationRelations();
    	processAggregationRelations();
    	processCompositionRelations();
    	processRealizationRelations();
    	processDependencyRelations();
    }
    
    
    /**
     * Determines the immediate parent package of each sub-package within the package hierarchy.
     * <p>
     * This creates a mapping where each child package points to its closest ancestor
     * (i.e., the longest matching prefix in the package name).
     * <p>
     * Example: For `org.mql.java.drawing`, the immediate parent would be `org.mql.java`
     * if `org`, `org.mql`, and `org.mql.java` are all present in the hierarchy.
     */
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
    
    /**
     * Detects generalization (inheritance) relationships between classes.
     * <p>
     * For each class in the project, this method checks whether it extends
     * another class that is also part of the analyzed classes. If so,
     * the relation is registered in the {@code generalizationRelations} map,
     * where the key is the subclass and the value is the superclass.
     */
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
    
    
    /**
     * Identifies aggregation relationships based on fields in a class.
     * <p>
     * If a class contains a non-primitive field whose type is another known class,
     * it is considered an aggregation. These relationships are stored in
     * {@code aggregationRelations}, with the containing class as the key
     * and the field's class as the value.
     */
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
    
    
    /**
     * Identifies composition relationships by inspecting field declarations and
     * their initialization context.
     * <p>
     * If a class contains a non-primitive field whose type is another known class,
     * and this field is instantiated within the same class (typically in constructors
     * or field initializers), it is marked as a composition relationship.
     * These relations are stored in {@code compositionRelations}, mapping
     * the owner class to the composed class.
     */
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
    
    
    /**
     * Identifies realization relationships between classes and interfaces.
     * <p>
     * For each class in the project, this method checks if it implements any interface
     * that is also part of the analyzed set. If such an interface is found,
     * the relationship is stored in {@code realizationRelations}, mapping the implementing
     * class to the interface it realizes.
     */
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
    
    
    /**
     * Detects dependency relationships between classes based on method signatures.
     * <p>
     * This method processes all methods in each class and checks their parameter types
     * and return types to determine if they use other known classes. Such uses
     * are considered dependencies and stored in {@code dependencyRelations}.
     */
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

    /**
     * Processes all declared methods of a given class to detect dependencies.
     * <p>
     * This involves analyzing each method’s parameters and return type
     * to find references to other classes within the analyzed set.
     *
     * @param clazz the class whose methods will be inspected
     */
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

    /**
     * Inspects a method's parameter types to identify dependencies.
     * <p>
     * If any parameter type belongs to the analyzed set of classes, a dependency
     * is registered from the method's declaring class to the parameter class.
     *
     * @param method    the method being analyzed
     * @param className the name of the class containing the method
     */
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


    /**
     * Inspects a method's return type to identify dependencies.
     * <p>
     * If the return type belongs to the analyzed set of classes, a dependency
     * is registered from the method's declaring class to the return type class.
     *
     * @param method    the method being analyzed
     * @param className the name of the class containing the method
     */
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

    /**
     * Prints all detected class-to-class dependency relationships.
     * <p>
     * For each dependency recorded in {@code dependencyRelations}, this method prints
     * a message indicating which class depends on which.
     */
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
