package com.gmmapowell.swimlane.tests.analysis.updater;

import java.io.IOException;
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
import com.gmmapowell.swimlane.testsupport.DirectRunner;
import com.gmmapowell.swimlane.testsupport.matchers.AdapterMatcher;
import com.gmmapowell.swimlane.testsupport.matchers.HexInfoMatcher;
import com.gmmapowell.swimlane.testsupport.matchers.PortInfoMatcher;

// This just tests everything from a "standing" start ...
public class SolutionCreationInitialTests {
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();
	ErrorAccumulator errors = context.mock(ErrorAccumulator.class);
	DateListener lsnr = context.mock(DateListener.class);
	ViewLayout layout = context.mock(ViewLayout.class);
	SwimlaneModel model = new SwimlaneModel(new DirectRunner(), errors, layout);
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
		helper.beginAnalysis();
	}

	@Test
	public void oneHexCreatesAnObjectAndThenPublishesIt() {
		context.checking(new Expectations() {{
			oneOf(layout).addHexagon(with(0), with(HexInfoMatcher.called(String.class)));
		}});
		helper.beginAnalysis();
		helper.hex(String.class.getName());
	}

	@Test
	public void twoHexesCreatesTwoObjectsAndThenPublishesThem() {
		context.checking(new Expectations() {{
			oneOf(layout).addHexagon(with(0), with(HexInfoMatcher.called(String.class)));
			oneOf(layout).addHexagon(with(1), with(HexInfoMatcher.called(Integer.class)));
		}});
		helper.beginAnalysis();
		helper.hex(String.class.getName());
		helper.hex(Integer.class.getName());
	}

	@Test
	public void oneHexOnePortPublishesTheRightInfo() {
		context.checking(new Expectations() {{
			oneOf(layout).addHexagon(with(0), with(HexInfoMatcher.called(String.class)));
			oneOf(layout).addHexagonPort(with(0), with(PortLocation.NORTHWEST), with(PortInfoMatcher.port(IOException.class)));
		}});
		helper.beginAnalysis();
		helper.hex(String.class.getName());
		helper.port(PortLocation.NORTHWEST, IOException.class.getName());
	}

	@Test
	public void oneHexOnePortOneAdapterPublishesTheRightInfo() {
		context.checking(new Expectations() {{
			oneOf(layout).addHexagon(with(0), with(HexInfoMatcher.called(String.class)));
			oneOf(layout).addHexagonPort(with(0), with(PortLocation.NORTHWEST), with(PortInfoMatcher.port(IOException.class)));
			oneOf(layout).addAdapter(with(0), with(PortLocation.NORTHWEST), with(0), with(AdapterMatcher.adapter(StringBuilder.class)));
		}});
		helper.beginAnalysis();
		helper.hex(String.class.getName());
		helper.port(PortLocation.NORTHWEST, IOException.class.getName());
		helper.adapter(StringBuilder.class.getName());
	}
}
