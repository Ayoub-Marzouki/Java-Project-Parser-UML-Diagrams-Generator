package org.mql.java.reflection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.mql.java.drawing.relations.pack.Merge;

public class Testing {
	public static void main(String[] args) throws ClassNotFoundException {
		Scanner scanner = new Scanner(System.in);

//        System.out.print("Enter the src folder's full path of your Java project: path = ");
//        String path = scanner.nextLine();
        String path = "C:\\Users\\ayoub\\git\\repository\\MARZOUKI Ayoub - TP4\\src";
//        scanner.close();
        
        ProjectProcessor.process(path); 
        UMLDiagramsGenerator.generateDiagrams();
	}
}
