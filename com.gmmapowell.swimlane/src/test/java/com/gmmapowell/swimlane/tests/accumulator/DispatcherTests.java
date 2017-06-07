package com.gmmapowell.swimlane.tests.accumulator;

import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.AccumulatorListener;
import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.BarDataListener;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonModelListener;
import com.gmmapowell.swimlane.eclipse.models.SolidModelDispatcher;
import com.gmmapowell.swimlane.tests.swtutil.TestBase;

public class DispatcherTests extends TestBase {
	SolidModelDispatcher md = new SolidModelDispatcher(null, null);
	
	@Test
	public void testThatTheDispatcherNotifiesAccumulatorListenersWhenTheModelChanges() throws Exception {
		AccumulatorListener lsnr = context.mock(AccumulatorListener.class);
		Accumulator model = context.mock(Accumulator.class);
		context.checking(new Expectations() {{
			oneOf(lsnr).setModel(model);
		}});
		md.addAccumulator(lsnr);
		md.setModel(model);
	}

	@Test
	public void testThatTheDispatcherNotifiesHMDListenersWhenTheModelChanges() throws Exception {
		HexagonModelListener lsnr = context.mock(HexagonModelListener.class);
		HexagonDataModel model = context.mock(HexagonDataModel.class);
		context.checking(new Expectations() {{
			oneOf(lsnr).setModel(model);
		}});
		md.addHexagonModelListener(lsnr);
		md.setModel(model);
	}

	@Test
	public void testThatTheDispatcherForwardsASimpleBarToAListener() {
		BarDataListener lsnr = context.mock(BarDataListener.class);
		BarData bar = context.mock(BarData.class);
		context.checking(new Expectations() {{
			allowing(bar).getId(); will(returnValue("bar"));
			oneOf(lsnr).barChanged(bar);
		}});
		md.addBarListener(bar, lsnr);
		md.barChanged(bar);
	}

	@Test
	public void testThatTheDispatcherCanDistinguishWhichBarsToSendToWhichListeners() {
		BarDataListener lsnr = context.mock(BarDataListener.class);
		BarData bar1 = context.mock(BarData.class, "bar1");
		BarData bar2 = context.mock(BarData.class, "bar2");
		context.checking(new Expectations() {{
			allowing(bar1).getId(); will(returnValue("bar1"));
			allowing(bar2).getId(); will(returnValue("bar2"));
			oneOf(lsnr).barChanged(bar1);
		}});
		md.addBarListener(bar1, lsnr);
		md.barChanged(bar1);
		md.barChanged(bar2);
	}

}
