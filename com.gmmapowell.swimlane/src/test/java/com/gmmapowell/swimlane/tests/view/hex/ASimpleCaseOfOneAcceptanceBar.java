package com.gmmapowell.swimlane.tests.view.hex;

import static org.junit.Assert.assertEquals;

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

public class ASimpleCaseOfOneAcceptanceBar extends BaseViewTest {
	
	@Test
	public void testAllTheControlsWeWantAreThere() throws Exception {
		specifyModel(10, 0, Status.OK);
		assertControls(shell, "hexagons.acceptance.1");
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
	public void testTheTooltipWhenNoTestsHaveRun() throws Exception {
		specifyModel(10, 0, Status.OK);
		Canvas acceptance = waitForControl(shell, "hexagons.acceptance.1");
		assertEquals("Acceptance - 1 group; 0 passed", acceptance.getToolTipText());
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
	public void testTheTooltipWhenFiveTestsHaveRunSuccessfully() throws Exception {
		specifyModel(10, 5, Status.OK);
		Canvas acceptance = waitForControl(shell, "hexagons.acceptance.1");
		assertEquals("Acceptance - 1 group; 5 passed", acceptance.getToolTipText());
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
			allowing(testModel).getHexagons(); will(returnValue(new ArrayList<HexData>()));
			allowing(testModel).getUtilityBar(); will(returnValue(null));
			allowing(a).getId(); will(returnValue("acceptance.1"));
			allowing(a).getTotal(); will(returnValue(total));
			allowing(a).getComplete(); will(returnValue(complete));
			allowing(a).getStatus(); will(returnValue(status));
			allowing(a).getMarks(); will(returnValue(new int[] { 1 }));
			
			oneOf(md).addBarListener(with(a), with(aNonNull(BarDataListener.class)));
		}});
		return testModel;
	}
}
