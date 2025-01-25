package org.mql.java.xml.structure;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class Project {
    private String name;
    private List<Package> packages;

    public Project(String name) {
        this.name = name;
        this.packages = new ArrayList<>();
    }

    public void addPackage(Package pkg) {
        packages.add(pkg);
    }

    public List<Package> getPackages() {
        return packages;
    }

    public Package getOrCreatePackage(String packageName) {
        for (Package pkg : packages) {
            if (pkg.getName().equals(packageName)) {
                return pkg;
            }
        }
        Package newPkg = new Package(packageName);
        packages.add(newPkg);
        return newPkg;
    }

    public void toXml(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            XMLStreamWriter xmlWriter = factory.createXMLStreamWriter(writer);

            xmlWriter.writeStartDocument();
            xmlWriter.writeStartElement("project");
            xmlWriter.writeAttribute("name", name);

            for (Package pkg : packages) {
                pkg.toXml(xmlWriter);
            }

            xmlWriter.writeEndElement();
            xmlWriter.writeEndDocument();
            xmlWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public String getName() {
		return name;
	}
}