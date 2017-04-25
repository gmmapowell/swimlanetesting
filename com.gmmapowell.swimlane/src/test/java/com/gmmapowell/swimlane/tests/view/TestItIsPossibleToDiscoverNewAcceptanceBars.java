package com.gmmapowell.swimlane.tests.view;

import java.util.ArrayList;

import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.models.BarData;
import com.gmmapowell.swimlane.eclipse.models.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.models.HexagonDataModel.Status;

public class TestItIsPossibleToDiscoverNewAcceptanceBars extends AViewTest {
	
	@Test
	public void testAllTheControlsWeWantAreThere() throws Exception {
		pushModel(initialModel());
		dumpControl("", shell);
		assertControls(shell, "hexagons.lastBuild", "hexagons.acceptance.1");
		pushModel(updatedModel());
		dumpControl("", shell);
		assertControls(shell, "hexagons.lastBuild", "hexagons.acceptance.1", "hexagons.acceptance.2");
	}
	
	protected HexagonDataModel initialModel() {
		HexagonDataModel testModel = context.mock(HexagonDataModel.class, "initial");
		ArrayList<BarData> accList = new ArrayList<BarData>();
		BarData a = context.mock(BarData.class, "ia");
		accList.add(a);
		context.checking(new Expectations() {{
			allowing(testModel).getBuildTime(); will(returnValue(exactDate(2017, 04, 20, 04, 20, 00, 420)));
			allowing(testModel).getAcceptanceTests(); will(returnValue(accList));
			allowing(a).getId(); will(returnValue("acceptance.1"));
			allowing(a).getTotal(); will(returnValue(10));
			allowing(a).getComplete(); will(returnValue(3));
			allowing(a).getStatus(); will(returnValue(Status.OK));
		}});
		return testModel;
	}
	
	protected HexagonDataModel updatedModel() {
		HexagonDataModel testModel = context.mock(HexagonDataModel.class, "updated");
		ArrayList<BarData> accList = new ArrayList<BarData>();
		BarData a = context.mock(BarData.class, "ua");
		accList.add(a);
		BarData b = context.mock(BarData.class, "ub");
		accList.add(b);
		context.checking(new Expectations() {{
			allowing(testModel).getBuildTime(); will(returnValue(exactDate(2017, 04, 20, 04, 20, 00, 420)));
			allowing(testModel).getAcceptanceTests(); will(returnValue(accList));
			allowing(a).getId(); will(returnValue("acceptance.1"));
			allowing(a).getTotal(); will(returnValue(10));
			allowing(a).getComplete(); will(returnValue(3));
			allowing(a).getStatus(); will(returnValue(Status.OK));
			allowing(b).getId(); will(returnValue("acceptance.2"));
			allowing(b).getTotal(); will(returnValue(6));
			allowing(b).getComplete(); will(returnValue(4));
			allowing(b).getStatus(); will(returnValue(Status.FAILURES));
		}});
		return testModel;
	}
}
