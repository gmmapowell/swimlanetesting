package com.gmmapowell.swimlane.tests.accumulator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
	Class<?> hexClass = Integer.class;
	Class<?> portClass = Long.class;

	@Test
	public void testThatIfWeAccumulateOneAdapterTestTheModelMustHaveTheHexagonForIt() {
		acc.adapter(grp, testCase1, hexClass, portClass);
		acc.analysisComplete();
		assertEquals(1, hdm.getHexCount());
		assertNotNull(hdm.getHexagons().get(0));
		assertEquals(hexClass.getName(), hdm.getHexagons().get(0).getId());
	}

	@Test
	public void testThatIfWeAccumulateOneAdapterTestWithAPortTheModelMustHaveThePortForTheHexagon() {
		acc.adapter(grp, testCase1, hexClass, portClass);
		acc.analysisComplete();
		HexData hd = hdm.getHexagons().get(0);
		assertEquals(1, hd.getPorts().size());
		PortData pd = hd.getPorts().get(0);
		assertEquals(Long.class.getName(), pd.getName());
	}

	@Test
	public void testWeCanAccumulateTwoAdapterTestsForTheSameAdapter() {
		acc.adapter(grp, testCase1, hexClass, portClass);
		acc.adapter(grp, testCase2, hexClass, portClass);
		acc.analysisComplete();
		HexData hd = hdm.getHexagons().get(0);
		assertEquals(1, hd.getPorts().size());
		PortData pd = hd.getPorts().get(0);
		assertEquals(Long.class.getName(), pd.getName());
	}
	
	// test what happens if we DON'T specify a port location (i.e. it should lay them out)
	// test what happens if we specify CONFLICTING port locations (it should raise errors)
	@Test
	public void testWeCanSpecifyTheAdapterPortLocation() {
		acc.adapter(grp, testCase1, hexClass, portClass);
		acc.portLocation(hexClass, portClass, PortLocation.SOUTHEAST);
		acc.analysisComplete();
		HexData hd = hdm.getHexagons().get(0);
		assertEquals(1, hd.getPorts().size());
		PortData pd = hd.getPorts().get(0);
		assertEquals(PortLocation.SOUTHEAST, pd.getLocation());
	}
	// test port location
	// test bar is created
	// test bar has the relevant cases from the groups
	
}
