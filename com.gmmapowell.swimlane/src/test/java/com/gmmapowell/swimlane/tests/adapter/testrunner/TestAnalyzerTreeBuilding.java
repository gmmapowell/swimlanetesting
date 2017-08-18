package com.gmmapowell.swimlane.tests.adapter.testrunner;

import org.eclipse.core.runtime.IProgressMonitor;
import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;
import com.gmmapowell.swimlane.eclipse.interfaces.Tree;
import com.gmmapowell.swimlane.eclipse.models.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.models.SimpleTree;
import com.gmmapowell.swimlane.eclipse.testrunner.TestCaseInfo;
import com.gmmapowell.swimlane.eclipse.testrunner.TestResultAnalyzer;
import com.gmmapowell.swimlane.tests.hamcrest.TreeMatcher;
import com.gmmapowell.swimlane.tests.swtutil.TestBase;

public class TestAnalyzerTreeBuilding extends TestBase {
	private GroupOfTests grp = context.mock(GroupOfTests.class);

	@Test
	public void testThatASimpleTestIsATree() {
		TestResultReporter trr = context.mock(TestResultReporter.class);
		IProgressMonitor monitor = context.mock(IProgressMonitor.class);
		Tree<TestInfo> top = new SimpleTree<TestInfo>(new TestCaseInfo(TestCaseInfo.Type.META, grp, "", "Top"));
		TestInfo me = new TestCaseInfo(TestCaseInfo.Type.TEST, grp, "com.gmmapowell.swimlane.sample.TestPasses", "test1");
		Tree<TestInfo> tree = new SimpleTree<TestInfo>(me);
		top.add(tree);
		context.checking(new Expectations() {{
			allowing(monitor);
			oneOf(trr).tree(with(TreeMatcher.of(top)));
		}});
		TestResultAnalyzer tra = new TestResultAnalyzer(monitor, trr, grp);
		tra.push("%TESTC  1 v2");
		tra.push("%TSTTREE1,test1(com.gmmapowell.swimlane.sample.TestPasses),false,1");
	}

	@Test
	public void testThatASimpleTestInAClassIsATree() {
		TestResultReporter trr = context.mock(TestResultReporter.class);
		IProgressMonitor monitor = context.mock(IProgressMonitor.class);
		Tree<TestInfo> top = new SimpleTree<TestInfo>(new TestCaseInfo(TestCaseInfo.Type.META, grp, "", "Top"));
		TestInfo sme = new TestCaseInfo(TestCaseInfo.Type.SUITE, grp, "com.gmmapowell.swimlane.sample.TestPasses", "com.gmmapowell.swimlane.sample.TestPasses");
		Tree<TestInfo> suite = new SimpleTree<TestInfo>(sme);
		top.add(suite);
		TestInfo me = new TestCaseInfo(TestCaseInfo.Type.TEST, grp, "com.gmmapowell.swimlane.sample.TestPasses", "test1");
		Tree<TestInfo> tc = new SimpleTree<TestInfo>(me);
		suite.add(tc);
		context.checking(new Expectations() {{
			allowing(monitor);
			oneOf(trr).tree(with(TreeMatcher.of(top)));
		}});
		TestResultAnalyzer tra = new TestResultAnalyzer(monitor, trr, grp);
		tra.push("%TESTC  1 v2");
		tra.push("%TSTTREE1,com.gmmapowell.swimlane.sample.TestPasses,true,1");
		tra.push("%TSTTREE2,test1(com.gmmapowell.swimlane.sample.TestPasses),false,1");
	}

	@Test
	public void testThatTwoTestsInAClassMakesATree() {
		TestResultReporter trr = context.mock(TestResultReporter.class);
		IProgressMonitor monitor = context.mock(IProgressMonitor.class);
		Tree<TestInfo> top = new SimpleTree<TestInfo>(new TestCaseInfo(TestCaseInfo.Type.META, grp, "", "Top"));
		TestInfo sme = new TestCaseInfo(TestCaseInfo.Type.SUITE, grp, "com.gmmapowell.swimlane.sample.TestPasses", "com.gmmapowell.swimlane.sample.TestPasses");
		Tree<TestInfo> suite = new SimpleTree<TestInfo>(sme);
		top.add(suite);
		suite.add(new SimpleTree<TestInfo>(new TestCaseInfo(TestCaseInfo.Type.TEST, grp, "com.gmmapowell.swimlane.sample.TestPasses", "test1")));
		suite.add(new SimpleTree<TestInfo>(new TestCaseInfo(TestCaseInfo.Type.TEST, grp, "com.gmmapowell.swimlane.sample.TestPasses", "test2")));
		context.checking(new Expectations() {{
			allowing(monitor);
			oneOf(trr).tree(with(TreeMatcher.of(top)));
		}});
		TestResultAnalyzer tra = new TestResultAnalyzer(monitor, trr, grp);
		tra.push("%TESTC  2 v2");
		tra.push("%TSTTREE1,com.gmmapowell.swimlane.sample.TestPasses,true,2");
		tra.push("%TSTTREE2,test1(com.gmmapowell.swimlane.sample.TestPasses),false,1");
		tra.push("%TSTTREE2,test2(com.gmmapowell.swimlane.sample.TestPasses),false,1");
	}

	@Test
	public void testThatFiveTestsAcrossTwoClassesMakeATree() {
		TestResultReporter trr = context.mock(TestResultReporter.class);
		IProgressMonitor monitor = context.mock(IProgressMonitor.class);
		Tree<TestInfo> top = new SimpleTree<TestInfo>(new TestCaseInfo(TestCaseInfo.Type.META, grp, "", "Top"));
		{
			TestInfo sme = new TestCaseInfo(TestCaseInfo.Type.SUITE, grp, "com.gmmapowell.swimlane.sample.TestPasses", "com.gmmapowell.swimlane.sample.TestPasses");
			Tree<TestInfo> suite = new SimpleTree<TestInfo>(sme);
			top.add(suite);
			suite.add(new SimpleTree<TestInfo>(new TestCaseInfo(TestCaseInfo.Type.TEST, grp, "com.gmmapowell.swimlane.sample.TestPasses", "test1")));
			suite.add(new SimpleTree<TestInfo>(new TestCaseInfo(TestCaseInfo.Type.TEST, grp, "com.gmmapowell.swimlane.sample.TestPasses", "test2")));
		}
		{
			TestInfo sme = new TestCaseInfo(TestCaseInfo.Type.SUITE, grp, "com.gmmapowell.swimlane.sample.TestFails", "com.gmmapowell.swimlane.sample.TestFails");
			Tree<TestInfo> suite = new SimpleTree<TestInfo>(sme);
			top.add(suite);
			suite.add(new SimpleTree<TestInfo>(new TestCaseInfo(TestCaseInfo.Type.TEST, grp, "com.gmmapowell.swimlane.sample.TestFails", "fail1")));
			suite.add(new SimpleTree<TestInfo>(new TestCaseInfo(TestCaseInfo.Type.TEST, grp, "com.gmmapowell.swimlane.sample.TestFails", "fail2")));
		}
		context.checking(new Expectations() {{
			allowing(monitor);
			oneOf(trr).tree(with(TreeMatcher.of(top)));
		}});
		TestResultAnalyzer tra = new TestResultAnalyzer(monitor, trr, grp);
		tra.push("%TESTC  4 v2");
		tra.push("%TSTTREE1,com.gmmapowell.swimlane.sample.TestPasses,true,2");
		tra.push("%TSTTREE2,test1(com.gmmapowell.swimlane.sample.TestPasses),false,1");
		tra.push("%TSTTREE3,test2(com.gmmapowell.swimlane.sample.TestPasses),false,1");
		tra.push("%TSTTREE4,com.gmmapowell.swimlane.sample.TestFails,true,2");
		tra.push("%TSTTREE5,fail1(com.gmmapowell.swimlane.sample.TestFails),false,1");
		tra.push("%TSTTREE6,fail2(com.gmmapowell.swimlane.sample.TestFails),false,1");
	}
}
