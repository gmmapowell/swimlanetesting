package com.gmmapowell.swimlane.tests.accumulator;

import static org.junit.Assert.*;

import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;
import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;
import com.gmmapowell.swimlane.eclipse.models.HexagonAccumulator;
import com.gmmapowell.swimlane.eclipse.models.SolidModelDispatcher;
import com.gmmapowell.swimlane.eclipse.models.TestGroup;
import com.gmmapowell.swimlane.tests.swtutil.TestBase;

public class AccumulationOfTestResultsTests extends TestBase {
	ModelDispatcher md = new SolidModelDispatcher(null, null);
	TestResultReporter trr = new HexagonAccumulator(md); 
	HexagonDataModel hdm = (HexagonDataModel) trr; 
	TestGroup grp = new TestGroup(null);

	@Test
	public void testWeCanRecoverATestGroupGivenTheBarId() {
		TestInfo test = context.mock(TestInfo.class);
		context.checking(new Expectations() {{
			oneOf(test).classUnderTest();
		}});
		trr.testSuccess(test);
		assertEquals(1, hdm.getTestResultsFor("utility.bar").size());
	}

}
