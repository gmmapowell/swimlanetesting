package com.gmmapowell.swimlane.eclipse.testrunner;

import java.util.List;

import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo;

public class TestCaseInfo implements TestInfo {
	private final Type type;
	private final String classUnderTest;
	private final String testname;
	private boolean isFailed;
	private List<String> stack;
	private String expected;
	private String actual;

	public TestCaseInfo(Type type, String classUnderTest, String testName) {
		this.type = type;
		this.classUnderTest = classUnderTest;
		this.testname = testName;
	}

	@Override
	public Type type() {
		return type;
	}

	@Override
	public String classUnderTest() {
		return classUnderTest;
	}

	@Override
	public String testName() {
		return testname;
	}
	
	public void failed() {
		isFailed = true;
	}
	
	@Override
	public boolean hasFailed() {
		return isFailed;
	}
	
	public void stack(List<String> stack) {
		this.stack = stack;
	}

	@Override
	public List<String> stack() {
		return stack;
	}
	
	public void expectedValue(String expected) {
		this.expected = expected;
	}
	
	public void actualValue(String actual) {
		this.actual = actual;
	}
	
	public String getExpected() {
		return expected;
	}

	public String getActual() {
		return actual;
	}

	@Override
	public String toString() {
		return "TC[" + classUnderTest + "." + testname + (isFailed?" failed":"") + (stack != null?" " +stack.size():"") + (expected != null ? " " + expected + " != " + actual : "") + "]";
	}
}
