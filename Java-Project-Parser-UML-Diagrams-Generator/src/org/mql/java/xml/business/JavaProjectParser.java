package org.mql.java.xml.business;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.mql.java.xml.model.Annotation;
import org.mql.java.xml.model.Class;
import org.mql.java.xml.model.Enum;
import org.mql.java.xml.model.Interface;
import org.mql.java.xml.model.Package;
import org.mql.java.xml.model.Project;
import org.mql.java.xml.model.Relationship;

public class JavaProjectParser {
	
	/**
	 * Parses the specified project directory and constructs a Project object.
	 * Recursively scans for all `.java` files and processes them to extract relevant information.
	 *
	 * @param projectPath The absolute or relative path to the root directory of the Java project.
	 * @return A Project object representing the structure and contents of the parsed project.
	 *
	 * @throws IllegalArgumentException if the provided path is not a valid directory.
	 */
    public Project parseProject(String projectPath) {
        File projectDir = new File(projectPath);
        String projectName = projectDir.getName();
        Project project = new Project(projectName);

        if (!projectDir.isDirectory()) {
            throw new IllegalArgumentException("The provided path is not a directory.");
        }

        traverseDirectory(projectDir, project);
        return project;
    }

    
    /**
     * Recursively traverses the given directory and its subdirectories,
     * identifying `.java` files and passing them to the Java file parser.
     *
     * @param dir     The directory to traverse.
     * @param project The Project object to which parsed Java files will be added.
     */
    private static void traverseDirectory(File dir, Project project) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                traverseDirectory(file, project); // Recursively traverse subdirectories
            } else if (file.getName().endsWith(".java")) {
                parseJavaFile(file, project); // Parse Java files
            }
        }
    }
    
    
    /**
     * Parses a single Java source file to identify and extract high-level declarations
     * such as classes, interfaces, enums, and annotations. Detected elements are organized
     * into their respective packages within the given Project object.
     *
     * @param javaFile The Java source file to parse.
     * @param project  The Project model to which the parsed elements will be added.
     */
    private static void parseJavaFile(File javaFile, Project project) {
        try (Scanner scanner = new Scanner(javaFile)) {
            String packageName = "default"; // Default package if none is specified
            Class currentClass = null;
            Interface currentInterface = null;
            Enum currentEnum = null;
            Annotation currentAnnotation = null;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                // Extract package
                if (line.startsWith("package ")) {
                    packageName = line.replace("package", "").replace(";", "").trim();
                }

                // Extract class
                if (line.startsWith("public class ") || line.startsWith("class ")) {
                	String className = line.replace("public", "").replace("class", "").split("extends|implements|\\{")[0].trim();                    currentClass = new Class(className);
                    extractRelationships(line, currentClass);

                    // Add the class to the appropriate package
                    Package pkg = project.getOrCreatePackage(packageName);
                    pkg.addClass(currentClass);
                }

                // Extract interface
                if (line.startsWith("public interface ") || line.startsWith("interface ")) {
                    String interfaceName = line.replace("public", "").replace("interface", "").split("\\{")[0].trim();
                    currentInterface = new Interface(interfaceName);
                    extractRelationships(line, currentInterface);

                    // Add the interface to the appropriate package
                    Package pkg = project.getOrCreatePackage(packageName);
                    pkg.addInterface(currentInterface);
                }

                // Extract enum
                if (line.startsWith("public enum ") || line.startsWith("enum ")) {
                    String enumName = line.replace("public", "").replace("enum", "").split("\\{")[0].trim();
                    currentEnum = new Enum(enumName);
                    extractRelationships(line, currentEnum);

                    // Add the enum to the appropriate package
                    Package pkg = project.getOrCreatePackage(packageName);
                    pkg.addEnum(currentEnum);
                }

                // Extract annotation
                if (line.startsWith("public @interface ") || line.startsWith("@interface ")) {
                    String annotationName = line.replace("public", "").replace("@interface", "").split("\\{")[0].trim();
                    currentAnnotation = new Annotation(annotationName);
                    extractRelationships(line, currentAnnotation);

                    // Add the annotation to the appropriate package
                    Package pkg = project.getOrCreatePackage(packageName);
                    pkg.addAnnotation(currentAnnotation);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    
    /**
     * Analyzes a line of Java code to extract relationships such as inheritance (extends, implements)
     * and field-based associations (aggregation). Updates the provided entity accordingly.
     *
     * @param line   The line of Java code to analyze.
     * @param entity The class-like entity (Class, Interface, etc.) to attach relationships to.
     */
    private static void extractRelationships(String line, Object entity) {
        // Handle inheritance (extends/implements)
        if (line.contains("extends ")) {
            String target = line.split("extends ")[1].split("[{<]")[0].trim();
            if (entity instanceof Class) {
                ((Class) entity).addRelationship(new Relationship(Relationship.Type.EXTENDS, target));
            }
        }

        if (line.contains("implements ")) {
            String[] targets = line.split("implements ")[1].split("[{<]")[0].split(",");
            for (String target : targets) {
                if (entity instanceof Class) {
                    ((Class) entity).addRelationship(new Relationship(Relationship.Type.IMPLEMENTS, target.trim()));
                }
            }
        }

        // Handle fields (aggregation/composition)
        if (line.contains(";") && !line.contains("(")) { // Simple field declaration
            String[] parts = line.split(" ");
            if (parts.length >= 2) {
                String target = parts[parts.length - 2].trim(); // Field type
                if (entity instanceof Class) {
                    ((Class) entity).addRelationship(new Relationship(Relationship.Type.AGGREGATION, target));
                }
            }
        }
    }
}