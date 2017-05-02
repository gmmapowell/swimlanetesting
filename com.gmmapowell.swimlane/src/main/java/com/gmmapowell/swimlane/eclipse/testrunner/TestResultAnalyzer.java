package com.gmmapowell.swimlane.eclipse.testrunner;

import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;

public class TestResultAnalyzer {
	private final TestResultReporter sink;

	public TestResultAnalyzer(TestResultReporter sink) {
		this.sink = sink;
	}

	public void push(String s) {
		System.out.println("Tester sent: " + s);
		if (s.startsWith("%TESTC")) {
			s = s.substring(8);
			String[] codes = s.split(" ");
			if (!"v2".equals(codes[1])) {
				sink.testError("Cannot handle protocol " + codes[1]);
				return;
			}
			sink.testCount(Integer.parseInt(codes[0]));
		}
		if (s.startsWith("%RUNTIME"))
			sink.testSuccess("com.gmmapowell.swimlane.sample.TestPasses", "testPasses");
	}

}
