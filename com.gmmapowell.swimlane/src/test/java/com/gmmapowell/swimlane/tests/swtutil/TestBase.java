package com.gmmapowell.swimlane.tests.swtutil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.junit.Rule;

public class TestBase {
	@Rule public final JUnitRuleMockery context = new JUnitRuleMockery() {{
		setThreadingPolicy(new Synchroniser());
	}};
	@Rule public final DisplayHelper displayHelper = new DisplayHelper();

	protected void assertControls(Control c, String... names) {
		List<String> list = new ArrayList<String>(Arrays.asList(names));
		assertControls(c, list);
		assertTrue("Controls were missing: " + list, list.isEmpty());
	}

	protected void assertControlsInOrder(Control c, String... names) {
		List<String> list = new ArrayList<String>(Arrays.asList(names));
		assertControlsInOrder(c, list);
		assertTrue("Controls were missing: " + list, list.isEmpty());
	}

	private void assertControls(Control c, List<String> list) {
		String key = (String) c.getData("org.eclipse.swtbot.widget.key");
		if (key != null && !list.remove(key))
			fail("An unexpected control " + key + " was found");
		if (c instanceof Composite) {
			for (Control ch : ((Composite)c).getChildren()) {
				assertControls(ch, list);
			}
		}
	}

	private void assertControlsInOrder(Control c, List<String> list) {
		String key = (String) c.getData("org.eclipse.swtbot.widget.key");
		if (key != null) {
			if (list.isEmpty())
				fail("Unexpected control: " + key);
			String expected = list.remove(0);
			assertEquals("controls out of order", expected, key);
		}
		if (c instanceof Composite) {
			for (Control ch : ((Composite)c).getChildren()) {
				assertControlsInOrder(ch, list);
			}
		}
	}

	protected <T extends Control> T waitForControl(Control c, String which) {
		for (int i=0;i<5;i++) {
			T ret = getControl(c, which);
			if (ret != null)
				return ret;
			displayHelper.flushPendingEvents();
			try {Thread.sleep(5);} catch (InterruptedException ex) {}
		}
		fail("control " + which + " could not be found under " + c);
		return null;
	}

	@SuppressWarnings("unchecked")
	private <T extends Control> T getControl(Control c, String which) {
		if (which.equals(c.getData("org.eclipse.swtbot.widget.key")))
			return (T)c;
		if (c instanceof Composite) {
			for (Control ch : ((Composite)c).getChildren()) {
				Control r = getControl(ch, which);
				if (r != null)
					return (T) r;
			}
				
		}
		return null;
	}

	protected void dumpComposite(Composite sw, String indent) {
		for (Control c : sw.getChildren()) {
			dumpControl(indent, c);
		}
	}

	protected void dumpControl(String indent, Control c) {
		if (c == null) {
			System.out.println(indent + "null");
			return;
		}
		System.out.println(indent + c + " " + c.getClass() + " " + c.getBounds());
		if (c.getData("org.eclipse.swtbot.widget.key") != null)
			System.out.println(indent + " -> " + c.getData("org.eclipse.swtbot.widget.key"));
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
		if (c instanceof ToolBar) {
			ToolBar tb = (ToolBar) c;
			ToolItem[] items = tb.getItems();
			String ind = indent + "  ";
			int item = 0;
			for (ToolItem ti : items) {
				System.out.println(ind + "Item " + (item++));
				if (ti.getText() != null)
					System.out.println(ind + "  text - " + ti.getText());
				if (ti.getToolTipText() != null)
					System.out.println(ind + "  tip  - " + ti.getToolTipText());
				if (ti.getData("org.eclipse.swtbot.widget.key") != null)
					System.out.println(ind + " -> " + ti.getData("org.eclipse.swtbot.widget.key"));
			}
		}
	}

	protected void assertToolBarTipsInOrder(ToolBar tb, String... tips) {
		assertEquals("The number of tools was not correct", tips.length, tb.getItems().length);
		for (int i=0;i<tips.length;i++) {
			ToolItem ti = tb.getItem(i);
			assertEquals("The tip for item " + i + " was wrong", tips[i], ti.getToolTipText());
		}
	}

	protected ToolItem getItem(ToolBar toolBar, String tooltip) {
		for (ToolItem ti : toolBar.getItems())
			if (tooltip.equals(ti.getToolTipText()))
				return ti;
		throw new RuntimeException("No tool with tip '" + tooltip + "' could be found");
	}

	protected static Date exactDate(int yr, int mth, int day, int hr, int min, int sec, int ms) {
		Calendar cal = Calendar.getInstance();
		cal.set(yr, mth, day, hr, min, sec);
		cal.set(Calendar.MILLISECOND, ms);
		return cal.getTime();
	}

}
