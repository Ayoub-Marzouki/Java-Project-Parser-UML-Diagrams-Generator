package org.mql.java.xml.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class Class {
    private String name;
    private List<Relationship> relationships;

    public Class(String name) {
        this.name = name;
        this.relationships = new ArrayList<>();
    }

    public void addRelationship(Relationship relationship) {
        relationships.add(relationship);
    }

    @Override
    public String toString() {
        return "Class: " + name + "\nRelationships: " + relationships;
    }
    
    
    /**
     * Converts this Class object to its XML representation.
     * The XML element will be named `class` and will include the name attribute and any relationships.
     *
     * @param xmlWriter The XMLStreamWriter used to write the XML.
     * @throws XMLStreamException If there is an error writing to the XML stream.
     */
    public void toXml(XMLStreamWriter xmlWriter) throws XMLStreamException {
        xmlWriter.writeStartElement("class");
        xmlWriter.writeAttribute("name", name);

        writeRelationships(xmlWriter);

        xmlWriter.writeEndElement();
    }
    
    
    /**
     * Writes the relationships of this Class to XML under the <relationships> element.
     * Each relationship will be written as an individual XML element using the Relationship object's toXml method.
     *
     * @param xmlWriter The XMLStreamWriter used to write the XML.
     * @throws XMLStreamException If there is an error writing to the XML stream.
     */
    private void writeRelationships(XMLStreamWriter xmlWriter) throws XMLStreamException {
        if (!relationships.isEmpty()) {
            xmlWriter.writeStartElement("relationships");
            for (Relationship relationship : relationships) {
                relationship.toXml(xmlWriter);
            }
            xmlWriter.writeEndElement();
        }
    }

	public String getName() {
		return this.name;
	}

	public Relationship[] getRelationships() {
		return relationships.toArray(new Relationship[0]);
	}
}