package com.gmmapowell.swimlane.tests.view.info;

import static org.junit.Assert.assertEquals;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.hamcrest.Description;
import org.jmock.Expectations;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.junit.Before;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.DataCentral;
import com.gmmapowell.swimlane.eclipse.interfaces.DateListener;
import com.gmmapowell.swimlane.eclipse.views.InfoBar;
import com.gmmapowell.swimlane.tests.swtutil.TestBase;

public class InfoBarTests extends TestBase {
	protected Shell shell;
	protected InfoBar bar;
	DataCentral model = context.mock(DataCentral.class);
	DateListener[] dls = new DateListener[2];
	
	@Before
	public void setup() throws Exception {
		shell = displayHelper.createShell();
		shell.setLayout(new GridLayout(1, false));
		context.checking(new Expectations() {{
			oneOf(model).addBuildDateListener(with(any(DateListener.class))); will(captureDL(0));
			oneOf(model).addTestDateListener(with(any(DateListener.class))); will(captureDL(1));
		}});
		bar = new InfoBar(shell, model);
		shell.setSize(600, 300);
		shell.open();
		displayHelper.flushPendingEvents();
	}

	@Test
	public void testAllTheControlsWeWantAreThere() throws Exception {
		assertControlsInOrder(shell, "swimlane.lastBuild", "swimlane.testsComplete");
	}
	
	@Test
	public void testTheBuildLabelHasTheRightTime() throws Exception {
        Label lastBuild = waitForControl(shell, "swimlane.lastBuild");
        dls[0].dateChanged(exactDate(2017, 04, 20, 04, 20, 00, 420));
        assertEquals("042000.420", lastBuild.getText());
	}

	@Test
	public void testTheTestCompleteLabelHasTheRightTime() throws Exception {
        Label tc = waitForControl(shell, "swimlane.testsComplete");
        dls[1].dateChanged(exactDate(2017, 04, 20, 16, 20, 00, 420));
        assertEquals("162000.420", tc.getText());
	}

	protected Action captureDL(int which) {
		return new Action() {
			@Override
			public void describeTo(Description arg0) {
				arg0.appendText("capture handler");
			}

			@Override
			public Object invoke(Invocation arg0) throws Throwable {
				dls[which] = (DateListener) arg0.getParameter(0);
				return null;
			}
			
		};
	}

}
