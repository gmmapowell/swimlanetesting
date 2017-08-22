package com.gmmapowell.swimlane.tests.accumulator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
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
import com.gmmapowell.swimlane.testsupport.matchers.TestInfoMatcher;

public class UpdatingRealTimeTestResultInfo {
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();
	ErrorAccumulator errors = context.mock(ErrorAccumulator.class);
	CaptureLayout capture = new CaptureLayout(context);
	SwimlaneModel acc = new SwimlaneModel(new DirectRunner(), errors, capture);
	TestResultReporter trr = (TestResultReporter) acc;
	GroupOfTests grp = context.mock(GroupOfTests.class);
	List<String> tests = new ArrayList<>();

	@Test
	public void weCanRegisterForUpdatesAboutBusinessLogicTestsInAHex() {
		AnalysisAccumulator analyzer = acc.startAnalysis(new Date());
		analyzer.haveTestClass(grp, "TestClass1", new BusinessRole(String.class), tests);
		analyzer.analysisComplete(new Date());
		BarDataListener lsnr = capture.hexes.get(0);
		context.checking(new Expectations() {{
			oneOf(lsnr).clearGroup(grp);
			oneOf(lsnr).testCompleted(with(TestInfoMatcher.success(grp, "TestClass1", "case1")));
		}});
		acc.testCount(grp, 1);
		acc.testSuccess(grp, "TestClass1", "case1");
		acc.testsCompleted(grp, new Date());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void weSeeFailuresAsWellAsSuccesses() {
		AnalysisAccumulator analyzer = acc.startAnalysis(new Date());
		analyzer.haveTestClass(grp, "TestClass1", new BusinessRole(String.class), tests);
		analyzer.analysisComplete(new Date());
		BarDataListener lsnr = capture.hexes.get(0);
		List<String> stack = context.mock(List.class, "stack");
		List<String> expected = context.mock(List.class, "expected");
		List<String> actual = context.mock(List.class, "actual");
		context.checking(new Expectations() {{
			oneOf(lsnr).clearGroup(grp);
			oneOf(lsnr).testCompleted(with(TestInfoMatcher.failure(grp, "TestClass1", "case1", stack, expected, actual)));
		}});
		acc.testCount(grp, 1);
		acc.testFailure(grp, "TestClass1", "case1", stack, expected, actual);
		acc.testsCompleted(grp, new Date());
	}

	@Test
	public void weCanSelectivelyFireABar() {
		AnalysisAccumulator analyzer = acc.startAnalysis(new Date());
		analyzer.haveTestClass(grp, "Acc", new AcceptanceRole(String.class, Double.class), tests);
		analyzer.haveTestClass(grp, "TestClass1", new BusinessRole(String.class), tests);
		analyzer.haveTestClass(grp, "TestClass2", new BusinessRole(Double.class), tests);
		analyzer.analysisComplete(new Date());
		BarDataListener lsnr1 = capture.hexes.get(0);
		BarDataListener lsnr2 = capture.hexes.get(1);
		context.checking(new Expectations() {{
			allowing(capture.acceptance.get(0));
			oneOf(lsnr1).clearGroup(grp);
			oneOf(lsnr2).clearGroup(grp);
			oneOf(lsnr1).testCompleted(with(TestInfoMatcher.success(grp, "TestClass1", "case1")));
			oneOf(lsnr2).testCompleted(with(TestInfoMatcher.success(grp, "TestClass2", "case1")));
		}});
		acc.testCount(grp, 2);
		acc.testSuccess(grp, "TestClass1", "case1");
		acc.testSuccess(grp, "TestClass2", "case1");
		acc.testsCompleted(grp, new Date());
	}

	@Test
	public void weCanRegisterForUpdatesAboutAcceptanceTests() {
		AnalysisAccumulator analyzer = acc.startAnalysis(new Date());
		analyzer.haveTestClass(grp, "Acc", new AcceptanceRole(String.class, Double.class), tests);
		analyzer.analysisComplete(new Date());
		BarDataListener lsnr1 = capture.acceptance.get(0);
		context.checking(new Expectations() {{
			oneOf(lsnr1).clearGroup(grp);
			oneOf(lsnr1).testCompleted(with(TestInfoMatcher.success(grp, "Acc", "case1")));
		}});
		acc.testCount(grp, 1);
		acc.testSuccess(grp, "Acc", "case1");
		acc.testsCompleted(grp, new Date());
	}

	@Test
	public void weCanRegisterForUpdatesAboutAdapterTests() {
		AnalysisAccumulator analyzer = acc.startAnalysis(new Date());
		analyzer.haveTestClass(grp, "Test1", new AdapterRole(String.class, Double.class, null, Integer.class), tests);
		analyzer.analysisComplete(new Date());
		BarDataListener lsnr1 = capture.adapters.get(Integer.class.getName());
		context.checking(new Expectations() {{
			oneOf(lsnr1).clearGroup(grp);
			oneOf(lsnr1).testCompleted(with(TestInfoMatcher.success(grp, "Test1", "case1")));
		}});
		acc.testCount(grp, 1);
		acc.testSuccess(grp, "Test1", "case1");
		acc.testsCompleted(grp, new Date());
	}

	@Test
	public void weCanRegisterForUpdatesAboutUtilityTests() {
		AnalysisAccumulator analyzer = acc.startAnalysis(new Date());
		analyzer.haveTestClass(grp, "Ute", new UtilityRole(), tests);
		analyzer.analysisComplete(new Date());
		BarDataListener lsnr1 = capture.utility;
		context.checking(new Expectations() {{
			oneOf(lsnr1).clearGroup(grp);
			oneOf(lsnr1).testCompleted(with(TestInfoMatcher.success(grp, "Ute", "case1")));
		}});
		acc.testCount(grp, 1);
		acc.testSuccess(grp, "Ute", "case1");
		acc.testsCompleted(grp, new Date());
	}

}
