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

	@Test
	public void testThatIfWeAccumulateOneAdapterTestTheModelMustHaveTheHexagonForIt() {
		acc.adapter(grp, testCase1, hexClass1, portClass1);
		acc.analysisComplete();
		assertEquals(1, hdm.getHexCount());
		assertNotNull(hdm.getHexagons().get(0));
		assertEquals(hexClass1.getName(), hdm.getHexagons().get(0).getId());
	}

	@Test
	public void testThatWeCanSupportDefaultHexagonsWithAnAdapterTest() {
		acc.adapter(grp, testCase1, null, portClass1);
		acc.analysisComplete();
		assertEquals(1, hdm.getHexCount());
		assertNotNull(hdm.getHexagons().get(0));
		assertEquals("-default-", hdm.getHexagons().get(0).getId());
	}

	@Test
	public void testThatItIsAnErrorToHaveMultipleHexagonsAndADefault() {
		acc.adapter(grp, testCase1, hexClass1, portClass1);
		acc.adapter(grp, testCase1, null, portClass1);
		acc.analysisComplete();
		assertTrue(hdm.getErrors().contains("Cannot use both a default hex and a non-default hex"));
	}

	@Test
	public void testThatIfWeAccumulateOneAdapterTestWithAPortTheModelMustHaveThePortForTheHexagon() {
		acc.adapter(grp, testCase1, hexClass1, portClass1);
		acc.analysisComplete();
		HexData hd = hdm.getHexagons().get(0);
		assertEquals(1, hd.getPorts().size());
		PortData pd = hd.getPorts().get(0);
		assertEquals(Long.class.getName(), pd.getName());
	}

	@Test
	public void testWeCanAccumulateTwoAdapterTestsForTheSameAdapter() {
		acc.adapter(grp, testCase1, hexClass1, portClass1);
		acc.adapter(grp, testCase2, hexClass1, portClass1);
		acc.analysisComplete();
		HexData hd = hdm.getHexagons().get(0);
		assertEquals(1, hd.getPorts().size());
		PortData pd = hd.getPorts().get(0);
		assertEquals(Long.class.getName(), pd.getName());
	}
	
	@Test
	public void testWeCanSpecifyTheAdapterPortLocation() {
		acc.adapter(grp, testCase1, hexClass1, portClass1);
		acc.portLocation(hexClass1, portClass1, PortLocation.SOUTHEAST);
		acc.analysisComplete();
		HexData hd = hdm.getHexagons().get(0);
		assertEquals(1, hd.getPorts().size());
		PortData pd = hd.getPorts().get(0);
		assertEquals(PortLocation.SOUTHEAST, pd.getLocation());
	}

	@Test
	public void testWeCannotSpecifyConflictingAdapterPortLocations() {
		acc.adapter(grp, testCase1, hexClass1, portClass1);
		acc.portLocation(hexClass1, portClass1, PortLocation.SOUTHEAST);
		acc.adapter(grp, testCase2, hexClass1, portClass1);
		acc.portLocation(hexClass1, portClass1, PortLocation.SOUTHWEST);
		acc.analysisComplete();
		assertTrue(hdm.getErrors().contains("Cannot specify multiple locations for port " + portClass1.getName()));
		HexData hd = hdm.getHexagons().get(0);
		assertEquals(1, hd.getPorts().size());
		PortData pd = hd.getPorts().get(0);
		assertEquals(PortLocation.SOUTHEAST, pd.getLocation());
	}
	// likewise can't have two in the same place ...

	@Test
	public void testOnePortWillDefaultToNorthWestPlacement() {
		acc.adapter(grp, testCase1, hexClass1, portClass1);
		acc.analysisComplete();
		HexData hd = hdm.getHexagons().get(0);
		assertEquals(1, hd.getPorts().size());
		PortData pd = hd.getPorts().get(0);
		assertEquals(PortLocation.NORTHWEST, pd.getLocation());
	}
	// can mix default and explicit placements ....

	// test port location
	// test bar is created
	// test bar has the relevant cases from the groups
	
}
