package com.gmmapowell.swimlane.tests.view.hex;

import java.util.ArrayList;

import org.jmock.Expectations;
import org.jmock.States;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel.Status;
import com.gmmapowell.swimlane.eclipse.interfaces.PortData;

public class UpdatingDoesNotCreateNewHexes extends BaseViewTest {
	States mode = context.states("mode").startsAs("initial");
	private BarData bd;
	
	@Test
	public void testAllTheControlsWeWantAreThere() throws Exception {
		HexagonDataModel hdm = specifyModel(10, 0, Status.NONE);
		pushModel(hdm);
		assertControls(shell, "hexagons.hex.1.bg", "hexagons.hex.1.bar");
	}
	
	protected HexagonDataModel specifyModel(int total, int complete, Status status) throws InterruptedException {
		return pushModel(defineModel(total, complete, status));
	}

	protected HexagonDataModel defineModel(int total, int complete, Status status) {
		HexagonDataModel testModel = context.mock(HexagonDataModel.class);
		ArrayList<BarData> accList = new ArrayList<BarData>();
		ArrayList<HexData> hexagons = new ArrayList<HexData>();
		HexData hd = context.mock(HexData.class);
		hexagons.add(hd);
		bd = context.mock(BarData.class);
		context.checking(new Expectations() {{
			allowing(testModel).getHexCount(); will(returnValue(1));
			allowing(testModel).getBuildTime(); will(returnValue(exactDate(2017, 04, 20, 04, 20, 00, 420)));
			allowing(testModel).getAcceptanceTests(); will(returnValue(accList));
			allowing(testModel).getHexagons(); will(returnValue(hexagons));
			allowing(hd).getId(); will(returnValue("hex.1"));
			allowing(hd).getBar(); will(returnValue(bd));
			allowing(hd).getPorts(); will(returnValue(new ArrayList<PortData>()));
			allowing(bd).getTotal(); will(returnValue(total));
			allowing(bd).getComplete(); will(returnValue(complete)); when(mode.is("initial"));
			allowing(bd).getComplete(); will(returnValue(complete+5)); when(mode.is("plus5"));
			allowing(bd).getStatus(); will(returnValue(status)); when(mode.is("initial"));
			allowing(bd).getStatus(); will(returnValue(Status.OK)); when(mode.is("plus5"));
			allowing(bd).getMarks(); will(returnValue(new int[] { 1 }));
		}});
		return testModel;
	}
}