package com.gmmapowell.swimlane.owntests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCanvas;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotLabel;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
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
	private static SWTBotView swimlaneView;
	private static Point viewSize;
	private static Date startBuildAt;

	@BeforeClass
	public static void setUp() throws Exception {
		bot = new SWTWorkbenchBot();
		ext = new ExtendedBot(bot);
		ext.closeWelcomeView();
		ext.turnOffAutoBuild();
		String cwd = System.getProperty("user.dir");
		ext.importSampleProject(new File(new File(cwd).getParentFile(), "sample-proj"));
		try { Thread.sleep(1000); } catch (InterruptedException ex) { }
		ext.showView("Swimlane Testing", "Swimlane");
		swimlaneView = bot.viewByTitle("Swimlane");
		viewSize = ext.getSize(swimlaneView.getWidget());
		startBuildAt = new Date();
		ext.projectMenu().menu("Build All").click();
	}

	@Test
	public void step01_testThatABuildOccurredRecently() {
		SWTBotLabel lastBuild = bot.labelWithId("swimlane.lastBuild");
		assertNotNull(lastBuild);
		bot.waitUntil(ext.labelAfterDate(lastBuild, startBuildAt));
		ext.dumpActiveShell();
	}
	
	/* Because of new visibility constraints, we don't display the acceptance bar until we have identified it has some tests
	 * TODO: this is probably wrong; the *existence* of a test should be enough to make it visible; that goes back to the proper
	 * handling of groups and updating runtime values
	@Test
	public void step02_testThatItFoundTheAcceptanceBar() throws Exception {
		System.out.println(new Date() + "Waiting for canvas");
		try {
			SWTBotCanvas acc123 = bot.canvasWithId("swimlane.acceptance.111");
			assertTrue(acc123.isVisible());
			Point ws = ext.getSize(acc123.widget);
			ext.assertPct(ws.x, viewSize.x, 95, 100);
			assertEquals(6, ws.y);
			ext.assertColor(acc123, SWT.COLOR_GRAY, ws.x/2, 3);
		} finally {
			System.out.println(new Date() + " have waited for canvas");
		}
	}
	*/
	
	@Test
	public void step03a_testThatItFoundOneOfTheHexagons() {
		SWTBotCanvas hex1 = bot.canvasWithId("swimlane.hexbg.0");
		assertTrue(hex1.isVisible());
		Point ws = ext.getSize(hex1.widget);
		ext.assertPct(ws.x, viewSize.x, 18, 22);
		ext.assertPct(ws.y, ws.x, 85, 89);
	}

	// This depends on whether or not we add any business logic tests for hex1 ... I suggest for the purposes of testing we
	// add some to some hexes and not to others ...
	@Test(expected=WidgetNotFoundException.class)
	public void step03b_testTheHexagonBarIsInvisibleWithNoBusinessLogic() {
		bot.performWithTimeout(new VoidResult() {
			@Override
			public void run() {
				SWTBotCanvas hexbar = bot.canvasWithId("swimlane.com.gmmapowell.swimlane.sample.code.Hexagon1.bar");
				assertFalse(hexbar.isVisible());
			}
		}, 1000);
	}
	
	// TODO: when we have "auto-run" unit tests in the sample project we should be able to check that they DID run as part of the post-build
	
	@Test
	public void step10_findAndPushTheRunTestsButton() {
		Date runTestsAt = new Date();
		SWTBotToolbarButton runTests = bot.toolbarButtonWithTooltip("Run All Tests");
		runTests.click();
		SWTBotLabel testsComplete = bot.labelWithId("swimlane.testsComplete");
		bot.waitUntil(ext.labelAfterDate(testsComplete, runTestsAt));
	}
	
	@Test
	public void step11_testThatTheAcceptanceBarIsNowGreen() {
		ext.dumpActiveShell();
		SWTBotCanvas acc123 = bot.canvasWithId("swimlane.bar.acceptance.1");
		assertTrue(acc123.isVisible());
		Point ws = ext.getSize(acc123.widget);
		ext.assertColor(acc123, SWT.COLOR_GREEN, ws.x/2, ws.y/2);
	}
	
	@Test
	public void step20_switchToTheErrorsTab() {
		SWTBotToolbarButton showErrors = bot.toolbarRadioButtonWithTooltip("Show Errors Pane");
		showErrors.click();
		bot.getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				assertTrue(showErrors.widget.getSelection());
				
			}
		});
		ext.dumpActiveShell();
		SWTBotTable t = bot.tableWithId("swimlane.errors");
		assertTrue(t.isVisible());
		bot.performWithTimeout(new VoidResult() {
			@Override
			public void run() {
				try {
					bot.canvasWithId("swimlane.acceptance.111");
					fail("Should not have found hexagons.acceptance.111");
				} catch (WidgetNotFoundException ex) {
				}
			}
		}, 1000);
	}
	
	@Test
	public void step21_andSwitchBack() {
		SWTBotToolbarButton showHexes = bot.toolbarRadioButtonWithTooltip("Show Swimlane Diagram");
		showHexes.click();
		bot.getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				assertTrue(showHexes.widget.getSelection());
			}
		});
		SWTBotCanvas acc123 = bot.canvasWithId("swimlane.bar.acceptance.111");
		assertTrue(acc123.isVisible());
		bot.performWithTimeout(new VoidResult() {
			@Override
			public void run() {
				try {
					bot.tableWithId("swimlane.errors");
					fail("Should not have found hexagons.errors");
				} catch (WidgetNotFoundException ex) {
				}
			}
		}, 1000);
	}
	
	@Test
	public void step30_checkToolTipOnAcc123() {
		SWTBotCanvas acc123 = bot.canvasWithId("swimlane.bar.acceptance.111");
		assertEquals("Acceptance - 1 group; 2 passed", acc123.getToolTipText());
	}
	
	@Test
	public void step31_clickAcc123ToMoveToTestResults() {
		SWTBotCanvas acc123 = bot.canvasWithId("swimlane.acceptance.111");
		acc123.click();
		bot.getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				// I feel it should be easy programmatically to change the state of a radio button
				// But the thing that everybody seems to say should work doesn't, and I can't figure out anything else to do
				// Instead, just check that we have the right set of things displayed
//				SWTBotToolbarButton showHexes = bot.toolbarRadioButtonWithTooltip("Show Swimlane Diagram");
//				assertFalse("hexes button should be deactivated", showHexes.widget.getSelection());
//				SWTBotToolbarButton showTests = bot.toolbarRadioButtonWithTooltip("Test Results");
//				assertTrue("results button should be activated", showTests.widget.getSelection());
			}
		});
		SWTBotTree cases = bot.treeWithId("swimlane.casesTree");
		assertTrue(cases.isVisible());
		bot.performWithTimeout(new VoidResult() {
			@Override
			public void run() {
				try {
					bot.tableWithId("swimlane.errors");
					fail("Should not have found hexagons.errors");
				} catch (WidgetNotFoundException ex) {
				}
				try {
					bot.canvasWithId("swimlane.acceptance.111");
					fail("Should not have found hexagons.acceptance.111");
				} catch (WidgetNotFoundException ex) {
				}
			}
		}, 1000);
	}
	
	@Test
	public void step32_assertThatTheTreeContainsTheSampleProjectAsTheTopNode() {
		SWTBotTree cases = bot.treeWithId("swimlane.casesTree");
		assertEquals(1, cases.rowCount());
		SWTBotTreeItem[] rows = cases.getAllItems();
		assertEquals("sample-proj", rows[0].getText());
	}
	
	@Test
	public void step33_clickToOpenTheProjectNode() {
		SWTBotTree cases = bot.treeWithId("swimlane.casesTree");
		SWTBotTreeItem[] rows = cases.getAllItems();
		rows[0].click();
		SWTBotTreeItem[] items = rows[0].getItems();
		assertEquals(2, items.length);
		assertEquals("com.gmmapowell.swimlane.sample.tests.AcceptanceTest", items[0].getText());
	}
	
	@Test
	public void step34_clickToOpenTheFirstTestClass() {
		SWTBotTree cases = bot.treeWithId("swimlane.casesTree");
		SWTBotTreeItem[] rows = cases.getAllItems();
		SWTBotTreeItem[] items = rows[0].getItems();
		items[0].click();
		SWTBotTreeItem[] tests = items[0].getItems();
		assertEquals(1, tests.length);
		assertEquals("testNothing", tests[0].getText());
	}
	
	@Test
	public void step35_andSwitchBackToHexMode() {
		SWTBotToolbarButton showHexes = bot.toolbarRadioButtonWithTooltip("Show Swimlane Diagram");
		showHexes.click();
		bot.getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				assertTrue(showHexes.widget.getSelection());
			}
		});
		SWTBotCanvas acc123 = bot.canvasWithId("swimlane.bar.acceptance.111");
		assertTrue(acc123.isVisible());
	}

	@Test
	public void step40_testAdapterHasAFailure() {
		SWTBotCanvas adapter = bot.canvasWithId("swimlane.bar.adapter.0.nw.0");
		assertEquals("Hex1Port1Adapter1 - 1 group; 0 passed, 1 failure", adapter.getToolTipText());
	}

	@Test
	public void step41_clickToMoveToAdapter() {
		SWTBotCanvas adapter = bot.canvasWithId("swimlane.bar.adapter.0.nw.0");
		adapter.click();
		SWTBotTree cases = bot.treeWithId("swimlane.casesTree");
		assertTrue(cases.isVisible());
	}

	@Test
	public void step42_assertThatTheTreeContainsTheSampleProjectAsTheTopNode() {
		SWTBotTree cases = bot.treeWithId("swimlane.casesTree");
		assertTrue(cases.isVisible());
		assertEquals(1, cases.rowCount());
		SWTBotTreeItem[] rows = cases.getAllItems();
		assertEquals("sample-proj", rows[0].getText());
	}
	
	@AfterClass
	public static void tearDown() throws Exception {
		Thread.sleep(1200);
	}

}
