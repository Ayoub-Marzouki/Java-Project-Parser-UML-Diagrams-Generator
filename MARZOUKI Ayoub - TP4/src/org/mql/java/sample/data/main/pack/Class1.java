/**
 * Serves as an example to showcase "generalization" relation of class diagram
 * Serves as an example to showcase "aggregation" relation of class diagram
 * Serves as an example to showcase "association class" relation of class diagram
 * Also serves as an example to showcase "import" relation of package diagram
 */

package org.mql.java.sample.data.main.pack;

import org.mql.java.sample.data.main.pack.subpackage1.Sub1Class2;

public class Class1 extends Class2 {
	private int attribute1;
	static String attribute2;
	public float attribute3;
	Sub1Class2 sub1class2attribute;
	
	public Class1() {}

	public int getAttribute1() {
		return attribute1;
	}

	public void setAttribute1(int attribute1) {
		this.attribute1 = attribute1;
	}
}
