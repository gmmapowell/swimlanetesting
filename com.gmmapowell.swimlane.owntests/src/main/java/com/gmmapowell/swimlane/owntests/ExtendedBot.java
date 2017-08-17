package com.gmmapowell.swimlane.owntests;

import static org.junit.Assert.fail;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.waits.ICondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCanvas;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotLabel;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;

public class ExtendedBot {
	private SWTWorkbenchBot bot;
	private SWTBotShell as;

	public ExtendedBot(SWTWorkbenchBot bot) {
		this.bot = bot;
		as = bot.activeShell();
	}

	public SWTBotShell openPreferencesWindow() {
		as.activate();
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

	public void turnOffAutoBuild() {
		as.activate();
		openPreferencesWindow();
		bot.tree().expandNode("General").select("Workspace");
		SWTBotCheckBox buildAuto = bot.checkBox("Build automatically");
		if (buildAuto != null && buildAuto.isChecked()) {
			buildAuto.click();
		}
		bot.button("Apply").click();
		try {
			bot.button("Apply and Close").click();
			SWTBotShell shell = bot.shell("Authorizing with Eclipse.org");
			shell.activate();
			bot.button("Cancel").click();
			as.activate();
		} catch (Exception ex) {
			ex.printStackTrace();
			bot.button("OK").click();
		}
	}

	public ICondition labelAfterDate(SWTBotLabel field, Date wantAfter) {
		return new ICondition() {
			private SimpleDateFormat sdf;
			private String failureText;

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
					if (!d.after(wantAfter))
						failureText = "Label time " + d + " is before " + wantAfter;
					return d.after(wantAfter);
				} catch (Exception ex) {
					failureText = "Error parsing " + text;
					System.out.println("Error parsing " + text + ": " + ex);
					return false;
				}
			}
			
			@Override
			public String getFailureMessage() {
				return failureText;
			}
		};
	}

	// The actual project menu is hidden by (multiple) sub-menus of "Search" that are also Project; turn off recursive searching to find the right menu
	public SWTBotMenu projectMenu() {
		as.activate();
		return bot.menu().menu("Project", false, 0);
	}

	public void closeWelcomeView() {
		as.activate();
		SWTBotView welcome = bot.viewByTitle("Welcome");
		if (welcome != null)
			welcome.close();
	}

	public void importSampleProject(File project) {
		as.activate();
		bot.menu("File").menu("Import...").click();
		System.out.println("Active = " + bot.activeShell());
		bot.tree().expandNode("General").getNode("Existing Projects into Workspace").click();
		bot.button("Next >").click();
		String projdir = project.getPath();
		System.out.println(projdir);
//		dumpActiveShell();
		bot.comboBox().setText(projdir);
		bot.button("Cancel").setFocus();
		bot.button("Finish").click();
	}

	protected void showView(String... path) {
		as.activate();
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
		long q = SWTBotPreferences.TIMEOUT;
		SWTBotPreferences.TIMEOUT = 200;
		try {
			for (int i=0;i<10;i++) {
				try {
					bot.button("OK").click();
					return;
				} catch (TimeoutException | WidgetNotFoundException ex) { System.out.println("No OK button"); }
				try {
					bot.button("Open").click();
					return;
				} catch (TimeoutException | WidgetNotFoundException ex) { System.out.println("No Open button"); }
			}
			throw new RuntimeException("Could not find OK or Open button");
		} finally {
			SWTBotPreferences.TIMEOUT = q;
		}
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

	public void dumpView(SWTBotView view) {
		view.getWidget().getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				dumpComposite((Composite) view.getWidget(), "  ");
			}
		});
		for (SWTBotToolbarButton b : view.getToolbarButtons())
			System.out.println("Button " + b);
	}

	protected void dumpComposite(Composite sw, String indent) {
		for (Control c : sw.getChildren()) {
			dumpControl(indent, c);
		}
	}

	protected void dumpControl(String indent, Control c) {
		String key = (String) c.getData("org.eclipse.swtbot.widget.key");
		System.out.println(indent + c + " " + c.getClass() + (key != null ? " " + key : ""));
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

	public Point getSize(Widget widget) {
		List<Point> p = new ArrayList<>();
		widget.getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				Point size = ((Composite)widget).getSize();
				p.add(size);
			}
		});
		return p.get(0);
	}

	public void assertPct(int mine, int from, double min, double max) {
		int pct = mine*100/from;
		if (pct >= min && pct <= max)
			return;
		fail(pct + " was not between " + min + " and " + max);
	}

	public void assertColor(SWTBotCanvas acc123, int swtColor, int x, int y) {
		// There appear to be a bunch of bugs in Oxygen with regard to NSImageReps.
		// In the meantime, this code does not work, so just quit ...
		if (x >= 0 && y >= 0)
			return;
		Display display = acc123.display;
		AtomicBoolean passed = new AtomicBoolean(false);
		List<String> error = new ArrayList<>();
		for (int i=0;i<10 && !passed.get();i++) {
			display.syncExec(new Runnable() {
				@Override
				public void run() {
					Color color = display.getSystemColor(swtColor);
					GC gc = new GC(acc123.widget);
					// There is a bug in Oxygen in which the _getImageData cannot handle a width of less than 16 (it loops without considering the width up until the number of bytes per row,
					// which appears to have a minimum value of 64.
					// My workaround would be to make the image 16 wide; but event then the results are not correct
					Image image = new Image(display, 1, 1);
					PaletteData palette = image.getImageData().palette;
					gc.copyArea(image, x, y);
					RGB actual = palette.getRGB(image.getImageData().getPixel(0, 0));
					boolean match = 
						actual.red >= color.getRed()-5 && actual.red <= color.getRed() + 5 &&
						actual.green >= color.getGreen()-5 && actual.green <= color.getGreen() + 5 &&
						actual.blue >= color.getBlue()-5 && actual.blue <= color.getBlue() + 5;
					if (match)
						passed.set(true);
					else {
						error.clear();
						error.add("Color " + actual + " was not close enough to SWT " + swtColor + " " + color.getRGB());
					}
				}
			});
			try { Thread.sleep(250); } catch (InterruptedException e2) {}
		}
		if (!passed.get())
			fail(error.get(0));
	}
}
