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
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.TestInfo;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;
import com.gmmapowell.swimlane.eclipse.interfaces.Tree;
import com.gmmapowell.swimlane.eclipse.models.HexagonAccumulator;
import com.gmmapowell.swimlane.eclipse.models.SimpleTree;
import com.gmmapowell.swimlane.eclipse.models.SolidModelDispatcher;
import com.gmmapowell.swimlane.eclipse.testrunner.TestCaseInfo;
import com.gmmapowell.swimlane.tests.swtutil.TestBase;

public class UpdatingRealTimeTestResultInfo extends TestBase {
	SolidModelDispatcher md = new SolidModelDispatcher();
	Accumulator acc = new HexagonAccumulator(md);
	HexagonDataModel hdm = (HexagonDataModel)acc;
	TestResultReporter trr = (TestResultReporter) acc;

	@Test
	public void whenTheTreeArrivesWeAreNotifiedOfTheNumberOfTests() {
		acc.acceptance(String.class, new ArrayList<>());
		acc.analysisComplete();
		List<BarData> bars = hdm.getAcceptanceTests();
		assertEquals(1, bars.size());
		BarData bar = bars.get(0);
		BarDataListener bl = context.mock(BarDataListener.class);
		md.addBarListener(bl);
		context.checking(new Expectations() {{
			oneOf(bl).barChanged(bar);
		}});
		issueTree();
		assertEquals(1, bar.getTotal());
	}

	@Test
	public void reportingTestSuccessFiresBarDataListener() {
		acc.acceptance(String.class, new ArrayList<>());
		acc.analysisComplete();
		List<BarData> bars = hdm.getAcceptanceTests();
		assertEquals(1, bars.size());
		BarData bar = bars.get(0);
		TestInfo test = context.mock(TestInfo.class);
		BarDataListener bl = context.mock(BarDataListener.class);
		md.addBarListener(bl);
		context.checking(new Expectations() {{
			allowing(test).classUnderTest(); will(returnValue(String.class.getName()));
			oneOf(bl).barChanged(bar);
		}});
		trr.testSuccess(test);
	}

	@Test
	public void reportingTestFailureFiresBarDataListener() {
		acc.acceptance(String.class, new ArrayList<>());
		acc.analysisComplete();
		List<BarData> bars = hdm.getAcceptanceTests();
		assertEquals(1, bars.size());
		BarData bar = bars.get(0);
		TestInfo test = context.mock(TestInfo.class);
		BarDataListener bl = context.mock(BarDataListener.class);
		md.addBarListener(bl);
		context.checking(new Expectations() {{
			allowing(test).classUnderTest(); will(returnValue(String.class.getName()));
			oneOf(bl).barChanged(bar);
		}});
		trr.testFailure(test);
	}

	@Test
	public void weCorrectlyIdentifyTheCorrectBarForTheTestNamedInteger() {
		acc.acceptance(Integer.class, Arrays.asList(String.class, Float.class));
		acc.acceptance(String.class, Arrays.asList(String.class));
		acc.acceptance(Double.class, Arrays.asList(Float.class));
		acc.analysisComplete();
		assertEquals(0, hdm.getErrors().size());
		List<BarData> bars = hdm.getAcceptanceTests();
		assertEquals(3, bars.size());
		BarData bar = findBar(bars, "acceptance.11");
		assertNotNull(bar);
		TestInfo test = context.mock(TestInfo.class);
		BarDataListener bl = context.mock(BarDataListener.class);
		md.addBarListener(bl);
		context.checking(new Expectations() {{
			allowing(test).classUnderTest(); will(returnValue(Integer.class.getName()));
			oneOf(bl).barChanged(bar);
		}});
		trr.testSuccess(test);
	}

	@Test
	public void weCorrectlyIdentifyTheCorrectBarForTheTestNamedString() {
		acc.acceptance(Integer.class, Arrays.asList(String.class, Float.class));
		acc.acceptance(String.class, Arrays.asList(String.class));
		acc.acceptance(Double.class, Arrays.asList(Float.class));
		acc.analysisComplete();
		assertEquals(0, hdm.getErrors().size());
		List<BarData> bars = hdm.getAcceptanceTests();
		assertEquals(3, bars.size());
		BarData bar = findBar(bars, "acceptance.10");
		assertNotNull(bar);
		TestInfo test = context.mock(TestInfo.class);
		BarDataListener bl = context.mock(BarDataListener.class);
		md.addBarListener(bl);
		context.checking(new Expectations() {{
			allowing(test).classUnderTest(); will(returnValue(String.class.getName()));
			exactly(2).of(bl).barChanged(bar);
		}});
		issueTree();
		trr.testSuccess(test);
		assertEquals(1, bar.classesUnderTest().size());
		assertEquals(1, bar.getTotal());
		assertEquals(1, bar.getComplete());
	}

	@Test
	public void weCorrectlyIdentifyTheCorrectBarForAFailingTestNamedString() {
		acc.acceptance(Integer.class, Arrays.asList(String.class, Float.class));
		acc.acceptance(String.class, Arrays.asList(String.class));
		acc.acceptance(Double.class, Arrays.asList(Float.class));
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
		md.addBarListener(bl);
		context.checking(new Expectations() {{
			allowing(test).classUnderTest(); will(returnValue(String.class.getName()));
			exactly(2).of(bl).barChanged(bar);
		}});
		issueTree();
		trr.testFailure(test);
		assertEquals(1, bar.classesUnderTest().size());
		assertEquals(1, bar.getTotal());
		assertEquals(1, bar.getComplete());
	}

	protected void issueTree() {
		{
			Tree<TestInfo> top = new SimpleTree<TestInfo>(new TestCaseInfo(TestCaseInfo.Type.META, "", "Top"));
			TestCaseInfo t1;
			{
				t1 = new TestCaseInfo(TestCaseInfo.Type.TEST, String.class.getName(), "fail1");
				t1.failed();
				top.add(new SimpleTree<TestInfo>(t1));
			}
			trr.tree(top);
		}
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
