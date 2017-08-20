package com.gmmapowell.swimlane.tests.analysis;

import java.io.IOException;
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

/** The purpose of this is to test that inputs allegedly coming from scanning of test files
 * are correctly "accumulated" by the Accumulator into a model the view can use in terms
 * of the HexagonDataModel.
 */
public class AdapterAccumulationTests {
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();
	Solution solution = context.mock(Solution.class);
	ErrorAccumulator errors = context.mock(ErrorAccumulator.class);
	AnalysisAccumulator acc = new SolutionCreator(errors, solution, new HashMap<GroupOfTests, AllConstraints>());
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

	@Test
	public void nothingHappensUntilWeCallComplete() {
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AdapterRole(hexClass1, portClass1, null, adapterClass1));
	}

	@Test
	public void ifWeAccumulateOneAdapterTestTheModelMustHaveTheHexagonForIt() {
		context.checking(new Expectations() {{
			oneOf(solution).beginHexes(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).hexesDone(); inSequence(seq);
			oneOf(solution).beginPorts(with(hm1)); inSequence(seq);
			oneOf(solution).port(PortLocation.NORTHWEST, portClass1.getName()); inSequence(seq);
			oneOf(solution).portsDone(with(hm1)); inSequence(seq);
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AdapterRole(hexClass1, portClass1, null, adapterClass1));
		acc.analysisComplete(bcd);
	}
	@Test
	public void weCanSupportDefaultHexagonsWithAnAdapterTest() {
		context.checking(new Expectations() {{
			oneOf(solution).beginHexes(); inSequence(seq);
			oneOf(solution).hex(with(hmd)); inSequence(seq);
			oneOf(solution).hexesDone(); inSequence(seq);
			oneOf(solution).beginPorts(with(hmd)); inSequence(seq);
			oneOf(solution).port(PortLocation.NORTHWEST, portClass1.getName()); inSequence(seq);
			oneOf(solution).portsDone(with(hmd)); inSequence(seq);
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase2", new AdapterRole(null, portClass1, null, adapterClass1));
		acc.analysisComplete(bcd);
	}

	@Test
	public void weCanAccumulateAnAdapterTestWithoutSpecifyingHexagonOrPortAsLongAsWeAlreadyKnowThePortForTheAdapter() {
		context.checking(new Expectations() {{
			oneOf(solution).beginHexes(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).hexesDone(); inSequence(seq);
			oneOf(solution).beginPorts(with(hm1)); inSequence(seq);
			oneOf(solution).port(PortLocation.NORTHWEST, portClass1.getName()); inSequence(seq);
			oneOf(solution).portsDone(with(hm1)); inSequence(seq);
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AdapterRole(hexClass1, portClass1, null, adapterClass1));
		acc.haveTestClass(grp, "TestCase2", new AdapterRole(null, null, null, adapterClass1));
		acc.analysisComplete(bcd);
	}

	@Test
	public void itIsAnErrorToNeverLinkAnAdapterToAPort() {
		context.checking(new Expectations() {{
			oneOf(solution).beginHexes(); inSequence(seq);
			oneOf(solution).hex(with(hmd)); inSequence(seq);
			oneOf(solution).hexesDone(); inSequence(seq);
			oneOf(solution).beginPorts(with(hmd)); inSequence(seq);
			oneOf(solution).portsDone(with(hmd)); inSequence(seq);
			oneOf(errors).error("did not bind adapter " + adapterClass1.getName() + " to a port");
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase2", new AdapterRole(null, null, null, adapterClass1));
		acc.analysisComplete(bcd);
	}

	@Test
	public void itIsAnErrorToHaveMultipleHexagonsAndADefault() {
		context.checking(new Expectations() {{
			oneOf(solution).beginHexes(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).hex(with(hm2)); inSequence(seq);
			oneOf(solution).hexesDone(); inSequence(seq);
			oneOf(solution).beginPorts(with(hm1)); inSequence(seq);
			oneOf(solution).port(PortLocation.NORTHWEST, portClass1.getName()); inSequence(seq);
			oneOf(solution).portsDone(with(hm1)); inSequence(seq);
			oneOf(solution).beginPorts(with(hm2)); inSequence(seq);
			oneOf(solution).port(PortLocation.NORTHWEST, portClass2.getName()); inSequence(seq);
			oneOf(solution).portsDone(with(hm2)); inSequence(seq);
			oneOf(errors).error("port " + portClass3.getName() + " was not bound to a hexagon");
			oneOf(errors).error("there is no ordering between " + hexClass1.getName() + " and " + hexClass2.getName());
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AdapterRole(hexClass1, portClass1, null, adapterClass1));
		acc.haveTestClass(grp, "TestCase2", new AdapterRole(hexClass2, portClass2, null, adapterClass2));
		acc.haveTestClass(grp, "TestCase3", new AdapterRole(null, portClass3, null, adapterClass3));
		acc.analysisComplete(bcd);
	}

	@Test
	public void weCanAccumulateTwoAdapterTestsForTheSameAdapter() {
		context.checking(new Expectations() {{
			oneOf(solution).beginHexes(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).hexesDone(); inSequence(seq);
			oneOf(solution).beginPorts(with(hm1)); inSequence(seq);
			oneOf(solution).port(PortLocation.NORTHWEST, portClass1.getName()); inSequence(seq);
			oneOf(solution).portsDone(with(hm1)); inSequence(seq);
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AdapterRole(hexClass1, portClass1, null, adapterClass1));
		acc.haveTestClass(grp, "TestCase2", new AdapterRole(hexClass1, portClass1, null, adapterClass1));
		acc.analysisComplete(bcd);
	}
	
	@Test
	public void weCanSpecifyTheAdapterPortLocation() {
		context.checking(new Expectations() {{
			oneOf(solution).beginHexes(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).hexesDone(); inSequence(seq);
			oneOf(solution).beginPorts(with(hm1)); inSequence(seq);
			oneOf(solution).port(PortLocation.SOUTHEAST, portClass1.getName()); inSequence(seq);
			oneOf(solution).portsDone(with(hm1)); inSequence(seq);
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AdapterRole(hexClass1, portClass1, PortLocation.SOUTHEAST, adapterClass1));
		acc.analysisComplete(bcd);
	}

	@Test
	public void testWeCannotSpecifyConflictingPortHexLocations() {
		context.checking(new Expectations() {{
			oneOf(solution).beginHexes(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).hexesDone(); inSequence(seq);
			oneOf(solution).beginPorts(with(hm1)); inSequence(seq);
			oneOf(solution).port(PortLocation.SOUTHWEST, portClass1.getName()); inSequence(seq);
			oneOf(solution).portsDone(with(hm1)); inSequence(seq);
			oneOf(errors).error("port " + portClass1.getName() + " cannot be in sw and se");
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AdapterRole(hexClass1, portClass1, PortLocation.SOUTHEAST, adapterClass1));
		acc.haveTestClass(grp, "TestCase1", new AdapterRole(hexClass1, portClass1, PortLocation.SOUTHWEST, adapterClass2));
		acc.analysisComplete(bcd);
	}

	/* I don't think this adds anything on the previous test as written - what case did it previously capture?
	@Test
	public void testWeCannotSpecifyConflictingAdapterHexLocations() {
		context.checking(new Expectations() {{
			oneOf(solution).beginHexes(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).hexesDone(); inSequence(seq);
			oneOf(solution).beginPorts(with(hm1)); inSequence(seq);
			oneOf(solution).port(with(hm1), with(PortInfoMatcher.port(PortLocation.SOUTHWEST, portClass1))); inSequence(seq);
			oneOf(solution).portsDone(with(hm1)); inSequence(seq);
			oneOf(errors).error("port " + portClass1.getName() + " cannot be in sw and se");
		}});
		acc.startAnalysis(bcd);
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AdapterRole(hexClass1, portClass1, PortLocation.SOUTHEAST, adapterClass1));
		acc.haveTestClass(grp, "TestCase2", new AdapterRole(hexClass1, portClass1, PortLocation.SOUTHWEST, adapterClass1));
		acc.analysisComplete(bcd);
//		acc.adapter(grp, testCase1, hexClass1, portClass1, adapterClass1);
//		acc.portLocation(adapterClass1, PortLocation.SOUTHEAST);
//		acc.adapter(grp, testCase2, hexClass1, portClass1, adapterClass1);
//		acc.portLocation(adapterClass1, PortLocation.SOUTHWEST);
//		acc.analysisComplete();
//		assertTrue(hdm.getErrors().contains("cannot assign locations se and sw to adapter " + adapterClass1.getName()));
//		HexData hd = hdm.getHexagons().get(0);
//		assertEquals(1, hd.getPorts().size());
//		PortData pd = hd.getPorts().get(0);
//		assertEquals(PortLocation.SOUTHEAST, pd.getLocation());
	}
	*/

	@Test
	public void testWeCannotSpecifyTwoAdaptersInTheSamePortLocation() {
		context.checking(new Expectations() {{
			oneOf(solution).beginHexes(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).hexesDone(); inSequence(seq);
			oneOf(solution).beginPorts(with(hm1)); inSequence(seq);
			oneOf(solution).port(PortLocation.SOUTHEAST, portClass2.getName()); inSequence(seq);
			oneOf(solution).port(PortLocation.NORTHWEST, portClass1.getName()); inSequence(seq);
			oneOf(solution).portsDone(with(hm1)); inSequence(seq);
			oneOf(errors).error("ports " + portClass2.getName() + " and " + portClass1.getName() + " cannot all be in se");
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AdapterRole(hexClass1, portClass1, PortLocation.SOUTHEAST, adapterClass1));
		acc.haveTestClass(grp, "TestCase2", new AdapterRole(hexClass1, portClass2, PortLocation.SOUTHEAST, adapterClass2));
		acc.analysisComplete(bcd);
	}

	@Test
	public void testWeCanMixExplicitAndDefaultPlacements() {
		context.checking(new Expectations() {{
			oneOf(solution).beginHexes(); inSequence(seq);
			oneOf(solution).hex(with(hm1)); inSequence(seq);
			oneOf(solution).hexesDone(); inSequence(seq);
			oneOf(solution).beginPorts(with(hm1)); inSequence(seq);
			oneOf(solution).port(PortLocation.NORTHWEST, portClass2.getName()); inSequence(seq);
			oneOf(solution).port(PortLocation.SOUTHEAST, portClass1.getName()); inSequence(seq);
			oneOf(solution).portsDone(with(hm1)); inSequence(seq);
			oneOf(solution).analysisDone(bcd); inSequence(seq);
		}});
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new AdapterRole(hexClass1, portClass1, PortLocation.SOUTHEAST, adapterClass1));
		acc.haveTestClass(grp, "TestCase2", new AdapterRole(hexClass1, portClass2, null, adapterClass2));
		acc.analysisComplete(bcd);
	}

	/* TODO: If these tests are relevant at all, they are relevant somewhere else
	@Test
	public void testWeCreateABarForTheAdapterTestsToDisplayIn() {
		acc.adapter(grp, testCase1, hexClass1, portClass1, adapterClass1);
		acc.analysisComplete();
		assertEquals(1, hdm.getHexagons().get(0).getPorts().get(0).getAdapters().size());
	}

	@Test
	public void testTheAdapterBarHasTheRightName() {
		acc.adapter(grp, testCase1, hexClass1, portClass1, adapterClass1);
		acc.analysisComplete();
		assertEquals(adapterClass1.getName(), hdm.getHexagons().get(0).getPorts().get(0).getAdapters().get(0).getName());
	}

	@Test
	public void testWeGroupMultipleTestsIntoASingleBarForTheSameAdapter() {
		acc.adapter(grp, testCase1, hexClass1, portClass1, adapterClass1);
		acc.adapter(grp, testCase2, hexClass1, portClass1, adapterClass1);
		acc.analysisComplete();
		assertEquals(1, hdm.getHexagons().get(0).getPorts().get(0).getAdapters().size());
		assertEquals(2, hdm.getHexagons().get(0).getPorts().get(0).getAdapters().get(0).classesUnderTest().size());
	}
	*/
}
