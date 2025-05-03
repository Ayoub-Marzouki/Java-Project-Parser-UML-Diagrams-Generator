package org.mql.java.xml.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class Enum {
    private String name;
    private List<Relationship> relationships;

    public Enum(String name) {
        this.name = name;
        this.relationships = new ArrayList<>();
    }

    public void addRelationship(Relationship relationship) {
        relationships.add(relationship);
    }

    @Override
    public String toString() {
        return "Enum: " + name + "\nRelationships: " + relationships;
    }
    
    
    /**
     * Converts this Enum object to its XML representation.
     * The XML element will be named `<enum>` and will include the name attribute and any relationships.
     *
     * @param xmlWriter The XMLStreamWriter used to write the XML.
     * @throws XMLStreamException If there is an error writing to the XML stream.
     */
    public void toXml(XMLStreamWriter xmlWriter) throws XMLStreamException {
        xmlWriter.writeStartElement("enum");
        xmlWriter.writeAttribute("name", name);

        writeRelationships(xmlWriter);

        xmlWriter.writeEndElement();
    }
    
    
    /**
     * Writes the relationships of this Enum to XML under the <relationships> element.
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
    
}