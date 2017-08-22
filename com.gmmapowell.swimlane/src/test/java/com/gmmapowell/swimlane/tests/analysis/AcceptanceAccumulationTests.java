package com.gmmapowell.swimlane.tests.analysis;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
import com.gmmapowell.swimlane.eclipse.roles.AcceptanceRole;
import com.gmmapowell.swimlane.testsupport.DirectRunner;

/* The purpose of the accumulator is to take input in one form (what we discover)
 * and to build a stable model out of it.
 * To decouple these two roles, we have two interfaces: Accumulator for input and HexagonDataModel for output;
 * here we assert that the two are coupled correctly internally.
 */
public class AcceptanceAccumulationTests {
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();
	Solution solution = context.mock(Solution.class);
	ErrorAccumulator errors = context.mock(ErrorAccumulator.class);
	AnalysisAccumulator acc = new SolutionCreator(new DirectRunner(), errors, solution, new HashMap<GroupOfTests, AllConstraints>());
	Sequence seq = context.sequence("solution");
	TestGroup grp = new TestGroup("Project", null);
	Date bcd = new Date();
	Class<?> hexClass1 = Integer.class;
	Class<?> hexClass2 = List.class;
	Class<?> hexClass3 = Set.class;
	Class<?> hexClass4 = Array.class;
	String hmd = "";
	String hm1 = hexClass1.getName();
	String hm2 = hexClass2.getName();
	String hm3 = hexClass3.getName();
	String hm4 = hexClass4.getName();
	List<String> tests = new ArrayList<>();

	@Test
	public void testNoTestsMeansNoHexes() {
		context.checking(new Expectations() {{
			oneOf(solution).beginAnalysis(); inSequence(seq);
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.analysisComplete(bcd);
	}
	
	@Test
	public void testOneUnboundAcceptanceGivesOneHex() {
		context.checking(new Expectations() {{
			oneOf(solution).beginAnalysis(); inSequence(seq);
			oneOf(solution).hex(with(hmd)); inSequence(seq);
			oneOf(solution).acceptance(hmd); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase1", tests); inSequence(seq);
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AcceptanceRole(), tests);
		acc.analysisComplete(bcd);
	}
	
	@Test
	public void testOneAcceptanceWithTwoHexesGivesTwoHexesAndNoErrors() {
		context.checking(new Expectations() {{
			oneOf(solution).beginAnalysis(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).hex(with(hm2)); inSequence(seq);
			oneOf(solution).acceptance(hm1, hm2); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase1", tests); inSequence(seq);
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AcceptanceRole(hexClass1, hexClass2), tests);
		acc.analysisComplete(bcd);
	}
	
	@Test
	public void testTwoAcceptancesWithTwoHexesGivesTwoHexesButComplainsAboutNoTotalOrdering() {
		context.checking(new Expectations() {{
			oneOf(solution).beginAnalysis(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).hex(with(hm2)); inSequence(seq);
			oneOf(solution).acceptance(hm1); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase1", tests); inSequence(seq);
			oneOf(solution).acceptance(hm2); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase2", tests); inSequence(seq);
			oneOf(errors).error("there is no ordering between java.lang.Integer and java.util.List");
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AcceptanceRole(hexClass1), tests);
		acc.haveTestClass(grp, "TestCase2", new AcceptanceRole(hexClass2), tests);
		acc.analysisComplete(bcd);
	}
	
	@Test
	public void testTwoAcceptancesEachWithTwoHexesInDifferentOrdersGivesTwoHexesButComplainsAboutInconsistentOrdering() {
		context.checking(new Expectations() {{
			oneOf(solution).beginAnalysis(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).hex(with(hm2)); inSequence(seq);
			oneOf(solution).acceptance(hm1, hm2); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase1", tests); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase2", tests); inSequence(seq);
			oneOf(errors).error("ordering between java.lang.Integer and java.util.List is inconsistent");
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AcceptanceRole(hexClass1, hexClass2), tests);
		acc.haveTestClass(grp, "TestCase2", new AcceptanceRole(hexClass2, hexClass1), tests);
		acc.analysisComplete(bcd);
	}
	
	@Test
	public void testTwoAcceptancesEachWithTwoOverlappingHexesThatMakeATotalOrderingOfThreeHexes() {
		context.checking(new Expectations() {{
			oneOf(solution).beginAnalysis(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).hex(with(hm2)); inSequence(seq);
			oneOf(solution).hex(with(hm3)); inSequence(seq);
			oneOf(solution).acceptance(hm1, hm2); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase1", tests); inSequence(seq);
			oneOf(solution).acceptance(hm2, hm3); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase2", tests); inSequence(seq);
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AcceptanceRole(hexClass1, hexClass2), tests);
		acc.haveTestClass(grp, "TestCase2", new AcceptanceRole(hexClass2, hexClass3), tests);
		acc.analysisComplete(bcd);
	}

	@Test
	public void testTwoAcceptancesEachWithTwoOrderedHexesIsNotTotal() {
		context.checking(new Expectations() {{
			oneOf(solution).beginAnalysis(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).hex(with(hm2)); inSequence(seq);
			oneOf(solution).hex(with(hm3)); inSequence(seq);
			oneOf(solution).acceptance(hm1, hm2); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase1", tests); inSequence(seq);
			oneOf(solution).acceptance(hm1, hm3); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase2", tests); inSequence(seq);
			oneOf(errors).error("there is no ordering between java.util.List and java.util.Set");
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AcceptanceRole(hexClass1, hexClass2), tests);
		acc.haveTestClass(grp, "TestCase2", new AcceptanceRole(hexClass1, hexClass3), tests);
		acc.analysisComplete(bcd);
	}

	@Test
	public void testThreeAcceptancesEachWithTwoOrderedHexesIsNotConsistent() {
		context.checking(new Expectations() {{
			oneOf(solution).beginAnalysis(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).hex(with(hm2)); inSequence(seq);
			oneOf(solution).hex(with(hm3)); inSequence(seq);
			oneOf(solution).acceptance(hm1, hm2); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase1", tests); inSequence(seq);
			oneOf(solution).acceptance(hm1, hm3); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase3", tests); inSequence(seq);
			oneOf(solution).acceptance(hm2, hm3); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase2", tests); inSequence(seq);
			oneOf(errors).error("there is a cycle between java.lang.Integer and java.util.Set");
			oneOf(errors).error("there is a cycle between java.lang.Integer and java.util.List");
			oneOf(errors).error("there is a cycle between java.util.List and java.util.Set");
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AcceptanceRole(hexClass1, hexClass2), tests);
		acc.haveTestClass(grp, "TestCase2", new AcceptanceRole(hexClass2, hexClass3), tests);
		acc.haveTestClass(grp, "TestCase3", new AcceptanceRole(hexClass3, hexClass1), tests);
		acc.analysisComplete(bcd);
	}

	@Test
	public void testThreeAcceptancesEachWithTwoOverlappingHexesThatMakeATotalOrderingAfterTwoPasses() {
		context.checking(new Expectations() {{
			oneOf(solution).beginAnalysis(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).hex(with(hm2)); inSequence(seq);
			oneOf(solution).hex(with(hm3)); inSequence(seq);
			oneOf(solution).hex(with(hm4)); inSequence(seq);
			oneOf(solution).acceptance(hm1, hm2); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase1", tests); inSequence(seq);
			oneOf(solution).acceptance(hm2, hm3); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase2", tests); inSequence(seq);
			oneOf(solution).acceptance(hm3, hm4); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase3", tests); inSequence(seq);
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AcceptanceRole(hexClass1, hexClass2), tests);
		acc.haveTestClass(grp, "TestCase2", new AcceptanceRole(hexClass2, hexClass3), tests);
		acc.haveTestClass(grp, "TestCase3", new AcceptanceRole(hexClass3, hexClass4), tests);
		acc.analysisComplete(bcd);
	}
	
	@Test
	public void testThreeAcceptancesWithATotalOfThreeHexesCanBeConsistentWithOneCoveringAllThree() {
		context.checking(new Expectations() {{
			oneOf(solution).beginAnalysis(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).hex(with(hm2)); inSequence(seq);
			oneOf(solution).hex(with(hm3)); inSequence(seq);
			oneOf(solution).acceptance(hm1, hm2, hm3); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase1", tests); inSequence(seq);
			oneOf(solution).acceptance(hm1, hm2); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase3", tests); inSequence(seq);
			oneOf(solution).acceptance(hm2, hm3); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase2", tests); inSequence(seq);
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AcceptanceRole(hexClass1, hexClass2, hexClass3), tests);
		acc.haveTestClass(grp, "TestCase3", new AcceptanceRole(hexClass1, hexClass2), tests);
		acc.haveTestClass(grp, "TestCase2", new AcceptanceRole(hexClass2, hexClass3), tests);
		acc.analysisComplete(bcd);
	}
	
	@Test
	public void testThreeAcceptancesWithATotalOfThreeHexesCanBeInonsistentWithOneCoveringAllThreeIfInADifferentOrder() {
		context.checking(new Expectations() {{
			oneOf(solution).beginAnalysis(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).hex(with(hm2)); inSequence(seq);
			oneOf(solution).hex(with(hm3)); inSequence(seq);
			oneOf(solution).acceptance(hm1, hm2, hm3); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase1", tests); inSequence(seq);
			oneOf(solution).acceptance(hm1, hm2); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase3", tests); inSequence(seq);
			oneOf(errors).error("ordering between java.lang.Integer and java.util.List is inconsistent");
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AcceptanceRole(hexClass1, hexClass2, hexClass3), tests);
		acc.haveTestClass(grp, "TestCase3", new AcceptanceRole(hexClass2, hexClass1), tests);
		acc.analysisComplete(bcd);
	}
}
