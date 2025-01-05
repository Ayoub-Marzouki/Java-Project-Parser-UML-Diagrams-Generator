package org.mql.java.reflection;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

public class Testing {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
//		System.out.print("Enter the src folder's full path of your java project : path = ");
//		String path = s.nextLine();
		String path = "C:\\Users\\ayoub\\git\\repository\\MARZOUKI Ayoub - TP4\\src";
		Path srcFolderPath = Path.of(path);
		File srcFolder = srcFolderPath.toFile();
		File[] files = srcFolder.listFiles();

//		Class<?> clazzLiteral = null;
//		try {
//			clazzLiteral = Class.forName("org.mql.java.sample.data.main.pack.subpackage1.subpackage1.Sub1Sub1Class2");
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//		Method[] m = clazzLiteral.getDeclaredMethods();
//		for (Method method : m) {
//			System.out.println(m.toString());
//		}
		
		UMLDiagramsGenerator.generate(srcFolderPath);
		
		UMLDiagramsGenerator.structure();	
		
//		HashMap<String, String> pair = new HashMap<>();
//		
//		Vector<String> filesNames = new Vector<String>();
//		filesNames.add("file 1");
//		filesNames.add("file 2");
//		filesNames.add("file 3");
//		filesNames.add("file 4");
//		String currentFolderName = "Folder 1";
//		pair.put(currentFolderName, filesNames.get(0));
//		pair.put(currentFolderName, filesNames.get(1));
//		pair.put(currentFolderName, filesNames.get(2));
//		
//		for (Map.Entry<String, String> entry : pair.entrySet()) {
//            System.out.println("Folder: " + entry.getKey());
//            System.out.println("Files: " + entry.getValue());
//        }
//		
		s.close();
	}
}
