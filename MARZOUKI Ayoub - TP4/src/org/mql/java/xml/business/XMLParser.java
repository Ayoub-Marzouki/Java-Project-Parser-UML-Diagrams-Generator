package org.mql.java.xml.business;

import org.mql.java.xml.model.Class;
import org.mql.java.xml.model.Interface;
import org.mql.java.xml.model.Package;
import org.mql.java.xml.model.Project;
import org.mql.java.xml.model.Relationship;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class XMLParser {

    public static Project parseProject(String filePath) {
        Project project = null;
        try {
            // Charger le fichier XML
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            // Extraire l'élément racine <project>
            Element projectElement = doc.getDocumentElement();
            String projectName = projectElement.getAttribute("name");
            project = new Project(projectName);

            // Extraire les packages
            NodeList packageList = projectElement.getElementsByTagName("package");
            for (int i = 0; i < packageList.getLength(); i++) {
                Node packageNode = packageList.item(i);
                if (packageNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element packageElement = (Element) packageNode;
                    Package pkg = parsePackage(packageElement);
                    project.addPackage(pkg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return project;
    }

    private static Package parsePackage(Element packageElement) {
        String packageName = packageElement.getAttribute("name");
        Package pkg = new Package(packageName);

        // Extraire les classes
        NodeList classList = packageElement.getElementsByTagName("class");
        for (int i = 0; i < classList.getLength(); i++) {
            Node classNode = classList.item(i);
            if (classNode.getNodeType() == Node.ELEMENT_NODE) {
                Element classElement = (Element) classNode;
                Class cls = parseClass(classElement);
                pkg.addClass(cls);
            }
        }

        // Extraire les interfaces
        NodeList interfaceList = packageElement.getElementsByTagName("interface");
        for (int i = 0; i < interfaceList.getLength(); i++) {
            Node interfaceNode = interfaceList.item(i);
            if (interfaceNode.getNodeType() == Node.ELEMENT_NODE) {
                Element interfaceElement = (Element) interfaceNode;
                Interface iface = parseInterface(interfaceElement);
                pkg.addInterface(iface);
            }
        }

        return pkg;
    }

    private static Class parseClass(Element classElement) {
        String className = classElement.getAttribute("name");
        Class cls = new Class(className);

        // Extraire les relations
        NodeList relationshipList = classElement.getElementsByTagName("relationship");
        for (int i = 0; i < relationshipList.getLength(); i++) {
            Node relationshipNode = relationshipList.item(i);
            if (relationshipNode.getNodeType() == Node.ELEMENT_NODE) {
                Element relationshipElement = (Element) relationshipNode;
                Relationship relationship = parseRelationship(relationshipElement);
                cls.addRelationship(relationship);
            }
        }

        return cls;
    }

    private static Interface parseInterface(Element interfaceElement) {
        String interfaceName = interfaceElement.getAttribute("name");
        return new Interface(interfaceName);
    }

    private static Relationship parseRelationship(Element relationshipElement) {
        String type = relationshipElement.getAttribute("type");
        String target = relationshipElement.getAttribute("target");
        return new Relationship(Relationship.Type.valueOf(type), target);
    }
}