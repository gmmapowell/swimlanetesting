package com.gmmapowell.swimlane.tests.accumulator;

import java.util.Date;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.AnalysisAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.DataCentral;
import com.gmmapowell.swimlane.eclipse.interfaces.DateListener;
import com.gmmapowell.swimlane.eclipse.models.HexagonAccumulator;

/* The purpose of the accumulator is to take input in one form (what we discover)
 * and to build a stable model out of it.
 * To decouple these two roles, we have two interfaces: Accumulator for input and HexagonDataModel for output;
 * here we assert that the two are coupled correctly internally.
 */
public class GeneralAccumulationTests {
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();
	AnalysisAccumulator acc = new HexagonAccumulator();
	DataCentral lsnrs = (DataCentral) acc;
	DateListener lsnr = context.mock(DateListener.class);
	
	@Test
	public void theTrivialDateThing() {
		lsnrs.addBuildDateListener(lsnr);
		Date d = new Date();
		context.checking(new Expectations() {{
			oneOf(lsnr).dateChanged(d);
		}});
		acc.analysisComplete(d);
	}

	@Test
	public void ifTheDateIsAlreadySetWhenWeAddTheListenerWeStillGetTheCallback() {
		Date d = new Date();
		acc.analysisComplete(d);
		context.checking(new Expectations() {{
			oneOf(lsnr).dateChanged(d);
		}});
		lsnrs.addBuildDateListener(lsnr);
	}
}
