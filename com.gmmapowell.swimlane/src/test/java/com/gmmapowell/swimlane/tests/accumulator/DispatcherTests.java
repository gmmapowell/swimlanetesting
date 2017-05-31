package com.gmmapowell.swimlane.tests.accumulator;

import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.BarDataListener;
import com.gmmapowell.swimlane.eclipse.models.SolidModelDispatcher;
import com.gmmapowell.swimlane.tests.swtutil.TestBase;

public class DispatcherTests extends TestBase {
	SolidModelDispatcher md = new SolidModelDispatcher();
	
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
