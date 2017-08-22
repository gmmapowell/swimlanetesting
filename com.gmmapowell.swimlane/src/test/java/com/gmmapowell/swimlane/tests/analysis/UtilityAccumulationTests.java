package com.gmmapowell.swimlane.tests.analysis;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.analyzer.UtilityRole;
import com.gmmapowell.swimlane.eclipse.interfaces.AnalysisAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.ErrorAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.Solution;
import com.gmmapowell.swimlane.eclipse.models.SolutionCreator;
import com.gmmapowell.swimlane.eclipse.models.TestGroup;
import com.gmmapowell.swimlane.eclipse.models.SolutionCreator.AllConstraints;
import com.gmmapowell.swimlane.testsupport.DirectRunner;

public class UtilityAccumulationTests {
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();
	Solution solution = context.mock(Solution.class);
	ErrorAccumulator errors = context.mock(ErrorAccumulator.class);
	AnalysisAccumulator acc = new SolutionCreator(new DirectRunner(), errors, solution, new HashMap<GroupOfTests, AllConstraints>());
	Date bcd = new Date();
	TestGroup grp = new TestGroup("Project", null);
	Sequence seq = context.sequence("solution");
	List<String> tests = new ArrayList<>();

	@Test
	public void testThatWeCanStoreAndRecoverATest() {
		context.checking(new Expectations() {{
			oneOf(solution).beginAnalysis(); inSequence(seq);
			oneOf(solution).needsUtilityBar(); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase1", tests); inSequence(seq);
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new UtilityRole(), tests);
		acc.analysisComplete(bcd);
	}

	@Test
	public void testThatWeCanStoreAndRecoverMultipleTest() {
		context.checking(new Expectations() {{
			oneOf(solution).beginAnalysis(); inSequence(seq);
			oneOf(solution).needsUtilityBar(); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase1", tests); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase2", tests); inSequence(seq);
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new UtilityRole(), tests);
		acc.haveTestClass(grp, "TestCase2", new UtilityRole(), tests);
		acc.analysisComplete(bcd);
	}
}
