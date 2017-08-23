package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.List;

import com.gmmapowell.swimlane.eclipse.testrunner.TestCaseInfo;

public interface UpdateBar {
	// TODO: should there be a "clear all record of this" action?
	// Statically declare that a class exists
	void testClass(GroupOfTests grp, String clzName, List<String> tests);
	// Clear out a group of test results (but the static definitions remain)
	void clearGroup(GroupOfTests grp);
	// Result of running one test
	void testCompleted(TestCaseInfo testCaseInfo);
}
