/**
 * Serves as an example to showcase "realization" relation of class diagram.
 * Serves as an example to showcase "composition" relation of class diagram.
 * Also serves as an example to showcase "import" relation of package diagram.
 */


package org.mql.java.sample.data.main.pack;

import org.mql.java.sample.data.main.pack.subpackage1.subpackage1.Sub1Sub1Class1;

public class Class2 implements Sub1Sub1Class1  {
	InnerClass attribute1;
	
	public class InnerClass {
		
	}

	@Override
	public void method1() {
		System.out.println("Realization example.");
		
	}

	@Override
	public String method2() {
		return "Realization example (Class2 implementing Sub1Sub1Class1)";
	}

}
