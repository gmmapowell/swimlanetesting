package com.gmmapowell.swimlane.eclipse.interfaces;

public interface GroupOfTests {
	String groupName();
	String[] getClasses();
	String getClassPath();
	void addTest(String clsName);

}
