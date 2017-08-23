package com.gmmapowell.swimlane.tests.adapter.testrunner;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.ErrorAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;
import com.gmmapowell.swimlane.eclipse.testrunner.TestDelegate;
import com.gmmapowell.swimlane.eclipse.testrunner.TestResultAnalyzer;
import com.gmmapowell.swimlane.tests.swtutil.TestBase;

public class TestAnalyzerTests extends TestBase {
	private GroupOfTests grp = context.mock(GroupOfTests.class);
	ErrorAccumulator eh = context.mock(ErrorAccumulator.class);

	@Test
	public void testThatV3IsNotSupported() {
		TestResultReporter trr = context.mock(TestResultReporter.class);
		IProgressMonitor monitor = context.mock(IProgressMonitor.class);
		context.checking(new Expectations() {{
			allowing(monitor);
			oneOf(eh).error("Cannot handle protocol v3");
		}});
		TestResultAnalyzer tra = new TestResultAnalyzer(monitor, eh, trr, grp);
		tra.push("%TESTC  2 v3");
	}

	@Test
	public void testSimpleSuccessIsReported() {
		TestResultReporter trr = context.mock(TestResultReporter.class);
		IProgressMonitor monitor = context.mock(IProgressMonitor.class);
		context.checking(new Expectations() {{
			allowing(monitor).beginTask("", 1000);
			allowing(monitor).isCanceled(); will(returnValue(false));
			oneOf(trr).testCount(grp);
			oneOf(trr).testSuccess(grp, "com.gmmapowell.swimlane.sample.TestPasses", "test1");
		}});
		TestResultAnalyzer tra = new TestResultAnalyzer(monitor, eh, trr, grp);
		tra.push("%TESTC  1 v2");
		tra.push("%TSTTREE1,test1(com.gmmapowell.swimlane.sample.TestPasses),false,1");
		tra.push("%TESTS  1,test1(com.gmmapowell.swimlane.sample.TestPasses)");
		tra.push("%TESTE  1,test1(com.gmmapowell.swimlane.sample.TestPasses)");
	}

	@Test
	public void testSimpleFailureIsReported() {
		TestResultReporter trr = context.mock(TestResultReporter.class);
		context.checking(new Expectations() {{
			oneOf(trr).testFailure(grp, "com.gmmapowell.swimlane.sample.TestFails", "fail1", null, null, null);
		}});
		TestDelegate td = new TestDelegate(grp, eh, trr);
		td.push("%FAILED 1,fail1(com.gmmapowell.swimlane.sample.TestFails)");
		td.push("%TESTE  1,fail1(com.gmmapowell.swimlane.sample.TestFails)");
	}

	@Test
	public void testAnErrorCaseIsReportedCorrectly() {
		TestResultReporter trr = context.mock(TestResultReporter.class);
		context.checking(new Expectations() {{
			oneOf(trr).testError(grp, "com.gmmapowell.swimlane.sample.TestError", "err1", null);
		}});
		TestDelegate td = new TestDelegate(grp, eh, trr);
		td.push("%ERROR  1,err1(com.gmmapowell.swimlane.sample.TestError)");
		td.push("%TESTE  1,err1(com.gmmapowell.swimlane.sample.TestError)");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testWeCaptureTheStackTrace() {
		TestResultReporter trr = context.mock(TestResultReporter.class);
		context.checking(new Expectations() {{
			oneOf(trr).testFailure(with(grp), with("com.gmmapowell.swimlane.sample.TestFails"), with("fail1"), with(any(List.class)), with(aNull(List.class)), with(aNull(List.class)));
		}});
		TestDelegate td = new TestDelegate(grp, eh, trr);
		td.push("%FAILED 1,fail1(com.gmmapowell.swimlane.sample.TestFails)");
		td.push("%TRACES");
		td.push("java.lang.Exception: ");
		td.push("  frame 1");
		td.push("  frame 2");
		td.push("%TRACEE");
		td.push("%TESTE  1,fail1(com.gmmapowell.swimlane.sample.TestFails)");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testWeCaptureExpectedActualText() {
		TestResultReporter trr = context.mock(TestResultReporter.class);
		context.checking(new Expectations() {{
			oneOf(trr).testFailure(with(grp), with("com.gmmapowell.swimlane.sample.TestFails"), with("fail1"), with(aNull(List.class)), with(any(List.class)), with(any(List.class)));
		}});
		TestDelegate td = new TestDelegate(grp, eh, trr);
		td.push("%FAILED 1,fail1(com.gmmapowell.swimlane.sample.TestFails)");
		td.push("%FAILED 1,fail1(com.gmmapowell.swimlane.sample.TestFails)");
		td.push("%EXPECTS");
		td.push("hello");
		td.push("%EXPECTE");
		td.push("%ACTUALS");
		td.push("goodbye");
		td.push("%ACTUALE");
		td.push("%TESTE  1,fail1(com.gmmapowell.swimlane.sample.TestFails)");
	}

}
