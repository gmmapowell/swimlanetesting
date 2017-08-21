package com.gmmapowell.swimlane.tests.view.layout;

import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.HexData;
import com.gmmapowell.swimlane.eclipse.views.BarControl;
import com.gmmapowell.swimlane.tests.view.hex.BaseHexViewTest;

public class LayoutTests extends BaseHexViewTest {

	@Test
	public void testThatASimpleLayoutOfOneHexWithBusinessLogicHasTheRightComponentsInTheRightPlaces() throws InterruptedException {
		HexData h1 = context.mock(HexData.class);
		context.checking(new Expectations() {{
			oneOf(h1).addBusinessLogicListener(with(any(BarControl.class)));
		}});
		swimlane.addHexagon(0, h1);
//		showFor(5000);
		assertControlsInOrder(shell, "swimlane.hexbg.0", "swimlane.bar.business.0");
		checkLocationSizeColors("swimlane.hexbg.0", 157, 26, 276, 238, proxy -> { });
		checkLocationSizeColors("swimlane.bar.business.0", 213, 142, 165, 6, proxy -> { });
	}

	@Test
	public void testThatALayoutOfTwoHexesWithBusinessLogicHasTheRightComponentsInTheRightPlaces() throws InterruptedException {
		HexData h1 = context.mock(HexData.class, "h1");
		HexData h2 = context.mock(HexData.class, "h2");
		context.checking(new Expectations() {{
			oneOf(h1).addBusinessLogicListener(with(any(BarControl.class)));
			oneOf(h2).addBusinessLogicListener(with(any(BarControl.class)));
		}});
		swimlane.addHexagon(0, h1);
		swimlane.addHexagon(1, h2);
//		showFor(5000);
		assertControlsInOrder(shell, "swimlane.hexbg.0", "swimlane.bar.business.0", "swimlane.hexbg.1", "swimlane.bar.business.1");
		checkLocationSizeColors("swimlane.hexbg.0", 29, 43, 236, 204, proxy -> { });
		checkLocationSizeColors("swimlane.bar.business.0", 77, 142, 141, 6, proxy -> { });
		checkLocationSizeColors("swimlane.hexbg.1", 324, 43, 236, 204, proxy -> { });
		checkLocationSizeColors("swimlane.bar.business.1", 372, 142, 141, 6, proxy -> { });
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
