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

import com.gmmapowell.swimlane.eclipse.interfaces.AnalysisAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.ErrorAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.Solution;
import com.gmmapowell.swimlane.eclipse.models.SolutionCreator;
import com.gmmapowell.swimlane.eclipse.models.SolutionCreator.AllConstraints;
import com.gmmapowell.swimlane.eclipse.models.TestGroup;
import com.gmmapowell.swimlane.eclipse.roles.UnlabelledTestRole;
import com.gmmapowell.swimlane.testsupport.DirectRunner;

public class UnlabelledAccumulationTests {
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();
	Solution solution = context.mock(Solution.class);
	ErrorAccumulator errors = context.mock(ErrorAccumulator.class);
	AnalysisAccumulator acc = new SolutionCreator(new DirectRunner(), errors, solution, new HashMap<GroupOfTests, AllConstraints>());
	Date bcd = new Date();
	TestGroup grp = new TestGroup("Project", null);
	Class<?> hexClass1 = Integer.class;
	Class<?> hexClass2 = List.class;
	String hmd = "";
	String hm1 = hexClass1.getName();
	String hm2 = hexClass2.getName();
	List<String> tests = new ArrayList<>();
	Sequence seq = context.sequence("solution");

	@Test
	public void itIsAnErrorToNotLabelATest() {
		context.checking(new Expectations() {{
			oneOf(errors).error("TestCase1 has @Test annotations but no swimlane annotations"); inSequence(seq);
			oneOf(solution).beginAnalysis(); inSequence(seq);
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new UnlabelledTestRole(), tests);
		acc.analysisComplete(bcd);
	}

}
