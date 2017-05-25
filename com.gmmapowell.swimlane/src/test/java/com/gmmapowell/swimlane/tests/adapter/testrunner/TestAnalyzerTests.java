package com.gmmapowell.swimlane.tests.adapter.testrunner;

import java.util.Arrays;

import org.eclipse.core.runtime.IProgressMonitor;
import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;
import com.gmmapowell.swimlane.eclipse.interfaces.Tree;
import com.gmmapowell.swimlane.eclipse.models.SimpleTree;
import com.gmmapowell.swimlane.eclipse.testrunner.TestCaseInfo;
import com.gmmapowell.swimlane.eclipse.testrunner.TestResultAnalyzer;
import com.gmmapowell.swimlane.tests.hamcrest.TestInfoMatcher;
import com.gmmapowell.swimlane.tests.hamcrest.TreeMatcher;
import com.gmmapowell.swimlane.tests.swtutil.TestBase;

public class TestAnalyzerTests extends TestBase {

	@Test
	public void testThatV3IsNotSupported() {
		TestResultReporter trr = context.mock(TestResultReporter.class);
		IProgressMonitor monitor = context.mock(IProgressMonitor.class);
		context.checking(new Expectations() {{
			allowing(monitor);
			oneOf(trr).testError("Cannot handle protocol v3");
		}});
		TestResultAnalyzer tra = new TestResultAnalyzer(monitor, trr);
		tra.push("%TESTC  2 v3");
	}

	@Test
	public void testSimpleSuccessIsReported() {
		TestResultReporter trr = context.mock(TestResultReporter.class);
		IProgressMonitor monitor = context.mock(IProgressMonitor.class);
		Tree<TestInfo> top = new SimpleTree<TestInfo>(new TestCaseInfo(TestCaseInfo.Type.META, "", "Top"));
		TestCaseInfo t1;
		{
			t1 = new TestCaseInfo(TestCaseInfo.Type.TEST, "com.gmmapowell.swimlane.sample.TestPasses", "test1");
			top.add(new SimpleTree<TestInfo>(t1));
		}
		context.checking(new Expectations() {{
			allowing(monitor).beginTask("", 1000);
			allowing(monitor).isCanceled(); will(returnValue(false));
			oneOf(trr).tree(with(TreeMatcher.of(top)));
			oneOf(trr).testSuccess(with(TestInfoMatcher.of(t1)));
		}});
		TestResultAnalyzer tra = new TestResultAnalyzer(monitor, trr);
		tra.push("%TESTC  1 v2");
		tra.push("%TSTTREE1,test1(com.gmmapowell.swimlane.sample.TestPasses),false,1");
		tra.push("%TESTS  1,test1(com.gmmapowell.swimlane.sample.TestPasses)");
		tra.push("%TESTE  1,test1(com.gmmapowell.swimlane.sample.TestPasses)");
	}

	@Test
	public void testSimpleFailureIsReported() {
		TestResultReporter trr = context.mock(TestResultReporter.class);
		IProgressMonitor monitor = context.mock(IProgressMonitor.class);
		Tree<TestInfo> top = new SimpleTree<TestInfo>(new TestCaseInfo(TestCaseInfo.Type.META, "", "Top"));
		TestCaseInfo t1;
		{
			t1 = new TestCaseInfo(TestCaseInfo.Type.TEST, "com.gmmapowell.swimlane.sample.TestFails", "fail1");
			t1.failed();
			top.add(new SimpleTree<TestInfo>(t1));
		}
		context.checking(new Expectations() {{
			allowing(monitor).beginTask("", 1000);
			allowing(monitor).isCanceled(); will(returnValue(false));
			oneOf(trr).tree(with(TreeMatcher.of(top)));
			oneOf(trr).testFailure(with(TestInfoMatcher.of(t1)));
		}});
		TestResultAnalyzer tra = new TestResultAnalyzer(monitor, trr);
		tra.push("%TESTC  1 v2");
		tra.push("%TSTTREE1,fail1(com.gmmapowell.swimlane.sample.TestFails),false,1");
		tra.push("%TESTS  1,fail1(com.gmmapowell.swimlane.sample.TestFails)");
		tra.push("%FAILED 1,fail1(com.gmmapowell.swimlane.sample.TestFails)");
		tra.push("%TESTE  1,fail1(com.gmmapowell.swimlane.sample.TestFails)");
	}

	@Test
	public void testAnErrorCaseIsReportedCorrectly() {
		TestResultReporter trr = context.mock(TestResultReporter.class);
		IProgressMonitor monitor = context.mock(IProgressMonitor.class);
		Tree<TestInfo> top = new SimpleTree<TestInfo>(new TestCaseInfo(TestCaseInfo.Type.META, "", "Top"));
		TestCaseInfo t1;
		{
			t1 = new TestCaseInfo(TestCaseInfo.Type.TEST, "com.gmmapowell.swimlane.sample.TestError", "err1");
			t1.failed();
			top.add(new SimpleTree<TestInfo>(t1));
		}
		context.checking(new Expectations() {{
			allowing(monitor).beginTask("", 1000);
			allowing(monitor).isCanceled(); will(returnValue(false));
			oneOf(trr).tree(with(TreeMatcher.of(top)));
			oneOf(trr).testFailure(with(TestInfoMatcher.of(t1)));
		}});
		TestResultAnalyzer tra = new TestResultAnalyzer(monitor, trr);
		tra.push("%TESTC  1 v2");
		tra.push("%TSTTREE1,err1(com.gmmapowell.swimlane.sample.TestError),false,1");
		tra.push("%TESTS  1,err1(com.gmmapowell.swimlane.sample.TestError)");
		tra.push("%ERROR  1,err1(com.gmmapowell.swimlane.sample.TestError)");
		tra.push("%TESTE  1,err1(com.gmmapowell.swimlane.sample.TestError)");
	}

	@Test
	public void testWeCaptureTheStackTrace() {
		TestResultReporter trr = context.mock(TestResultReporter.class);
		IProgressMonitor monitor = context.mock(IProgressMonitor.class);
		Tree<TestInfo> top = new SimpleTree<TestInfo>(new TestCaseInfo(TestCaseInfo.Type.META, "", "Top"));
		TestCaseInfo t1;
		{
			t1 = new TestCaseInfo(TestCaseInfo.Type.TEST, "com.gmmapowell.swimlane.sample.TestFails", "fail1");
			t1.failed();
			t1.stack(Arrays.asList("java.lang.Exception: ", "  frame 1", "  frame 2"));
			top.add(new SimpleTree<TestInfo>(t1));
		}
		context.checking(new Expectations() {{
			allowing(monitor).beginTask("", 1000);
			allowing(monitor).isCanceled(); will(returnValue(false));
			oneOf(trr).tree(with(TreeMatcher.of(top)));
			oneOf(trr).testFailure(with(TestInfoMatcher.of(t1)));
		}});
		TestResultAnalyzer tra = new TestResultAnalyzer(monitor, trr);
		tra.push("%TESTC  1 v2");
		tra.push("%TSTTREE1,fail1(com.gmmapowell.swimlane.sample.TestFails),false,1");
		tra.push("%TESTS  1,fail1(com.gmmapowell.swimlane.sample.TestFails)");
		tra.push("%FAILED 1,fail1(com.gmmapowell.swimlane.sample.TestFails)");
		tra.push("%FAILED 1,fail1(com.gmmapowell.swimlane.sample.TestFails)");
		tra.push("%TRACES");
		tra.push("java.lang.Exception: ");
		tra.push("  frame 1");
		tra.push("  frame 2");
		tra.push("%TRACEE");
		tra.push("%TESTE  1,fail1(com.gmmapowell.swimlane.sample.TestFails)");
	}

	@Test
	public void testWeCaptureExpectedActualText() {
		TestResultReporter trr = context.mock(TestResultReporter.class);
		IProgressMonitor monitor = context.mock(IProgressMonitor.class);
		Tree<TestInfo> top = new SimpleTree<TestInfo>(new TestCaseInfo(TestCaseInfo.Type.META, "", "Top"));
		TestCaseInfo t1;
		{
			t1 = new TestCaseInfo(TestCaseInfo.Type.TEST, "com.gmmapowell.swimlane.sample.TestFails", "fail1");
			t1.failed();
			t1.expectedValue("hello");
			t1.actualValue("goodbye");
			top.add(new SimpleTree<TestInfo>(t1));
		}
		context.checking(new Expectations() {{
			allowing(monitor).beginTask("", 1000);
			allowing(monitor).isCanceled(); will(returnValue(false));
			oneOf(trr).tree(with(TreeMatcher.of(top)));
			oneOf(trr).testFailure(with(TestInfoMatcher.of(t1)));
		}});
		TestResultAnalyzer tra = new TestResultAnalyzer(monitor, trr);
		tra.push("%TESTC  1 v2");
		tra.push("%TSTTREE1,fail1(com.gmmapowell.swimlane.sample.TestFails),false,1");
		tra.push("%TESTS  1,fail1(com.gmmapowell.swimlane.sample.TestFails)");
		tra.push("%FAILED 1,fail1(com.gmmapowell.swimlane.sample.TestFails)");
		tra.push("%FAILED 1,fail1(com.gmmapowell.swimlane.sample.TestFails)");
		tra.push("%EXPECTS");
		tra.push("hello");
		tra.push("%EXPECTE");
		tra.push("%ACTUALS");
		tra.push("goodbye");
		tra.push("%ACTUALE");
		tra.push("%TESTE  1,fail1(com.gmmapowell.swimlane.sample.TestFails)");
	}

	@Test
	public void testWeReportTheRuntime() {
		TestResultReporter trr = context.mock(TestResultReporter.class);
		IProgressMonitor monitor = context.mock(IProgressMonitor.class);
		Tree<TestInfo> top = new SimpleTree<TestInfo>(new TestCaseInfo(TestCaseInfo.Type.META, "", "Top"));
		TestCaseInfo t1;
		{
			t1 = new TestCaseInfo(TestCaseInfo.Type.TEST, "com.gmmapowell.swimlane.sample.TestSuccess", "test1");
			top.add(new SimpleTree<TestInfo>(t1));
		}
		context.checking(new Expectations() {{
			allowing(monitor).beginTask("", 1000);
			allowing(monitor).isCanceled(); will(returnValue(false));
			oneOf(trr).tree(with(TreeMatcher.of(top)));
			oneOf(trr).testSuccess(with(TestInfoMatcher.of(t1)));
			oneOf(trr).testRuntime(420);
		}});
		TestResultAnalyzer tra = new TestResultAnalyzer(monitor, trr);
		tra.push("%TESTC  1 v2");
		tra.push("%TSTTREE1,test1(com.gmmapowell.swimlane.sample.TestSuccess),false,1");
		tra.push("%TESTS  1,test1(com.gmmapowell.swimlane.sample.TestSuccess)");
		tra.push("%TESTE  1,test1(com.gmmapowell.swimlane.sample.TestSuccess)");
		tra.push("%RUNTIME420");
	}
}
