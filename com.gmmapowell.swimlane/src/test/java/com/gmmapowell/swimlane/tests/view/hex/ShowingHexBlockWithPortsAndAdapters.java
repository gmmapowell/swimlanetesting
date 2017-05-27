package com.gmmapowell.swimlane.tests.view.hex;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Canvas;
import org.jmock.Expectations;
import org.jmock.States;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.AdapterData;
import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel.Status;
import com.gmmapowell.swimlane.eclipse.interfaces.PortData;
import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;
import com.gmmapowell.swimlane.tests.swtutil.ImageChecker;
import com.gmmapowell.swimlane.tests.swtutil.ImageProxy;

public class ShowingHexBlockWithPortsAndAdapters extends BaseViewTest {
	States mode = context.states("mode").startsAs("initial");
	private BarData bd;
	
	@Test
	public void testAllTheControlsWeWantAreThere() throws Exception {
		specifyModel();
		assertControls(shell, "hexagons.hex.1.bg", "hexagons.hex.1.bar", "hexagons.hex.1.port.nw", "hexagons.hex.1.adapter.nw.1", "hexagons.hex.1.adapter.nw.2");
	}
	
	@Test
	public void testWeCanFindThePort() throws Exception {
		specifyModel();
		Canvas hexagon = waitForControl(shell, "hexagons.hex.1.port.nw");
//		try { Thread.sleep(5000); } catch (Exception ex) {}
		assertEquals(148, hexagon.getBounds().x);
		assertEquals(26, hexagon.getBounds().y);
		checkSizeColors(hexagon, 44, 60, new ImageChecker() {
			@Override
			public void checkImage(ImageProxy proxy) {
				// outside
				proxy.assertColorOfPixel(SWT.COLOR_WIDGET_BACKGROUND, 0, 0); // top left
				proxy.assertColorOfPixel(SWT.COLOR_WIDGET_BACKGROUND, 32, 0); // top right
				proxy.assertColorOfPixel(SWT.COLOR_WIDGET_BACKGROUND, 43, 59); // bottom right
				proxy.assertColorOfPixel(SWT.COLOR_WIDGET_BACKGROUND, 12, 59); // bottom left
				
				// inside
				Color expected = new Color(displayHelper.getDisplay(), 200, 200, 155);
				proxy.assertColorOfPixel(expected, 17, 35); // middle
				proxy.assertColorOfPixel(expected, 42, 1); // top right
				proxy.assertColorOfPixel(expected, 1, 58); // bottom left
				expected.dispose();
			}
		});
	}
	/*
	@Test
	public void testTheHexagonsBarChangesColorAfterUpdate() throws Exception {
		specifyModel();
		Canvas hexagon = waitForControl(shell, "hexagons.hex.1.bg");
		mode.become("plus5");
		md.barChanged(bd);
		for (int i=0;i<10;i++) {
			try { Thread.sleep(100); } catch (Exception ex) {}
			displayHelper.flushPendingEvents();
		}
		checkSizeColors(hexagon, 590, 290, new ImageChecker() {
			@Override
			public void checkImage(ImageProxy proxy) {
				int mx = 295, my = 145;

				// in the bar
				proxy.assertColorOfPixel(SWT.COLOR_GREEN, mx-5, my); // left hand half
				proxy.assertColorOfPixel(SWT.COLOR_GRAY, mx+5, my); // right hand half
			}
		});
	}
*/
	protected void specifyModel() throws InterruptedException {
		pushModel(defineModel());
	}

	protected HexagonDataModel defineModel() {
		HexagonDataModel testModel = context.mock(HexagonDataModel.class);
		ArrayList<BarData> accList = new ArrayList<BarData>();
		ArrayList<HexData> hexagons = new ArrayList<HexData>();
		ArrayList<PortData> portList = new ArrayList<PortData>();
		HexData hd = context.mock(HexData.class);
		hexagons.add(hd);
		PortData pd = context.mock(PortData.class);
		portList.add(pd);
		ArrayList<AdapterData> adapterList = new ArrayList<AdapterData>();
		AdapterData a1 = context.mock(AdapterData.class, "a1");
		AdapterData a2 = context.mock(AdapterData.class, "a2");
		adapterList.add(a1);
		adapterList.add(a2);
		bd = context.mock(BarData.class);
		context.checking(new Expectations() {{
			allowing(testModel).getHexCount(); will(returnValue(1));
			allowing(testModel).getBuildTime(); will(returnValue(exactDate(2017, 04, 20, 04, 20, 00, 420)));
			allowing(testModel).getAcceptanceTests(); will(returnValue(accList));
			allowing(testModel).getHexagons(); will(returnValue(hexagons));
			allowing(hd).getId(); will(returnValue("hex.1"));
			allowing(hd).getBar(); will(returnValue(bd));
			allowing(hd).getPorts(); will(returnValue(portList));
			allowing(bd).getTotal(); will(returnValue(10));
			allowing(bd).getComplete(); will(returnValue(0)); when(mode.is("initial"));
			allowing(bd).getComplete(); will(returnValue(5)); when(mode.is("plus5"));
			allowing(bd).getStatus(); will(returnValue(Status.NONE)); when(mode.is("initial"));
			allowing(bd).getStatus(); will(returnValue(Status.OK)); when(mode.is("plus5"));
			allowing(bd).getMarks(); will(returnValue(new int[] { 1 }));
			allowing(pd).getLocation(); will(returnValue(PortLocation.NORTHWEST));
			allowing(pd).getAdapters(); will(returnValue(adapterList));
		}});
		return testModel;
	}
}
