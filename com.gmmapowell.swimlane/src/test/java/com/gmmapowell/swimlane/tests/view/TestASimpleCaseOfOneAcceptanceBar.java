package com.gmmapowell.swimlane.tests.view;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Label;
import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.models.BarData;
import com.gmmapowell.swimlane.eclipse.models.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.models.HexagonDataModel.Status;

public class TestASimpleCaseOfOneAcceptanceBar extends AViewTest {
	
	@Test
	public void testAllTheControlsWeWantAreThere() throws Exception {
		specifyModel(10, 0, Status.OK);
		assertControls(shell, "hexagons.lastBuild", "hexagons.acceptance.1");
	}
	
	@Test
	public void testTheBuildLabelHasTheRightTime() throws Exception {
		specifyModel(10, 0, Status.OK);
        Label lastBuild = waitForControl(shell, "hexagons.lastBuild");
        assertEquals("042000.420", lastBuild.getText());
	}

	@Test
	public void testTheAcceptanceRowLooksRightWhenNoTestsHaveRun() throws Exception {
		specifyModel(10, 0, Status.OK);
		Canvas acceptance = waitForControl(shell, "hexagons.acceptance.1");
		checkSizeColors(acceptance, 590, 6, new ImageChecker() {
			@Override
			public void checkImage(ImageProxy proxy) {
				proxy.assertColorOfPixel(SWT.COLOR_GRAY, 5, 3);
				proxy.assertColorOfPixel(SWT.COLOR_GRAY, 500, 3);
			}
		});
	}

	@Test
	public void testTheAcceptanceRowLooksRightWhenFiveTestsHaveRunSuccessfully() throws Exception {
		specifyModel(10, 5, Status.OK);
		Canvas acceptance = waitForControl(shell, "hexagons.acceptance.1");
		checkSizeColors(acceptance, 590, 6, new ImageChecker() {
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
	public void testTheAcceptanceRowLooksRightWhenFiveTestsHaveRunWithFailures() throws Exception {
		specifyModel(10, 5, Status.FAILURES);
		Canvas acceptance = waitForControl(shell, "hexagons.acceptance.1");
		checkSizeColors(acceptance, 590, 6, new ImageChecker() {
			@Override
			public void checkImage(ImageProxy proxy) {
				proxy.assertColorOfPixel(SWT.COLOR_RED, 290, 3);
				proxy.assertColorOfPixel(SWT.COLOR_GRAY, 300, 3);
			}
		});
	}

	protected void specifyModel(int total, int complete, Status status) throws InterruptedException {
		pushModel(defineModel(total, complete, status));
	}

	protected HexagonDataModel defineModel(int total, int complete, Status status) {
		HexagonDataModel testModel = context.mock(HexagonDataModel.class);
		ArrayList<BarData> accList = new ArrayList<BarData>();
		BarData a = context.mock(BarData.class);
		accList.add(a);
		context.checking(new Expectations() {{
			allowing(testModel).getHexCount(); will(returnValue(1));
			allowing(testModel).getBuildTime(); will(returnValue(exactDate(2017, 04, 20, 04, 20, 00, 420)));
			allowing(testModel).getAcceptanceTests(); will(returnValue(accList));
			allowing(a).getId(); will(returnValue("acceptance.1"));
			allowing(a).getTotal(); will(returnValue(total));
			allowing(a).getComplete(); will(returnValue(complete));
			allowing(a).getStatus(); will(returnValue(status));
			allowing(a).getMarks(); will(returnValue(1));
		}});
		return testModel;
	}
}
