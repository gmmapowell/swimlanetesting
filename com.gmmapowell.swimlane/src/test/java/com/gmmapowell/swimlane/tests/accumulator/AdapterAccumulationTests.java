package com.gmmapowell.swimlane.tests.accumulator;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.AnalysisAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.DataCentral;
import com.gmmapowell.swimlane.eclipse.interfaces.ErrorMessageListener;
import com.gmmapowell.swimlane.eclipse.interfaces.ViewLayout;
import com.gmmapowell.swimlane.eclipse.models.HexInfo;
import com.gmmapowell.swimlane.eclipse.models.HexagonAccumulator;
import com.gmmapowell.swimlane.eclipse.models.TestGroup;
import com.gmmapowell.swimlane.eclipse.roles.AdapterRole;

/** The purpose of this is to test that inputs allegedly coming from scanning of test files
 * are correctly "accumulated" by the Accumulator into a model the view can use in terms
 * of the HexagonDataModel.
 */
public class AdapterAccumulationTests {
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();
	AnalysisAccumulator acc = new HexagonAccumulator();
	ViewLayout layout = context.mock(ViewLayout.class);
	ErrorMessageListener errors = context.mock(ErrorMessageListener.class);
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

	// These tests would be more compelling with a HexInfoMatcher
	@Before
	public void setup() {
		context.checking(new Expectations() {{
			oneOf(errors).clear();
		}});
		((DataCentral)acc).setViewLayout(layout);
		((DataCentral)acc).addErrorMessageListener(errors);
	}
	
	@Test
	public void testThatIfWeAccumulateOneAdapterTestTheModelMustHaveTheHexagonForIt() {
		context.checking(new Expectations() {{
			oneOf(layout).addHexagon(with(0), with(aNonNull(HexInfo.class)));
		}});
		acc.startAnalysis(bcd);
		acc.haveTestClass(grp, "TestCase1", new AdapterRole(hexClass1, portClass1, null, adapterClass1));
		acc.analysisComplete(bcd);
	}

	@Test
	public void testThatWeCanSupportDefaultHexagonsWithAnAdapterTest() {
		context.checking(new Expectations() {{
			oneOf(layout).addHexagon(with(0), with(aNonNull(HexInfo.class)));
		}});
		acc.startAnalysis(bcd);
		acc.haveTestClass(grp, "TestCase2", new AdapterRole(null, portClass1, null, adapterClass1));
		acc.analysisComplete(bcd);
	}

	@Test
	public void testWeCanAccumulateAnAdapterTestWithoutSpecifyingHexagonOrPortAsLongAsWeAlreadyKnowThePortForTheAdapter() {
		context.checking(new Expectations() {{
			oneOf(layout).addHexagon(with(0), with(aNonNull(HexInfo.class)));
		}});
		acc.startAnalysis(bcd);
		acc.haveTestClass(grp, "TestCase1", new AdapterRole(hexClass1, portClass1, null, adapterClass1));
		acc.haveTestClass(grp, "TestCase2", new AdapterRole(null, null, null, adapterClass1));
		acc.analysisComplete(bcd);
	}

	@Test
	public void testItIsAnErrorToNeverLinkAnAdapterToAPort() {
		context.checking(new Expectations() {{
			oneOf(layout).addHexagon(with(0), with(aNonNull(HexInfo.class)));
			oneOf(errors).error("did not bind adapter " + adapterClass1.getName() + " to a port");
		}});
		acc.startAnalysis(bcd);
		acc.haveTestClass(grp, "TestCase2", new AdapterRole(null, null, null, adapterClass1));
		acc.analysisComplete(bcd);
	}

	@Test
	public void testThatItIsAnErrorToHaveMultipleHexagonsAndADefault() {
		context.checking(new Expectations() {{
			oneOf(layout).addHexagon(with(0), with(aNonNull(HexInfo.class)));
			oneOf(layout).addHexagon(with(1), with(aNonNull(HexInfo.class)));
			oneOf(errors).error("port " + portClass3.getName() + " was not bound to a hexagon");
		}});
		acc.startAnalysis(bcd);
		acc.haveTestClass(grp, "TestCase1", new AdapterRole(hexClass1, portClass1, null, adapterClass1));
		acc.haveTestClass(grp, "TestCase2", new AdapterRole(hexClass2, portClass2, null, adapterClass2));
		acc.haveTestClass(grp, "TestCase3", new AdapterRole(null, portClass3, null, adapterClass3));
		acc.analysisComplete(bcd);
//		acc.adapter(grp, testCase1, hexClass1, portClass1, adapterClass1);
//		acc.adapter(grp, testCase2, null, portClass2, adapterClass2);
//		acc.analysisComplete();
//		assertEquals(1, hdm.getErrors().size());
//		assertTrue(hdm.getErrors().contains());
	}

	/*
	@Test
	public void testThatIfWeAccumulateOneAdapterTestWithAPortTheModelMustHaveThePortForTheHexagon() {
		acc.adapter(grp, testCase1, hexClass1, portClass1, adapterClass1);
		acc.analysisComplete();
		HexData hd = hdm.getHexagons().get(0);
		assertEquals(1, hd.getPorts().size());
		PortData pd = hd.getPorts().get(0);
		assertEquals(Long.class.getName(), pd.getName());
	}

	@Test
	public void testWeCanAccumulateTwoAdapterTestsForTheSameAdapter() {
		acc.adapter(grp, testCase1, hexClass1, portClass1, adapterClass1);
		acc.adapter(grp, testCase2, hexClass1, portClass1, adapterClass1);
		acc.analysisComplete();
		HexData hd = hdm.getHexagons().get(0);
		assertEquals(1, hd.getPorts().size());
		PortData pd = hd.getPorts().get(0);
		assertEquals(Long.class.getName(), pd.getName());
	}
	
	@Test
	public void testWeCanSpecifyTheAdapterPortLocation() {
		acc.adapter(grp, testCase1, hexClass1, portClass1, adapterClass1);
		acc.portLocation(adapterClass1, PortLocation.SOUTHEAST);
		acc.analysisComplete();
		HexData hd = hdm.getHexagons().get(0);
		assertEquals(1, hd.getPorts().size());
		PortData pd = hd.getPorts().get(0);
		assertEquals(PortLocation.SOUTHEAST, pd.getLocation());
	}

	@Test
	public void testWeCannotSpecifyConflictingPortHexLocations() {
		acc.adapter(grp, testCase1, hexClass1, portClass1, adapterClass1);
		acc.portLocation(adapterClass1, PortLocation.SOUTHEAST);
		acc.adapter(grp, testCase2, hexClass1, portClass1, adapterClass2);
		acc.portLocation(adapterClass2, PortLocation.SOUTHWEST);
		acc.analysisComplete();
		assertEquals(1, hdm.getErrors().size());
		assertTrue(hdm.getErrors().contains("Cannot specify multiple locations for port " + portClass1.getName()));
		assertEquals(1, hdm.getHexCount());
		HexData hd = hdm.getHexagons().get(0);
		assertEquals(1, hd.getPorts().size());
		PortData pd = hd.getPorts().get(0);
		assertTrue(PortLocation.SOUTHEAST.equals(pd.getLocation()) || PortLocation.SOUTHWEST.equals(pd.getLocation()));
	}

	@Test
	public void testWeCannotSpecifyConflictingAdapterHexLocations() {
		acc.adapter(grp, testCase1, hexClass1, portClass1, adapterClass1);
		acc.portLocation(adapterClass1, PortLocation.SOUTHEAST);
		acc.adapter(grp, testCase2, hexClass1, portClass1, adapterClass1);
		acc.portLocation(adapterClass1, PortLocation.SOUTHWEST);
		acc.analysisComplete();
		assertTrue(hdm.getErrors().contains("cannot assign locations se and sw to adapter " + adapterClass1.getName()));
		HexData hd = hdm.getHexagons().get(0);
		assertEquals(1, hd.getPorts().size());
		PortData pd = hd.getPorts().get(0);
		assertEquals(PortLocation.SOUTHEAST, pd.getLocation());
	}

	@Test
	public void testWeCannotSpecifyTwoAdaptersInTheSamePortLocation() {
		acc.adapter(grp, testCase1, hexClass1, portClass1, adapterClass1);
		acc.portLocation(adapterClass1, PortLocation.SOUTHEAST);
		acc.adapter(grp, testCase2, hexClass1, portClass2, adapterClass2);
		acc.portLocation(adapterClass2, PortLocation.SOUTHEAST);
		acc.analysisComplete();
		assertEquals(1, hdm.getErrors().size());
		assertTrue(hdm.getErrors().contains("Cannot place multiple ports in the se"));
	}

	@Test
	public void testOnePortWillDefaultToNorthWestPlacement() {
		acc.adapter(grp, testCase1, hexClass1, portClass1, adapterClass1);
		acc.analysisComplete();
		HexData hd = hdm.getHexagons().get(0);
		assertEquals(1, hd.getPorts().size());
		PortData pd = hd.getPorts().get(0);
		assertEquals(PortLocation.NORTHWEST, pd.getLocation());
	}

	@Test
	public void testWeCanMixExplicitAndDefaultPlacements() {
		acc.adapter(grp, testCase1, hexClass1, portClass1, adapterClass1);
		acc.portLocation(adapterClass1, PortLocation.SOUTHEAST);
		acc.adapter(grp, testCase2, hexClass1, portClass2, adapterClass2);
		acc.analysisComplete();
		assertEquals(0, hdm.getErrors().size());
		HexData hd = hdm.getHexagons().get(0);
		assertEquals(2, hd.getPorts().size());
		PortData p1 = hd.getPorts().get(0);
		PortData p2 = hd.getPorts().get(1);
		if (p1.getName().equals(portClass1.getName())) {
			assertEquals(PortLocation.SOUTHEAST, p1.getLocation());
			assertEquals(PortLocation.NORTHWEST, p2.getLocation());
		} else {
			assertEquals(PortLocation.NORTHWEST, p1.getLocation());
			assertEquals(PortLocation.SOUTHEAST, p2.getLocation());
		}
	}

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

	@Test
	public void testWeCanHaveMultipleAdaptersForASinglePort() {
		acc.adapter(grp, testCase1, hexClass1, portClass1, adapterClass1);
		acc.adapter(grp, testCase2, hexClass1, portClass1, adapterClass2);
		acc.analysisComplete();
		assertEquals(2, hdm.getHexagons().get(0).getPorts().get(0).getAdapters().size());
		assertEquals(1, hdm.getHexagons().get(0).getPorts().get(0).getAdapters().get(0).classesUnderTest().size());
	}
	*/
}
