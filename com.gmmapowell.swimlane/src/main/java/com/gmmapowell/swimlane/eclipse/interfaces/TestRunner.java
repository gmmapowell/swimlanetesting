package com.gmmapowell.swimlane.eclipse.interfaces;

public interface TestRunner {

	void runClass(TestResultReporter sink, String classpath, String... classesUnderTest);

}
