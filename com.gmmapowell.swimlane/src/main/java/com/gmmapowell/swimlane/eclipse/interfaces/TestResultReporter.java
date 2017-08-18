package com.gmmapowell.swimlane.eclipse.interfaces;

import java.util.Date;
import java.util.List;

// Possibly not the best name, because this is the guy that *is* notified,
// not the one doing the reporting
public interface TestResultReporter {
	void testsStarted(GroupOfTests grp, Date currentDate);
	void testCount(GroupOfTests grp, int cnt);
	void testSuccess(GroupOfTests grp, String testClz, String testFn);
	void testFailure(GroupOfTests grp, String testClz, String testFn, List<String> stack, List<String> expected, List<String> actual);
	void testError(GroupOfTests grp, String testClz, String testFn, List<String> stack);
	void testsCompleted(GroupOfTests grp, Date currentDate);
}
