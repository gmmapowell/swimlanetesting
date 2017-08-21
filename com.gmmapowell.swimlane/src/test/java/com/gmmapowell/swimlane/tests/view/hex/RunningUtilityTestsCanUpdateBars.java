package com.gmmapowell.swimlane.tests.view.hex;

public class RunningUtilityTestsCanUpdateBars extends BaseHexViewTest {
	/*
	@Test
	@Ignore // anything with image checker is broken in Oxygen
	public void testWeCanGoFromNoBarToABar() throws Exception {
		pushModel(modelWith("initial", null));
		assertControls(shell); // there aren't any
		
		// OK, moving on
		pushModel(modelWith("updated", a(null)));
		assertControls(shell, "swimlane.utility"); // the ute bar appears
		
		Canvas ute = waitForControl(shell, "swimlane.utility");
		assertEquals(0, ute.getBounds().x);
		assertEquals(282, ute.getBounds().y);
		checkSizeColors(ute, 590, 6, new ImageChecker() {
			@Override
			public void checkImage(ImageProxy proxy) {
				proxy.assertColorOfPixel(SWT.COLOR_GREEN, 105, 3);
				proxy.assertColorOfPixel(SWT.COLOR_GRAY, 300, 3);
				proxy.assertColorOfPixel(SWT.COLOR_GRAY, 585, 3);
			}
		});
	}

	@Test
	@Ignore // anything with image checker is broken in Oxygen
	public void testUpdatingABarUpdatesIt() throws Exception {
		BarData ea = empty();
		pushModel(modelWith("initial", ea));
		assertControls(shell, "swimlane.utility"); // the ute bar appears
		
		// OK, moving on
		pushModel(modelWith("updated", a(ea)));
		assertControls(shell, "swimlane.utility"); // still there, just one ...
		
		Canvas ute = waitForControl(shell, "swimlane.utility");
		assertEquals(0, ute.getBounds().x);
		assertEquals(282, ute.getBounds().y);
		checkSizeColors(ute, 590, 6, new ImageChecker() {
			@Override
			public void checkImage(ImageProxy proxy) {
				proxy.assertColorOfPixel(SWT.COLOR_GREEN, 105, 3);
				proxy.assertColorOfPixel(SWT.COLOR_GRAY, 300, 3);
				proxy.assertColorOfPixel(SWT.COLOR_GRAY, 585, 3);
			}
		});
	}

	protected HexagonDataModel modelWith(String s, BarData ute) {
		HexagonDataModel testModel = context.mock(HexagonDataModel.class, s);
		context.checking(new Expectations() {{
			allowing(testModel).getHexCount(); will(returnValue(1));
			allowing(testModel).getBuildTime(); will(returnValue(exactDate(2017, 04, 20, 04, 20, 00, 420)));
			allowing(testModel).getAcceptanceTests(); will(returnValue(new ArrayList<BarData>()));
			allowing(testModel).getHexagons(); will(returnValue(new ArrayList<HexData>()));
			allowing(testModel).getUtilityBar(); will(returnValue(ute));
		}});
		return testModel;
	}
	
	protected BarData empty() {
		BarData a = context.mock(BarData.class, "ea");
		context.checking(new Expectations() {{
			allowing(a).getId(); will(returnValue("utility"));
			allowing(a).getTotal(); will(returnValue(0));
			allowing(a).getComplete(); will(returnValue(0));
			allowing(a).getPassed(); will(returnValue(0));
			allowing(a).getFailures(); will(returnValue(0));
			allowing(a).getStatus(); will(returnValue(Status.OK));
			allowing(a).getMarks(); will(returnValue(new int[] { 1 }));
			oneOf(md).addBarListener(with(a), with(aNonNull(BarDataListener.class)));
		}});
		return a;
	}
	
	protected BarData a(BarData old) {
		BarData a = context.mock(BarData.class, "ua");
		context.checking(new Expectations() {{
			allowing(a).getId(); will(returnValue("utility"));
			allowing(a).getTotal(); will(returnValue(10));
			allowing(a).getComplete(); will(returnValue(5));
			allowing(a).getPassed(); will(returnValue(5));
			allowing(a).getFailures(); will(returnValue(0));
			allowing(a).getStatus(); will(returnValue(Status.OK));
			allowing(a).getMarks(); will(returnValue(new int[] { 1 }));
			oneOf(md).addBarListener(with(a), with(aNonNull(BarDataListener.class)));
			if (old != null)
				oneOf(md).removeBarListener(with(old), with(aNonNull(BarDataListener.class)));
		}});
		return a;
	}
	*/
}
