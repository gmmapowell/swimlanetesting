package com.gmmapowell.swimlane.tests.view.hex;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.BarDataListener;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel.Status;
import com.gmmapowell.swimlane.eclipse.views.BarControl;

public class ClickingOnABar extends BaseViewTest {

	@Test
	public void testWeCanClickOnABar() throws Exception {
		BarData bar = context.mock(BarData.class);
		String barId = "hexagons.utility";
		context.checking(new Expectations() {{
			allowing(bar).getId(); will(returnValue(barId));
			allowing(bar).getPassed(); will(returnValue(4));
			allowing(bar).getFailures(); will(returnValue(1));
			allowing(bar).getTotal(); will(returnValue(6));
			allowing(bar).getComplete(); will(returnValue(5));
			allowing(bar).getStatus(); will(returnValue(Status.OK));
			allowing(bar).getMarks(); will(returnValue(new int[] { 1 }));
			allowing(md).addBarListener(with(bar), with(aNonNull(BarDataListener.class)));
			oneOf(md).barClicked(barId);
		}});
		BarControl bc = new BarControl(fmd, shell, bar, "utility", barId);
		bc.getCanvas().setBounds(0, 10, shell.getSize().x, 6);
		bc.getCanvas().notifyListeners(SWT.MouseUp, new Event());
		updateShell();
	}

}
