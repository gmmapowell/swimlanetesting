package com.gmmapowell.swimlane.eclipse.testrunner;

import java.util.Arrays;
import java.util.Date;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobFunction;
import org.eclipse.core.runtime.jobs.ISchedulingRule;

import com.gmmapowell.swimlane.eclipse.interfaces.DataCentral;
import com.gmmapowell.swimlane.eclipse.interfaces.EclipseAbstractor;
import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;
import com.gmmapowell.swimlane.eclipse.interfaces.TestRunner;

// This is a centralized object for running tests and should do so off the main UI thread
// We only want one set of tests to be running at once, no matter how many have been dispatched
// We thus probably need some sort of coordination signal such as "the backlog is done"
public class RemoteJUnitTestRunner implements TestRunner {
	private final EclipseAbstractor eclipse;
	private class TestRunnerRule implements ISchedulingRule {
		@Override
		public boolean contains(ISchedulingRule rule) {
			return this == rule;
		}

		@Override
		public boolean isConflicting(ISchedulingRule rule) {
			return this == rule;
		}
	}
	
	private TestRunnerRule singleThreadedTestRunner = new TestRunnerRule();
	

	public RemoteJUnitTestRunner(EclipseAbstractor eclipse) {
		this.eclipse = eclipse;
	}

	@Override
	public void runAll(DataCentral central, TestResultReporter reporter) {
		if (reporter == null) {
			System.out.println("Cannot run tests without a reporter");
			return;
		}
		eclipse.backgroundWithProgressLocked(singleThreadedTestRunner, new IJobFunction() {
			
			@Override
			public IStatus run(IProgressMonitor monitor) {
				IStatus ret = Status.OK_STATUS;
				reporter.testsStarted(eclipse.currentDate());
				central.allGroups(g -> runGroup(monitor, g));
				reporter.testsCompleted(eclipse.currentDate());
				return ret;
			}

			private IStatus runGroup(IProgressMonitor monitor, GroupOfTests g) {
				IStatus ret;
				String classpath = g.getClassPath();
				String[] classesUnderTest = g.getClasses();
				System.out.println(new Date() + " Group " + g + " is running tests " + Arrays.asList(classesUnderTest) + " in classpath " + classpath);
				ret = Status.OK_STATUS;
				try {
					SingleRunner.exec(monitor, reporter, g, classpath, classesUnderTest);
				} catch (Exception ex) {
					ex.printStackTrace(); // how to deal with this?
					ret = Status.CANCEL_STATUS;
				}
				return ret;
			}
			
		});
	}

}
