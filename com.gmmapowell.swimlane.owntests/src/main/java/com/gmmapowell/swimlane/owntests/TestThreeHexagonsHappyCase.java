package com.gmmapowell.swimlane.owntests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCanvas;
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
public class TestThreeHexagonsHappyCase {
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
	public void step02_testThatItFoundTheAcceptanceBar() {
		SWTBotCanvas acc123 = bot.canvasWithId("hexagons.acceptance.111");
		assertTrue(acc123.isVisible());
		Point ws = ext.getSize(acc123.widget);
		ext.assertPct(ws.x, viewSize.x, 95, 100);
		assertEquals(6, ws.y);
		ext.assertColor(acc123, SWT.COLOR_GRAY, ws.x/2, 3);
	}
	
	@Test
	public void step03a_testThatItFoundOneOfTheHexagons() {
		SWTBotCanvas hex1 = bot.canvasWithId("hexagons.com.gmmapowell.swimlane.sample.code.Hexagon1.bg");
		assertTrue(hex1.isVisible());
		Point ws = ext.getSize(hex1.widget);
		ext.assertPct(ws.x, viewSize.x, 18, 22);
		ext.assertPct(ws.y, ws.x, 85, 89);
	}

	// This depends on whether or not we add any business logic tests for hex1 ... I suggest for the purposes of testing we
	// add some to some hexes and not to others ...
	@Test(expected=WidgetNotFoundException.class)
	public void step03b_testTheHexagonBarIsInvisibleWithNoBusinessLogic() {
		bot.canvasWithId("hexagons.com.gmmapowell.swimlane.sample.code.Hexagon1.bar");
//		Point ws = ext.getSize(bar1.widget);
//		ext.assertPct(ws.x, viewSize.x, 14, 16);
//		assertEquals(6, ws.y);
//		ext.assertColor(bar1, SWT.COLOR_GRAY, ws.x/2, ws.y/2);
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
	
	@Test
	public void step11_testThatTheAcceptanceBarIsNowGreen() {
		SWTBotCanvas acc123 = bot.canvasWithId("hexagons.acceptance.111");
		assertTrue(acc123.isVisible());
		Point ws = ext.getSize(acc123.widget);
		ext.assertColor(acc123, SWT.COLOR_GREEN, ws.x/2, ws.y/2);
	}
	
	@Test
	public void step20_switchToTheErrorsTab() {
		SWTBotToolbarButton showErrors = bot.toolbarRadioButtonWithTooltip("Show Errors Pane");
		showErrors.click();
		SWTBotTable t = bot.tableWithId("hexagons.errors");
		assertTrue(t.isVisible());
	}
	
	@AfterClass
	public static void tearDown() throws Exception {
	}

}
