package org.mql.java.xml.structure;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class Package {
    private String name;
    private List<Class> classes;
    private List<Interface> interfaces;
    private List<Enum> enums;
    private List<Annotation> annotations;

    public Package(String name) {
        this.name = name;
        this.classes = new ArrayList<>();
        this.interfaces = new ArrayList<>();
        this.enums = new ArrayList<>();
        this.annotations = new ArrayList<>();
    }

    public void addClass(Class cls) {
        classes.add(cls);
    }

    public void addInterface(Interface iface) {
        interfaces.add(iface);
    }

    public void addEnum(Enum enm) {
        enums.add(enm);
    }

    public void addAnnotation(Annotation annotation) {
        annotations.add(annotation);
    }

    @Override
    public String toString() {
        return "Package: " + name + "\nClasses: " + classes + "\nInterfaces: " + interfaces +
               "\nEnums: " + enums + "\nAnnotations: " + annotations;
    }

	public String getName() {
		return name;
	}
	
	public void toXml(XMLStreamWriter xmlWriter) throws XMLStreamException {
        xmlWriter.writeStartElement("package");
        xmlWriter.writeAttribute("name", name);

        for (Class cls : classes) {
            cls.toXml(xmlWriter);
        }
        for (Interface iface : interfaces) {
            iface.toXml(xmlWriter);
        }
        for (Enum enm : enums) {
            enm.toXml(xmlWriter);
        }
        for (Annotation annotation : annotations) {
            annotation.toXml(xmlWriter);
        }

        xmlWriter.writeEndElement();
    }

	public Class[] getClasses() {
	    return classes.toArray(new Class[0]);
	}
	
	
}