package com.gmmapowell.swimlane.tests.accumulator;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;
import com.gmmapowell.swimlane.eclipse.models.HexagonAccumulator;
import com.gmmapowell.swimlane.eclipse.models.SolidModelDispatcher;

/* The purpose of the accumulator is to take input in one form (what we discover)
 * and to build a stable model out of it.
 * To decouple these two roles, we have two interfaces: Accumulator for input and HexagonDataModel for output;
 * here we assert that the two are coupled correctly internally.
 */
public class AcceptanceAccumulationTests {
	ModelDispatcher md = new SolidModelDispatcher();
	Accumulator acc = new HexagonAccumulator(md);
	HexagonDataModel hdm = (HexagonDataModel)acc;
	
	@Test
	public void testNoTestsMeansNoHexes() {
		assertEquals(0, hdm.getHexCount());
		assertEquals(0, hdm.getAcceptanceTests().size());
	}
	
	@Test
	public void testOneUnboundAcceptanceGivesOneHex() {
		acc.acceptance(String.class, new ArrayList<>());
		acc.analysisComplete();
		assertEquals(1, hdm.getHexCount());
		List<BarData> acceptanceTests = hdm.getAcceptanceTests();
		assertEquals(1, acceptanceTests.size());
		assertEquals("acceptance.1", acceptanceTests.get(0).getId());
	}
	
	@Test
	public void testOneAcceptanceWithTwoHexesGivesTwoHexesAndNoErrors() {
		acc.acceptance(String.class, Arrays.asList(Integer.class, String.class));
		acc.analysisComplete();
		assertEquals(2, hdm.getHexCount());
		assertEquals(0, hdm.getErrors().size());
		List<BarData> acceptanceTests = hdm.getAcceptanceTests();
		assertEquals(1, acceptanceTests.size());
		assertEquals("acceptance.11", acceptanceTests.get(0).getId());
	}
	
	@Test
	public void testTwoAcceptancesWithTwoHexesGivesTwoHexesButComplainsAboutNoTotalOrdering() {
		acc.acceptance(String.class, Arrays.asList(Integer.class));
		acc.acceptance(String.class, Arrays.asList(String.class));
		acc.analysisComplete();
		assertEquals(2, hdm.getHexCount());
		assertEquals(2, hdm.getErrors().size());
		Object[] errs = hdm.getErrors().toArray();
		assertEquals("There is a cycle between java.lang.Integer and java.lang.String", errs[0]);
		assertEquals("There is no ordering between java.lang.Integer and java.lang.String", errs[1]);
		List<BarData> acceptanceTests = hdm.getAcceptanceTests();
		assertEquals(2, acceptanceTests.size());
		assertEquals("acceptance.10", acceptanceTests.get(0).getId());
		assertEquals("acceptance.01", acceptanceTests.get(1).getId());
	}
	
	@Test
	public void testTwoAcceptancesEachWithTwoHexesInDifferentOrdersGivesTwoHexesButComplainsAboutInconsistentOrdering() {
		acc.acceptance(String.class, Arrays.asList(Integer.class, String.class));
		acc.acceptance(String.class, Arrays.asList(String.class, Integer.class));
		acc.analysisComplete();
		assertEquals(2, hdm.getHexCount());
		assertEquals(2, hdm.getErrors().size());
		Object[] errs = hdm.getErrors().toArray();
		assertEquals("Ordering between java.lang.Integer and java.lang.String is inconsistent", errs[0]);
		assertEquals("There is a cycle between java.lang.Integer and java.lang.String", errs[1]);
		List<BarData> acceptanceTests = hdm.getAcceptanceTests();
		assertEquals(1, acceptanceTests.size());
		assertEquals("acceptance.11", acceptanceTests.get(0).getId());
	}
	
	@Test
	public void testTwoAcceptancesEachWithTwoOverlappingHexesThatMakeATotalOrderingOfThreeHexes() {
		acc.acceptance(Long.class, Arrays.asList(Double.class, Integer.class));
		acc.acceptance(Float.class, Arrays.asList(Integer.class, String.class));
		acc.analysisComplete();
		assertEquals(3, hdm.getHexCount());
		assertEquals(0, hdm.getErrors().size());
		List<BarData> acceptanceTests = hdm.getAcceptanceTests();
		assertEquals(2, acceptanceTests.size());
		BarData acc0 = acceptanceTests.get(0);
		assertEquals("acceptance.110", acc0.getId());
		assertEquals(1, acc0.classesUnderTest().size());
		assertEquals(Long.class.getName(), acc0.classesUnderTest().get(0));
		BarData acc1 = acceptanceTests.get(1);
		assertEquals("acceptance.011", acc1.getId());
		assertEquals(1, acc1.classesUnderTest().size());
		assertEquals(Float.class.getName(), acc1.classesUnderTest().get(0));
	}

	@Test
	public void testTwoAcceptancesEachWithTwoOrderedHexesIsNotTotal() {
		acc.acceptance(String.class, Arrays.asList(Double.class, Integer.class));
		acc.acceptance(Long.class, Arrays.asList(Double.class, String.class));
		acc.analysisComplete();
		assertEquals(3, hdm.getHexCount());
		assertEquals(2, hdm.getErrors().size());
		Object[] errs = hdm.getErrors().toArray();
		assertEquals("There is a cycle between java.lang.Integer and java.lang.String", errs[0]);
		assertEquals("There is no ordering between java.lang.Integer and java.lang.String", errs[1]);
		List<BarData> acceptanceTests = hdm.getAcceptanceTests();
		assertEquals(2, acceptanceTests.size());
		assertEquals("acceptance.110", acceptanceTests.get(0).getId());
		assertEquals("acceptance.101", acceptanceTests.get(1).getId());
	}

	@Test
	public void testTwoAcceptancesEachWithTwoOrderedHexesIsNotConsistent() {
		acc.acceptance(String.class, Arrays.asList(Double.class, Integer.class));
		acc.acceptance(Long.class, Arrays.asList(Integer.class, String.class));
		acc.acceptance(Float.class, Arrays.asList(String.class, Double.class));
		acc.analysisComplete();
		assertEquals(3, hdm.getHexCount());
		assertEquals(3, hdm.getErrors().size());
		Object[] errs = hdm.getErrors().toArray();
		assertEquals("There is a cycle between java.lang.Double and java.lang.Integer", errs[0]);
		assertEquals("There is a cycle between java.lang.Double and java.lang.String", errs[1]);
		assertEquals("There is a cycle between java.lang.Integer and java.lang.String", errs[2]);
		List<BarData> acceptanceTests = hdm.getAcceptanceTests();
		assertEquals(3, acceptanceTests.size());
		assertEquals("acceptance.110", acceptanceTests.get(0).getId());
		assertEquals("acceptance.101", acceptanceTests.get(1).getId());
		assertEquals("acceptance.011", acceptanceTests.get(2).getId());
	}

	@Test
	public void testThreeAcceptancesEachWithTwoOverlappingHexesThatMakeATotalOrderingAfterTwoPasses() {
		acc.acceptance(String.class, Arrays.asList(Double.class, Float.class));
		acc.acceptance(String.class, Arrays.asList(Float.class, Integer.class));
		acc.acceptance(String.class, Arrays.asList(Integer.class, String.class));
		acc.analysisComplete();
		assertEquals(4, hdm.getHexCount());
		assertEquals(0, hdm.getErrors().size());
		List<BarData> acceptanceTests = hdm.getAcceptanceTests();
		assertEquals(3, acceptanceTests.size());
		assertEquals("acceptance.1100", acceptanceTests.get(0).getId());
		assertEquals("acceptance.0110", acceptanceTests.get(1).getId());
		assertEquals("acceptance.0011", acceptanceTests.get(2).getId());
	}
	
	@Test
	public void testThreeAcceptancesWithATotalOfThreeHexesCanBeConsistentWithOneCoveringAllThree() {
		acc.acceptance(String.class, Arrays.asList(Double.class, Integer.class, String.class));
		acc.acceptance(String.class, Arrays.asList(Double.class, Integer.class));
		acc.acceptance(String.class, Arrays.asList(Integer.class, String.class));
		acc.analysisComplete();
		assertEquals(3, hdm.getHexCount());
		assertEquals(0, hdm.getErrors().size());
		List<BarData> acceptanceTests = hdm.getAcceptanceTests();
		assertEquals(3, acceptanceTests.size());
		assertEquals("acceptance.111", acceptanceTests.get(0).getId());
		assertEquals("acceptance.110", acceptanceTests.get(1).getId());
		assertEquals("acceptance.011", acceptanceTests.get(2).getId());
	}
	
	@Test
	public void testThreeAcceptancesWithATotalOfThreeHexesCanBeInonsistentWithOneCoveringAllThreeIfInADifferentOrder() {
		acc.acceptance(String.class, Arrays.asList(Double.class, Integer.class, String.class));
		acc.acceptance(String.class, Arrays.asList(Integer.class, Double.class));
		acc.analysisComplete();
		assertEquals(3, hdm.getHexCount());
		assertEquals(2, hdm.getErrors().size());
		Object[] errs = hdm.getErrors().toArray();
		assertEquals("Ordering between java.lang.Double and java.lang.Integer is inconsistent", errs[0]);
		assertEquals("There is a cycle between java.lang.Double and java.lang.Integer", errs[1]);
		List<BarData> acceptanceTests = hdm.getAcceptanceTests();
		assertEquals(2, acceptanceTests.size());
		assertEquals("acceptance.111", acceptanceTests.get(0).getId());
		assertEquals("acceptance.110", acceptanceTests.get(1).getId());
	}
}
