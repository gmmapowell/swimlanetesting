package com.gmmapowell.swimlane.owntests;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotLabel;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class SwtBotTestCase {
	private static SWTWorkbenchBot bot;
	private static ExtendedBot ext;
	private static SWTBotView hexView;

	@BeforeClass
	public static void setUp() throws Exception {
		bot = new SWTWorkbenchBot();
		ext = new ExtendedBot(bot);
		ext.closeWelcomeView();
		ext.turnOffAutoBuild();
		ext.importSampleProject();
		Conditions.waitForJobs(Job.NONE, null);
		try { Thread.sleep(1000); } catch (InterruptedException ex) { }
		ext.showView("Swimlane Testing", "Hexagons");
		hexView = bot.viewByTitle("Hexagons");
		ext.dumpView(hexView);
		Date startBuildAt = new Date();
		ext.projectMenu().menu("Build All").click();
		SWTBotLabel lastBuild = bot.labelWithId("hexagons.lastBuild");
		assertNotNull(lastBuild);
		bot.waitUntil(ext.labelAfterDate(lastBuild, startBuildAt));
	}

	@Test
	public void test() {
	}

	@AfterClass
	public static void tearDown() throws Exception {
		bot.sleep(2000);
	}

}
