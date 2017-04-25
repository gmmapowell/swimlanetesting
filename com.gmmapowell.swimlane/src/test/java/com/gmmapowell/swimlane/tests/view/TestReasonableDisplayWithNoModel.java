package com.gmmapowell.swimlane.tests.view;

import java.util.ArrayList;

import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.models.BarData;
import com.gmmapowell.swimlane.eclipse.models.HexagonDataModel;

// As the name implies, we want to test what happens to the view part display
// when there is no data model in place
public class TestReasonableDisplayWithNoModel extends AViewTest {

	@Test
	public void testAllTheControlsWeWantAreThere() throws Exception {
		pushModel(defineModel());
		assertControls(shell, "hexagons.lastBuild");
	}
	
	protected HexagonDataModel defineModel() {
		HexagonDataModel testModel = context.mock(HexagonDataModel.class);
		ArrayList<BarData> accList = new ArrayList<BarData>();
		context.checking(new Expectations() {{
			allowing(testModel).getBuildTime(); will(returnValue(exactDate(2017, 04, 20, 04, 20, 00, 420)));
			allowing(testModel).getAcceptanceTests(); will(returnValue(accList));
		}});
		return testModel;
	}
}
