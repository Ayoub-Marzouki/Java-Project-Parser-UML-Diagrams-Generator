package org.mql.java.main;

import org.mql.java.xml.structure.Project;
import org.mql.java.xml.structure.Package;
import org.mql.java.xml.JavaProjectParser;
import org.mql.java.xml.XMLParser;
import org.mql.java.xml.structure.Class;
import org.mql.java.xml.structure.Relationship;

public class XMLMain {
    public static void main(String[] args) {
        JavaProjectParser parser = new JavaProjectParser();
        Project project = parser.parseProject("C:\\Users\\ayoub\\git\\repository\\MARZOUKI Ayoub - TP4");
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