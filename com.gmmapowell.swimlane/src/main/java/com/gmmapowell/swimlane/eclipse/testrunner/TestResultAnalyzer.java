package com.gmmapowell.swimlane.eclipse.testrunner;

import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;

public class TestResultAnalyzer {
	private final TestResultReporter sink;

	public TestResultAnalyzer(TestResultReporter sink) {
		this.sink = sink;
		// TODO Auto-generated constructor stub
	}

	public void push(String s) {
		System.out.println("Tester sent: " + s);
		if (s.startsWith("%TESTC")) {
			s = s.substring(8);
			sink.testCount(Integer.parseInt(s.split(" ")[0]));
		}
		if (s.startsWith("%RUNTIME"))
			sink.testSuccess("com.gmmapowell.swimlane.sample.TestPasses", "testPasses");
	}

}
