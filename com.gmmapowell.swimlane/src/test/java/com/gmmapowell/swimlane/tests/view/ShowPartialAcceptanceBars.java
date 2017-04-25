package com.gmmapowell.swimlane.tests.view;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.models.BarData;
import com.gmmapowell.swimlane.eclipse.models.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.models.HexagonDataModel.Status;

public class ShowPartialAcceptanceBars extends AViewTest {
	
	@Test
	public void testAllTheControlsWeWantAreThere() throws Exception {
		specifyModel();
		assertControlsInOrder(shell, "hexagons.lastBuild", "hexagons.acceptance.111", "hexagons.acceptance.110", "hexagons.acceptance.101", "hexagons.acceptance.011");
	}
	
	@Test
	public void testFirstBarIsComplete() throws Exception {
		specifyModel();
		Canvas acceptance = waitForControl(shell, "hexagons.acceptance.111");
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
	public void testSecondBarIsMissingRHS() throws Exception {
		specifyModel();
		Canvas acceptance = waitForControl(shell, "hexagons.acceptance.110");
		checkSizeColors(acceptance, 590, 6, new ImageChecker() {
			@Override
			public void checkImage(ImageProxy proxy) {
				proxy.assertColorOfPixel(SWT.COLOR_RED, 105, 3);
				proxy.assertColorOfPixel(SWT.COLOR_GRAY, 300, 3);
				proxy.assertColorOfPixel(SWT.COLOR_WIDGET_BACKGROUND, 500, 3);
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
			allowing(a).getId(); will(returnValue("acceptance.111"));
			allowing(a).getTotal(); will(returnValue(10));
			allowing(a).getComplete(); will(returnValue(3));
			allowing(a).getStatus(); will(returnValue(Status.OK));
			allowing(a).getMarks(); will(returnValue(3));
			allowing(b).getId(); will(returnValue("acceptance.110"));
			allowing(b).getTotal(); will(returnValue(6));
			allowing(b).getComplete(); will(returnValue(2));
			allowing(b).getStatus(); will(returnValue(Status.FAILURES));
			allowing(b).getMarks(); will(returnValue(2));
			allowing(c).getId(); will(returnValue("acceptance.101"));
			allowing(c).getTotal(); will(returnValue(12));
			allowing(c).getComplete(); will(returnValue(11));
			allowing(c).getStatus(); will(returnValue(Status.NONE));
			allowing(c).getMarks(); will(returnValue(2));
			allowing(d).getId(); will(returnValue("acceptance.011"));
			allowing(d).getTotal(); will(returnValue(12));
			allowing(d).getComplete(); will(returnValue(11));
			allowing(d).getStatus(); will(returnValue(Status.NONE));
			allowing(d).getMarks(); will(returnValue(2));
		}});
		return testModel;
	}
}