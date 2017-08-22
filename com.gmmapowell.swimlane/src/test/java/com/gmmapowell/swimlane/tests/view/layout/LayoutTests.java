package com.gmmapowell.swimlane.tests.view.layout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.AcceptanceData;
import com.gmmapowell.swimlane.eclipse.interfaces.AdapterData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexData;
import com.gmmapowell.swimlane.eclipse.interfaces.PortData;
import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;
import com.gmmapowell.swimlane.eclipse.interfaces.UtilityData;
import com.gmmapowell.swimlane.eclipse.views.BarControl;
import com.gmmapowell.swimlane.tests.view.hex.BaseHexViewTest;

public class LayoutTests extends BaseHexViewTest {

	@Test
	public void oneHexWithBusinessLogicHasTheRightComponentsInTheRightPlaces() throws InterruptedException {
		HexData h1 = context.mock(HexData.class);
		context.checking(new Expectations() {{
			oneOf(h1).addBusinessLogicListener(with(any(BarControl.class)));
		}});
		swimlane.addHexagon(0, h1);
//		showFor(5000);
		assertControlsInOrder(shell, "swimlane.bar.business.0", "swimlane.hexbg.0");
		checkLocationSizeColors("swimlane.hexbg.0", 157, 26, 276, 238, proxy -> { });
		checkLocationSizeColors("swimlane.bar.business.0", 213, 142, 165, 6, proxy -> { });
	}

	@Test
	public void twoHexesWithBusinessLogicHasTheRightComponentsInTheRightPlaces() throws InterruptedException {
		HexData h1 = context.mock(HexData.class, "h1");
		HexData h2 = context.mock(HexData.class, "h2");
		context.checking(new Expectations() {{
			oneOf(h1).addBusinessLogicListener(with(any(BarControl.class)));
			oneOf(h2).addBusinessLogicListener(with(any(BarControl.class)));
		}});
		swimlane.addHexagon(0, h1);
		swimlane.addHexagon(1, h2);
//		dumpComposite((Composite) swimlane.getTop(), "");
//		showFor(5000);
		assertControlsInOrder(shell, "swimlane.bar.business.1", "swimlane.bar.business.0", "swimlane.hexbg.0", "swimlane.hexbg.1");
		checkLocationSizeColors("swimlane.hexbg.0", 37, 50, 220, 190, proxy -> { });
		checkLocationSizeColors("swimlane.bar.business.0", 81, 142, 132, 6, proxy -> { });
		checkLocationSizeColors("swimlane.hexbg.1", 332, 50, 220, 190, proxy -> { });
		checkLocationSizeColors("swimlane.bar.business.1", 376, 142, 132, 6, proxy -> { });
	}

	@Test
	public void oneHexAndOneAcceptanceHasTheRightComponentsInTheRightPlaces() throws InterruptedException {
		HexData h1 = context.mock(HexData.class);
		AcceptanceData ad = context.mock(AcceptanceData.class);
		context.checking(new Expectations() {{
			oneOf(h1).addBusinessLogicListener(with(any(BarControl.class)));
			oneOf(ad).addTestListener(with(any(BarControl.class)));
		}});
		swimlane.addHexagon(0, h1);
		swimlane.addAcceptance(new int[] { 1 }, ad);
//		showFor(5000);
		assertControlsInOrder(shell, "swimlane.bar.acceptance.1", "swimlane.bar.business.0", "swimlane.hexbg.0");
		checkLocationSizeColors("swimlane.bar.acceptance.1", 0, 2, 590, 6, proxy -> { });
		checkLocationSizeColors("swimlane.hexbg.0", 161, 34, 268, 232, proxy -> { });
		checkLocationSizeColors("swimlane.bar.business.0", 215, 147, 160, 6, proxy -> { });
	}

	@Test
	public void twoHexesAndThreeAcceptanceBarsHasTheRightComponentsInTheRightPlaces() throws InterruptedException {
		HexData h1 = context.mock(HexData.class, "h1");
		HexData h2 = context.mock(HexData.class, "h2");
		AcceptanceData a11 = context.mock(AcceptanceData.class, "a11");
		AcceptanceData a01 = context.mock(AcceptanceData.class, "a10");
		AcceptanceData a10 = context.mock(AcceptanceData.class, "a01");
		context.checking(new Expectations() {{
			oneOf(h1).addBusinessLogicListener(with(any(BarControl.class)));
			oneOf(h2).addBusinessLogicListener(with(any(BarControl.class)));
			oneOf(a11).addTestListener(with(any(BarControl.class)));
			oneOf(a10).addTestListener(with(any(BarControl.class)));
			oneOf(a01).addTestListener(with(any(BarControl.class)));
		}});
		swimlane.addHexagon(0, h1);
		swimlane.addHexagon(1, h2);
		// don't add them in the right order, although I believe this will be the default first case
		swimlane.addAcceptance(new int[] { 0, 1 }, a01); // bottom
		swimlane.addAcceptance(new int[] { 1, 1 }, a11); // top
		swimlane.addAcceptance(new int[] { 1, 0 }, a10); // middle
		
//		dumpComposite((Composite) swimlane.getTop(), "");
//		showFor(5000);
		// this is the order, but the y-order is not relevant here (just the z-order)
		assertControlsInOrder(shell, "swimlane.bar.acceptance.10", "swimlane.bar.acceptance.11", "swimlane.bar.acceptance.01", "swimlane.bar.business.1", "swimlane.bar.business.0", "swimlane.hexbg.0", "swimlane.hexbg.1");
		// what is relevant is the y-placements here
		checkLocationSizeColors("swimlane.bar.acceptance.11", 0,  2, 590, 6, proxy -> { });
		checkLocationSizeColors("swimlane.bar.acceptance.10", 0, 12, 590, 6, proxy -> { });
		checkLocationSizeColors("swimlane.bar.acceptance.01", 0, 22, 590, 6, proxy -> { });
		checkLocationSizeColors("swimlane.hexbg.0", 37, 65, 220, 190, proxy -> { });
		checkLocationSizeColors("swimlane.bar.business.0", 81, 157, 132, 6, proxy -> { });
		checkLocationSizeColors("swimlane.hexbg.1", 332, 65, 220, 190, proxy -> { });
		checkLocationSizeColors("swimlane.bar.business.1", 376, 157, 132, 6, proxy -> { });
	}

	@Test
	public void justTheUtilityBarHasTheRightComponentsInTheRightPlaces() throws InterruptedException {
		UtilityData ute = context.mock(UtilityData.class);
		context.checking(new Expectations() {{
			oneOf(ute).addTestListener(with(any(BarControl.class)));
		}});
		swimlane.addUtility(ute);
//		showFor(5000);
		assertControlsInOrder(shell, "swimlane.bar.utility");
		checkLocationSizeColors("swimlane.bar.utility", 0, 2, 590, 6, proxy -> {
			proxy.assertColorOfPixel(SWT.COLOR_BLUE, 10, 5);
		});
	}

	@Test
	public void oneHexWithOnePortAndOneAdapterHasTheRightComponentsInTheRightPlaces() throws InterruptedException {
		HexData h1 = context.mock(HexData.class);
		PortData port = context.mock(PortData.class);
		AdapterData adapter = context.mock(AdapterData.class);
		context.checking(new Expectations() {{
			oneOf(h1).addBusinessLogicListener(with(any(BarControl.class)));
			oneOf(adapter).addTestListener(with(any(BarControl.class)));
		}});
		swimlane.addHexagon(0, h1);
		swimlane.addHexagonPort(0, PortLocation.NORTHWEST, port);
		swimlane.addAdapter(0, PortLocation.NORTHWEST, 0, adapter);
//		dumpComposite((Composite) swimlane.getTop(), "");
//		showFor(5000);
		assertControlsInOrder(shell, "swimlane.bar.adapter.0.nw.0", "swimlane.port.0.nw", "swimlane.bar.business.0", "swimlane.hexbg.0");
		checkLocationSizeColors("swimlane.hexbg.0", 157, 26, 276, 238, proxy -> { });
		checkLocationSizeColors("swimlane.bar.business.0", 213, 142, 165, 6, proxy -> { });
		checkLocationSizeColors("swimlane.port.0.nw", 148, 26, 44, 60, proxy -> { });
		checkLocationSizeColors("swimlane.bar.adapter.0.nw.0", 113, 53, 34, 6, proxy -> { });
	}

	@Test
	public void oneHexWithOnePortAndMultipleAdaptersHasTheRightComponentsInTheRightPlaces() throws InterruptedException {
		HexData h1 = context.mock(HexData.class);
		PortData port = context.mock(PortData.class);
		AdapterData adapter = context.mock(AdapterData.class);
		context.checking(new Expectations() {{
			oneOf(h1).addBusinessLogicListener(with(any(BarControl.class)));
			exactly(5).of(adapter).addTestListener(with(any(BarControl.class)));
		}});
		swimlane.addHexagon(0, h1);
		swimlane.addHexagonPort(0, PortLocation.NORTHWEST, port);
		swimlane.addAdapter(0, PortLocation.NORTHWEST, 0, adapter);
		swimlane.addAdapter(0, PortLocation.NORTHWEST, 1, adapter);
		swimlane.addAdapter(0, PortLocation.NORTHWEST, 2, adapter);
		swimlane.addAdapter(0, PortLocation.NORTHWEST, 3, adapter);
		swimlane.addAdapter(0, PortLocation.NORTHWEST, 4, adapter);
//		dumpComposite((Composite) swimlane.getTop(), "");
//		showFor(5000);
		assertControlsInOrder(shell,
				"swimlane.bar.adapter.0.nw.4",
				"swimlane.bar.adapter.0.nw.3",
				"swimlane.bar.adapter.0.nw.2",
				"swimlane.bar.adapter.0.nw.1",
				"swimlane.bar.adapter.0.nw.0",
				"swimlane.port.0.nw", "swimlane.bar.business.0", "swimlane.hexbg.0");
		checkLocationSizeColors("swimlane.hexbg.0", 157, 26, 276, 238, proxy -> { });
		checkLocationSizeColors("swimlane.bar.business.0", 213, 142, 165, 6, proxy -> { });
		checkLocationSizeColors("swimlane.port.0.nw", 148, 26, 44, 60, proxy -> { });
		checkLocationSizeColors("swimlane.bar.adapter.0.nw.0", 113, 33, 34, 6, proxy -> { });
		checkLocationSizeColors("swimlane.bar.adapter.0.nw.1", 113, 43, 34, 6, proxy -> { });
		checkLocationSizeColors("swimlane.bar.adapter.0.nw.2", 113, 53, 34, 6, proxy -> { });
		checkLocationSizeColors("swimlane.bar.adapter.0.nw.3", 113, 63, 34, 6, proxy -> { });
		checkLocationSizeColors("swimlane.bar.adapter.0.nw.4", 113, 73, 34, 6, proxy -> { });
	}

	@Test
	public void aComplexLayoutHasTheRightComponentsInTheRightPlaces() throws InterruptedException {
		HexData h1 = context.mock(HexData.class, "h1");
		HexData h2 = context.mock(HexData.class, "h2");
		HexData h3 = context.mock(HexData.class, "h3");
		PortData port = context.mock(PortData.class);
		AdapterData adapter = context.mock(AdapterData.class);
		AcceptanceData a111 = context.mock(AcceptanceData.class, "a111");
		AcceptanceData a011 = context.mock(AcceptanceData.class, "a011");
		AcceptanceData a100 = context.mock(AcceptanceData.class, "a100");
		AcceptanceData a010 = context.mock(AcceptanceData.class, "a010");
		UtilityData ute = context.mock(UtilityData.class);
		context.checking(new Expectations() {{
			oneOf(h1).addBusinessLogicListener(with(any(BarControl.class)));
			oneOf(h2).addBusinessLogicListener(with(any(BarControl.class)));
			oneOf(h3).addBusinessLogicListener(with(any(BarControl.class)));
			exactly(15).of(adapter).addTestListener(with(any(BarControl.class)));
			oneOf(a111).addTestListener(with(any(BarControl.class)));
			oneOf(a011).addTestListener(with(any(BarControl.class)));
			oneOf(a100).addTestListener(with(any(BarControl.class)));
			oneOf(a010).addTestListener(with(any(BarControl.class)));
			oneOf(ute).addTestListener(with(any(BarControl.class)));
		}});
		swimlane.addHexagon(0, h1);
		swimlane.addHexagon(1, h2);
		swimlane.addHexagon(2, h3);
		swimlane.addHexagonPort(0, PortLocation.NORTHWEST, port);
		swimlane.addAdapter(0, PortLocation.NORTHWEST, 0, adapter);
		swimlane.addAdapter(0, PortLocation.NORTHWEST, 1, adapter);
		swimlane.addAdapter(0, PortLocation.NORTHWEST, 2, adapter);
		swimlane.addAdapter(0, PortLocation.NORTHWEST, 3, adapter);
		swimlane.addAdapter(0, PortLocation.NORTHWEST, 4, adapter);
		swimlane.addHexagonPort(0, PortLocation.SOUTHEAST, port);
		swimlane.addAdapter(0, PortLocation.SOUTHEAST, 0, adapter);
		swimlane.addHexagonPort(1, PortLocation.SOUTHWEST, port);
		swimlane.addAdapter(1, PortLocation.SOUTHWEST, 0, adapter);
		swimlane.addHexagonPort(1, PortLocation.NORTHEAST, port);
		swimlane.addAdapter(1, PortLocation.NORTHEAST, 0, adapter);
		swimlane.addHexagonPort(2, PortLocation.NORTHWEST, port);
		swimlane.addAdapter(2, PortLocation.NORTHWEST, 0, adapter);
		swimlane.addAdapter(2, PortLocation.NORTHWEST, 1, adapter);
		swimlane.addHexagonPort(2, PortLocation.SOUTHWEST, port);
		swimlane.addAdapter(2, PortLocation.SOUTHWEST, 0, adapter);
		swimlane.addHexagonPort(2, PortLocation.SOUTHEAST, port);
		swimlane.addAdapter(2, PortLocation.SOUTHEAST, 0, adapter);
		swimlane.addAdapter(2, PortLocation.SOUTHEAST, 1, adapter);
		swimlane.addHexagonPort(2, PortLocation.NORTHEAST, port);
		swimlane.addAdapter(2, PortLocation.NORTHEAST, 0, adapter);
		swimlane.addAdapter(2, PortLocation.NORTHEAST, 1, adapter);
		swimlane.addAcceptance(new int[] { 0, 1, 0 }, a010); // bottom
		swimlane.addAcceptance(new int[] { 1, 1, 1 }, a111); // top
		swimlane.addAcceptance(new int[] { 1, 0, 0 }, a100); // upper/middle
		swimlane.addAcceptance(new int[] { 0, 1, 1 }, a011); // lower/middle
		swimlane.addUtility(ute);
		
//		dumpComposite((Composite) swimlane.getTop(), "");
//		showFor(5000);
		// this is the order, but the y-order is not relevant here (just the z-order)
		assertControlsInOrder(shell,
			"swimlane.bar.utility",
			"swimlane.bar.acceptance.011",
			"swimlane.bar.acceptance.100",
			"swimlane.bar.acceptance.111",
			"swimlane.bar.acceptance.010",
			"swimlane.bar.adapter.2.ne.1",
			"swimlane.bar.adapter.2.ne.0",
			"swimlane.port.2.ne", 
			"swimlane.bar.adapter.2.se.1",
			"swimlane.bar.adapter.2.se.0",
			"swimlane.port.2.se", 
			"swimlane.bar.adapter.2.sw.0",
			"swimlane.port.2.sw", 
			"swimlane.bar.adapter.2.nw.1",
			"swimlane.bar.adapter.2.nw.0",
			"swimlane.port.2.nw", 
			"swimlane.bar.adapter.1.ne.0",
			"swimlane.port.1.ne", 
			"swimlane.bar.adapter.1.sw.0",
			"swimlane.port.1.sw", 
			"swimlane.bar.adapter.0.se.0",
			"swimlane.port.0.se", 
			"swimlane.bar.adapter.0.nw.4",
			"swimlane.bar.adapter.0.nw.3",
			"swimlane.bar.adapter.0.nw.2",
			"swimlane.bar.adapter.0.nw.1",
			"swimlane.bar.adapter.0.nw.0",
			"swimlane.port.0.nw", 
			"swimlane.bar.business.2", "swimlane.bar.business.1", "swimlane.bar.business.0",
			"swimlane.hexbg.0", "swimlane.hexbg.1", "swimlane.hexbg.2");
		// what is relevant is the y-placements here
		checkLocationSizeColors("swimlane.bar.acceptance.111", 0,  2, 590, 6, proxy -> { });
		checkLocationSizeColors("swimlane.bar.acceptance.100", 0, 12, 590, 6, proxy -> { });
		checkLocationSizeColors("swimlane.bar.acceptance.011", 0, 22, 590, 6, proxy -> { });
		checkLocationSizeColors("swimlane.bar.acceptance.010", 0, 32, 590, 6, proxy -> { });
		checkLocationSizeColors("swimlane.hexbg.0", 28, 100, 140, 120, proxy -> { });
		checkLocationSizeColors("swimlane.bar.business.0", 56, 157, 84, 6, proxy -> { });
		checkLocationSizeColors("swimlane.port.0.nw", 19, 100, 27, 30, proxy -> { });
		checkLocationSizeColors("swimlane.bar.adapter.0.nw.0", 1, 92, 17, 6, proxy -> { });
		checkLocationSizeColors("swimlane.bar.adapter.0.nw.1", 1, 102, 17, 6, proxy -> { });
		checkLocationSizeColors("swimlane.bar.adapter.0.nw.2", 1, 112, 17, 6, proxy -> { });
		checkLocationSizeColors("swimlane.bar.adapter.0.nw.3", 1, 122, 17, 6, proxy -> { });
		checkLocationSizeColors("swimlane.bar.adapter.0.nw.4", 1, 132, 17, 6, proxy -> { });
		checkLocationSizeColors("swimlane.port.0.se", 150, 190, 27, 30, proxy -> { });
		checkLocationSizeColors("swimlane.bar.adapter.0.se.0", 178, 202, 17, 6, proxy -> { });
		checkLocationSizeColors("swimlane.hexbg.1", 225, 100, 140, 120, proxy -> { });
		checkLocationSizeColors("swimlane.bar.business.1", 253, 157, 84, 6, proxy -> { });
		checkLocationSizeColors("swimlane.port.1.sw", 216, 190, 27, 30, proxy -> { });
		checkLocationSizeColors("swimlane.bar.adapter.1.sw.0", 198, 202, 17, 6, proxy -> { });
		checkLocationSizeColors("swimlane.port.1.ne", 347, 100, 27, 30, proxy -> { });
		checkLocationSizeColors("swimlane.bar.adapter.1.ne.0", 375, 112, 17, 6, proxy -> { });
		checkLocationSizeColors("swimlane.hexbg.2", 421, 100, 140, 120, proxy -> { });
		checkLocationSizeColors("swimlane.bar.business.2", 449, 157, 84, 6, proxy -> { });
		checkLocationSizeColors("swimlane.port.2.nw", 412, 100, 27, 30, proxy -> { });
		checkLocationSizeColors("swimlane.bar.adapter.2.nw.0", 394, 107, 17, 6, proxy -> { });
		checkLocationSizeColors("swimlane.bar.adapter.2.nw.1", 394, 117, 17, 6, proxy -> { });
		checkLocationSizeColors("swimlane.port.2.sw", 412, 190, 27, 30, proxy -> { });
		checkLocationSizeColors("swimlane.bar.adapter.2.sw.0", 394, 202, 17, 6, proxy -> { });
		checkLocationSizeColors("swimlane.port.2.se", 543, 190, 27, 30, proxy -> { });
		checkLocationSizeColors("swimlane.bar.adapter.2.se.0", 571, 197, 17, 6, proxy -> { });
		checkLocationSizeColors("swimlane.bar.adapter.2.se.1", 571, 207, 17, 6, proxy -> { });
		checkLocationSizeColors("swimlane.port.2.ne", 543, 100, 27, 30, proxy -> { });
		checkLocationSizeColors("swimlane.bar.adapter.2.ne.0", 571, 107, 17, 6, proxy -> { });
		checkLocationSizeColors("swimlane.bar.adapter.2.ne.1", 571, 117, 17, 6, proxy -> { });
		checkLocationSizeColors("swimlane.bar.utility", 0, 282, 590, 6, proxy -> { });
	}
}
