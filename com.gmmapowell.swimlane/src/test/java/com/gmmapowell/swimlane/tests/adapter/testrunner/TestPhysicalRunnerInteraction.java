package com.gmmapowell.swimlane.tests.adapter.testrunner;

import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;
import com.gmmapowell.swimlane.eclipse.interfaces.Tree;
import com.gmmapowell.swimlane.eclipse.testrunner.RemoteJUnitTestRunner;
import com.gmmapowell.swimlane.tests.swtutil.TestBase;

public class TestPhysicalRunnerInteraction extends TestBase {
	private String cp = System.getProperty("java.class.path");

	@SuppressWarnings("unchecked")
	@Test
	public void testThatWeCanInvokeOneClassWithJustTwoSuccessfulTests() {
		RemoteJUnitTestRunner runner = new RemoteJUnitTestRunner();
		TestResultReporter sink = context.mock(TestResultReporter.class);
		context.checking(new Expectations() {{
			oneOf(sink).tree(with(any(Tree.class)));
			oneOf(sink).testSuccess("com.gmmapowell.swimlane.sample.TestPasses", "testPasses");
		}});
		runner.runClass(sink, cp, "com.gmmapowell.swimlane.sample.TestPasses");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testThatWeCanInvokeTwoClassesWithJustThreeTotalTests() {
		RemoteJUnitTestRunner runner = new RemoteJUnitTestRunner();
		TestResultReporter sink = context.mock(TestResultReporter.class);
		context.checking(new Expectations() {{
			oneOf(sink).tree(with(any(Tree.class)));
			oneOf(sink).testSuccess("com.gmmapowell.swimlane.sample.TestPasses", "testPasses");
		}});
		runner.runClass(sink, cp, "com.gmmapowell.swimlane.sample.TestPasses", "com.gmmapowell.swimlane.sample.TestFails");
	}

}
