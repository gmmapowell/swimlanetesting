package com.gmmapowell.swimlane.owntests;

import java.io.File;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.eclipse.finder.waits.WaitForJobs;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarButton;
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
		SWTBotShell as = bot.activeShell();
		closeWelcomeView();
		importSampleProject();
		Conditions.waitForJobs(Job.NONE, null);
//		try { Thread.sleep(1000); } catch (InterruptedException ex) { }
		as.activate();
	}

	@Test
	public void test() {
		showView("Swimlane Testing", "Hexagons");
		SWTBotView view = bot.viewByTitle("Hexagons");
		dumpView(view);
	}

	protected static void closeWelcomeView() {
		SWTBotView welcome = bot.viewByTitle("Welcome");
		if (welcome != null)
			welcome.close();
	}

	private static void importSampleProject() {
		bot.menu("File").menu("Import...").click();
		System.out.println("Active = " + bot.activeShell());
		bot.tree().expandNode("General").getNode("Existing Projects into Workspace").click();
		bot.button("Next >").click();
		String cwd = System.getProperty("user.dir");
		String projdir = new File(new File(cwd).getParentFile(), "sample-proj").getPath();
		System.out.println(projdir);
//		dumpActiveShell();
		bot.comboBox().setText(projdir);
		bot.button("Cancel").setFocus();
		bot.button("Finish").click();
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

	public static void dumpActiveShell() {
		SWTBotShell s = bot.activeShell();
		System.out.println("Widgets in active shell " + s);
		s.display.asyncExec(new Runnable() {
			@Override
			public void run() {
				dumpComposite(s.widget, "  ");
			}
		});
	}

	private void dumpView(SWTBotView view) {
		view.getWidget().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				dumpComposite((Composite) view.getWidget(), "  ");
			}
		});
		for (SWTBotToolbarButton b : view.getToolbarButtons())
			System.out.println("Button " + b);
	}

	protected static void dumpComposite(Composite sw, String indent) {
		for (Control c : sw.getChildren()) {
			dumpControl(indent, c);
		}
	}

	protected static void dumpControl(String indent, Control c) {
		System.out.println(indent + c + " " + c.getClass());
		if (c instanceof Composite)
			dumpComposite((Composite) c, indent + "  ");
		// we should also specifically consider other "composite" items such as lists, trees and tables
		if (c instanceof Table) {
			Table t = (Table) c;
			for (int i=0;i<t.getItemCount();i++) {
				System.out.println(indent + "  row " + i + " " + t.getItem(i));
//				dumpControl(indent + "    ", t.getItem(i));
			}
		}
	}

	@AfterClass
	public static void tearDown() throws Exception {
		bot.sleep(2000);
	}
	
}
