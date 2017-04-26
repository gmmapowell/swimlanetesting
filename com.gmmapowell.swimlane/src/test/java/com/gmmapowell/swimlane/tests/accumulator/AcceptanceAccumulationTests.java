package com.gmmapowell.swimlane.tests.accumulator;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.models.HexagonAccumulator;

/* The purpose of the accumulator is to take input in one form (what we discover)
 * and to build a stable model out of it.
 * To decouple these two roles, we have two interfaces: Accumulator for input and HexagonDataModel for output;
 * here we assert that the two are coupled correctly internally.
 */
public class AcceptanceAccumulationTests {
	Accumulator acc = new HexagonAccumulator();
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
		assertEquals(1, hdm.getErrors().size());
		assertEquals("There is no ordering between java.lang.Integer and java.lang.String", hdm.getErrors().get(0));
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
		assertEquals(1, hdm.getErrors().size());
		assertEquals("Ordering between java.lang.Integer and java.lang.String is inconsistent", hdm.getErrors().get(0));
		List<BarData> acceptanceTests = hdm.getAcceptanceTests();
		assertEquals(1, acceptanceTests.size());
		assertEquals("acceptance.11", acceptanceTests.get(0).getId());
	}
}
