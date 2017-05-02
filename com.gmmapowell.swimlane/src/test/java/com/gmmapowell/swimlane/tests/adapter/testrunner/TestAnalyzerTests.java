package com.gmmapowell.swimlane.tests.adapter.testrunner;

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
		TestResultAnalyzer tra = new TestResultAnalyzer(trr);
		context.checking(new Expectations() {{
			oneOf(trr).testError("Cannot handle protocol v3");
		}});
		tra.push("%TESTC  2 v3");
	}

	@Test
	public void testSimpleSuccessIsReported() {
		TestResultReporter trr = context.mock(TestResultReporter.class);
		TestResultAnalyzer tra = new TestResultAnalyzer(trr);
		Tree<TestInfo> top = new SimpleTree<TestInfo>(new TestCaseInfo("Top"));
		TestCaseInfo t1;
		{
			t1 = new TestCaseInfo("test1");
			top.add(new SimpleTree<TestInfo>(t1));
		}
		context.checking(new Expectations() {{
			oneOf(trr).tree(with(TreeMatcher.of(top)));
			oneOf(trr).testSuccess(with(TestInfoMatcher.of(t1)));
		}});
		tra.push("%TESTC  1 v2");
		tra.push("%TSTTREE1,test1(com.gmmapowell.swimlane.sample.TestPasses),false,1");
		tra.push("%TESTS  1,test1(com.gmmapowell.swimlane.sample.TestPasses)");
		tra.push("%TESTE  1,test1(com.gmmapowell.swimlane.sample.TestPasses)");
	}

	@Test
	public void testSimpleFailureIsReported() {
		TestResultReporter trr = context.mock(TestResultReporter.class);
		TestResultAnalyzer tra = new TestResultAnalyzer(trr);
		Tree<TestInfo> top = new SimpleTree<TestInfo>(new TestCaseInfo("Top"));
		TestCaseInfo t1;
		{
			t1 = new TestCaseInfo("fail1");
			t1.failed();
			top.add(new SimpleTree<TestInfo>(t1));
		}
		context.checking(new Expectations() {{
			oneOf(trr).tree(with(TreeMatcher.of(top)));
			oneOf(trr).testFailure(with(TestInfoMatcher.of(t1)));
		}});
		tra.push("%TESTC  1 v2");
		tra.push("%TSTTREE1,fail1(com.gmmapowell.swimlane.sample.TestFails),false,1");
		tra.push("%TESTS  1,fail1(com.gmmapowell.swimlane.sample.TestFails)");
		tra.push("%FAILED 1,fail1(com.gmmapowell.swimlane.sample.TestFails)");
		tra.push("%TESTE  1,fail1(com.gmmapowell.swimlane.sample.TestFails)");
	}
}
