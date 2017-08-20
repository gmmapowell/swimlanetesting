package com.gmmapowell.swimlane.tests.accumulator;

import static org.junit.Assert.fail;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.DateListener;
import com.gmmapowell.swimlane.eclipse.interfaces.ErrorAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;
import com.gmmapowell.swimlane.eclipse.interfaces.ViewLayout;
import com.gmmapowell.swimlane.eclipse.models.SwimlaneModel;

public class UpdatingRealTimeTestResultInfo {
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();
	ErrorAccumulator errors = context.mock(ErrorAccumulator.class);
	DateListener lsnr = context.mock(DateListener.class);
	ViewLayout layout = context.mock(ViewLayout.class);
	SwimlaneModel acc = new SwimlaneModel(errors, layout);
	TestResultReporter trr = (TestResultReporter) acc;
	GroupOfTests grp = context.mock(GroupOfTests.class);

	@Test
	public void whenTheTreeArrivesWeAreNotifiedOfTheNumberOfTests() {
		fail("not yet refactored");
		/*
		acc.acceptance(grp, String.class, new ArrayList<>());
		acc.analysisComplete();
		List<BarData> bars = hdm.getAcceptanceTests();
		assertEquals(1, bars.size());
		BarData bar = bars.get(0);
		BarDataListener bl = context.mock(BarDataListener.class);
		md.addBarListener(bar, bl);
		context.checking(new Expectations() {{
			oneOf(bl).barChanged(bar);
		}});
		assertEquals(4, bar.getTotal());
		assertEquals(0, bar.getComplete());
		assertEquals(Status.OK, bar.getStatus());
		*/
	}
/*
	@Test
	public void reportingTestSuccessFiresBarDataListener() {
		acc.acceptance(grp, String.class, new ArrayList<>());
		acc.analysisComplete();
		List<BarData> bars = hdm.getAcceptanceTests();
		assertEquals(1, bars.size());
		BarData bar = bars.get(0);
		TestInfo test = context.mock(TestInfo.class);
		BarDataListener bl = context.mock(BarDataListener.class);
		md.addBarListener(bar, bl);
		context.checking(new Expectations() {{
			allowing(test).classUnderTest(); will(returnValue(String.class.getName()));
			allowing(test).groupName(); will(returnValue(grp));
			exactly(2).of(bl).barChanged(bar);
		}});
		trr.testSuccess(test);
		assertEquals(4, bar.getTotal());
		assertEquals(1, bar.getComplete());
		assertEquals(Status.OK, bar.getStatus());
	}

	@Test
	public void reportingTestFailureFiresBarDataListener() {
		acc.acceptance(grp, String.class, new ArrayList<>());
		acc.analysisComplete();
		List<BarData> bars = hdm.getAcceptanceTests();
		assertEquals(1, bars.size());
		BarData bar = bars.get(0);
		TestInfo test = context.mock(TestInfo.class);
		BarDataListener bl = context.mock(BarDataListener.class);
		md.addBarListener(bar, bl);
		context.checking(new Expectations() {{
			allowing(test).classUnderTest(); will(returnValue(String.class.getName()));
			exactly(2).of(bl).barChanged(bar);
		}});
		trr.testFailure(test);
		assertEquals(4, bar.getTotal());
		assertEquals(1, bar.getComplete());
		assertEquals(Status.FAILURES, bar.getStatus());
	}

	@Test
	public void weCorrectlyIdentifyTheCorrectBarForTheTestNamedInteger() {
		acc.acceptance(grp, Integer.class, Arrays.asList(String.class, Float.class));
		acc.acceptance(grp, String.class, Arrays.asList(String.class));
		acc.acceptance(grp, Double.class, Arrays.asList(Float.class));
		acc.analysisComplete();
		assertEquals(0, hdm.getErrors().size());
		List<BarData> bars = hdm.getAcceptanceTests();
		assertEquals(3, bars.size());
		BarData bar = findBar(bars, "acceptance.11");
		assertNotNull(bar);
		TestInfo test = context.mock(TestInfo.class);
		BarDataListener bl = context.mock(BarDataListener.class);
		md.addBarListener(bar, bl);
		context.checking(new Expectations() {{
			allowing(test).classUnderTest(); will(returnValue(Integer.class.getName()));
			allowing(test).groupName(); will(returnValue(grp));
			oneOf(bl).barChanged(bar);
		}});
		trr.testSuccess(test);
	}

	@Test
	public void weCorrectlyIdentifyTheCorrectBarForTheTestNamedString() {
		acc.acceptance(grp, Integer.class, Arrays.asList(String.class, Float.class));
		acc.acceptance(grp, String.class, Arrays.asList(String.class));
		acc.acceptance(grp, Double.class, Arrays.asList(Float.class));
		acc.analysisComplete();
		assertEquals(0, hdm.getErrors().size());
		List<BarData> bars = hdm.getAcceptanceTests();
		assertEquals(3, bars.size());
		BarData bar = findBar(bars, "acceptance.10");
		assertNotNull(bar);
		TestInfo test = context.mock(TestInfo.class);
		BarDataListener bl = context.mock(BarDataListener.class);
		md.addBarListener(bar, bl);
		context.checking(new Expectations() {{
			allowing(test).classUnderTest(); will(returnValue(String.class.getName()));
			allowing(test).groupName(); will(returnValue(grp));
			exactly(2).of(bl).barChanged(bar);
		}});
		trr.testSuccess(test);
		assertEquals(1, bar.classesUnderTest().size());
		assertEquals(4, bar.getTotal());
		assertEquals(1, bar.getComplete());
		assertEquals(Status.OK, bar.getStatus());
	}

	@Test
	public void weCorrectlyIdentifyTheCorrectBarForAFailingTestNamedString() {
		acc.acceptance(grp, Integer.class, Arrays.asList(String.class, Float.class));
		acc.acceptance(grp, String.class, Arrays.asList(String.class));
		acc.acceptance(grp, Double.class, Arrays.asList(Float.class));
		acc.analysisComplete();
		assertEquals(0, hdm.getErrors().size());
		List<BarData> bars = hdm.getAcceptanceTests();
		assertEquals(3, bars.size());
		BarData bar = findBar(bars, "acceptance.10");
		assertNotNull(bar);
		assertEquals(1, bar.classesUnderTest().size());
		assertEquals(String.class.getName(), bar.classesUnderTest().get(0));
		TestInfo test = context.mock(TestInfo.class);
		BarDataListener bl = context.mock(BarDataListener.class);
		md.addBarListener(bar, bl);
		context.checking(new Expectations() {{
			allowing(test).classUnderTest(); will(returnValue(String.class.getName()));
			exactly(2).of(bl).barChanged(bar);
		}});
		trr.testFailure(test);
		assertEquals(1, bar.classesUnderTest().size());
		assertEquals(4, bar.getTotal());
		assertEquals(1, bar.getComplete());
		assertEquals(Status.FAILURES, bar.getStatus());
	}

	@Test
	public void businessLogicTestsAreNotifiedOfTheNumberOfTests() {
		acc.logic(grp, String.class, null);
		acc.analysisComplete();
		List<HexData> hexes = hdm.getHexagons();
		assertEquals(1, hexes.size());
		BarData bar = hexes.get(0).getBar();
		BarDataListener bl = context.mock(BarDataListener.class);
		md.addBarListener(bar, bl);
		context.checking(new Expectations() {{
			oneOf(bl).barChanged(bar);
		}});
		assertEquals(4, bar.getTotal());
		assertEquals(0, bar.getComplete());
		assertEquals(Status.OK, bar.getStatus());
	}

	@Test
	public void utilityTestsAreNotifiedOfTheNumberOfTests() {
		acc.utility(grp, String.class);
		acc.analysisComplete();
		List<HexData> hexes = hdm.getHexagons();
		assertEquals(0, hexes.size());
		BarData bar = hdm.getUtilityBar();
		BarDataListener bl = context.mock(BarDataListener.class);
		md.addBarListener(bar, bl);
		context.checking(new Expectations() {{
			oneOf(bl).barChanged(bar);
		}});
		assertEquals(4, bar.getTotal());
		assertEquals(0, bar.getComplete());
		assertEquals(Status.OK, bar.getStatus());
	}

	@Test
	public void adapterTestsAreNotifiedOfTheNumberOfTests() {
		acc.adapter(grp, String.class, null, Integer.class, Float.class);
		acc.analysisComplete();
		List<HexData> hexes = hdm.getHexagons();
		assertEquals(1, hexes.size());
		BarData bar = hexes.get(0).getPorts().get(0).getAdapters().get(0);
		BarDataListener bl = context.mock(BarDataListener.class);
		md.addBarListener(bar, bl);
		context.checking(new Expectations() {{
			oneOf(bl).barChanged(bar);
		}});
		assertEquals(4, bar.getTotal());
		assertEquals(0, bar.getComplete());
		assertEquals(Status.OK, bar.getStatus());
	}

	protected BarData findBar(List<BarData> bars, String name) {
		BarData bar = null;
		for (BarData b : bars) {
			if (b.getId().equals(name))
				bar = b;
		}
		return bar;
	}
*/
	// also need to test for tree() calls
	// and what if there is no bar
}
