package com.gmmapowell.swimlane.eclipse.interfaces;

// Possibly not the best name, because this is the guy that *is* notified,
// not the one doing the reporting
public interface TestResultReporter {
	void tree(Tree<TestInfo> tree);
	void testSuccess(String testClass, String testFunction);
	void testError(String msg);
}
