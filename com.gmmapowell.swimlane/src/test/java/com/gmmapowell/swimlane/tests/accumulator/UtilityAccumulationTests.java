package com.gmmapowell.swimlane.tests.accumulator;

import java.util.Date;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.analyzer.UtilityRole;
import com.gmmapowell.swimlane.eclipse.interfaces.AnalysisAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.DataCentral;
import com.gmmapowell.swimlane.eclipse.interfaces.ErrorMessageListener;
import com.gmmapowell.swimlane.eclipse.interfaces.Solution;
import com.gmmapowell.swimlane.eclipse.models.HexagonAccumulator;
import com.gmmapowell.swimlane.eclipse.models.TestGroup;

public class UtilityAccumulationTests {
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();
	AnalysisAccumulator acc = new HexagonAccumulator();
	Solution solution = context.mock(Solution.class);
	ErrorMessageListener errors = context.mock(ErrorMessageListener.class);
	Date bcd = new Date();
	TestGroup grp = new TestGroup("Project", null);
	Sequence seq = context.sequence("solution");

	@Before
	public void setup() {
		context.checking(new Expectations() {{
			oneOf(errors).clear();
		}});
		((DataCentral)acc).setSolution(solution);
		((DataCentral)acc).addErrorMessageListener(errors);
	}
	
	@Test
	public void testThatWeCanStoreAndRecoverATest() {
		context.checking(new Expectations() {{
			oneOf(solution).beginHexes(); inSequence(seq);
			oneOf(solution).hexesDone(); inSequence(seq);
			oneOf(solution).needsUtilityBar(); inSequence(seq);
		}});
		acc.startAnalysis(bcd);
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new UtilityRole());
		acc.analysisComplete(bcd);
	}

	@Test
	public void testThatWeCanStoreAndRecoverMultipleTest() {
		context.checking(new Expectations() {{
			oneOf(solution).beginHexes(); inSequence(seq);
			oneOf(solution).hexesDone(); inSequence(seq);
			oneOf(solution).needsUtilityBar(); inSequence(seq);
		}});
		acc.startAnalysis(bcd);
		acc.clean(grp);
		acc.haveTestClass(grp, "TestCase1", new UtilityRole());
		acc.haveTestClass(grp, "TestCase1", new UtilityRole());
		acc.analysisComplete(bcd);
	}
}
