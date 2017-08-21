package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.List;

public interface TestInfo extends Comparable<TestInfo> {
	public enum State { SUCCESS, FAILURE, ERROR }
	GroupOfTests groupName();
	String classUnderTest();
	String testName();
	List<String> stack();
	List<String> getExpected();
	List<String> getActual();
	State outcome();
}
