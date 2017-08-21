package com.gmmapowell.swimlane.tests.view.hex;

public class PartialAcceptanceBars extends BaseHexViewTest {
	/*
	@Test
	public void testAllTheControlsWeWantAreThere() throws Exception {
		specifyModel();
		assertControlsInOrder(shell, "swimlane.acceptance.111", "swimlane.acceptance.110", "swimlane.acceptance.101", "swimlane.acceptance.011");
	}
	
	@Test
	@Ignore // anything with image checker is broken in Oxygen
	public void testFirstBarIsComplete() throws Exception {
		specifyModel();
		Canvas acceptance = waitForControl(shell, "swimlane.acceptance.111");
		checkSizeColors(acceptance, 590, 6, new ImageChecker() {
			@Override
			public void checkImage(ImageProxy proxy) {
				proxy.assertColorOfPixel(SWT.COLOR_GREEN, 105, 3);
				proxy.assertColorOfPixel(SWT.COLOR_GRAY, 300, 3);
				proxy.assertColorOfPixel(SWT.COLOR_GRAY, 500, 3);
			}
		});
	}

	@Test
	@Ignore // anything with image checker is broken in Oxygen
	public void testSecondBarIsMissingRHS() throws Exception {
		specifyModel();
		Canvas acceptance = waitForControl(shell, "swimlane.acceptance.110");
		checkSizeColors(acceptance, 590, 6, new ImageChecker() {
			@Override
			public void checkImage(ImageProxy proxy) {
				proxy.assertColorOfPixel(SWT.COLOR_RED, 105, 3);
				proxy.assertColorOfPixel(SWT.COLOR_GRAY, 300, 3);
				proxy.assertColorOfPixel(SWT.COLOR_WIDGET_BACKGROUND, 500, 3);
			}
		});
	}

	@Test
	@Ignore // anything with image checker is broken in Oxygen
	public void testThirdBarIsMissingLHS() throws Exception {
		specifyModel();
		Canvas acceptance = waitForControl(shell, "swimlane.acceptance.101");
		checkSizeColors(acceptance, 590, 6, new ImageChecker() {
			@Override
			public void checkImage(ImageProxy proxy) {
				proxy.assertColorOfPixel(SWT.COLOR_WIDGET_BACKGROUND, 300, 3);
				proxy.assertColorOfPixel(SWT.COLOR_YELLOW, 105, 3);
				proxy.assertColorOfPixel(SWT.COLOR_YELLOW, 550, 3);
				proxy.assertColorOfPixel(SWT.COLOR_GRAY, 560, 3);
			}
		});
	}

	@Test
	@Ignore // anything with image checker is broken in Oxygen
	public void testFourthBarIsMissingLHS() throws Exception {
		specifyModel();
		Canvas acceptance = waitForControl(shell, "swimlane.acceptance.011");
		checkSizeColors(acceptance, 590, 6, new ImageChecker() {
			@Override
			public void checkImage(ImageProxy proxy) {
				proxy.assertColorOfPixel(SWT.COLOR_WIDGET_BACKGROUND, 105, 3);
				proxy.assertColorOfPixel(SWT.COLOR_GREEN, 300, 3);
				proxy.assertColorOfPixel(SWT.COLOR_GREEN, 550, 3);
				proxy.assertColorOfPixel(SWT.COLOR_GRAY, 560, 3);
			}
		});
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
		BarData d = context.mock(BarData.class, "d");
		accList.add(d);
		context.checking(new Expectations() {{
			allowing(testModel).getHexCount(); will(returnValue(3));
			allowing(testModel).getBuildTime(); will(returnValue(exactDate(2017, 04, 20, 04, 20, 00, 420)));
			allowing(testModel).getAcceptanceTests(); will(returnValue(accList));
			allowing(testModel).getHexagons(); will(returnValue(new ArrayList<HexData>()));
			allowing(testModel).getUtilityBar(); will(returnValue(null));
			allowing(a).getId(); will(returnValue("acceptance.111"));
			allowing(a).getTotal(); will(returnValue(10));
			allowing(a).getComplete(); will(returnValue(3));
			allowing(a).getPassed(); will(returnValue(3));
			allowing(a).getFailures(); will(returnValue(0));
			allowing(a).getStatus(); will(returnValue(Status.OK));
			allowing(a).getMarks(); will(returnValue(new int[] { 1, 1, 1}));
			allowing(b).getId(); will(returnValue("acceptance.110"));
			allowing(b).getTotal(); will(returnValue(6));
			allowing(b).getComplete(); will(returnValue(2));
			allowing(b).getPassed(); will(returnValue(2));
			allowing(b).getFailures(); will(returnValue(0));
			allowing(b).getStatus(); will(returnValue(Status.FAILURES));
			allowing(b).getMarks(); will(returnValue(new int[] { 1, 1, 0}));
			allowing(c).getId(); will(returnValue("acceptance.101"));
			allowing(c).getTotal(); will(returnValue(12));
			allowing(c).getComplete(); will(returnValue(11));
			allowing(c).getPassed(); will(returnValue(11));
			allowing(c).getFailures(); will(returnValue(0));
			allowing(c).getStatus(); will(returnValue(Status.SKIPPED));
			allowing(c).getMarks(); will(returnValue(new int[] { 1, 0, 1}));
			allowing(d).getId(); will(returnValue("acceptance.011"));
			allowing(d).getTotal(); will(returnValue(12));
			allowing(d).getComplete(); will(returnValue(11));
			allowing(d).getPassed(); will(returnValue(11));
			allowing(d).getFailures(); will(returnValue(0));
			allowing(d).getStatus(); will(returnValue(Status.OK));
			allowing(d).getMarks(); will(returnValue(new int[] { 0, 1, 1}));

			oneOf(md).addBarListener(with(a), with(aNonNull(BarDataListener.class)));
			oneOf(md).addBarListener(with(b), with(aNonNull(BarDataListener.class)));
			oneOf(md).addBarListener(with(c), with(aNonNull(BarDataListener.class)));
			oneOf(md).addBarListener(with(d), with(aNonNull(BarDataListener.class)));
		}});
		return testModel;
	}
	*/
}
