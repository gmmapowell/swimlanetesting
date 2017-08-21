package com.gmmapowell.swimlane.tests.view.hex;

public class UtilityBar extends BaseHexViewTest {
	/*
	@Test
	public void testAllTheControlsWeWantAreThere() throws Exception {
		specifyModel(10, 0, 0, Status.OK);
		assertControls(shell, "swimlane.utility");
	}
	
	@Test
	@Ignore // anything with image checker is broken in Oxygen
	public void testTheUtilityBarLooksRightWhenNoTestsHaveRun() throws Exception {
		specifyModel(10, 0, 0, Status.OK);
		Canvas ute = waitForControl(shell, "swimlane.utility");
		checkSizeColors(ute, 590, 6, new ImageChecker() {
			@Override
			public void checkImage(ImageProxy proxy) {
				proxy.assertColorOfPixel(SWT.COLOR_GRAY, 5, 3);
				proxy.assertColorOfPixel(SWT.COLOR_GRAY, 500, 3);
			}
		});
	}
	
	@Test
	public void testTheTooltipWhenNoTestsHaveRun() throws Exception {
		specifyModel(10, 0, 0, Status.OK);
		Canvas ute = waitForControl(shell, "swimlane.utility");
		assertEquals("Utilities - 1 group; 0 passed", ute.getToolTipText());
	}

	@Test
	@Ignore // anything with image checker is broken in Oxygen
	public void testTheUtilityBarLooksRightWhenFiveTestsHaveRunSuccessfully() throws Exception {
		specifyModel(10, 5, 0, Status.OK);
		Canvas ute = waitForControl(shell, "swimlane.utility");
		checkSizeColors(ute, 590, 6, new ImageChecker() {
			@Override
			public void checkImage(ImageProxy proxy) {
				proxy.assertColorOfPixel(SWT.COLOR_GREEN, 5, 3);
				proxy.assertColorOfPixel(SWT.COLOR_GREEN, 292, 3);
				proxy.assertColorOfPixel(SWT.COLOR_GRAY, 297, 3);
				proxy.assertColorOfPixel(SWT.COLOR_GRAY, 500, 3);
			}
		});
	}

	@Test
	@Ignore // anything with image checker is broken in Oxygen
	public void testTheUtilityBarLooksRightWhenFiveTestsHaveRunWithFailures() throws Exception {
		specifyModel(10, 5, 1, Status.FAILURES);
		Canvas ute = waitForControl(shell, "swimlane.utility");
		checkSizeColors(ute, 590, 6, new ImageChecker() {
			@Override
			public void checkImage(ImageProxy proxy) {
				proxy.assertColorOfPixel(SWT.COLOR_RED, 290, 3);
				proxy.assertColorOfPixel(SWT.COLOR_GRAY, 300, 3);
			}
		});
	}

	@Test
	public void testTheTooltipWhenThereAreSomeFailures() throws Exception {
		specifyModel(10, 5, 2, Status.OK);
		Canvas ute = waitForControl(shell, "swimlane.utility");
		assertEquals("Utilities - 1 group; 3 passed, 2 failures", ute.getToolTipText());
	}

	protected void specifyModel(int total, int complete, int failures, Status status) throws InterruptedException {
		pushModel(defineModel(total, complete, failures, status));
	}

	protected HexagonDataModel defineModel(int total, int complete, int failures, Status status) {
		HexagonDataModel testModel = context.mock(HexagonDataModel.class);
		BarData uteBar = context.mock(BarData.class);
		context.checking(new Expectations() {{
			allowing(testModel).getHexCount(); will(returnValue(0));
			allowing(testModel).getBuildTime(); will(returnValue(exactDate(2017, 04, 20, 04, 20, 00, 420)));
			allowing(testModel).getAcceptanceTests(); will(returnValue(new ArrayList<>()));
			allowing(testModel).getHexagons(); will(returnValue(new ArrayList<HexData>()));
			allowing(testModel).getUtilityBar(); will(returnValue(uteBar));
			allowing(uteBar).getId(); will(returnValue("utility"));
			allowing(uteBar).getTotal(); will(returnValue(total));
			allowing(uteBar).getComplete(); will(returnValue(complete));
			allowing(uteBar).getPassed(); will(returnValue(complete - failures));
			allowing(uteBar).getFailures(); will(returnValue(failures));
			allowing(uteBar).getStatus(); will(returnValue(status));
			allowing(uteBar).getMarks(); will(returnValue(new int[] { 1 }));
			
			oneOf(md).addBarListener(with(uteBar), with(aNonNull(BarDataListener.class)));
		}});
		return testModel;
	}
	*/
}
