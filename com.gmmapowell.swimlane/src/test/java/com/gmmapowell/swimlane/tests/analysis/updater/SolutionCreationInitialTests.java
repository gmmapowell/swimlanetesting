package com.gmmapowell.swimlane.tests.analysis.updater;

import java.util.Date;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.DateListener;
import com.gmmapowell.swimlane.eclipse.interfaces.ErrorAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;
import com.gmmapowell.swimlane.eclipse.interfaces.ViewLayout;
import com.gmmapowell.swimlane.eclipse.models.SwimlaneModel;
import com.gmmapowell.swimlane.eclipse.models.SwimlaneModel.SolutionHelper;
import com.gmmapowell.swimlane.testsupport.matchers.HexInfoMatcher;
import com.gmmapowell.swimlane.testsupport.matchers.PortInfoMatcher;

// This just tests everything from a "standing" start ...
public class SolutionCreationInitialTests {
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

	@Test
	public void weCanTriviallyHandleThereBeingNoHexesAtAll() {
		context.checking(new Expectations() {{
		}});
		helper.beginHexes();
		helper.hexesDone();
	}

	@Test
	public void oneHexCreatesAnObjectAndThenPublishesIt() {
		context.checking(new Expectations() {{
			oneOf(layout).addHexagon(with(0), with(HexInfoMatcher.called(String.class)));
		}});
		helper.beginHexes();
		helper.hex(String.class.getName());
		helper.hexesDone();
	}

	@Test
	public void twoHexesCreatesTwoObjectsAndThenPublishesThem() {
		context.checking(new Expectations() {{
			oneOf(layout).addHexagon(with(0), with(HexInfoMatcher.called(String.class)));
			oneOf(layout).addHexagon(with(1), with(HexInfoMatcher.called(Integer.class)));
		}});
		helper.beginHexes();
		helper.hex(String.class.getName());
		helper.hex(Integer.class.getName());
		helper.hexesDone();
	}

	@Test
	public void oneHexOnePortFindsPublishesTheRightInfo() {
		context.checking(new Expectations() {{
			oneOf(layout).addHexagon(with(0), with(HexInfoMatcher.called(String.class)));
			oneOf(layout).addHexagonPort(with(0), with(PortLocation.NORTHWEST), with(PortInfoMatcher.port(String.class)));
		}});
		helper.beginHexes();
		helper.hex(String.class.getName());
		helper.hexesDone();
		helper.beginPorts(String.class.getName());
		helper.port(PortLocation.NORTHWEST, String.class.getName());
		helper.portsDone(String.class.getName());
	}

}
