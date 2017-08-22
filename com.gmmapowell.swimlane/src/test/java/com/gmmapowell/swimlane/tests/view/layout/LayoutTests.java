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
//		showFor(5000);
		assertControlsInOrder(shell, "swimlane.bar.business.1", "swimlane.bar.business.0", "swimlane.hexbg.0", "swimlane.hexbg.1");
		checkLocationSizeColors("swimlane.hexbg.0", 29, 43, 236, 204, proxy -> { });
		checkLocationSizeColors("swimlane.bar.business.0", 77, 142, 141, 6, proxy -> { });
		checkLocationSizeColors("swimlane.hexbg.1", 324, 43, 236, 204, proxy -> { });
		checkLocationSizeColors("swimlane.bar.business.1", 372, 142, 141, 6, proxy -> { });
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
		
//		showFor(5000);
		// this is the order, but the y-order is not relevant here (just the z-order)
		assertControlsInOrder(shell, "swimlane.bar.acceptance.10", "swimlane.bar.acceptance.11", "swimlane.bar.acceptance.01", "swimlane.bar.business.1", "swimlane.bar.business.0", "swimlane.hexbg.0", "swimlane.hexbg.1");
		// what is relevant is the y-placements here
		checkLocationSizeColors("swimlane.bar.acceptance.11", 0,  2, 590, 6, proxy -> { });
		checkLocationSizeColors("swimlane.bar.acceptance.10", 0, 12, 590, 6, proxy -> { });
		checkLocationSizeColors("swimlane.bar.acceptance.01", 0, 22, 590, 6, proxy -> { });
		checkLocationSizeColors("swimlane.hexbg.0", 29, 58, 236, 204, proxy -> { });
		checkLocationSizeColors("swimlane.bar.business.0", 77, 157, 141, 6, proxy -> { });
		checkLocationSizeColors("swimlane.hexbg.1", 324, 58, 236, 204, proxy -> { });
		checkLocationSizeColors("swimlane.bar.business.1", 372, 157, 141, 6, proxy -> { });
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
	public void aComplexLayoutHasTheRightComponentsInTheRightPlaces() throws InterruptedException {
		HexData h1 = context.mock(HexData.class, "h1");
		HexData h2 = context.mock(HexData.class, "h2");
		HexData h3 = context.mock(HexData.class, "h3");
		AcceptanceData a111 = context.mock(AcceptanceData.class, "a111");
		AcceptanceData a011 = context.mock(AcceptanceData.class, "a011");
		AcceptanceData a100 = context.mock(AcceptanceData.class, "a100");
		AcceptanceData a010 = context.mock(AcceptanceData.class, "a010");
		UtilityData ute = context.mock(UtilityData.class);
		context.checking(new Expectations() {{
			oneOf(h1).addBusinessLogicListener(with(any(BarControl.class)));
			oneOf(h2).addBusinessLogicListener(with(any(BarControl.class)));
			oneOf(h3).addBusinessLogicListener(with(any(BarControl.class)));
			oneOf(a111).addTestListener(with(any(BarControl.class)));
			oneOf(a011).addTestListener(with(any(BarControl.class)));
			oneOf(a100).addTestListener(with(any(BarControl.class)));
			oneOf(a010).addTestListener(with(any(BarControl.class)));
			oneOf(ute).addTestListener(with(any(BarControl.class)));
		}});
		swimlane.addHexagon(0, h1);
		swimlane.addHexagon(1, h2);
		swimlane.addHexagon(2, h3);
		// don't add them in the right order, although I believe this will be the default first case
		swimlane.addAcceptance(new int[] { 0, 1, 0 }, a010); // bottom
		swimlane.addAcceptance(new int[] { 1, 1, 1 }, a111); // top
		swimlane.addAcceptance(new int[] { 1, 0, 0 }, a100); // upper/middle
		swimlane.addAcceptance(new int[] { 0, 1, 1 }, a011); // lower/middle
		swimlane.addUtility(ute);
		
//		showFor(5000);
		// this is the order, but the y-order is not relevant here (just the z-order)
		assertControlsInOrder(shell,
			"swimlane.bar.utility",
			"swimlane.bar.acceptance.011",
			"swimlane.bar.acceptance.100",
			"swimlane.bar.acceptance.111",
			"swimlane.bar.acceptance.010",
			"swimlane.bar.business.2", "swimlane.bar.business.1", "swimlane.bar.business.0",
			"swimlane.hexbg.0", "swimlane.hexbg.1", "swimlane.hexbg.2");
		// what is relevant is the y-placements here
		checkLocationSizeColors("swimlane.bar.acceptance.111", 0,  2, 590, 6, proxy -> { });
		checkLocationSizeColors("swimlane.bar.acceptance.100", 0, 12, 590, 6, proxy -> { });
		checkLocationSizeColors("swimlane.bar.acceptance.011", 0, 22, 590, 6, proxy -> { });
		checkLocationSizeColors("swimlane.bar.acceptance.010", 0, 32, 590, 6, proxy -> { });
		checkLocationSizeColors("swimlane.hexbg.0", 20, 93, 156, 134, proxy -> { });
		checkLocationSizeColors("swimlane.bar.business.0", 52, 157, 93, 6, proxy -> { });
		checkLocationSizeColors("swimlane.hexbg.1", 217, 93, 156, 134, proxy -> { });
		checkLocationSizeColors("swimlane.bar.business.1", 249, 157, 93, 6, proxy -> { });
		checkLocationSizeColors("swimlane.hexbg.2", 413, 93, 156, 134, proxy -> { });
		checkLocationSizeColors("swimlane.bar.business.2", 445, 157, 93, 6, proxy -> { });
		checkLocationSizeColors("swimlane.bar.utility", 0, 282, 590, 6, proxy -> { });
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
		dumpComposite((Composite) swimlane.getTop(), "");
		showFor(5000);
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

	/*
	@Test
	public void testThatAllTheControlsArePresent() throws InterruptedException {
		specifyModel();
		assertControls(shell, "swimlane.acceptance.11", "swimlane.acceptance.10", "swimlane.acceptance.01", "swimlane.hex.0.bg", "swimlane.hex.0.bar", "swimlane.hex.1.bg", "swimlane.hex.1.bar", "swimlane.utility");
	}

	@Test
	public void testThatAcc1AppearsInAboutTheRightPlace() throws InterruptedException {
		specifyModel();
		Canvas acc1 = waitForControl(shell, "swimlane.acceptance.11");
		assertEquals(0, acc1.getBounds().x);
		assertEquals(2, acc1.getBounds().y);
		assertEquals(590, acc1.getBounds().width);
		assertEquals(6, acc1.getBounds().height);
	}

	@Test
	public void testThatAcc2AppearsInAboutTheRightPlace() throws InterruptedException {
		specifyModel();
		Canvas acc2 = waitForControl(shell, "swimlane.acceptance.10");
		assertEquals(0, acc2.getBounds().x);
		assertEquals(12, acc2.getBounds().y);
		assertEquals(590, acc2.getBounds().width);
		assertEquals(6, acc2.getBounds().height);
	}

	@Test
	public void testThatAcc3AppearsInAboutTheRightPlace() throws InterruptedException {
		specifyModel();
		Canvas acc2 = waitForControl(shell, "swimlane.acceptance.01");
		assertEquals(0, acc2.getBounds().x);
		assertEquals(22, acc2.getBounds().y);
		assertEquals(590, acc2.getBounds().width);
		assertEquals(6, acc2.getBounds().height);
	}

	@Test
	public void testThatHex1AppearsInAboutTheRightPlace() throws InterruptedException {
		specifyModel();
		Canvas hex1 = waitForControl(shell, "swimlane.hex.0.bg");
		assertEquals(29, hex1.getBounds().x);
		assertEquals(53, hex1.getBounds().y);
		assertEquals(236, hex1.getBounds().width);
		assertEquals(204, hex1.getBounds().height);
	}

	@Test
	public void testThatHex1BarAppearsInAboutTheRightPlace() throws InterruptedException {
		specifyModel();
		Canvas hex1 = waitForControl(shell, "swimlane.hex.0.bar");
		assertEquals(59, hex1.getBounds().x);
		assertEquals(152, hex1.getBounds().y);
		assertEquals(177, hex1.getBounds().width);
		assertEquals(6, hex1.getBounds().height);
	}

	@Test
	public void testThatHex2AppearsInAboutTheRightPlace() throws InterruptedException {
		specifyModel();
		Canvas hex2 = waitForControl(shell, "swimlane.hex.1.bg");
		assertEquals(324, hex2.getBounds().x);
		assertEquals(53, hex2.getBounds().y);
		assertEquals(236, hex2.getBounds().width);
		assertEquals(204, hex2.getBounds().height);
	}

	@Test
	public void testThatHex2BarAppearsInAboutTheRightPlace() throws InterruptedException {
		specifyModel();
		Canvas hex2 = waitForControl(shell, "swimlane.hex.1.bar");
		assertEquals(354, hex2.getBounds().x);
		assertEquals(152, hex2.getBounds().y);
		assertEquals(177, hex2.getBounds().width);
		assertEquals(6, hex2.getBounds().height);
	}

	@Test
	public void testThatUteBarAppearsInAboutTheRightPlace() throws InterruptedException {
		specifyModel();
		Canvas ute = waitForControl(shell, "swimlane.utility");
		assertEquals(0, ute.getBounds().x);
		assertEquals(282, ute.getBounds().y);
		assertEquals(590, ute.getBounds().width);
		assertEquals(6, ute.getBounds().height);
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
		ArrayList<HexData> hexagons = new ArrayList<HexData>();
		ArrayList<BarData> bars = new ArrayList<BarData>();
		for (int i=0;i<2;i++) {
			HexData hd = context.mock(HexData.class, "hex" + i);
			hexagons.add(hd);
			BarData bd = context.mock(BarData.class, "hb" + i);
			bars.add(bd);
		}
		BarData uteBar = context.mock(BarData.class);
		context.checking(new Expectations() {{
			allowing(testModel).getHexCount(); will(returnValue(1));
			allowing(testModel).getBuildTime(); will(returnValue(exactDate(2017, 04, 20, 04, 20, 00, 420)));
			allowing(testModel).getAcceptanceTests(); will(returnValue(accList));
			allowing(testModel).getHexagons(); will(returnValue(hexagons));
			allowing(testModel).getUtilityBar(); will(returnValue(uteBar));
			
			allowing(a).getId(); will(returnValue("acceptance.11"));
			allowing(a).getName(); will(returnValue("org.sample.Hex1"));
			allowing(a).getTotal(); will(returnValue(10));
			allowing(a).getComplete(); will(returnValue(3));
			allowing(a).getPassed(); will(returnValue(3));
			allowing(a).getFailures(); will(returnValue(0));
			allowing(a).getStatus(); will(returnValue(Status.OK));
			allowing(a).getMarks(); will(returnValue(new int[] { 1, 1 }));
			allowing(b).getId(); will(returnValue("acceptance.10"));
			allowing(b).getName(); will(returnValue("org.sample.Hex1"));
			allowing(b).getTotal(); will(returnValue(6));
			allowing(b).getComplete(); will(returnValue(4));
			allowing(b).getPassed(); will(returnValue(4));
			allowing(b).getFailures(); will(returnValue(0));
			allowing(b).getStatus(); will(returnValue(Status.FAILURES));
			allowing(b).getMarks(); will(returnValue(new int[] { 1, 0 }));
			allowing(c).getId(); will(returnValue("acceptance.01"));
			allowing(c).getName(); will(returnValue("org.sample.Hex1"));
			allowing(c).getTotal(); will(returnValue(12));
			allowing(c).getComplete(); will(returnValue(11));
			allowing(c).getPassed(); will(returnValue(11));
			allowing(c).getFailures(); will(returnValue(0));
			allowing(c).getStatus(); will(returnValue(Status.NONE));
			allowing(c).getMarks(); will(returnValue(new int[] { 0, 1 }));

			for (int i=0;i<2;i++) {
				HexData hi = hexagons.get(i);
				BarData bi = bars.get(i);
				allowing(hi).getId(); will(returnValue("hex." + i));
				allowing(hi).getBar(); will(returnValue(bi));
				allowing(hi).getPorts(); will(returnValue(new ArrayList<PortData>()));
				allowing(bi).getId(); will(returnValue("bar" + i));
				allowing(bi).getName(); will(returnValue("org.sample.Hex" + i));
				allowing(bi).getTotal(); will(returnValue(12));
				allowing(bi).getComplete(); will(returnValue(4*i));
				allowing(bi).getPassed(); will(returnValue(4*i));
				allowing(bi).getFailures(); will(returnValue(0));
				allowing(bi).getStatus(); will(returnValue(Status.OK));
				allowing(bi).getMarks(); will(returnValue(new int[] { 1 }));
				
				oneOf(md).addBarListener(with(bi), with(aNonNull(BarDataListener.class)));
			}
			allowing(uteBar).getId(); will(returnValue("utility"));
			allowing(uteBar).getName(); will(returnValue("org.sample.Hex1"));
			allowing(uteBar).getTotal(); will(returnValue(10));
			allowing(uteBar).getComplete(); will(returnValue(5));
			allowing(uteBar).getPassed(); will(returnValue(5));
			allowing(uteBar).getFailures(); will(returnValue(0));
			allowing(uteBar).getStatus(); will(returnValue(Status.OK));
			allowing(uteBar).getMarks(); will(returnValue(new int[] { 1 }));

			oneOf(md).addBarListener(with(a), with(aNonNull(BarDataListener.class)));
			oneOf(md).addBarListener(with(b), with(aNonNull(BarDataListener.class)));
			oneOf(md).addBarListener(with(c), with(aNonNull(BarDataListener.class)));

			oneOf(md).addBarListener(with(uteBar), with(aNonNull(BarDataListener.class)));
		}});
		return testModel;
	}
	*/

}
