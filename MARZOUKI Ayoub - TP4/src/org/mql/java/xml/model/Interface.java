package org.mql.java.xml.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class Interface {
    private String name;
    private List<Relationship> relationships;

    public Interface(String name) {
        this.name = name;
        this.relationships = new ArrayList<>();
    }

    public void addRelationship(Relationship relationship) {
        relationships.add(relationship);
    }

    @Override
    public String toString() {
        return "Interface: " + name + "\nRelationships: " + relationships;
    }
    
    
    public void toXml(XMLStreamWriter xmlWriter) throws XMLStreamException {
        xmlWriter.writeStartElement("interface");
        xmlWriter.writeAttribute("name", name);

        writeRelationships(xmlWriter);

        xmlWriter.writeEndElement();
    }

    private void writeRelationships(XMLStreamWriter xmlWriter) throws XMLStreamException {
        if (!relationships.isEmpty()) {
            xmlWriter.writeStartElement("relationships");
            for (Relationship relationship : relationships) {
                relationship.toXml(xmlWriter);
            }
            xmlWriter.writeEndElement();
        }
    }
}