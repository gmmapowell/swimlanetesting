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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
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
	public void testThatABuildOccurredRecently() {
		SWTBotLabel lastBuild = bot.labelWithId("hexagons.lastBuild");
		assertNotNull(lastBuild);
		bot.waitUntil(ext.labelAfterDate(lastBuild, startBuildAt));
	}
	
	@Test
	public void testThatItFoundTheAcceptanceTest() {
		SWTBotCanvas acc123 = bot.canvasWithId("hexagons.acc123");
		assertTrue(acc123.isVisible());
		Point ws = ext.getSize(acc123.widget);
		ext.assertPct(ws.x, viewSize.x, 95, 100);
		assertEquals(6, ws.y);
	}

	@AfterClass
	public static void tearDown() throws Exception {
	}

}
