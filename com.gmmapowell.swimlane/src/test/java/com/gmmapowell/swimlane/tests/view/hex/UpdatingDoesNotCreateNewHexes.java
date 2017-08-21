package com.gmmapowell.swimlane.tests.view.hex;

public class UpdatingDoesNotCreateNewHexes extends BaseHexViewTest {
	/*
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
			allowing(testModel).getUtilityBar(); will(returnValue(null));
			allowing(hd).getId(); will(returnValue("hex.1"));
			allowing(hd).getBar(); will(returnValue(bd));
			allowing(hd).getPorts(); will(returnValue(new ArrayList<PortData>()));
			allowing(bd).getId(); will(returnValue("bar1"));
			allowing(bd).getName(); will(returnValue("org.sample.Hex1"));
			allowing(bd).getTotal(); will(returnValue(total));
			allowing(bd).getComplete(); will(returnValue(complete)); when(mode.is("initial"));
			allowing(bd).getPassed(); will(returnValue(complete));
			allowing(bd).getFailures(); will(returnValue(0));
			allowing(bd).getComplete(); will(returnValue(complete+5)); when(mode.is("plus5"));
			allowing(bd).getStatus(); will(returnValue(status)); when(mode.is("initial"));
			allowing(bd).getStatus(); will(returnValue(Status.OK)); when(mode.is("plus5"));
			allowing(bd).getMarks(); will(returnValue(new int[] { 1 }));

			oneOf(md).addBarListener(with(bd), with(aNonNull(BarDataListener.class)));
		}});
		return testModel;
	}
	*/
}
