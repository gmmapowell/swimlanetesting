package com.gmmapowell.swimlane.eclipse.testrunner;

import java.util.Arrays;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobFunction;

import com.gmmapowell.swimlane.eclipse.interfaces.EclipseAbstractor;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;
import com.gmmapowell.swimlane.eclipse.interfaces.TestRunner;

// This is a centralized object for running tests and should do so off the main UI thread
// We only want one set of tests to be running at once, no matter how many have been dispatched
// We thus probably need some sort of coordination signal such as "the backlog is done"
public class RemoteJUnitTestRunner implements TestRunner {
	private final EclipseAbstractor eclipse;

	public RemoteJUnitTestRunner(EclipseAbstractor eclipse) {
		this.eclipse = eclipse;
	}

	@Override
	public void runClass(TestResultReporter sink, String classpath, String... classesUnderTest) {
		System.out.println("Running tests " + Arrays.asList(classesUnderTest) + " in classpath " + classpath);
		eclipse.backgroundWithProgress(new IJobFunction() {
			
			@Override
			public IStatus run(IProgressMonitor monitor) {
				SingleRunner runner = null;
				try {
					runner = new SingleRunner(monitor, sink, classpath, classesUnderTest);
					runner.exec();
					return Status.OK_STATUS;
				} catch (Exception ex) {
					ex.printStackTrace(); // how to deal with this?
					return Status.CANCEL_STATUS;
				}
			}
			
		});
	}

}
