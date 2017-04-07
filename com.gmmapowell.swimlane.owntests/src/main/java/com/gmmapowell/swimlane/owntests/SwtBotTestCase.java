package com.gmmapowell.swimlane.owntests;

import static org.junit.Assert.fail;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class SwtBotTestCase {
	private static SWTWorkbenchBot bot;

	@BeforeClass
	public static void setUp() throws Exception {
		bot = new SWTWorkbenchBot();
		closeWelcomeView();
	}

	@Test
	public void test() {
		showView("Swimlane Testing", "Hexagons");
		bot.viewByTitle("Hexagons");
	}

	protected static void closeWelcomeView() {
		SWTBotView welcome = bot.viewByTitle("Welcome");
		if (welcome != null)
			welcome.close();
	}

	protected void showView(String... path) {
		SWTBotMenu window = bot.menu("Window");
		SWTBotMenu sv = window.menu("Show View");
		SWTBotMenu other = sv.menu("Other...");
		other.click();
		SWTBotShell ows = bot.shell("Show View");
		ows.activate();
		SWTBotTree t = bot.tree();
		if (path.length > 1) {
			String[] subpath = new String[path.length-1];
			for (int i=0;i<path.length-1;i++)
				subpath[i] = path[i];
			t.expandNode(subpath).getNode(path[path.length-1]).click();
		} else {
			t.getTreeItem(path[0]).click();
		}
		bot.button("OK").click();
	}

	public void dumpActiveShell() {
		SWTBotShell s = bot.activeShell();
		System.out.println("Widgets in active shell " + s);
		s.display.asyncExec(new Runnable() {
			@Override
			public void run() {
				dumpComposite(s.widget, "  ");
			}
		});
	}

	protected void dumpComposite(Composite sw, String indent) {
		for (Control c : sw.getChildren()) {
			System.out.println(indent + c);
			if (c instanceof Composite)
				dumpComposite((Composite) c, indent + "  ");
		}
	}

	@AfterClass
	public static void tearDown() throws Exception {
		bot.sleep(2000);
	}
	
}
