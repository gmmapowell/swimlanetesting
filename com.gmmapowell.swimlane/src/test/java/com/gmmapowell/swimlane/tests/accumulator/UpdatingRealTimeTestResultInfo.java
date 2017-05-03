package com.gmmapowell.swimlane.tests.accumulator;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.BarDataListener;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;
import com.gmmapowell.swimlane.eclipse.models.HexagonAccumulator;
import com.gmmapowell.swimlane.eclipse.models.SolidModelDispatcher;
import com.gmmapowell.swimlane.tests.swtutil.TestBase;

public class UpdatingRealTimeTestResultInfo extends TestBase {
	SolidModelDispatcher md = new SolidModelDispatcher();
	Accumulator acc = new HexagonAccumulator(md);
	HexagonDataModel hdm = (HexagonDataModel)acc;
	TestResultReporter trr = (TestResultReporter) acc;

	@Test
	public void incomingTestResultsAreReflectedInBarData() {
		acc.acceptance(String.class, new ArrayList<>());
		acc.analysisComplete();
		List<BarData> bars = hdm.getAcceptanceTests();
		assertEquals(1, bars.size());
		BarData bar = bars.get(0);
		TestInfo test = context.mock(TestInfo.class);
		BarDataListener bl = context.mock(BarDataListener.class);
		md.addBarListener(bl);
		context.checking(new Expectations() {{
			oneOf(bl).barChanged(bar);
		}});
		trr.testSuccess(test);
	}

}
