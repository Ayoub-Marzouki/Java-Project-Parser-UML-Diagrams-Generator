package org.mql.java.reflection;

import java.util.Scanner;

public class Testing {
	public static void main(String[] args) throws ClassNotFoundException {
		Scanner scanner = new Scanner(System.in);

//        System.out.print("Enter the src folder's full path of your Java project: path = ");
//        String path = scanner.nextLine();
        String path = "C:\\Users\\ayoub\\git\\repository\\MARZOUKI Ayoub - TP4\\src";
//        scanner.close();
//        
        ProjectProcessor.process(path);
//        
//        String s = ProjectProcessor.getFullyQualifiedClassNames().get(0);
//        
//        Class<?> c = Class.forName(s);
//        System.out.println("\n\n\n\n testing : " + s + "\ngetPackage : " + c.getPackage() + "\nPackage name : " + c.getPackageName());
        
        UMLDiagramsGenerator.generateDiagrams();
        
	}
}
