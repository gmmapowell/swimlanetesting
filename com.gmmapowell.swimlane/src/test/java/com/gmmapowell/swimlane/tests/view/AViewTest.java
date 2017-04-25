package com.gmmapowell.swimlane.tests.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import com.gmmapowell.swimlane.eclipse.models.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.views.HexagonViewPart;
import com.gmmapowell.swimlane.tests.hamcrest.DisplayHelper;

public abstract class AViewTest {
	@Rule public final JUnitRuleMockery context = new JUnitRuleMockery();
	@Rule public final DisplayHelper displayHelper = new DisplayHelper();

	protected Shell shell;
	protected HexagonViewPart part;

	@Before
	public void setup() throws Exception {
		shell = displayHelper.createShell();
		part = new HexagonViewPart();
		part.createControls(shell);
		shell.setSize(600, 300);
		shell.open();
		displayHelper.flushPendingEvents();
	}

	
	@After
	public void tearDown() throws Exception {
//		Thread.sleep(3000);
		displayHelper.dispose();
	}
	

	protected void checkSizeColors(Canvas canvas, int x, int y, ImageChecker checker) {
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

	protected void pushModel(HexagonDataModel testModel) {
		part.setModel(testModel);
		shell.redraw();
		shell.update();
		displayHelper.flushPendingEvents();
	}

	protected static Date exactDate(int yr, int mth, int day, int hr, int min, int sec, int ms) {
		Calendar cal = Calendar.getInstance();
		cal.set(yr, mth, day, hr, min, sec);
		cal.set(Calendar.MILLISECOND, ms);
		return cal.getTime();
	}
}
