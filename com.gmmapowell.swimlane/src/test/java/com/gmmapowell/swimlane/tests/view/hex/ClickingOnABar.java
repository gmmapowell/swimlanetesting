package com.gmmapowell.swimlane.tests.view.hex;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.jmock.Expectations;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.ShowErrorsPane;
import com.gmmapowell.swimlane.eclipse.views.BarControl;

public class ClickingOnABar extends BaseViewTest {
	@Test
	public void testWeCanClickOnABar() throws Exception {
		String barId = "bar.to.show";
		ShowErrorsPane sep = context.mock(ShowErrorsPane.class);
		BarData bar = context.mock(BarData.class);
		context.checking(new Expectations() {{
			allowing(bar).isPassing(); will(returnValue(true));
			allowing(bar).getComplete(); will(returnValue(5));
			allowing(bar).getTotal(); will(returnValue(6));
			allowing(bar).getTooltip(barId); will(returnValue("tooltip"));
			oneOf(sep).showFor(bar);
		}});
		BarControl bc = new BarControl(shell, barId, sep);
		bc.barChanged(bar);
		bc.getCanvas().setBounds(0, 10, shell.getSize().x, 6);
		bc.getCanvas().notifyListeners(SWT.MouseUp, new Event());
		updateShell();
	}
}
