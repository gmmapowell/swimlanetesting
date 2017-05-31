package com.gmmapowell.swimlane.tests.view.hex;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.BarDataListener;
import com.gmmapowell.swimlane.eclipse.interfaces.HexData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel.Status;
import com.gmmapowell.swimlane.tests.swtutil.ImageChecker;
import com.gmmapowell.swimlane.tests.swtutil.ImageProxy;

public class UtilityBar extends BaseViewTest {
	
	@Test
	public void testAllTheControlsWeWantAreThere() throws Exception {
		specifyModel(10, 0, Status.OK);
		assertControls(shell, "hexagons.utility");
	}
	
	@Test
	public void testTheUtilityBarLooksRightWhenNoTestsHaveRun() throws Exception {
		specifyModel(10, 0, Status.OK);
		Canvas acceptance = waitForControl(shell, "hexagons.utility");
		checkSizeColors(acceptance, 590, 6, new ImageChecker() {
			@Override
			public void checkImage(ImageProxy proxy) {
				proxy.assertColorOfPixel(SWT.COLOR_GRAY, 5, 3);
				proxy.assertColorOfPixel(SWT.COLOR_GRAY, 500, 3);
			}
		});
	}

	@Test
	public void testTheUtilityBarLooksRightWhenFiveTestsHaveRunSuccessfully() throws Exception {
		specifyModel(10, 5, Status.OK);
		Canvas acceptance = waitForControl(shell, "hexagons.utility");
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
	public void testTheUtilityBarLooksRightWhenFiveTestsHaveRunWithFailures() throws Exception {
		specifyModel(10, 5, Status.FAILURES);
		Canvas acceptance = waitForControl(shell, "hexagons.utility");
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
			allowing(uteBar).getStatus(); will(returnValue(status));
			allowing(uteBar).getMarks(); will(returnValue(new int[] { 1 }));
			
			oneOf(md).addBarListener(with(uteBar), with(aNonNull(BarDataListener.class)));
		}});
		return testModel;
	}
}