package com.gmmapowell.swimlane.tests.view.results;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;

import com.gmmapowell.swimlane.eclipse.views.TestResultsView;
import com.gmmapowell.swimlane.tests.swtutil.TestBase;

public abstract class BaseResultsViewTest extends TestBase {
	public Shell shell;
	public TestResultsView trv;

	@Before
	public void setup() throws Exception {
		shell = displayHelper.createShell();
		shell.setLayout(new GridLayout(1, false));
		trv = new TestResultsView(shell);
		shell.setSize(600, 300);
		shell.open();
		displayHelper.flushPendingEvents();
	}

	@After
	public void tearDown() throws Exception {
//		Thread.sleep(3000);
		displayHelper.dispose();
	}

	protected void pushModel() {
		shell.redraw();
		shell.update();
		displayHelper.flushPendingEvents();
	}
}
