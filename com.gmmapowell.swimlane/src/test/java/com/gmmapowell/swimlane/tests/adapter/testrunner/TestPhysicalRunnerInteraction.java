package com.gmmapowell.swimlane.tests.adapter.testrunner;

import java.util.List;

import org.hamcrest.Matchers;
import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.ErrorAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;
import com.gmmapowell.swimlane.eclipse.testrunner.SingleRunner;
import com.gmmapowell.swimlane.tests.swtutil.TestBase;

public class TestPhysicalRunnerInteraction extends TestBase {
	ErrorAccumulator eh = context.mock(ErrorAccumulator.class);
	private GroupOfTests grp = context.mock(GroupOfTests.class);
	private String cp = System.getProperty("java.class.path");

	@Test
	public void testThatWeCanInvokeOneClassWithJustTwoSuccessfulTests() throws Exception {
		TestResultReporter sink = context.mock(TestResultReporter.class);
		context.checking(new Expectations() {{
			oneOf(sink).testCount(grp, 2);
			oneOf(sink).testSuccess(grp, "com.gmmapowell.swimlane.sample.TestPasses", "testPasses");
			oneOf(sink).testSuccess(grp, "com.gmmapowell.swimlane.sample.TestPasses", "test2");
//			oneOf(sink).testRuntime(with(any(Integer.class)));
		}});
		SingleRunner.exec(null, eh, sink, grp, cp, "com.gmmapowell.swimlane.sample.TestPasses");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testThatWeCanInvokeTwoClassesWithJustThreeTotalTests() throws Exception {
		TestResultReporter sink = context.mock(TestResultReporter.class);
		context.checking(new Expectations() {{
			oneOf(sink).testCount(grp, 4);
			oneOf(sink).testSuccess(grp, "com.gmmapowell.swimlane.sample.TestPasses", "testPasses");
			oneOf(sink).testSuccess(grp, "com.gmmapowell.swimlane.sample.TestPasses", "test2");
			oneOf(sink).testFailure(with(grp), with("com.gmmapowell.swimlane.sample.TestFails"), with("fail1"), with(aNonNull(List.class)), with(aNull(List.class)), with(aNull(List.class)));
			oneOf(sink).testFailure(with(grp), with("com.gmmapowell.swimlane.sample.TestFails"), with("fail2"), with(aNonNull(List.class)), (List<String>) with(Matchers.contains("hello")), (List<String>) with(Matchers.contains("goodbye")));
//			oneOf(sink).testRuntime(with(any(Integer.class)));
		}});
		SingleRunner.exec(null, eh, sink, grp, cp, "com.gmmapowell.swimlane.sample.TestPasses", "com.gmmapowell.swimlane.sample.TestFails");
	}

}
