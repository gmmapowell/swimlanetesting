package com.gmmapowell.swimlane.eclipse.testrunner;

import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo;

public class TestCaseInfo implements TestInfo {
	private final String testname;
	private boolean isFailed;

	public TestCaseInfo(String testname) {
		this.testname = testname;
	}

	@Override
	public String testName() {
		return testname;
	}
	
	@Override
	public void failed() {
		isFailed = true;
	}
	
	@Override
	public boolean hasFailed() {
		return isFailed;
	}

	@Override
	public String toString() {
		return "TC[" + testname + "]";
	}
}
