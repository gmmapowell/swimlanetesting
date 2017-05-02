package com.gmmapowell.swimlane.eclipse.testrunner;

import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;
import com.gmmapowell.swimlane.eclipse.interfaces.TestRunner;

// This is a centralized object for running tests and should do so off the main UI thread
// We only want one set of tests to be running at once, no matter how many have been dispatched
// We thus probably need some sort of coordination signal such as "the backlog is done"
public class RemoteJUnitTestRunner implements TestRunner {

	@Override
	public void runClass(TestResultReporter sink, String classpath, String... classesUnderTest) {
		// TODO: put this on a queue for later execution and use an IProgressMonitor to track it ...
		SingleRunner runner = null;
		try {
			runner = new SingleRunner(this, sink, classpath, classesUnderTest);
			runner.exec();
		} catch (Exception ex) {
			ex.printStackTrace(); // how to deal with this?
		}
	}

}
