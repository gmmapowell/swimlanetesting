package com.gmmapowell.swimlane.eclipse.testrunner;

import java.util.List;

import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo;

public class TestCaseInfo implements TestInfo {
	private final GroupOfTests testGroup;
	private final String classUnderTest;
	private final String testname;
	private final State outcome;
	private final List<String> stack;
	private final List<String> expected;
	private final List<String> actual;

	// success constructor
	public TestCaseInfo(GroupOfTests grp, String classUnderTest, String testName) {
		this(grp, classUnderTest, testName, State.SUCCESS, null, null, null);
	}

	// success constructor
	public TestCaseInfo(GroupOfTests grp, String classUnderTest, String testName, List<String> stack) {
		this(grp, classUnderTest, testName, State.ERROR, stack, null, null);
	}

	// failure constructor
	public TestCaseInfo(GroupOfTests grp, String classUnderTest, String testName, List<String> stack, List<String> expected, List<String> actual) {
		this(grp, classUnderTest, testName, State.FAILURE, stack, expected, actual);
	}

	// combined constructor
	private TestCaseInfo(GroupOfTests grp, String classUnderTest, String testName, State outcome, List<String> stack, List<String> expected, List<String> actual) {
		this.testGroup = grp;
		this.classUnderTest = classUnderTest;
		this.testname = testName;
		this.outcome = outcome;
		this.stack = stack;
		this.expected = expected;
		this.actual = actual;
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

	@Override
	public State outcome() {
		return outcome;
	}
	
	@Override
	public List<String> stack() {
		return stack;
	}
	
	public List<String> getExpected() {
		return expected;
	}

	public List<String> getActual() {
		return actual;
	}

	@Override
	public String toString() {
		return "TC[" + classUnderTest + "." + testname + " " + outcome + (stack != null?" " +stack.size():"") + (expected != null ? " " + expected + " != " + actual : "") + "]";
	}
}
