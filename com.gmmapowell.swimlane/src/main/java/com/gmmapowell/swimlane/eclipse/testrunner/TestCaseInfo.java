package com.gmmapowell.swimlane.eclipse.testrunner;

import java.util.List;

import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo;

public class TestCaseInfo implements TestInfo {
	private final Type type;
	private final GroupOfTests testGroup;
	private final String classUnderTest;
	private final String testname;
	private boolean isFailed;
	private boolean isError;
	private List<String> stack;
	private String expected;
	private String actual;

	public TestCaseInfo(Type type, GroupOfTests grp, String classUnderTest, String testName) {
		this.type = type;
		this.testGroup = grp;
		this.classUnderTest = classUnderTest;
		this.testname = testName;
	}

	@Override
	public Type type() {
		return type;
	}

	@Override
	public int compareTo(TestInfo o) {
		int ret = classUnderTest.compareTo(o.classUnderTest());
		if (ret != 0)
			return ret;
		ret = testname.compareTo(o.testName());
		return ret;
	}

	@Override
	public GroupOfTests groupName() {
		return testGroup;
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

	public void error() {
		isError = true;
	}
	
	@Override
	public boolean hasFailed() {
		return isFailed || isError;
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
		return "TC[" + classUnderTest + "." + testname + (isError?" error":isFailed?" failed":"") + (stack != null?" " +stack.size():"") + (expected != null ? " " + expected + " != " + actual : "") + "]";
	}
}
