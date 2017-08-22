package com.gmmapowell.swimlane.tests.accumulator;

import java.util.Date;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.AnalysisAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.DataCentral;
import com.gmmapowell.swimlane.eclipse.interfaces.DateListener;
import com.gmmapowell.swimlane.eclipse.interfaces.ErrorAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.Solution;
import com.gmmapowell.swimlane.eclipse.interfaces.ViewLayout;
import com.gmmapowell.swimlane.eclipse.models.SwimlaneModel;
import com.gmmapowell.swimlane.testsupport.DirectRunner;

/* The purpose of the accumulator is to take input in one form (what we discover)
 * and to build a stable model out of it.
 * To decouple these two roles, we have two interfaces: Accumulator for input and HexagonDataModel for output;
 * here we assert that the two are coupled correctly internally.
 */
public class GeneralAccumulationTests {
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();
	Solution solution = context.mock(Solution.class);
	ErrorAccumulator errors = context.mock(ErrorAccumulator.class);
	ViewLayout layout = context.mock(ViewLayout.class);
	DataCentral acc = new SwimlaneModel(new DirectRunner(), errors, layout);
	DateListener lsnr = context.mock(DateListener.class);
	
	@Test
	public void theTrivialDateThing() {
		acc.addBuildDateListener(lsnr);
		Date d = new Date();
		context.checking(new Expectations() {{
			oneOf(lsnr).dateChanged(d);
		}});
		AnalysisAccumulator sc = acc.startAnalysis(d);
		sc.analysisComplete(d);
	}

	@Test
	public void ifTheDateIsAlreadySetWhenWeAddTheListenerWeStillGetTheCallback() {
		Date d = new Date();
		AnalysisAccumulator sc = acc.startAnalysis(d);
		sc.analysisComplete(d);
		context.checking(new Expectations() {{
			oneOf(lsnr).dateChanged(d);
		}});
		acc.addBuildDateListener(lsnr);
	}
}
