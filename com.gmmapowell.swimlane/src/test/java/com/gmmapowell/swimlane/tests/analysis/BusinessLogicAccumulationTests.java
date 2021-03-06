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

import com.gmmapowell.swimlane.eclipse.analyzer.BusinessRole;
import com.gmmapowell.swimlane.eclipse.interfaces.AnalysisAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.ErrorAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.Solution;
import com.gmmapowell.swimlane.eclipse.models.SolutionCreator;
import com.gmmapowell.swimlane.eclipse.models.SolutionCreator.AllConstraints;
import com.gmmapowell.swimlane.eclipse.models.TestGroup;
import com.gmmapowell.swimlane.eclipse.roles.AcceptanceRole;
import com.gmmapowell.swimlane.testsupport.DirectRunner;

public class BusinessLogicAccumulationTests {
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
	public void testThatIfWeAccumulateOneLogicTestTheModelMustHaveTheHexagonForIt() {
		context.checking(new Expectations() {{
			oneOf(solution).beginAnalysis(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase1", tests); inSequence(seq);
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new BusinessRole(hexClass1), tests);
		acc.analysisComplete(bcd);
	}

	@Test
	public void testWeCanAssociateTestsWithTheDefaultHexagon() {
		context.checking(new Expectations() {{
			oneOf(solution).beginAnalysis(); inSequence(seq);
			oneOf(solution).hex(with(hmd)); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase1", tests); inSequence(seq);
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new BusinessRole(null), tests);
		acc.analysisComplete(bcd);
	}

	@Test
	public void testWeCanAssociateTestsWithTheDefaultHexagonAndNameIt() {
		context.checking(new Expectations() {{
			oneOf(solution).beginAnalysis(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase1", tests); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase2", tests); inSequence(seq);
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new BusinessRole(null), tests);
		acc.haveTestClass(grp, "TestCase2", new BusinessRole(hexClass1), tests);
		acc.analysisComplete(bcd);
	}

	@Test
	public void testWeCannotAssociateTestsWithTheDefaultHexagonAndAlsoHaveASecondHexagon() {
		context.checking(new Expectations() {{
			oneOf(solution).beginAnalysis(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase1", tests); inSequence(seq);
			oneOf(solution).hex(with(hm2)); inSequence(seq);
			oneOf(solution).acceptance(hm1, hm2);
			oneOf(solution).testClass(grp, "TestCase2", tests); inSequence(seq);
			oneOf(errors).error("cannot use @BusinessLogic with default hexagon in TestCase1 since there are multiple hexagons");
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new BusinessRole(null), tests);
		acc.haveTestClass(grp, "TestCase2", new AcceptanceRole(hexClass1, hexClass2), tests);
		acc.analysisComplete(bcd);
	}

	/* All of this relates to non-existent concepts in the modern era
	@Test
	public void testThatIfWeDontAccumulateAnyLogicTestsThereIsNoBar() {
		acc.adapter(grp, testCase1, hexClass1, Long.class, Exception.class);
		acc.analysisComplete();
		assertNull(hdm.getHexagons().get(0).getBar());
	}

	@Test
	public void testThatIfWeAccumulateAnyLogicTestsThereWillBeABar() {
		acc.logic(grp, testCase1, hexClass1);
		acc.analysisComplete();
		assertNotNull(hdm.getHexagons().get(0).getBar());
	}

	@Test
	public void testTheBarHasTheRightName() {
		acc.logic(grp, testCase1, hexClass1);
		acc.analysisComplete();
		assertEquals(hexClass1.getName(), hdm.getHexagons().get(0).getBar().getName());
	}

	@Test
	public void testThatOneTestMakesOneEntryInTheBar() {
		acc.logic(grp, testCase1, hexClass1);
		acc.analysisComplete();
		assertEquals(1, hdm.getHexagons().get(0).getBar().classesUnderTest().size());
	}

	@Test
	public void testThatTwoTestsMakeTwoEntriesInTheBar() {
		acc.logic(grp, testCase1, hexClass1);
		acc.logic(grp, testCase2, hexClass1);
		acc.analysisComplete();
		assertEquals(2, hdm.getHexagons().get(0).getBar().classesUnderTest().size());
	}

	@Test
	public void testThatTwoTestsInSeparateHexesAreKeptDistinct() {
		acc.logic(grp, testCase1, hexClass1);
		acc.logic(grp, testCase2, hexClass2);
		acc.analysisComplete();
		assertEquals(1, hdm.getHexagons().get(0).getBar().classesUnderTest().size());
		assertEquals(1, hdm.getHexagons().get(1).getBar().classesUnderTest().size());
	}
	*/
}
