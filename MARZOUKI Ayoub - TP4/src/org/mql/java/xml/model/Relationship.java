package org.mql.java.xml.model;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class Relationship {
    public enum Type {
        EXTENDS, // Inheritance
        IMPLEMENTS, // Interface implementation
        AGGREGATION, // Aggregation (has-a relationship)
        COMPOSITION, // Composition (strong has-a relationship)
        USES // Dependency (uses in method parameters, return types, etc.)
    }

    private Type type;
    private String target; // Name of the target class/interface

    public Relationship(Type type, String target) {
        this.type = type;
        this.target = target;
    }

    @Override
    public String toString() {
        return type + " -> " + target;
    }
    
    public void toXml(XMLStreamWriter xmlWriter) throws XMLStreamException {
        xmlWriter.writeStartElement("relationship");
        xmlWriter.writeAttribute("type", type.name());
        xmlWriter.writeAttribute("target", target);
        xmlWriter.writeEndElement();
    }
    
    public Type getType() {
        return type;
    }

    public String getTarget() {
        return target;
    }
    
}