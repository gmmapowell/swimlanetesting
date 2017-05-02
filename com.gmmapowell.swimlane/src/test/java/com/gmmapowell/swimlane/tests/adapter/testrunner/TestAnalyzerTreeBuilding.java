package com.gmmapowell.swimlane.tests.adapter.testrunner;

import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;
import com.gmmapowell.swimlane.eclipse.interfaces.Tree;
import com.gmmapowell.swimlane.eclipse.models.SimpleTree;
import com.gmmapowell.swimlane.eclipse.testrunner.TestCaseInfo;
import com.gmmapowell.swimlane.eclipse.testrunner.TestResultAnalyzer;
import com.gmmapowell.swimlane.tests.hamcrest.TreeMatcher;
import com.gmmapowell.swimlane.tests.swtutil.TestBase;

public class TestAnalyzerTreeBuilding extends TestBase {

	@Test
	public void testThatASimpleTestIsATree() {
		TestResultReporter trr = context.mock(TestResultReporter.class);
		TestResultAnalyzer tra = new TestResultAnalyzer(trr);
		TestInfo me = new TestCaseInfo("test2(com.gmmapowell.swimlane.sample.TestPasses)");
		Tree<TestInfo> tree = new SimpleTree<TestInfo>(me);
		context.checking(new Expectations() {{
			oneOf(trr).tree(with(TreeMatcher.of(tree)));
		}});
		tra.push("%TESTC  1 v2");
		tra.push("%TSTTREE1,test2(com.gmmapowell.swimlane.sample.TestPasses),false,1");
	}

}
