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
			oneOf(lsnr).barChanged(bar);
		}});
		md.addBarListener(lsnr);
		md.barChanged(bar);
	}

}
