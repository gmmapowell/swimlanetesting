package com.gmmapowell.swimlane.tests.view.hex;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Canvas;
import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.BarDataListener;
import com.gmmapowell.swimlane.eclipse.interfaces.HexData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel.Status;
import com.gmmapowell.swimlane.eclipse.interfaces.PortData;
import com.gmmapowell.swimlane.tests.swtutil.ImageChecker;
import com.gmmapowell.swimlane.tests.swtutil.ImageProxy;

public class ShowingMultipleHexBlocks extends BaseHexViewTest {
	
	@Test
	public void testAllTheControlsWeWantAreThereForTwoHexagons() throws Exception {
		specifyModel(2, 10, 0, Status.NONE);
		assertControls(shell, "hexagons.hex.1.bg", "hexagons.hex.1.bar", "hexagons.hex.2.bg", "hexagons.hex.2.bar");
	}
	
	@Test
	public void testTheLeftOfTwoHexagonsHasAHexBackgroundBeforeWeStart() throws Exception {
		specifyModel(2, 10, 0, Status.NONE);
		Canvas hexagon = waitForControl(shell, "hexagons.hex.1.bg");
		checkSizeColors(hexagon, 236, 204, new ImageChecker() {
			@Override
			public void checkImage(ImageProxy proxy) {
				int mx = 118, my = 102, a = 58;
				int h = (int) (a*Math.sqrt(3));
				int lx = mx-2*a;
				int rx = mx+2*a;
				int ty = my-h;
				int by = my+h;

				// outside the hexagon
				proxy.assertColorOfPixel(SWT.COLOR_WIDGET_BACKGROUND, 10, 10); // top left
				proxy.assertColorOfPixel(SWT.COLOR_WIDGET_BACKGROUND, 230, 10); // top right
				proxy.assertColorOfPixel(SWT.COLOR_WIDGET_BACKGROUND, 10, 200); // bottom left
				proxy.assertColorOfPixel(SWT.COLOR_WIDGET_BACKGROUND, 230, 200); // bottom right
				
				// outside the corners
				proxy.assertColorOfPixel(SWT.COLOR_WIDGET_BACKGROUND, mx-a-5, ty); // top left corner
				proxy.assertColorOfPixel(SWT.COLOR_WIDGET_BACKGROUND, mx-a-5, by); // bottom left corner
				proxy.assertColorOfPixel(SWT.COLOR_WIDGET_BACKGROUND, mx+a+5, ty); // top right corner
				proxy.assertColorOfPixel(SWT.COLOR_WIDGET_BACKGROUND, mx+a+5, by); // bottom right corner
				
				// inside the hexagon
				Color expected = new Color(displayHelper.getDisplay(), 220, 220, 170);
				proxy.assertColorOfPixel(expected, lx+5, my); // middle left
				proxy.assertColorOfPixel(expected, rx-5, my); // middle right
				proxy.assertColorOfPixel(expected, mx, ty+5); // top middle
				proxy.assertColorOfPixel(expected, mx, by-5); // top middle
				
				// corners
				proxy.assertColorOfPixel(expected, mx-a, ty+5); // top left corner
				proxy.assertColorOfPixel(expected, mx-a, by-5); // bottom left corner
				proxy.assertColorOfPixel(expected, mx+a, ty+5); // top right corner
				proxy.assertColorOfPixel(expected, mx+a, by-5); // bottom right corner
				expected.dispose();
				
				// in the bar
				proxy.assertColorOfPixel(SWT.COLOR_GRAY, mx, my); // middle
			}
		});
	}

	@Test
	public void testATooltipWhenNoTestsHaveRun() throws Exception {
		specifyModel(2, 10, 0, Status.NONE);
		Canvas ute = waitForControl(shell, "hexagons.hex.1.bar");
		assertEquals("Left - 1 group; 0 passed", ute.getToolTipText());
	}

	@Test
	public void testMiddleTooltipWhenSomeTestsHaveRun() throws Exception {
		specifyModel(2, 10, 4, Status.OK);
		Canvas ute = waitForControl(shell, "hexagons.hex.2.bar");
		assertEquals("Middle - 1 group; 4 passed", ute.getToolTipText());
	}

	@Test
	public void testAllTheControlsWeWantAreThereForThreeHexagons() throws Exception {
		specifyModel(3, 10, 0, Status.NONE);
		assertControls(shell, "hexagons.hex.1.bg", "hexagons.hex.1.bar", "hexagons.hex.2.bg", "hexagons.hex.2.bar", "hexagons.hex.3.bg", "hexagons.hex.3.bar");
	}
	
	protected void specifyModel(int nhex, int total, int complete, Status status) throws InterruptedException {
		pushModel(defineModel(nhex, total, complete, status));
	}

	protected HexagonDataModel defineModel(int nhex, int total, int complete, Status status) {
		HexagonDataModel testModel = context.mock(HexagonDataModel.class);
		ArrayList<BarData> accList = new ArrayList<BarData>();
		ArrayList<HexData> hexagons = new ArrayList<HexData>();
		ArrayList<BarData> bars = new ArrayList<BarData>();
		for (int i=0;i<nhex;i++) {
			HexData hd = context.mock(HexData.class, "hex" + i);
			hexagons.add(hd);
			BarData bd = context.mock(BarData.class, "hb" + i);
			bars.add(bd);
		}
		context.checking(new Expectations() {{
			allowing(testModel).getHexCount(); will(returnValue(1));
			allowing(testModel).getBuildTime(); will(returnValue(exactDate(2017, 04, 20, 04, 20, 00, 420)));
			allowing(testModel).getAcceptanceTests(); will(returnValue(accList));
			allowing(testModel).getHexagons(); will(returnValue(hexagons));
			allowing(testModel).getUtilityBar(); will(returnValue(null));
			if (nhex > 0) {
				allowing(hexagons.get(0)).getId(); will(returnValue("hex.1"));
				allowing(hexagons.get(0)).getBar(); will(returnValue(bars.get(0)));
				allowing(hexagons.get(0)).getPorts(); will(returnValue(new ArrayList<PortData>()));
				allowing(bars.get(0)).getId(); will(returnValue("bar1"));
				allowing(bars.get(0)).getName(); will(returnValue("org.sample.Left"));
				allowing(bars.get(0)).getTotal(); will(returnValue(total));
				allowing(bars.get(0)).getComplete(); will(returnValue(complete));
				allowing(bars.get(0)).getPassed(); will(returnValue(complete));
				allowing(bars.get(0)).getFailures(); will(returnValue(0));
				allowing(bars.get(0)).getStatus(); will(returnValue(status));
				allowing(bars.get(0)).getMarks(); will(returnValue(new int[] { 1 }));
				oneOf(md).addBarListener(with(bars.get(0)), with(aNonNull(BarDataListener.class)));
			}
			if (nhex > 1) {
				allowing(hexagons.get(1)).getId(); will(returnValue("hex.2"));
				allowing(hexagons.get(1)).getBar(); will(returnValue(bars.get(1)));
				allowing(hexagons.get(1)).getPorts(); will(returnValue(new ArrayList<PortData>()));
				allowing(bars.get(1)).getId(); will(returnValue("bar2"));
				allowing(bars.get(1)).getName(); will(returnValue("org.sample.Middle"));
				allowing(bars.get(1)).getTotal(); will(returnValue(total));
				allowing(bars.get(1)).getPassed(); will(returnValue(complete));
				allowing(bars.get(1)).getComplete(); will(returnValue(complete));
				allowing(bars.get(1)).getFailures(); will(returnValue(0));
				allowing(bars.get(1)).getStatus(); will(returnValue(status));
				allowing(bars.get(1)).getMarks(); will(returnValue(new int[] { 1 }));
				oneOf(md).addBarListener(with(bars.get(1)), with(aNonNull(BarDataListener.class)));
			}
			if (nhex > 2) {
				allowing(hexagons.get(2)).getId(); will(returnValue("hex.3"));
				allowing(hexagons.get(2)).getBar(); will(returnValue(bars.get(2)));
				allowing(hexagons.get(2)).getPorts(); will(returnValue(new ArrayList<PortData>()));
				allowing(bars.get(2)).getId(); will(returnValue("bar3"));
				allowing(bars.get(2)).getName(); will(returnValue("org.sample.Hex1"));
				allowing(bars.get(2)).getTotal(); will(returnValue(total));
				allowing(bars.get(2)).getPassed(); will(returnValue(complete));
				allowing(bars.get(2)).getComplete(); will(returnValue(complete));
				allowing(bars.get(2)).getFailures(); will(returnValue(0));
				allowing(bars.get(2)).getStatus(); will(returnValue(status));
				allowing(bars.get(2)).getMarks(); will(returnValue(new int[] { 1 }));
				oneOf(md).addBarListener(with(bars.get(2)), with(aNonNull(BarDataListener.class)));
			}
		}});
		return testModel;
	}
}
