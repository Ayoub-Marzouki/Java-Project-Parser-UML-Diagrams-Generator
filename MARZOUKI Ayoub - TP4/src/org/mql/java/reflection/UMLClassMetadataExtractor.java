package org.mql.java.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class UMLClassMetadataExtractor {
    private Class<?> clazz; // The class to analyze
    private String className;
    private List<Field> fields;
    private List<Method> methods;
    private List<Constructor<?>> constructors;
    private String fieldsUML; // UML representation of fields
    private String methodsUML; // UML representation of methods and constructors

    // Constructor
    public UMLClassMetadataExtractor(Class<?> clazz) {
        this.clazz = clazz;
        this.className = clazz.getSimpleName();
        this.fields = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.constructors = new ArrayList<>();
        this.fieldsUML = "";
        this.methodsUML = "";

        // Extract metadata
        extractFields();
        extractMethods();
        extractConstructors();

        // Generate UML representation
        generateFieldsUML();
        generateMethodsUML();
    }

    // Extract all fields from the class
    private void extractFields() {
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            fields.add(field);
        }
    }

    // Extract all methods from the class
    private void extractMethods() {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            methods.add(method);
        }
    }

    // Extract all constructors from the class
    private void extractConstructors() {
        Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : declaredConstructors) {
            constructors.add(constructor);
        }
    }

    // Format a single field into UML notation
    private String formatField(Field field) {
        StringBuilder fieldString = new StringBuilder();

        // Add visibility modifier
        int modifiers = field.getModifiers();
        if (Modifier.isPrivate(modifiers)) {
            fieldString.append("- ");
        } else if (Modifier.isPublic(modifiers)) {
            fieldString.append("+ ");
        } else if (Modifier.isProtected(modifiers)) {
            fieldString.append("# ");
        } else {
            fieldString.append("~ ");
        }

        // Add field name and type
        fieldString.append(field.getName()).append(" : ").append(field.getType().getSimpleName());

        return fieldString.toString();
    }

    // Format a single method into UML notation
    private String formatMethod(Method method) {
        StringBuilder methodString = new StringBuilder();

        // Add visibility modifier
        int modifiers = method.getModifiers();
        if (Modifier.isPrivate(modifiers)) {
            methodString.append("- ");
        } else if (Modifier.isPublic(modifiers)) {
            methodString.append("+ ");
        } else if (Modifier.isProtected(modifiers)) {
            methodString.append("# ");
        } else {
            methodString.append("~ ");
        }

        // Add method name
        methodString.append(method.getName()).append("(");

        // Add parameters
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            methodString.append("param").append(i + 1).append(": ").append(parameterTypes[i].getSimpleName());
            if (i < parameterTypes.length - 1) {
                methodString.append(", ");
            }
        }

        // Add return type
        methodString.append(") : ").append(method.getReturnType().getSimpleName());

        return methodString.toString();
    }

    // Format a single constructor into UML notation
    private String formatConstructor(Constructor<?> constructor) {
        StringBuilder constructorString = new StringBuilder();

        // Add visibility modifier
        int modifiers = constructor.getModifiers();
        if (Modifier.isPrivate(modifiers)) {
            constructorString.append("- ");
        } else if (Modifier.isPublic(modifiers)) {
            constructorString.append("+ ");
        } else if (Modifier.isProtected(modifiers)) {
            constructorString.append("# ");
        } else {
            constructorString.append("~ ");
        }

        // Add constructor name (class name)
        constructorString.append(className).append("(");

        // Add parameters
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            constructorString.append("param").append(i + 1).append(": ").append(parameterTypes[i].getSimpleName());
            if (i < parameterTypes.length - 1) {
                constructorString.append(", ");
            }
        }

        constructorString.append(")");

        return constructorString.toString();
    }

    // Generate the UML representation of the fields
    private void generateFieldsUML() {
        StringBuilder fieldsBuilder = new StringBuilder();

        // Add fields
        for (Field field : fields) {
            fieldsBuilder.append(formatField(field)).append("\n");
        }

        // Store the fields UML representation
        fieldsUML = fieldsBuilder.toString();
    }

    // Generate the UML representation of the methods and constructors
    private void generateMethodsUML() {
        StringBuilder methodsBuilder = new StringBuilder();

        // Add constructors
        for (Constructor<?> constructor : constructors) {
            methodsBuilder.append(formatConstructor(constructor)).append("\n");
        }

        // Add methods
        for (Method method : methods) {
            methodsBuilder.append(formatMethod(method)).append("\n");
        }

        // Store the methods UML representation
        methodsUML = methodsBuilder.toString();
    }

    // Get the UML representation of the fields
    public String getFieldsUML() {
        return fieldsUML;
    }

    // Get the UML representation of the methods and constructors
    public String getMethodsUML() {
        return methodsUML;
    }

    // Print the UML representation of the fields
    public void printFieldsUML() {
        System.out.println("Fields:");
        System.out.println(fieldsUML);
    }

    // Print the UML representation of the methods and constructors
    public void printMethodsUML() {
        System.out.println("Methods and Constructors:");
        System.out.println(methodsUML);
    }
}