package com.gmmapowell.swimlane.tests.view.toolbar;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.junit.Before;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.views.HexagonViewPart;
import com.gmmapowell.swimlane.tests.swtutil.TestBase;

public class ToolBarTests extends TestBase {
	protected Shell shell;
	protected HexagonViewPart part;
	private ToolBar toolBar;

	@Before
	public void setup() throws Exception {
		shell = displayHelper.createShell();
		shell.setLayout(new GridLayout(1, false));
		ToolBarManager mgr = new ToolBarManager();
		toolBar = mgr.createControl(shell);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		part = new HexagonViewPart();
		part.configureToolbar(mgr);
		mgr.update(false);
		shell.setSize(600, 300);
		shell.open();
		displayHelper.flushPendingEvents();
	}

	@Test
	public void testAllTheControlsWeWantAreThere() throws Exception {
		assertToolBarTipsInOrder(toolBar, "Run All Tests");
	}
}
