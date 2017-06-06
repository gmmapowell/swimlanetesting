package com.gmmapowell.swimlane.tests.view.hex;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Canvas;
import org.jmock.Expectations;
import org.jmock.States;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.BarDataListener;
import com.gmmapowell.swimlane.eclipse.interfaces.HexData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel.Status;
import com.gmmapowell.swimlane.eclipse.interfaces.PortData;
import com.gmmapowell.swimlane.tests.swtutil.ImageChecker;
import com.gmmapowell.swimlane.tests.swtutil.ImageProxy;

public class ShowingOneHexBlock extends BaseViewTest {
	States mode = context.states("mode").startsAs("initial");
	private BarData bd;
	
	@Test
	public void testAllTheControlsWeWantAreThere() throws Exception {
		specifyModel(10, 0, Status.NONE);
		assertControls(shell, "hexagons.hex.1.bg", "hexagons.hex.1.bar");
	}
	
	@Test
	public void testTheHexagonHasAHexBackgroundBeforeWeStart() throws Exception {
		specifyModel(10, 0, Status.NONE);
		Canvas hexagon = waitForControl(shell, "hexagons.hex.1.bg");
		checkSizeColors(hexagon, 276, 238, new ImageChecker() {
			@Override
			public void checkImage(ImageProxy proxy) {
				int mx = 138, my = 119, h = 238/2;
				int a = (int) (h/Math.sqrt(3)); // around 69
				int lx = mx-2*a;
				int rx = mx+2*a;
				int ty = my-h;
				int by = my+h;

				// outside the hexagon
				proxy.assertColorOfPixel(SWT.COLOR_WIDGET_BACKGROUND, 10, 10); // top left
				proxy.assertColorOfPixel(SWT.COLOR_WIDGET_BACKGROUND, 270, 10); // top right
				proxy.assertColorOfPixel(SWT.COLOR_WIDGET_BACKGROUND, 10, 230); // bottom left
				proxy.assertColorOfPixel(SWT.COLOR_WIDGET_BACKGROUND, 270, 230); // bottom right
				
				// outside the corners
				proxy.assertColorOfPixel(SWT.COLOR_WIDGET_BACKGROUND, mx-a-5, ty); // top left corner
				proxy.assertColorOfPixel(SWT.COLOR_WIDGET_BACKGROUND, mx-a-5, by-1); // bottom left corner
				proxy.assertColorOfPixel(SWT.COLOR_WIDGET_BACKGROUND, mx+a+5, ty); // top right corner
				proxy.assertColorOfPixel(SWT.COLOR_WIDGET_BACKGROUND, mx+a+5, by-1); // bottom right corner
				
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
	public void testTheTooltipWhenNoTestsHaveRun() throws Exception {
		specifyModel(10, 0, Status.NONE);
		Canvas ute = waitForControl(shell, "hexagons.hex.1.bar");
		assertEquals("1 group; 0 passed", ute.getToolTipText());
	}

	@Test
	public void testTheHexagonsBarChangesColorAfterUpdate() throws Exception {
		specifyModel(10, 0, Status.NONE);
		Canvas hexagon = waitForControl(shell, "hexagons.hex.1.bg");
		mode.become("plus5");
		fmd.real().barChanged(bd);
		displayHelper.flushPendingEvents();
		checkSizeColors(hexagon, 276, 238, new ImageChecker() {
			@Override
			public void checkImage(ImageProxy proxy) {
				int mx = 138, my = 119;

				// in the bar
				proxy.assertColorOfPixel(SWT.COLOR_GREEN, mx-5, my); // left hand half
				proxy.assertColorOfPixel(SWT.COLOR_GRAY, mx+5, my); // right hand half
			}
		});
	}

	protected void specifyModel(int total, int complete, Status status) throws InterruptedException {
		pushModel(defineModel(total, complete, status));
	}

	protected HexagonDataModel defineModel(int total, int complete, Status status) {
		HexagonDataModel testModel = context.mock(HexagonDataModel.class);
		ArrayList<BarData> accList = new ArrayList<BarData>();
		ArrayList<HexData> hexagons = new ArrayList<HexData>();
		HexData hd = context.mock(HexData.class);
		hexagons.add(hd);
		bd = context.mock(BarData.class);
		context.checking(new Expectations() {{
			allowing(testModel).getHexCount(); will(returnValue(1));
			allowing(testModel).getBuildTime(); will(returnValue(exactDate(2017, 04, 20, 04, 20, 00, 420)));
			allowing(testModel).getAcceptanceTests(); will(returnValue(accList));
			allowing(testModel).getHexagons(); will(returnValue(hexagons));
			allowing(testModel).getUtilityBar(); will(returnValue(null));
			allowing(hd).getId(); will(returnValue("hex.1"));
			allowing(hd).getBar(); will(returnValue(bd));
			allowing(hd).getPorts(); will(returnValue(new ArrayList<PortData>()));
			allowing(bd).getId(); will(returnValue("bar"));
			allowing(bd).getTotal(); will(returnValue(total));
			allowing(bd).getName(); will(returnValue(null));
			allowing(bd).getComplete(); will(returnValue(complete)); when(mode.is("initial"));
			allowing(bd).getComplete(); will(returnValue(complete+5)); when(mode.is("plus5"));
			allowing(bd).getPassed(); will(returnValue(0));
			allowing(bd).getFailures(); will(returnValue(0));
			allowing(bd).getStatus(); will(returnValue(status)); when(mode.is("initial"));
			allowing(bd).getStatus(); will(returnValue(Status.OK)); when(mode.is("plus5"));
			allowing(bd).getMarks(); will(returnValue(new int[] { 1 }));
			oneOf(md).addBarListener(with(bd), with(aNonNull(BarDataListener.class)));
		}});
		return testModel;
	}
}
