package com.gmmapowell.swimlane.tests.accumulator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.HexData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;
import com.gmmapowell.swimlane.eclipse.interfaces.PortData;
import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;
import com.gmmapowell.swimlane.eclipse.models.HexagonAccumulator;
import com.gmmapowell.swimlane.eclipse.models.SolidModelDispatcher;
import com.gmmapowell.swimlane.eclipse.models.TestGroup;

/** The purpose of this is to test that inputs allegedly coming from scanning of test files
 * are correctly "accumulated" by the Accumulator into a model the view can use in terms
 * of the HexagonDataModel.
 */
public class AdapterAccumulationTests {
	ModelDispatcher md = new SolidModelDispatcher();
	Accumulator acc = new HexagonAccumulator(md);
	HexagonDataModel hdm = (HexagonDataModel)acc;
	TestGroup grp = new TestGroup(null);
	Class<?> testCase1 = String.class;
	Class<?> testCase2 = Character.class;
	Class<?> hexClass1 = Integer.class;
	Class<?> portClass1 = Long.class;
	Class<?> portClass2 = Float.class;
	Class<?> portClass3 = Double.class;
	Class<?> portClass4 = Short.class;
	Class<?> portClass5 = Number.class;
	Class<?> adapterClass1 = Exception.class;
	Class<?> adapterClass2 = RuntimeException.class;

	@Test
	public void testThatIfWeAccumulateOneAdapterTestTheModelMustHaveTheHexagonForIt() {
		acc.adapter(grp, testCase1, hexClass1, portClass1, adapterClass1);
		acc.analysisComplete();
		assertEquals(1, hdm.getHexCount());
		assertNotNull(hdm.getHexagons().get(0));
		assertEquals(hexClass1.getName(), hdm.getHexagons().get(0).getId());
	}

	@Test
	public void testThatWeCanSupportDefaultHexagonsWithAnAdapterTest() {
		acc.adapter(grp, testCase1, null, portClass1, adapterClass1);
		acc.analysisComplete();
		assertEquals(0, hdm.getErrors().size());
		assertEquals(1, hdm.getHexCount());
		assertNotNull(hdm.getHexagons().get(0));
		assertEquals("-default-", hdm.getHexagons().get(0).getId());
	}

	@Test
	public void testWeCanAccumulateAnAdapterTestWithoutSpecifyingHexagonOrPortAsLongAsWeAlreadyKnowThePortForTheAdapter() {
		acc.adapter(grp, testCase1, hexClass1, portClass1, adapterClass1);
		acc.adapter(grp, testCase1, null, null, adapterClass1);
		acc.analysisComplete();
		assertEquals(1, hdm.getHexCount());
		assertNotNull(hdm.getHexagons().get(0));
		assertEquals(hexClass1.getName(), hdm.getHexagons().get(0).getId());
	}

	@Test
	public void testItIsAnErrorToNeverLinkAnAdapterToAPort() {
		acc.adapter(grp, testCase1, null, null, adapterClass1);
		acc.analysisComplete();
		assertEquals(1, hdm.getErrors().size());
		assertTrue(hdm.getErrors().contains("did not bind adapter " + adapterClass1.getName() + " to a port"));
	}

	@Test
	public void testThatItIsAnErrorToHaveMultipleHexagonsAndADefault() {
		acc.adapter(grp, testCase1, hexClass1, portClass1, adapterClass1);
		acc.adapter(grp, testCase2, null, portClass2, adapterClass2);
		acc.analysisComplete();
		assertEquals(1, hdm.getErrors().size());
		assertTrue(hdm.getErrors().contains("port " + portClass2.getName() + " was not bound to a hexagon"));
	}

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
}
