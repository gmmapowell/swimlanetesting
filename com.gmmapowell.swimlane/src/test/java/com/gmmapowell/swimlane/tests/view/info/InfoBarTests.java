package com.gmmapowell.swimlane.tests.view.info;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.interfaces.BarData;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel.Status;
import com.gmmapowell.swimlane.eclipse.models.ModelDispatcher;
import com.gmmapowell.swimlane.eclipse.views.InfoBar;
import com.gmmapowell.swimlane.tests.swtutil.TestBase;

public class InfoBarTests extends TestBase {
	protected Shell shell;
	protected InfoBar bar;
	private ModelDispatcher hmd;

	@Before
	public void setup() throws Exception {
		shell = displayHelper.createShell();
		shell.setLayout(new GridLayout(1, false));
		hmd = new ModelDispatcher();
		bar = new InfoBar(shell, hmd);
		shell.setSize(600, 300);
		shell.open();
		displayHelper.flushPendingEvents();
	}

	@Test
	public void testAllTheControlsWeWantAreThere() throws Exception {
		specifyModel(10, 0, Status.OK);
		assertControlsInOrder(shell, "hexagons.lastBuild", "hexagons.testsComplete");
	}
	
	@Test
	public void testTheBuildLabelHasTheRightTime() throws Exception {
		specifyModel(10, 0, Status.OK);
        Label lastBuild = waitForControl(shell, "hexagons.lastBuild");
        assertEquals("042000.420", lastBuild.getText());
	}

	@Test
	public void testTheTestCompleteLabelHasTheRightTime() throws Exception {
		specifyModel(10, 0, Status.OK);
        Label tc = waitForControl(shell, "hexagons.testsComplete");
        assertEquals("162000.420", tc.getText());
	}

	protected void specifyModel(int total, int complete, Status status) throws InterruptedException {
		pushModel(defineModel(total, complete, status));
	}

	protected HexagonDataModel defineModel(int total, int complete, Status status) {
		HexagonDataModel testModel = context.mock(HexagonDataModel.class);
		ArrayList<BarData> accList = new ArrayList<BarData>();
		context.checking(new Expectations() {{
			allowing(testModel).getHexCount(); will(returnValue(0));
			allowing(testModel).getBuildTime(); will(returnValue(exactDate(2017, 04, 20, 04, 20, 00, 420)));
			allowing(testModel).getTestCompleteTime(); will(returnValue(exactDate(2017, 04, 20, 16, 20, 00, 420)));
			allowing(testModel).getAcceptanceTests(); will(returnValue(accList));
		}});
		return testModel;
	}
	
	protected void pushModel(HexagonDataModel testModel) {
		hmd.setModel(testModel);
		shell.redraw();
		shell.update();
		displayHelper.flushPendingEvents();
	}
}
