package com.gmmapowell.swimlane.tests.analysis;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.ViewLayout;
import com.gmmapowell.swimlane.eclipse.models.SwimlaneModel;
import com.gmmapowell.swimlane.eclipse.models.SwimlaneModel.SolutionHelper;
import com.gmmapowell.swimlane.testsupport.matchers.IntArrayMatcher;

public class SolutionHelperTests {
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery();

	@Test
	public void theAcceptanceBarGetsTheRightName() {
		ViewLayout layout = context.mock(ViewLayout.class);
		context.checking(new Expectations() {{
			oneOf(layout).addHexagon(with(0), with(any(BarData.class)));
			oneOf(layout).addHexagon(with(1), with(any(BarData.class)));
			oneOf(layout).addHexagon(with(2), with(any(BarData.class)));
			oneOf(layout).addAcceptance(with(IntArrayMatcher.with(1, 0, 1)), with(any(BarData.class)));
		}});
		SwimlaneModel model = new SwimlaneModel(null, null, layout);
		SolutionHelper helper = model.new SolutionHelper();
		helper.hex("hello");
		helper.hex("there");
		helper.hex("world");
		helper.acceptance("hello", "world");
	}

}
