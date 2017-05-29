package com.gmmapowell.swimlane.tests.accumulator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;
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
	Class<?> tc = String.class;

	@Test
	public void testThatIfWeAccumulateOneAdapterTestTheModelMustHaveTheHexagonForIt() {
		acc.adapter(grp, tc, Integer.class, Long.class);
		assertEquals(1, hdm.getHexCount());
		assertNotNull(hdm.getHexagons().get(0));
	}
}
