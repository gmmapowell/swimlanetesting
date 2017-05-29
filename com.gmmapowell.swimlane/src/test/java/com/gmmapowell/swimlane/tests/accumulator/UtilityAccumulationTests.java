package com.gmmapowell.swimlane.tests.accumulator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;
import com.gmmapowell.swimlane.eclipse.models.HexagonAccumulator;
import com.gmmapowell.swimlane.eclipse.models.SolidModelDispatcher;
import com.gmmapowell.swimlane.eclipse.models.TestGroup;

public class UtilityAccumulationTests {
	ModelDispatcher md = new SolidModelDispatcher();
	Accumulator acc = new HexagonAccumulator(md);
	HexagonDataModel hdm = (HexagonDataModel)acc;
	TestGroup grp = new TestGroup(null);
	Class<?> testCase1 = String.class;
	Class<?> testCase2 = Character.class;
	
	@Test
	public void testNoUtilityTestsMeansNoUtilityBar() {
		acc.analysisComplete();
		assertNull(hdm.getUtilityBar());
	}

	@Test
	public void testThatWeCanStoreAndRecoverATest() {
		acc.utility(grp, testCase1);
		acc.analysisComplete();
		assertNotNull(hdm.getUtilityBar());
		assertEquals(1, hdm.getUtilityBar().classesUnderTest().size());
	}

	@Test
	public void testThatWeCanStoreAndRecoverMultipleTest() {
		acc.utility(grp, testCase1);
		acc.utility(grp, testCase2);
		acc.analysisComplete();
		assertNotNull(hdm.getUtilityBar());
		assertEquals(2, hdm.getUtilityBar().classesUnderTest().size());
	}
}
