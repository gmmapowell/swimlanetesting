package com.gmmapowell.swimlane.tests.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import com.gmmapowell.swimlane.eclipse.models.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.views.HexagonViewPart;
import com.gmmapowell.swimlane.tests.hamcrest.DisplayHelper;

public class TestASimpleCaseOfOneAcceptanceTest {
	@ClassRule
	public static final DisplayHelper displayHelper = new DisplayHelper();
	private static Shell shell;
	private static HexagonViewPart part;

	@BeforeClass
	public static void setup() throws Exception {
		shell = displayHelper.createShell();
		part = new HexagonViewPart();
		part.createControls(shell);
		shell.setSize(600, 300);
		shell.open();
		displayHelper.flushPendingEvents();
		part.setModel(testModel());
		displayHelper.flushPendingEvents();
	}
	
	@Test
	public void testAllTheControlsWeWantAreThere() throws Exception{
		assertControls(shell, "hexagons.lastBuild", "hexagons.acceptance.1");
	}
	
	@Test
	public void testTheBuildLabelHasTheRightTime() {
        dumpControl("", shell);
        Label lastBuild = getControl(shell, "hexagons.lastBuild");
        assertNotNull(lastBuild);
        assertEquals("042020.420", lastBuild.getText());
	}

	@Test
	public void testTheAcceptanceRowLooksRight() throws Exception {
		Canvas acceptance = getControl(shell, "hexagons.acceptance.1");
		assertNotNull("No acceptance test was found", acceptance);
		checkSizeColors(acceptance, 590, 6, new ImageChecker() {
			@Override
			public void checkImage(ImageProxy proxy) {
				proxy.assertColorOfPixel(SWT.COLOR_GREEN, 5, 3);
				proxy.assertColorOfPixel(SWT.COLOR_GRAY, 500, 3);
			}
		});
	}

	private void checkSizeColors(Canvas canvas, int x, int y, ImageChecker checker) {
		Point pt = canvas.getSize();
		assertEquals(x, pt.x);
		assertEquals(y, pt.y);
		GC gc = new GC(canvas);
		Image image = new Image(canvas.getDisplay(), pt.x, pt.y);
		PaletteData palette = image.getImageData().palette;
		gc.copyArea(image, 0, 0);
		checker.checkImage(new ImageProxy() {
			@Override
			public void assertColorOfPixel(int swtColor, int x, int y) {
				Color color = displayHelper.getDisplay().getSystemColor(swtColor);
				RGB actual = palette.getRGB(image.getImageData().getPixel(x, y));
				boolean match = 
					actual.red >= color.getRed()-5 && actual.red <= color.getRed() + 5 &&
					actual.green >= color.getGreen()-5 && actual.green <= color.getGreen() + 5 &&
					actual.blue >= color.getBlue()-5 && actual.blue <= color.getBlue() + 5;
				if (!match)
					fail("Color " + actual + " was not close enough to SWT " + swtColor + " " + color.getRGB());
			}
		});
		image.dispose();
		gc.dispose();
	}

	private void assertControls(Control c, String... names) {
		List<String> list = new ArrayList<String>(Arrays.asList(names));
		assertControls(c, list);
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

	private static HexagonDataModel testModel() {
		HexagonDataModel hdm = new HexagonDataModel();
		Calendar cal = Calendar.getInstance();
		cal.set(2017, 04, 20, 4, 20, 20);
		cal.set(Calendar.MILLISECOND, 420);
		hdm.setBuildTime(cal.getTime());
		return hdm;
	}
	
	@AfterClass
	public static void tearDownClass() throws Exception {
	}
}
