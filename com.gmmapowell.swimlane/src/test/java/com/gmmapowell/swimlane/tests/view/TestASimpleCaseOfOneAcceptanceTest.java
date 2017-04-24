package com.gmmapowell.swimlane.tests.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.junit.Rule;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.models.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.views.HexagonView;
import com.gmmapowell.swimlane.tests.hamcrest.DisplayHelper;

public class TestASimpleCaseOfOneAcceptanceTest {
	@Rule
	public final DisplayHelper displayHelper = new DisplayHelper();
	private Shell shell;
	private HexagonView part;

	protected void setup() {
		shell = displayHelper.createShell();
		part = new HexagonView();
		part.createControls(shell);
		shell.open();
		displayHelper.flushPendingEvents();
		part.setModel(testModel());
		displayHelper.flushPendingEvents();
	}
	
	@Test
	public void testTheBuildLabelHasTheRightTime() {
        setup();
        dumpControl("", shell);
        Label lastBuild = getControl(shell, "hexagons.lastBuild");
        assertNotNull(lastBuild);
        assertEquals("042020.420", lastBuild.getText());
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
		System.out.println(indent + c + " " + c.getClass());
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
	}

	private HexagonDataModel testModel() {
		HexagonDataModel hdm = new HexagonDataModel();
		Calendar cal = Calendar.getInstance();
		cal.set(2017, 04, 20, 4, 20, 20);
		cal.set(Calendar.MILLISECOND, 420);
		hdm.setBuildTime(cal.getTime());
		return hdm;
	}
}
