package com.gmmapowell.swimlane.tests.view.errors;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;

import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;
import com.gmmapowell.swimlane.eclipse.views.ErrorView;
import com.gmmapowell.swimlane.fakes.FakeModelDispatcher;
import com.gmmapowell.swimlane.tests.swtutil.TestBase;

public abstract class BaseViewTest extends TestBase {
	public Shell shell;
	public ErrorView hv;
	protected ModelDispatcher md;
	protected FakeModelDispatcher fmd;

	@Before
	public void setup() throws Exception {
		shell = displayHelper.createShell();
		shell.setLayout(new GridLayout(1, false));
		md = context.mock(ModelDispatcher.class);
		fmd = new FakeModelDispatcher(md);
		hv = new ErrorView(shell, fmd);
		shell.setSize(600, 300);
		shell.open();
		displayHelper.flushPendingEvents();
	}

	@After
	public void tearDown() throws Exception {
//		Thread.sleep(3000);
		displayHelper.dispose();
	}

	protected HexagonDataModel pushModel(HexagonDataModel testModel) {
		fmd.setModel(testModel);
		shell.redraw();
		shell.update();
		displayHelper.flushPendingEvents();
		return testModel;
	}
}
