package com.gmmapowell.swimlane.tests.view.toolbar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.junit.Before;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.Accumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;
import com.gmmapowell.swimlane.eclipse.models.SolidModelDispatcher;
import com.gmmapowell.swimlane.eclipse.views.HexagonViewPart;
import com.gmmapowell.swimlane.tests.swtutil.TestBase;

public class ToolBarTests extends TestBase {
	protected Shell shell;
	protected HexagonViewPart part;
	private ToolBar toolBar;
	private ModelDispatcher md = new SolidModelDispatcher();

	@Before
	public void setup() throws Exception {
		shell = displayHelper.createShell();
		shell.setLayout(new GridLayout(1, false));
		ToolBarManager mgr = new ToolBarManager();
		toolBar = mgr.createControl(shell);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		part = new HexagonViewPart();
		part.configureToolbar(mgr, md);
		mgr.update(false);
		shell.setSize(600, 300);
		shell.open();
		displayHelper.flushPendingEvents();
	}

	@Test
	public void testAllTheControlsWeWantAreThere() throws Exception {
		assertToolBarTipsInOrder(toolBar, "Run All Tests");
	}

	@Test
	public void testTheRunAllButtonIsInitiallyDisabledBecauseThereIsNoModel() throws Exception {
		ToolItem ti = getItem(toolBar, "Run All Tests");
		assertFalse("The run item was not initially disabled", ti.getEnabled());
	}

	@Test
	public void testTheRunAllButtonIsEnabledOnceThereIsAModel() throws Exception {
		ToolItem ti = getItem(toolBar, "Run All Tests");
		Accumulator hex = context.mock(Accumulator.class);
		md.setModel(hex);
		assertTrue("The run item was not enabled after setting the model", ti.getEnabled());
	}
}
