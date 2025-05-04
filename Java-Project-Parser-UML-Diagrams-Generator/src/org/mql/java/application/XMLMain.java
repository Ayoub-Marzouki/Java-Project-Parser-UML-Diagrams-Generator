package org.mql.java.application;

import org.mql.java.xml.business.JavaProjectParser;
import org.mql.java.xml.business.XMLParser;
import org.mql.java.xml.model.Class;
import org.mql.java.xml.model.Package;
import org.mql.java.xml.model.Project;
import org.mql.java.xml.model.Relationship;

public class XMLMain {
    public static void main(String[] args) {
        JavaProjectParser parser = new JavaProjectParser();
        Project project = parser.parseProject("C:\\Users\\ayoub\\git\\repository\\Java-Project-Parser-UML-Diagrams-Generator");
        project.toXml("output.xml");

        String xmlFilePath = "output.xml";
        Project project2 = XMLParser.parseProject(xmlFilePath);


        System.out.println("Project: " + project2.getName());
        for (Package pkg : project2.getPackages()) {
            System.out.println("Package: " + pkg.getName());
            for (Class cls : pkg.getClasses()) {
                System.out.println("Class: " + cls.getName());
                for (Relationship relationship : cls.getRelationships()) {
                    System.out.println("Relationship: " + relationship.getType() + " -> " + relationship.getTarget());
                }
            }
        }
    }
}