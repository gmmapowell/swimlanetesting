package com.gmmapowell.swimlane.tests.adapter.testrunner;

import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.EclipseAbstractor;
import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;
import com.gmmapowell.swimlane.eclipse.interfaces.Tree;
import com.gmmapowell.swimlane.eclipse.models.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.testrunner.RemoteJUnitTestRunner;
import com.gmmapowell.swimlane.tests.swtutil.TestBase;

public class TestPhysicalRunnerInteraction extends TestBase {
	private GroupOfTests grp = context.mock(GroupOfTests.class);
	private String cp = System.getProperty("java.class.path");
	private EclipseAbstractor eclipse = context.mock(EclipseAbstractor.class);

	@SuppressWarnings("unchecked")
	@Test
	public void testThatWeCanInvokeOneClassWithJustTwoSuccessfulTests() {
		RemoteJUnitTestRunner runner = new RemoteJUnitTestRunner(eclipse);
		TestResultReporter sink = context.mock(TestResultReporter.class);
		context.checking(new Expectations() {{
			oneOf(sink).tree(with(any(Tree.class)));
			exactly(2).of(sink).testSuccess(with(any(TestInfo.class)));
			oneOf(sink).testRuntime(with(any(Integer.class)));
		}});
		runner.runClass(null, sink, grp, cp, "com.gmmapowell.swimlane.sample.TestPasses");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testThatWeCanInvokeTwoClassesWithJustThreeTotalTests() {
		RemoteJUnitTestRunner runner = new RemoteJUnitTestRunner(eclipse);
		TestResultReporter sink = context.mock(TestResultReporter.class);
		context.checking(new Expectations() {{
			oneOf(sink).tree(with(any(Tree.class)));
			exactly(2).of(sink).testSuccess(with(any(TestInfo.class)));
			exactly(2).of(sink).testFailure(with(any(TestInfo.class)));
			oneOf(sink).testRuntime(with(any(Integer.class)));
		}});
		runner.runClass(null, sink, grp, cp, "com.gmmapowell.swimlane.sample.TestPasses", "com.gmmapowell.swimlane.sample.TestFails");
	}

}
