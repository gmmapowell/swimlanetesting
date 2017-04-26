package com.gmmapowell.swimlane.tests.accumulator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.models.HexagonAccumulator;

/* The purpose of the accumulator is to take input in one form (what we discover)
 * and to build a stable model out of it.
 * To decouple these two roles, we have two interfaces: Accumulator for input and HexagonDataModel for output;
 * here we assert that the two are coupled correctly internally.
 */
public class GeneralAccumulationTests {
	Accumulator acc = new HexagonAccumulator();
	HexagonDataModel hdm = (HexagonDataModel)acc;
	
	@Test
	public void testDateCanBeNull() {
		assertNull(hdm.getBuildTime());
	}
	
	@Test
	public void testTheTrivialDateThing() {
		Date d = new Date();
		acc.setBuildTime(d);
		assertEquals(d, hdm.getBuildTime());
	}
}