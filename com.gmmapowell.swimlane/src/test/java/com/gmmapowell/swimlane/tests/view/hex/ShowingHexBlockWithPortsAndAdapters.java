package com.gmmapowell.swimlane.tests.view.hex;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Canvas;
import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel.Status;
import com.gmmapowell.swimlane.eclipse.interfaces.PortData;
import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;
import com.gmmapowell.swimlane.tests.swtutil.ImageChecker;
import com.gmmapowell.swimlane.tests.swtutil.ImageProxy;

public class ShowingHexBlockWithPortsAndAdapters extends BaseViewTest {
	private BarData bd;
	
	@Test
	public void testAllTheControlsWeWantAreThere() throws Exception {
		specifyModel();
		assertControls(shell, "hexagons.hex.1.bg", "hexagons.hex.1.bar", "hexagons.hex.1.port.nw", "hexagons.hex.1.adapter.nw.1", "hexagons.hex.1.adapter.nw.2");
	}
	
	@Test
	public void testWeCanFindThePort() throws Exception {
		specifyModel();
		Canvas port = waitForControl(shell, "hexagons.hex.1.port.nw");
		assertEquals(148, port.getBounds().x);
		assertEquals(26, port.getBounds().y);
		checkSizeColors(port, 44, 60, new ImageChecker() {
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

	@Test
	public void testWeCanFindTheTopAdapter() throws Exception {
		specifyModel();
		Canvas adapter = waitForControl(shell, "hexagons.hex.1.adapter.nw.1");
		assertEquals(113, adapter.getBounds().x);
		assertEquals(48, adapter.getBounds().y);
		checkSizeColors(adapter, 34, 6, new ImageChecker() {
			@Override
			public void checkImage(ImageProxy proxy) {
				proxy.assertColorOfPixel(SWT.COLOR_GREEN, 3, 3); // left
				proxy.assertColorOfPixel(SWT.COLOR_GREEN, 7, 3); // end of done
				proxy.assertColorOfPixel(SWT.COLOR_GRAY, 10, 3); // start of missing
				proxy.assertColorOfPixel(SWT.COLOR_GRAY, 30, 3); // right
			}
		});
	}

	@Test
	public void testWeCanFindTheBottomAdapter() throws Exception {
		specifyModel();
		Canvas adapter = waitForControl(shell, "hexagons.hex.1.adapter.nw.2");
		assertEquals(113, adapter.getBounds().x);
		assertEquals(58, adapter.getBounds().y);
		checkSizeColors(adapter, 34, 6, new ImageChecker() {
			@Override
			public void checkImage(ImageProxy proxy) {
				proxy.assertColorOfPixel(SWT.COLOR_RED, 3, 3); // left
				proxy.assertColorOfPixel(SWT.COLOR_RED, 26, 3); // end of done
				proxy.assertColorOfPixel(SWT.COLOR_GRAY, 30, 3); // right
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
		ArrayList<PortData> portList = new ArrayList<PortData>();
		HexData hd = context.mock(HexData.class);
		hexagons.add(hd);
		PortData pd = context.mock(PortData.class);
		portList.add(pd);
		ArrayList<BarData> adapterList = new ArrayList<>();
		BarData a1 = context.mock(BarData.class, "a1");
		BarData a2 = context.mock(BarData.class, "a2");
		adapterList.add(a1);
		adapterList.add(a2);
		bd = context.mock(BarData.class, "bd");
		context.checking(new Expectations() {{
			allowing(testModel).getHexCount(); will(returnValue(1));
			allowing(testModel).getBuildTime(); will(returnValue(exactDate(2017, 04, 20, 04, 20, 00, 420)));
			allowing(testModel).getAcceptanceTests(); will(returnValue(accList));
			allowing(testModel).getHexagons(); will(returnValue(hexagons));
			allowing(hd).getId(); will(returnValue("hex.1"));
			allowing(hd).getBar(); will(returnValue(bd));
			allowing(hd).getPorts(); will(returnValue(portList));
			allowing(bd).getTotal(); will(returnValue(10));
			allowing(bd).getComplete(); will(returnValue(0));
			allowing(bd).getStatus(); will(returnValue(Status.NONE));
			allowing(bd).getMarks(); will(returnValue(new int[] { 1 }));
			allowing(a1).getTotal(); will(returnValue(20));
			allowing(a1).getComplete(); will(returnValue(5));
			allowing(a1).getStatus(); will(returnValue(Status.OK));
			allowing(a1).getMarks(); will(returnValue(new int[] { 1 }));
			allowing(a2).getTotal(); will(returnValue(5));
			allowing(a2).getComplete(); will(returnValue(4));
			allowing(a2).getStatus(); will(returnValue(Status.FAILURES));
			allowing(a2).getMarks(); will(returnValue(new int[] { 1 }));
			allowing(pd).getLocation(); will(returnValue(PortLocation.NORTHWEST));
			allowing(pd).getAdapters(); will(returnValue(adapterList));
		}});
		return testModel;
	}
}