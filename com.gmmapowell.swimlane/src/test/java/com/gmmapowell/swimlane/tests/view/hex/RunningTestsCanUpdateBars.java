package com.gmmapowell.swimlane.tests.view.hex;

public class RunningTestsCanUpdateBars extends BaseHexViewTest {
	/*
	@Test
	@Ignore // anything with image checker is broken in Oxygen
	public void testTheBarCanBeRedrawnProgressivelyMoreGreen() throws Exception {
		States progress = context.states("progress");
		progress.become("initial");
		BarData a = a();
		context.checking(new Expectations() {{
			allowing(a).getStatus(); will(returnValue(Status.OK));
			allowing(a).getComplete(); will(returnValue(0)); when(progress.is("initial"));
			allowing(a).getComplete(); will(returnValue(5)); when(progress.is("halfway"));
			allowing(a).getComplete(); will(returnValue(10)); when(progress.is("complete"));
		}});
		pushModel(modelWith("initial", a));
		Canvas acceptance = waitForControl(shell, "swimlane.acceptance.11");
		checkSizeColors(acceptance, 590, 6, new ImageChecker() {
			@Override
			public void checkImage(ImageProxy proxy) {
				proxy.assertColorOfPixel(SWT.COLOR_GRAY, 105, 3);
				proxy.assertColorOfPixel(SWT.COLOR_GRAY, 300, 3);
				proxy.assertColorOfPixel(SWT.COLOR_GRAY, 585, 3);
			}
		});
		progress.become("halfway");
//		fmd.barChanged(a);
		displayHelper.flushPendingEvents();
		checkSizeColors(acceptance, 590, 6, new ImageChecker() {
			@Override
			public void checkImage(ImageProxy proxy) {
				proxy.assertColorOfPixel(SWT.COLOR_GREEN, 105, 3);
				proxy.assertColorOfPixel(SWT.COLOR_GRAY, 300, 3);
				proxy.assertColorOfPixel(SWT.COLOR_GRAY, 585, 3);
			}
		});
		progress.become("complete");
//		fmd.barChanged(a);
		displayHelper.flushPendingEvents();
		checkSizeColors(acceptance, 590, 6, new ImageChecker() {
			@Override
			public void checkImage(ImageProxy proxy) {
				proxy.assertColorOfPixel(SWT.COLOR_GREEN, 105, 3);
				proxy.assertColorOfPixel(SWT.COLOR_GREEN, 300, 3);
				proxy.assertColorOfPixel(SWT.COLOR_GREEN, 585, 3);
			}
		});
	}
	
	protected HexagonDataModel modelWith(String s, BarData... bars) {
		HexagonDataModel testModel = context.mock(HexagonDataModel.class, s);
		ArrayList<BarData> accList = new ArrayList<BarData>();
		for (BarData b : bars) {
			accList.add(b);
			context.checking(new Expectations() {{
				oneOf(md).addBarListener(with(b), with(aNonNull(BarDataListener.class)));
			}});
		}
		context.checking(new Expectations() {{
			allowing(testModel).getHexCount(); will(returnValue(1));
			allowing(testModel).getBuildTime(); will(returnValue(exactDate(2017, 04, 20, 04, 20, 00, 420)));
			allowing(testModel).getAcceptanceTests(); will(returnValue(accList));
			allowing(testModel).getHexagons(); will(returnValue(new ArrayList<HexData>()));
			allowing(testModel).getUtilityBar(); will(returnValue(null));
		}});
		return testModel;
	}
	
	protected BarData a() {
		BarData a = context.mock(BarData.class, "ia");
		context.checking(new Expectations() {{
			allowing(a).getId(); will(returnValue("acceptance.11"));
			allowing(a).getTotal(); will(returnValue(10));
			allowing(a).getMarks(); will(returnValue(new int[] { 1 }));
			allowing(a).getPassed(); will(returnValue(3));
			allowing(a).getFailures(); will(returnValue(0));
			exactly(2).of(md).barChanged(a);
		}});
		return a;
	}
	*/
}
