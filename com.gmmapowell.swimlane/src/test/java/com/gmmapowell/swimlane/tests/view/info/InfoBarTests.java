package com.gmmapowell.swimlane.tests.view.info;

import org.eclipse.swt.widgets.Shell;

import com.gmmapowell.swimlane.eclipse.views.InfoBar;
import com.gmmapowell.swimlane.tests.swtutil.TestBase;

public class InfoBarTests extends TestBase {
	protected Shell shell;
	protected InfoBar bar;

	/*
	@Before
	public void setup() throws Exception {
		shell = displayHelper.createShell();
		shell.setLayout(new GridLayout(1, false));
		bar = new InfoBar(shell);
		shell.setSize(600, 300);
		shell.open();
		displayHelper.flushPendingEvents();
	}

	@Test
	public void testAllTheControlsWeWantAreThere() throws Exception {
		specifyModel(10, 0, Status.OK);
		assertControlsInOrder(shell, "swimlane.lastBuild", "swimlane.testsComplete");
	}
	
	@Test
	public void testTheBuildLabelHasTheRightTime() throws Exception {
		specifyModel(10, 0, Status.OK);
        Label lastBuild = waitForControl(shell, "swimlane.lastBuild");
        assertEquals("042000.420", lastBuild.getText());
	}

	@Test
	public void testTheTestCompleteLabelHasTheRightTime() throws Exception {
		specifyModel(10, 0, Status.OK);
        Label tc = waitForControl(shell, "swimlane.testsComplete");
        assertEquals("162000.420", tc.getText());
	}

	protected void specifyModel(int total, int complete, Status status) throws InterruptedException {
//		pushModel(defineModel(total, complete, status));
	}
	*/
}
