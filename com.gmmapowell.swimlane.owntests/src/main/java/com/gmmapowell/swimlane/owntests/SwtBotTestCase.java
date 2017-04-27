package com.gmmapowell.swimlane.owntests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCanvas;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotLabel;
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
public class SwtBotTestCase {
	private static SWTWorkbenchBot bot;
	private static ExtendedBot ext;
	private static SWTBotView hexView;
	private static Point viewSize;
	private static Date startBuildAt;

	@BeforeClass
	public static void setUp() throws Exception {
		bot = new SWTWorkbenchBot();
		ext = new ExtendedBot(bot);
		ext.closeWelcomeView();
		ext.turnOffAutoBuild();
		ext.importSampleProject();
		try { Thread.sleep(1000); } catch (InterruptedException ex) { }
		ext.showView("Swimlane Testing", "Hexagons");
		hexView = bot.viewByTitle("Hexagons");
		viewSize = ext.getSize(hexView.getWidget());
		System.out.println("hex view is size " + viewSize);
		startBuildAt = new Date();
		ext.projectMenu().menu("Build All").click();
	}

	@Test
	public void step01_testThatABuildOccurredRecently() {
		SWTBotLabel lastBuild = bot.labelWithId("hexagons.lastBuild");
		assertNotNull(lastBuild);
		bot.waitUntil(ext.labelAfterDate(lastBuild, startBuildAt));
	}
	
	@Test
	public void step02_testThatItFoundTheAcceptanceTest() {
		SWTBotCanvas acc123 = bot.canvasWithId("hexagons.acceptance.111");
		assertTrue(acc123.isVisible());
		Point ws = ext.getSize(acc123.widget);
		ext.assertPct(ws.x, viewSize.x, 95, 100);
		assertEquals(6, ws.y);
	}
	
	// TODO: when we have "auto-run" unit tests in the sample project we should be able to check that they DID run as part of the post-build
	
	@Test
	public void step10_findAndPushTheRunTestsButton() {
		Date runTestsAt = new Date();
		SWTBotToolbarButton runTests = bot.toolbarButtonWithTooltip("Run All Tests");
		runTests.click();
		SWTBotLabel testsComplete = bot.labelWithId("hexagons.testsComplete");
		bot.waitUntil(ext.labelAfterDate(testsComplete, runTestsAt));
	}
	
	@AfterClass
	public static void tearDown() throws Exception {
	}

}
