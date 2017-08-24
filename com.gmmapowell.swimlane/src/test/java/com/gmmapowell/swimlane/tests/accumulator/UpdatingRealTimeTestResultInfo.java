package com.gmmapowell.swimlane.tests.accumulator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.analyzer.BusinessRole;
import com.gmmapowell.swimlane.eclipse.analyzer.UtilityRole;
import com.gmmapowell.swimlane.eclipse.interfaces.AnalysisAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.BarDataListener;
import com.gmmapowell.swimlane.eclipse.interfaces.ErrorAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.GroupOfTests;
import com.gmmapowell.swimlane.eclipse.interfaces.TestResultReporter;
import com.gmmapowell.swimlane.eclipse.models.SwimlaneModel;
import com.gmmapowell.swimlane.eclipse.roles.AcceptanceRole;
import com.gmmapowell.swimlane.eclipse.roles.AdapterRole;
import com.gmmapowell.swimlane.testsupport.CaptureLayout;
import com.gmmapowell.swimlane.testsupport.DirectRunner;
import com.gmmapowell.swimlane.testsupport.matchers.BarInfoMatcher;

public class UpdatingRealTimeTestResultInfo {
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();
	ErrorAccumulator errors = context.mock(ErrorAccumulator.class);
	CaptureLayout capture = new CaptureLayout(context);
	SwimlaneModel acc = new SwimlaneModel(new DirectRunner(), errors, capture);
	TestResultReporter trr = (TestResultReporter) acc;
	GroupOfTests grp = context.mock(GroupOfTests.class);
	List<String> testsForClass1 = new ArrayList<>();

	@Before
	public void setup() {
		testsForClass1.add("case1");
		testsForClass1.add("case2");
		testsForClass1.add("case3");
		context.checking(new Expectations() {{
			allowing(grp).addTest("Acc");
			allowing(grp).addTest("TestClass1");
			allowing(grp).addTest("TestClass2");
			allowing(grp).addTest("Ute");
		}});
	}

	@Test
	public void weCanRegisterForUpdatesAboutBusinessLogicTestsInAHex() {
		AnalysisAccumulator analyzer = acc.startAnalysis(new Date());
		analyzer.haveTestClass(grp, "TestClass1", new BusinessRole(String.class), testsForClass1);
		analyzer.analysisComplete(new Date());
		BarDataListener lsnr = capture.hexes.get(0);
		Sequence passing = context.sequence("passing");
		context.checking(new Expectations() {{
			oneOf(lsnr).barChanged(with(BarInfoMatcher.passing(0, 3))); inSequence(passing);
			oneOf(lsnr).barChanged(with(BarInfoMatcher.passing(1, 3))); inSequence(passing);
			oneOf(lsnr).barChanged(with(BarInfoMatcher.passing(2, 3))); inSequence(passing);
			oneOf(lsnr).barChanged(with(BarInfoMatcher.passing(3, 3))); inSequence(passing);
		}});
		acc.testsStarted(grp, new Date());
		acc.testSuccess(grp, "TestClass1", "case1");
		acc.testSuccess(grp, "TestClass1", "case2");
		acc.testSuccess(grp, "TestClass1", "case3");
		acc.testsCompleted(grp, new Date());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void weSeeFailuresAsWellAsSuccesses() {
		AnalysisAccumulator analyzer = acc.startAnalysis(new Date());
		analyzer.haveTestClass(grp, "TestClass1", new BusinessRole(String.class), testsForClass1);
		analyzer.analysisComplete(new Date());
		BarDataListener lsnr = capture.hexes.get(0);
		List<String> stack = context.mock(List.class, "stack");
		List<String> expected = context.mock(List.class, "expected");
		List<String> actual = context.mock(List.class, "actual");
		Sequence running = context.sequence("running");
		context.checking(new Expectations() {{
			oneOf(lsnr).barChanged(with(BarInfoMatcher.passing(0, 3))); inSequence(running);
			oneOf(lsnr).barChanged(with(BarInfoMatcher.passing(1, 3))); inSequence(running);
			oneOf(lsnr).barChanged(with(BarInfoMatcher.failing(2, 3))); inSequence(running);
			oneOf(lsnr).barChanged(with(BarInfoMatcher.failing(3, 3))); inSequence(running);
		}});
		acc.testsStarted(grp, new Date());
		acc.testSuccess(grp, "TestClass1", "case1");
		acc.testFailure(grp, "TestClass1", "case2", stack, expected, actual);
		acc.testSuccess(grp, "TestClass1", "case3");
		acc.testsCompleted(grp, new Date());
		acc.testsCompleted(grp, new Date());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void barUpdatesAreIndependent() {
		AnalysisAccumulator analyzer = acc.startAnalysis(new Date());
		analyzer.haveTestClass(grp, "Acc", new AcceptanceRole(String.class, Double.class), testsForClass1);
		analyzer.haveTestClass(grp, "TestClass1", new BusinessRole(String.class), testsForClass1);
		analyzer.haveTestClass(grp, "TestClass2", new BusinessRole(Double.class), testsForClass1);
		analyzer.analysisComplete(new Date());
		BarDataListener accl = capture.acceptance.get(0);
		BarDataListener lsnr1 = capture.hexes.get(0);
		BarDataListener lsnr2 = capture.hexes.get(1);
		List<String> stack = context.mock(List.class, "stack");
		List<String> expected = context.mock(List.class, "expected");
		List<String> actual = context.mock(List.class, "actual");
		Sequence seq = context.sequence("seq");
		context.checking(new Expectations() {{
			oneOf(accl).barChanged(with(BarInfoMatcher.passing(0, 3))); inSequence(seq);
			oneOf(lsnr1).barChanged(with(BarInfoMatcher.passing(0, 3))); inSequence(seq);
			oneOf(lsnr2).barChanged(with(BarInfoMatcher.passing(0, 3))); inSequence(seq);
			oneOf(lsnr1).barChanged(with(BarInfoMatcher.passing(1, 3))); inSequence(seq);
			oneOf(lsnr1).barChanged(with(BarInfoMatcher.passing(2, 3))); inSequence(seq);
			oneOf(accl).barChanged(with(BarInfoMatcher.passing(1, 3))); inSequence(seq);
			oneOf(lsnr2).barChanged(with(BarInfoMatcher.passing(1, 3))); inSequence(seq);
			oneOf(lsnr2).barChanged(with(BarInfoMatcher.failing(2, 3))); inSequence(seq);
			oneOf(lsnr2).barChanged(with(BarInfoMatcher.failing(3, 3))); inSequence(seq);
			oneOf(lsnr1).barChanged(with(BarInfoMatcher.passing(3, 3))); inSequence(seq);
		}});
		acc.testsStarted(grp, new Date());
		acc.testSuccess(grp, "TestClass1", "case1");
		acc.testSuccess(grp, "TestClass1", "case2");
		acc.testSuccess(grp, "Acc", "case3");
		acc.testSuccess(grp, "TestClass2", "case1");
		acc.testFailure(grp, "TestClass2", "case2", stack, expected, actual);
		acc.testSuccess(grp, "TestClass2", "case3");
		acc.testSuccess(grp, "TestClass1", "case3");
		acc.testsCompleted(grp, new Date());
	}

	@Test
	public void weCanRegisterForUpdatesAboutAdapterTests() {
		AnalysisAccumulator analyzer = acc.startAnalysis(new Date());
		analyzer.haveTestClass(grp, "TestClass1", new AdapterRole(String.class, Double.class, null, Integer.class), testsForClass1);
		analyzer.analysisComplete(new Date());
		BarDataListener lsnr1 = capture.adapters.get("adapter_0_nw_0");
		context.checking(new Expectations() {{
			oneOf(lsnr1).barChanged(with(BarInfoMatcher.passing(0, 3)));
			oneOf(lsnr1).barChanged(with(BarInfoMatcher.passing(1, 3)));
		}});
		acc.testsStarted(grp, new Date());
		acc.testSuccess(grp, "TestClass1", "case1");
		acc.testsCompleted(grp, new Date());
	}

	@Test
	public void weCanRegisterForUpdatesAboutUtilityTests() {
		AnalysisAccumulator analyzer = acc.startAnalysis(new Date());
		analyzer.haveTestClass(grp, "Ute", new UtilityRole(), testsForClass1);
		analyzer.analysisComplete(new Date());
		BarDataListener lsnr1 = capture.utility;
		context.checking(new Expectations() {{
			oneOf(lsnr1).barChanged(with(BarInfoMatcher.passing(0, 3)));
			oneOf(lsnr1).barChanged(with(BarInfoMatcher.passing(1, 3)));
		}});
		acc.testsStarted(grp, new Date());
		acc.testSuccess(grp, "Ute", "case1");
		acc.testsCompleted(grp, new Date());
	}
}
