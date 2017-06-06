package com.gmmapowell.swimlane.eclipse.interfaces;

public interface SingleStore {

	Accumulator getAccumulator();
	ModelDispatcher getDispatcher();
	TestRunner getTestRunner();

	void showHexPane();
	void showTestResults(String id);
	void showErrorPane();
}
