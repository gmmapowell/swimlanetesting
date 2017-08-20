package com.gmmapowell.swimlane.tests.analysis.updater;

import java.util.Date;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.DateListener;
import com.gmmapowell.swimlane.eclipse.interfaces.ErrorAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.ViewLayout;
import com.gmmapowell.swimlane.eclipse.models.SwimlaneModel;
import com.gmmapowell.swimlane.eclipse.models.SwimlaneModel.SolutionHelper;

public class SolutionCreationUpdateTests {
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();
	ErrorAccumulator errors = context.mock(ErrorAccumulator.class);
	DateListener lsnr = context.mock(DateListener.class);
	ViewLayout layout = context.mock(ViewLayout.class);
	SwimlaneModel model = new SwimlaneModel(errors, layout);
	Date bcd = new Date();
	SolutionHelper helper;

	@Before
	public void setup() {
		helper = model.new SolutionHelper();
	}
	
	@Test
	public void atEndOfAnalysisDateListenersAreNotified() {
		model.addBuildDateListener(lsnr);
		context.checking(new Expectations() {{
			oneOf(lsnr).dateChanged(bcd);
		}});
		helper.analysisDone(bcd);
	}

}
