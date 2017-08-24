package com.gmmapowell.swimlane.tests.analysis;

import java.io.IOException;
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
import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;
import com.gmmapowell.swimlane.eclipse.interfaces.Solution;
import com.gmmapowell.swimlane.eclipse.models.SolutionCreator;
import com.gmmapowell.swimlane.eclipse.models.SolutionCreator.AllConstraints;
import com.gmmapowell.swimlane.eclipse.models.TestGroup;
import com.gmmapowell.swimlane.eclipse.roles.AdapterRole;
import com.gmmapowell.swimlane.testsupport.DirectRunner;

/** The purpose of this is to test that inputs allegedly coming from scanning of test files
 * are correctly "accumulated" by the Accumulator into a model the view can use in terms
 * of the HexagonDataModel.
 */
public class AdapterAccumulationTests {
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();
	Solution solution = context.mock(Solution.class);
	ErrorAccumulator errors = context.mock(ErrorAccumulator.class);
	AnalysisAccumulator acc = new SolutionCreator(new DirectRunner(), errors, solution, new HashMap<GroupOfTests, AllConstraints>());
	Date bcd = new Date();
	TestGroup grp = new TestGroup("Project", null);
	Class<?> hexClass1 = Integer.class;
	Class<?> hexClass2 = List.class;
	Class<?> portClass1 = Long.class;
	Class<?> portClass2 = Float.class;
	Class<?> portClass3 = Double.class;
	Class<?> portClass4 = Short.class;
	Class<?> portClass5 = Number.class;
	Class<?> adapterClass1 = Exception.class;
	Class<?> adapterClass2 = RuntimeException.class;
	Class<?> adapterClass3 = IOException.class;
	String hmd = "";
	String hm1 = hexClass1.getName();
	String hm2 = hexClass2.getName();
	Sequence seq = context.sequence("solution");
	List<String> tests = new ArrayList<>();

	@Test
	public void nothingHappensUntilWeCallComplete() {
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AdapterRole(hexClass1, portClass1, null, adapterClass1), tests);
	}

	@Test
	public void ifWeAccumulateOneAdapterTestTheModelMustHaveTheHexagonForIt() {
		context.checking(new Expectations() {{
			oneOf(solution).beginAnalysis(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).port(PortLocation.NORTHWEST, portClass1.getName()); inSequence(seq);
			oneOf(solution).adapter(adapterClass1.getName()); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase1", tests); inSequence(seq);
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AdapterRole(hexClass1, portClass1, null, adapterClass1), tests);
		acc.analysisComplete(bcd);
	}
	@Test
	public void weCanSupportDefaultHexagonsWithAnAdapterTest() {
		context.checking(new Expectations() {{
			oneOf(solution).beginAnalysis(); inSequence(seq);
			oneOf(solution).hex(with(hmd)); inSequence(seq);
			oneOf(solution).port(PortLocation.NORTHWEST, portClass1.getName()); inSequence(seq);
			oneOf(solution).adapter(adapterClass1.getName()); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase2", tests); inSequence(seq);
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase2", new AdapterRole(null, portClass1, null, adapterClass1), tests);
		acc.analysisComplete(bcd);
	}

	@Test
	public void weCanAccumulateAnAdapterTestWithoutSpecifyingHexagonOrPortAsLongAsWeAlreadyKnowThePortForTheAdapter() {
		context.checking(new Expectations() {{
			oneOf(solution).beginAnalysis(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).port(PortLocation.NORTHWEST, portClass1.getName()); inSequence(seq);
			oneOf(solution).adapter(adapterClass1.getName()); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase1", tests); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase2", tests); inSequence(seq);
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AdapterRole(hexClass1, portClass1, null, adapterClass1), tests);
		acc.haveTestClass(grp, "TestCase2", new AdapterRole(null, null, null, adapterClass1), tests);
		acc.analysisComplete(bcd);
	}

	@Test
	public void itIsAnErrorToNeverLinkAnAdapterToAPort() {
		context.checking(new Expectations() {{
			oneOf(solution).beginAnalysis(); inSequence(seq);
			oneOf(solution).hex(with(hmd)); inSequence(seq);
			oneOf(errors).error("did not bind adapter " + adapterClass1.getName() + " to a port");
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase2", new AdapterRole(null, null, null, adapterClass1), tests);
		acc.analysisComplete(bcd);
	}

	@Test
	public void itIsAnErrorToHaveMultipleHexagonsAndADefault() {
		context.checking(new Expectations() {{
			oneOf(solution).beginAnalysis(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).port(PortLocation.NORTHWEST, portClass1.getName()); inSequence(seq);
			oneOf(solution).adapter(adapterClass1.getName()); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase1", tests); inSequence(seq);
			oneOf(solution).hex(with(hm2)); inSequence(seq);
			oneOf(solution).port(PortLocation.NORTHWEST, portClass2.getName()); inSequence(seq);
			oneOf(solution).adapter(adapterClass2.getName()); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase2", tests); inSequence(seq);
			oneOf(errors).error("port " + portClass3.getName() + " was not bound to a hexagon");
			oneOf(errors).error("there is no ordering between " + hexClass1.getName() + " and " + hexClass2.getName());
			// we literally cannot figure out where this should go ...
//			oneOf(solution).testClass(grp, "TestCase3", tests); inSequence(seq);
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AdapterRole(hexClass1, portClass1, null, adapterClass1), tests);
		acc.haveTestClass(grp, "TestCase2", new AdapterRole(hexClass2, portClass2, null, adapterClass2), tests);
		acc.haveTestClass(grp, "TestCase3", new AdapterRole(null, portClass3, null, adapterClass3), tests);
		acc.analysisComplete(bcd);
	}

	@Test
	public void weCanAccumulateTwoAdapterTestsForTheSameAdapter() {
		context.checking(new Expectations() {{
			oneOf(solution).beginAnalysis(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).port(PortLocation.NORTHWEST, portClass1.getName()); inSequence(seq);
			oneOf(solution).adapter(adapterClass1.getName()); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase1", tests); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase2", tests); inSequence(seq);
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AdapterRole(hexClass1, portClass1, null, adapterClass1), tests);
		acc.haveTestClass(grp, "TestCase2", new AdapterRole(hexClass1, portClass1, null, adapterClass1), tests);
		acc.analysisComplete(bcd);
	}
	
	@Test
	public void weCanSpecifyTheAdapterPortLocation() {
		context.checking(new Expectations() {{
			oneOf(solution).beginAnalysis(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).port(PortLocation.SOUTHEAST, portClass1.getName()); inSequence(seq);
			oneOf(solution).adapter(adapterClass1.getName()); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase1", tests); inSequence(seq);
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AdapterRole(hexClass1, portClass1, PortLocation.SOUTHEAST, adapterClass1), tests);
		acc.analysisComplete(bcd);
	}

	@Test
	public void testWeCannotSpecifyConflictingPortHexLocations() {
		context.checking(new Expectations() {{
			oneOf(solution).beginAnalysis(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).port(PortLocation.SOUTHWEST, portClass1.getName()); inSequence(seq);
			oneOf(solution).adapter(adapterClass1.getName()); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase1", tests); inSequence(seq);
			oneOf(solution).adapter(adapterClass2.getName()); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase2", tests); inSequence(seq);
			oneOf(errors).error("port " + portClass1.getName() + " cannot be in sw and se");
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AdapterRole(hexClass1, portClass1, PortLocation.SOUTHEAST, adapterClass1), tests);
		acc.haveTestClass(grp, "TestCase2", new AdapterRole(hexClass1, portClass1, PortLocation.SOUTHWEST, adapterClass2), tests);
		acc.analysisComplete(bcd);
	}

	@Test
	public void testWeCannotSpecifyTwoAdaptersInTheSamePortLocation() {
		context.checking(new Expectations() {{
			oneOf(solution).beginAnalysis(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).port(PortLocation.SOUTHEAST, portClass2.getName()); inSequence(seq);
			oneOf(solution).adapter(adapterClass2.getName()); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase2", tests); inSequence(seq);
			oneOf(solution).port(PortLocation.NORTHWEST, portClass1.getName()); inSequence(seq);
			oneOf(solution).adapter(adapterClass1.getName()); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase1", tests); inSequence(seq);
			oneOf(errors).error("ports " + portClass2.getName() + " and " + portClass1.getName() + " cannot all be in se");
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AdapterRole(hexClass1, portClass1, PortLocation.SOUTHEAST, adapterClass1), tests);
		acc.haveTestClass(grp, "TestCase2", new AdapterRole(hexClass1, portClass2, PortLocation.SOUTHEAST, adapterClass2), tests);
		acc.analysisComplete(bcd);
	}

	@Test
	public void testWeCanMixExplicitAndDefaultPlacements() {
		context.checking(new Expectations() {{
			oneOf(solution).beginAnalysis(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).port(PortLocation.NORTHWEST, portClass2.getName()); inSequence(seq);
			oneOf(solution).adapter(adapterClass2.getName()); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase2", tests); inSequence(seq);
			oneOf(solution).port(PortLocation.SOUTHEAST, portClass1.getName()); inSequence(seq);
			oneOf(solution).adapter(adapterClass1.getName()); inSequence(seq);
			oneOf(solution).testClass(grp, "TestCase1", tests); inSequence(seq);
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AdapterRole(hexClass1, portClass1, PortLocation.SOUTHEAST, adapterClass1), tests);
		acc.haveTestClass(grp, "TestCase2", new AdapterRole(hexClass1, portClass2, null, adapterClass2), tests);
		acc.analysisComplete(bcd);
	}
	
	@Test
	public void weCannotBindTheSameAdapterToTwoDifferentPorts() {
		context.checking(new Expectations() {{
			oneOf(errors).error("cannot bind adapter java.lang.Exception to both java.lang.Float and java.lang.Long"); inSequence(seq);
			oneOf(solution).beginAnalysis(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).port(PortLocation.NORTHWEST, portClass1.getName()); inSequence(seq);
			oneOf(solution).adapter(adapterClass1.getName()); inSequence(seq);
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AdapterRole(hexClass1, portClass1, null, adapterClass1), tests);
		acc.haveTestClass(grp, "TestCase2", new AdapterRole(hexClass1, portClass2, null, adapterClass1), tests);
		acc.analysisComplete(bcd);
	}
}