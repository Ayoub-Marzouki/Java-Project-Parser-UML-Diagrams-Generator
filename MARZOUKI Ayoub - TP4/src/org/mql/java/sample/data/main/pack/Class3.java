/**
 * Serves as an example to showcase "dependecy" relation of class diagram
 * Also serves as an example to showcase "import" relation of package diagram
 */
package org.mql.java.sample.data.main.pack;

import org.mql.java.sample.data.main.pack.subpackage1.subpackage1.Sub1Sub1Class2;

public class Class3 {
//	Same attribute1 and get/set methods of class1
	private int attribute1;
	
	public void method1() {
		System.out.println("This method1 of class3 will use sub1sub1class2's static method to showcase dependecy relation of class diagram :\n");
		Sub1Sub1Class2.method3();
	}

	public int getAttribute1() {
		return attribute1;
	}

	public void setAttribute1(int attribute1) {
		this.attribute1 = attribute1;
	}
}
