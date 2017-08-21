package com.gmmapowell.swimlane.tests.view.hex;

public class MultipleAcceptanceBars extends BaseHexViewTest {
	/*
	@Test
	public void testAllTheControlsWeWantAreThere() throws Exception {
		specifyModel();
		assertControls(shell, "swimlane.acceptance.1", "swimlane.acceptance.2", "swimlane.acceptance.3");
	}
	
	protected void specifyModel() throws InterruptedException {
		pushModel(defineModel());
	}

	protected HexagonDataModel defineModel() {
		HexagonDataModel testModel = context.mock(HexagonDataModel.class);
		ArrayList<BarData> accList = new ArrayList<BarData>();
		BarData a = context.mock(BarData.class, "a");
		accList.add(a);
		BarData b = context.mock(BarData.class, "b");
		accList.add(b);
		BarData c = context.mock(BarData.class, "c");
		accList.add(c);
		context.checking(new Expectations() {{
			allowing(testModel).getHexCount(); will(returnValue(1));
			allowing(testModel).getBuildTime(); will(returnValue(exactDate(2017, 04, 20, 04, 20, 00, 420)));
			allowing(testModel).getAcceptanceTests(); will(returnValue(accList));
			allowing(testModel).getHexagons(); will(returnValue(new ArrayList<HexData>()));
			allowing(testModel).getUtilityBar(); will(returnValue(null));
			allowing(a).getId(); will(returnValue("acceptance.1"));
			allowing(a).getTotal(); will(returnValue(10));
			allowing(a).getComplete(); will(returnValue(3));
			allowing(a).getPassed(); will(returnValue(3));
			allowing(a).getFailures(); will(returnValue(0));
			allowing(a).getStatus(); will(returnValue(Status.OK));
			allowing(a).getMarks(); will(returnValue(new int[] { 1 }));
			allowing(b).getId(); will(returnValue("acceptance.2"));
			allowing(b).getTotal(); will(returnValue(6));
			allowing(b).getComplete(); will(returnValue(4));
			allowing(b).getPassed(); will(returnValue(4));
			allowing(b).getFailures(); will(returnValue(0));
			allowing(b).getStatus(); will(returnValue(Status.FAILURES));
			allowing(b).getMarks(); will(returnValue(new int[] { 1 }));
			allowing(c).getId(); will(returnValue("acceptance.3"));
			allowing(c).getTotal(); will(returnValue(12));
			allowing(c).getComplete(); will(returnValue(11));
			allowing(c).getPassed(); will(returnValue(11));
			allowing(c).getFailures(); will(returnValue(0));
			allowing(c).getStatus(); will(returnValue(Status.NONE));
			allowing(c).getMarks(); will(returnValue(new int[] { 1 }));


			oneOf(md).addBarListener(with(a), with(aNonNull(BarDataListener.class)));
			oneOf(md).addBarListener(with(b), with(aNonNull(BarDataListener.class)));
			oneOf(md).addBarListener(with(c), with(aNonNull(BarDataListener.class)));
		}});
		return testModel;
	}
	*/
}
