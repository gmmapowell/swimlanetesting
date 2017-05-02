package com.gmmapowell.swimlane.eclipse.testrunner;

import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;
import com.gmmapowell.swimlane.eclipse.models.SimpleTree;

public class TestResultAnalyzer {
	private final TestResultReporter sink;
	private int totalTests;

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
			totalTests = Integer.parseInt(codes[0]);
		} else if (s.startsWith("%TSTTREE")) {
			s = s.substring(8);
			String[] parts = s.split(",");
			if (--totalTests == 0)
				sink.tree(new SimpleTree<TestInfo>(new TestCaseInfo(parts[1])));
		}
		if (s.startsWith("%RUNTIME"))
			sink.testSuccess("com.gmmapowell.swimlane.sample.TestPasses", "testPasses");
	}

}
