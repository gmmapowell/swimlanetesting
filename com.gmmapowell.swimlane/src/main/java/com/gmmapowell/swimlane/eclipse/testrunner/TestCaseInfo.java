package com.gmmapowell.swimlane.eclipse.testrunner;

import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo;

public class TestCaseInfo implements TestInfo {
	private final String testname;

	public TestCaseInfo(String testname) {
		this.testname = testname;
	}

	@Override
	public String testName() {
		return testname;
	}
}
