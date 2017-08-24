package com.gmmapowell.swimlane.tests.accumulator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.analyzer.BusinessRole;
import com.gmmapowell.swimlane.eclipse.analyzer.UtilityRole;
import com.gmmapowell.swimlane.eclipse.interfaces.AnalysisAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.BarDataListener;
import com.gmmapowell.swimlane.eclipse.interfaces.ErrorAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;
import com.gmmapowell.swimlane.eclipse.models.SwimlaneModel;
import com.gmmapowell.swimlane.testsupport.CaptureLayout;
import com.gmmapowell.swimlane.testsupport.DirectRunner;
import com.gmmapowell.swimlane.testsupport.matchers.BarInfoMatcher;

public class UpdatingTestGroupsIndependently {
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();
	ErrorAccumulator errors = context.mock(ErrorAccumulator.class);
	CaptureLayout capture = new CaptureLayout(context);
	SwimlaneModel acc = new SwimlaneModel(new DirectRunner(), errors, capture);
	TestResultReporter trr = (TestResultReporter) acc;
	GroupOfTests grp1 = context.mock(GroupOfTests.class, "grp1");
	GroupOfTests grp2 = context.mock(GroupOfTests.class, "grp2");
	List<String> testsForClass1 = new ArrayList<>();
	List<String> testsForClass2 = new ArrayList<>();
	List<String> testsForClass3 = new ArrayList<>();
	private BarDataListener lsnr1;
	private BarDataListener lsnr2;
	private List<String> stack;
	private List<String> expected;
	private List<String> actual;

	@SuppressWarnings("unchecked")
	@Before
	public void setup() {
		testsForClass1.add("case1");
		testsForClass1.add("case2");
		testsForClass1.add("case3");

		testsForClass2.add("case4");
		testsForClass2.add("case5");

		testsForClass3.add("case6");

		context.checking(new Expectations() {{
			allowing(grp1).addTest("TestClass1");
			allowing(grp2).addTest("TestClass2");
			allowing(grp1).addTest("TestClass3");
		}});

		AnalysisAccumulator analyzer = acc.startAnalysis(new Date());
		analyzer.haveTestClass(grp1, "TestClass1", new BusinessRole(String.class), testsForClass1);
		analyzer.haveTestClass(grp2, "TestClass2", new BusinessRole(String.class), testsForClass2);
		analyzer.haveTestClass(grp1, "TestClass3", new UtilityRole(), testsForClass3);
		analyzer.analysisComplete(new Date());

		lsnr1 = capture.hexes.get(0);
		lsnr2 = capture.utility;

		stack = context.mock(List.class, "stack");
		expected = context.mock(List.class, "expected");
		actual = context.mock(List.class, "actual");

	}

	// Start off by checking that we can run the tests back-to-back without any overwriting
	@Test
	public void weCanRunTheGroup1TestsThenGroup2() {
		runEverythingOnce();
	}

	@Test
	public void weCanRerunTheGroup1TestsWithoutCausingHarmToGroup2() {
		runEverythingOnce();

		context.checking(new Expectations() {{
			oneOf(lsnr1).barChanged(with(BarInfoMatcher.passing(2, 5)));
			oneOf(lsnr2).barChanged(with(BarInfoMatcher.passing(0, 1)));
			oneOf(lsnr1).barChanged(with(BarInfoMatcher.passing(3, 5)));
			oneOf(lsnr1).barChanged(with(BarInfoMatcher.passing(4, 5)));
			oneOf(lsnr1).barChanged(with(BarInfoMatcher.passing(5, 5)));
			oneOf(lsnr2).barChanged(with(BarInfoMatcher.passing(1, 1)));
		}});
		
		// Now go back and run grp1 without affecting grp2
		acc.testsStarted(grp1, new Date());
		acc.testSuccess(grp1, "TestClass1", "case1");
		acc.testSuccess(grp1, "TestClass1", "case2");
		acc.testSuccess(grp1, "TestClass1", "case3");
		acc.testSuccess(grp1, "TestClass3", "case6");
		acc.testsCompleted(grp1, new Date());
	}

	@Test
	public void weCanRerunTheGroup2TestsWithoutCausingHarmToGroup1() {
		runEverythingOnce();

		context.checking(new Expectations() {{
			oneOf(lsnr1).barChanged(with(BarInfoMatcher.passing(3, 5)));
			oneOf(lsnr1).barChanged(with(BarInfoMatcher.passing(4, 5)));
			oneOf(lsnr1).barChanged(with(BarInfoMatcher.passing(5, 5)));
		}});
		
		// Now go back and run grp2 without affecting grp1
		acc.testsStarted(grp2, new Date());
		acc.testSuccess(grp2, "TestClass2", "case4");
		acc.testSuccess(grp2, "TestClass2", "case5");
		acc.testsCompleted(grp2, new Date());
	}

	@Test
	public void weThatWeCanCauseTheBarToGoRed() {
		runEverythingOnce();

		context.checking(new Expectations() {{
			oneOf(lsnr1).barChanged(with(BarInfoMatcher.passing(3, 5)));
			oneOf(lsnr1).barChanged(with(BarInfoMatcher.failing(4, 5)));
			oneOf(lsnr1).barChanged(with(BarInfoMatcher.failing(5, 5)));
		}});
		
		// Now go back and run grp2 without affecting grp1
		acc.testsStarted(grp2, new Date());
		acc.testFailure(grp2, "TestClass2", "case4", stack, expected, actual);
		acc.testSuccess(grp2, "TestClass2", "case5");
		acc.testsCompleted(grp2, new Date());
	}

	@Test
	public void weThatRerunningTheFailingGroupCausesTheBarToGoBackToGreen() {
		runEverythingOnce();

		context.checking(new Expectations() {{
			oneOf(lsnr1).barChanged(with(BarInfoMatcher.passing(3, 5)));
			oneOf(lsnr1).barChanged(with(BarInfoMatcher.failing(4, 5)));
			oneOf(lsnr1).barChanged(with(BarInfoMatcher.failing(5, 5)));

			oneOf(lsnr1).barChanged(with(BarInfoMatcher.passing(3, 5)));
		}});
		
		// Now go back and run grp2 without affecting grp1
		acc.testsStarted(grp2, new Date());
		acc.testFailure(grp2, "TestClass2", "case4", stack, expected, actual);
		acc.testSuccess(grp2, "TestClass2", "case5");
		acc.testsCompleted(grp2, new Date());

		// run them again and it goes green again ...
		acc.testsStarted(grp2, new Date());
	}

	// Run all the tests one time to get to the right place ...
	private void runEverythingOnce() {
		context.checking(new Expectations() {{
			oneOf(lsnr1).barChanged(with(BarInfoMatcher.passing(0, 5)));
			oneOf(lsnr2).barChanged(with(BarInfoMatcher.passing(0, 1)));
			oneOf(lsnr1).barChanged(with(BarInfoMatcher.passing(1, 5)));
			oneOf(lsnr1).barChanged(with(BarInfoMatcher.passing(2, 5)));
			oneOf(lsnr1).barChanged(with(BarInfoMatcher.passing(3, 5)));
			
			// we repeat this, because it is sent with testCount, but nothing got changed ...
			oneOf(lsnr1).barChanged(with(BarInfoMatcher.passing(3, 5)));
			oneOf(lsnr1).barChanged(with(BarInfoMatcher.passing(4, 5)));
			oneOf(lsnr1).barChanged(with(BarInfoMatcher.passing(5, 5)));
			oneOf(lsnr2).barChanged(with(BarInfoMatcher.passing(1, 1)));
		}});
		acc.testsStarted(grp1, new Date());
		acc.testSuccess(grp1, "TestClass1", "case1");
		acc.testSuccess(grp1, "TestClass1", "case2");
		acc.testSuccess(grp1, "TestClass1", "case3");
		acc.testSuccess(grp1, "TestClass3", "case6");
		acc.testsCompleted(grp1, new Date());
		acc.testsStarted(grp2, new Date());
		acc.testSuccess(grp2, "TestClass2", "case4");
		acc.testSuccess(grp2, "TestClass2", "case5");
		acc.testsCompleted(grp2, new Date());
		
		context.assertIsSatisfied();
	}
}
