package com.gmmapowell.swimlane.owntests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Date;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotLabel;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarButton;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

/* Since this is more a "script" than a set of tests, I have invoked the "FixMethodOrder" with
 * the NAME_ASCENDING option and named everything "stepXX_" and then the real name
 * Things that aren't tests have started a new decade (step10_ etc) so that there is room to retrofit more tests and don't have "test" in the name
 */
@RunWith(SWTBotJunit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestErrorDisplayCase {
	private static SWTWorkbenchBot bot;
	private static ExtendedBot ext;
	private static Date startBuildAt;

	@BeforeClass
	public static void setUp() throws Exception {
		bot = new SWTWorkbenchBot();
		ext = new ExtendedBot(bot);
		ext.closeWelcomeView();
		ext.turnOffAutoBuild();
		String cwd = System.getProperty("user.dir");
		ext.importSampleProject(new File(new File(cwd).getParentFile(), "error-sample-proj"));
		try { Thread.sleep(1000); } catch (InterruptedException ex) { }
		ext.showView("Swimlane Testing", "Swimlane");
		startBuildAt = new Date();
		ext.projectMenu().menu("Build All").click();
	}

	@Test
	public void step01_testThatABuildOccurredRecently() {
		SWTBotLabel lastBuild = bot.labelWithId("swimlane.lastBuild");
		assertNotNull(lastBuild);
		bot.waitUntil(ext.labelAfterDate(lastBuild, startBuildAt));
	}
	
	@Test
	public void step02_switchToTheErrorsTab() {
		SWTBotToolbarButton showErrors = bot.toolbarRadioButtonWithTooltip("Show Errors Pane");
		showErrors.click();
		bot.getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				assertTrue(showErrors.widget.getSelection());
				
			}
		});
		SWTBotTable t = bot.tableWithId("swimlane.errors");
		assertTrue(t.isVisible());
		assertEquals(2, t.columnCount());
		assertEquals(3, t.rowCount());
	}
	
	@Test
	public void step02a_checkForCycleMessage() {
		SWTBotTable t = bot.tableWithId("swimlane.errors");
		boolean found = false;
		for (int i=0;i<t.rowCount();i++) {
			if (t.cell(i, 1).equals("there is no ordering between com.gmmapowell.swimlane.sample.code.Hexagon2 and com.gmmapowell.swimlane.sample.code.Hexagon3")) {
				found = true;
				break;
			} else
				System.out.println(t.cell(i, 1));
		}
		assertTrue("did not find no ordering message", found);
	}
	
	/* This does not come up because we don't process the second location because of the port error (2c)
	@Test
	public void step02b_checkForLocationProblemMessage() {
		SWTBotTable t = bot.tableWithId("swimlane.errors");
		boolean found = false;
		for (int i=0;i<t.rowCount();i++) {
			if (t.cell(i, 1).equals("cannot assign locations nw and se to adapter com.gmmapowell.swimlane.sample.code.Hex1Port1Adapter1")) {
				found = true;
				break;
			} else
				System.out.println(t.cell(i, 1));
		}
		assertTrue("did not find location message", found);
	}
	*/
	
	@Test
	public void step02c_checkForAdapterPortError() {
		SWTBotTable t = bot.tableWithId("swimlane.errors");
		boolean found = false;
		for (int i=0;i<t.rowCount();i++) {
			if (t.cell(i, 1).equals("cannot bind adapter com.gmmapowell.swimlane.sample.code.Hex1Port1Adapter1 to both com.gmmapowell.swimlane.sample.code.Hex1Port1 and com.gmmapowell.swimlane.sample.code.Hex2Port1")) {
				found = true;
				break;
			} else
				System.out.println(t.cell(i, 1));
		}
		assertTrue("did not find adapter port message", found);
	}
	
	@Test
	public void step02d_checkForNoAnnotationsError() {
		SWTBotTable t = bot.tableWithId("swimlane.errors");
		boolean found = false;
		for (int i=0;i<t.rowCount();i++) {
			if (t.cell(i, 1).equals("com.gmmapowell.swimlane.sample.tests.UtilityTest has @Test annotations but no swimlane annotations")) {
				found = true;
				break;
			} else
				System.out.println(t.cell(i, 1));
		}
		assertTrue("did not find no annotations message", found);
	}
	
	/* This doesn't actually come up, because we ignore the fact that it exists
	@Test
	public void step02e_checkForNotBoundError() {
		SWTBotTable t = bot.tableWithId("swimlane.errors");
		boolean found = false;
		for (int i=0;i<t.rowCount();i++) {
			if (t.cell(i, 1).equals("port com.gmmapowell.swimlane.sample.code.Hex2Port1 was not bound to a hexagon")) {
				found = true;
				break;
			} else
				System.out.println(t.cell(i, 1));
		}
		assertTrue("did not find not bound message", found);
	}
	*/
	
	@Test
	public void step10_checkWeStillHaveThreeAfterAnotherBuild() throws Exception {
		// It seems we can already be in a build, which seems odd ...
		Thread.sleep(2500);
		startBuildAt = new Date();
		ext.projectMenu().menu("Build All").click();
		SWTBotLabel lastBuild = bot.labelWithId("swimlane.lastBuild");
		assertNotNull(lastBuild);
		bot.waitUntil(ext.labelAfterDate(lastBuild, startBuildAt));

		SWTBotTable t = bot.tableWithId("swimlane.errors");
		assertTrue(t.isVisible());
		assertEquals(2, t.columnCount());
		assertEquals(3, t.rowCount());
	}
	
	@AfterClass
	public static void tearDown() throws Exception {
	}

}
