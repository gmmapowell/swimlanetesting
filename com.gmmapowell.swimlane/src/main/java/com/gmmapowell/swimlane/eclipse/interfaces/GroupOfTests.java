package com.gmmapowell.swimlane.eclipse.interfaces;

public interface GroupOfTests {

	String[] getClasses();
	String getClassPath();
	void addTest(String clsName);

}
