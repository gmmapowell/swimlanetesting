package com.gmmapowell.swimlane.tests.view.hex;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Canvas;
import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.tests.swtutil.ImageChecker;
import com.gmmapowell.swimlane.tests.swtutil.ImageProxy;

public class ShowingOneHexBlock extends BaseViewTest {
	
	@Test
	public void testAllTheControlsWeWantAreThere() throws Exception {
		specifyModel();
		assertControls(shell, "hexagons.hex.1");
	}
	
	@Test
	public void testTheHexagonHasAHexBackgroundBeforeWeStart() throws Exception {
		specifyModel();
		Canvas hexagon = waitForControl(shell, "hexagons.hex.1");
		checkSizeColors(hexagon, 590, 290, new ImageChecker() {
			@Override
			public void checkImage(ImageProxy proxy) {
				Color expected = new Color(displayHelper.getDisplay(), 220, 220, 170); 
				proxy.assertColorOfPixel(expected, 295, 147);
				proxy.assertColorOfPixel(SWT.COLOR_WIDGET_BACKGROUND, 500, 3);
				expected.dispose();
			}
		});
	}

	protected void specifyModel() throws InterruptedException {
		pushModel(defineModel());
	}

	protected HexagonDataModel defineModel() {
		HexagonDataModel testModel = context.mock(HexagonDataModel.class);
		ArrayList<BarData> accList = new ArrayList<BarData>();
		ArrayList<HexData> hexagons = new ArrayList<HexData>();
		HexData hd = context.mock(HexData.class);
		hexagons.add(hd);
		context.checking(new Expectations() {{
			allowing(testModel).getHexCount(); will(returnValue(1));
			allowing(testModel).getBuildTime(); will(returnValue(exactDate(2017, 04, 20, 04, 20, 00, 420)));
			allowing(testModel).getAcceptanceTests(); will(returnValue(accList));
			allowing(testModel).getHexagons(); will(returnValue(hexagons));
			allowing(hd).getId(); will(returnValue("hex.1"));
		}});
		return testModel;
	}
}
