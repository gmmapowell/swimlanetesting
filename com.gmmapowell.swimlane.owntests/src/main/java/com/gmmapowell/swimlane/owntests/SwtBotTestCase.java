package com.gmmapowell.swimlane.owntests;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.waits.ICondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotLabel;
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
		turnOffAutoBuild();
		as.activate();
		importSampleProject();
		Conditions.waitForJobs(Job.NONE, null);
		try { Thread.sleep(1000); } catch (InterruptedException ex) { }
		as.activate();
	}

	protected static SWTBotShell openPreferencesWindow() {
		bot.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				Menu sm = bot.getDisplay().getSystemMenu();
				for (MenuItem x : sm.getItems()) {
					if (x.getText().startsWith("Preferences")) {
						try {
							x.notifyListeners(SWT.Selection, new Event());
						} catch (Exception ex) {
							ex.printStackTrace(System.out);
						}
					}
				}
				
			}
		});
		return bot.shell("Preferences");
	}

	protected static void turnOffAutoBuild() {
		openPreferencesWindow();
		bot.tree().expandNode("General").select("Workspace");
		SWTBotCheckBox buildAuto = bot.checkBox("Build automatically");
		if (buildAuto != null && buildAuto.isChecked()) {
			buildAuto.click();
		}
		bot.button("Apply").click();
		bot.button("OK").click();
	}

	@Test
	public void test() {
		showView("Swimlane Testing", "Hexagons");
		SWTBotView view = bot.viewByTitle("Hexagons");
		dumpView(view);
		Date startBuildAt = new Date();
		projectMenu().menu("Build All").click();
		SWTBotLabel lastBuild = bot.labelWithId("hexagons.lastBuild");
		assertNotNull(lastBuild);
		bot.waitUntil(labelAfterDate(lastBuild, startBuildAt));
	}

	private ICondition labelAfterDate(SWTBotLabel field, Date wantAfter) {
		return new ICondition() {
			private SimpleDateFormat sdf;

			@Override
			public void init(SWTBot bot) {
				sdf = new SimpleDateFormat("HHmmss.SSS");
			}
			
			@Override
			public boolean test() throws Exception {
				String text = field.getText();
				System.out.println(sdf.format(new Date()) + " " + new Date() + " " + wantAfter + " " + text);
				try {
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date());

					Date d1 = sdf.parse(text);
					Calendar c1 = Calendar.getInstance();
					c1.setTime(d1);
					
					int copy = Calendar.HOUR_OF_DAY|Calendar.MINUTE|Calendar.SECOND|Calendar.MILLISECOND;
					cal.set(copy, c1.get(copy));
					Date d = cal.getTime();
					System.out.println("Parsed out " + d);
					return d.after(wantAfter);
				} catch (Exception ex) {
					System.out.println("Error parsing " + text + ": " + ex);
					return false;
				}
			}
			
			@Override
			public String getFailureMessage() {
				return "not implemented";
			}
		};
	}

	// The actual project menu is hidden by (multiple) sub-menus of "Search" that are also Project; turn off recursive searching to find the right menu
	protected SWTBotMenu projectMenu() {
		return bot.menu().menu("Project", false, 0);
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
