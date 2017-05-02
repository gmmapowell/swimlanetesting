package com.gmmapowell.swimlane.eclipse.testrunner;

import java.util.List;

import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo;

public class TestCaseInfo implements TestInfo {
	private final String testname;
	private boolean isFailed;
	private List<String> stack;

	public TestCaseInfo(String testname) {
		this.testname = testname;
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

	@Override
	public String toString() {
		return "TC[" + testname + (isFailed?" failed":"") + (stack != null?" " +stack.size():"") + "]";
	}
}
