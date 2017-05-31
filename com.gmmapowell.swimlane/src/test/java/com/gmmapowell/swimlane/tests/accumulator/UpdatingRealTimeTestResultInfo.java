package com.gmmapowell.swimlane.tests.accumulator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.BarDataListener;
import com.gmmapowell.swimlane.eclipse.interfaces.HexData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel.Status;
import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;
import com.gmmapowell.swimlane.eclipse.interfaces.Tree;
import com.gmmapowell.swimlane.eclipse.models.HexagonAccumulator;
import com.gmmapowell.swimlane.eclipse.models.SimpleTree;
import com.gmmapowell.swimlane.eclipse.models.SolidModelDispatcher;
import com.gmmapowell.swimlane.eclipse.models.TestGroup;
import com.gmmapowell.swimlane.eclipse.testrunner.TestCaseInfo;
import com.gmmapowell.swimlane.tests.swtutil.TestBase;

public class UpdatingRealTimeTestResultInfo extends TestBase {
	SolidModelDispatcher md = new SolidModelDispatcher();
	Accumulator acc = new HexagonAccumulator(md);
	HexagonDataModel hdm = (HexagonDataModel)acc;
	TestResultReporter trr = (TestResultReporter) acc;
	TestGroup grp = new TestGroup(null);

	@Test
	public void whenTheTreeArrivesWeAreNotifiedOfTheNumberOfTests() {
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
		issueTree();
		assertEquals(4, bar.getTotal());
		assertEquals(0, bar.getComplete());
		assertEquals(Status.OK, bar.getStatus());
	}

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
			exactly(2).of(bl).barChanged(bar);
		}});
		issueTree();
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
		issueTree();
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
			exactly(2).of(bl).barChanged(bar);
		}});
		issueTree();
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
		issueTree();
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
		issueTree();
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
		issueTree();
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
		issueTree();
		assertEquals(4, bar.getTotal());
		assertEquals(0, bar.getComplete());
		assertEquals(Status.OK, bar.getStatus());
	}

	protected void issueTree() {
		Tree<TestInfo> top = new SimpleTree<TestInfo>(new TestCaseInfo(TestCaseInfo.Type.META, "", "Top"));
		{
			TestCaseInfo t1 = new TestCaseInfo(TestCaseInfo.Type.TEST, String.class.getName(), "test1");
			top.add(new SimpleTree<TestInfo>(t1));
		}
		{
			TestCaseInfo t2 = new TestCaseInfo(TestCaseInfo.Type.TEST, String.class.getName(), "test2");
			top.add(new SimpleTree<TestInfo>(t2));
		}
		{
			TestCaseInfo t3 = new TestCaseInfo(TestCaseInfo.Type.TEST, String.class.getName(), "test3");
			top.add(new SimpleTree<TestInfo>(t3));
		}
		{
			TestCaseInfo t4 = new TestCaseInfo(TestCaseInfo.Type.TEST, String.class.getName(), "test4");
			top.add(new SimpleTree<TestInfo>(t4));
		}
		trr.tree(top);
	}

	protected BarData findBar(List<BarData> bars, String name) {
		BarData bar = null;
		for (BarData b : bars) {
			if (b.getId().equals(name))
				bar = b;
		}
		return bar;
	}

	// also need to test for tree() calls
	// and what if there is no bar
}
