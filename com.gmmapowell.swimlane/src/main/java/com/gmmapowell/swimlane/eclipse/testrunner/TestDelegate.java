package com.gmmapowell.swimlane.eclipse.testrunner;

import java.util.ArrayList;
import java.util.List;

import com.gmmapowell.swimlane.eclipse.interfaces.ErrorAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;

public class TestDelegate {
	private final ErrorAccumulator eh;
	private final TestResultReporter sink;
	private final GroupOfTests grp;
	private boolean failed, error;
	private List<String> stack = null;
	private List<String> expected = null;
	private List<String> actual = null;
	private List<String> capture;

	public TestDelegate(GroupOfTests grp, ErrorAccumulator eh, TestResultReporter sink) {
		this.grp = grp;
		this.eh = eh;
		this.sink = sink;
	}

	public void push(String s) {
		if (s.startsWith("%FAILED")) {
			failed = true;
		} else if (s.startsWith("%ERROR")) {
			error = true;
		} else if (s.startsWith("%TRACES")) {
			capture = stack = new ArrayList<>();
		} else if (s.startsWith("%ACTUALS")) {
			capture = actual = new ArrayList<>();
		} else if (s.startsWith("%EXPECTS")) {
			capture = expected = new ArrayList<>();
		} else if (s.startsWith("%TRACEE") || s.startsWith("%ACTUALE") || s.startsWith("%EXPECTE")) {
			capture = null;
		} else if (s.startsWith("%TESTE")) {
			s = s.substring(8);
			String[] parts = s.split(",");
			String name = parts[1];
			if (error)
				sink.testError(grp, extractClassName(name), extractTestName(name), stack);
			else if (failed)
				sink.testFailure(grp, extractClassName(name), extractTestName(name), stack, expected, actual);
			else
				sink.testSuccess(grp, extractClassName(name), extractTestName(name));
		} else if (capture != null) {
			capture.add(s);
		} else
			eh.error("Encountered unexpected msg from test runner: " + s);
		
	}

	private String extractTestName(String name) {
		int idx = name.indexOf("(");
		if (idx == -1)
			return name;
		else
			return name.substring(0, idx);
	}

	private String extractClassName(String name) {
		int idx = name.indexOf("(");
		int idx2 = name.indexOf(")");
		if (idx == -1)
			return name;
		else
			return name.substring(idx+1, idx2);
	}
}
