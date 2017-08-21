package com.gmmapowell.swimlane.tests.view.hex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;

import com.gmmapowell.swimlane.tests.swtutil.ImageChecker;
import com.gmmapowell.swimlane.tests.swtutil.ImageProxy;
import com.gmmapowell.swimlane.tests.swtutil.TestBase;

public abstract class BaseViewTest extends TestBase {
	public Shell shell;

	@Before
	public void setup() throws Exception {
		create();
		complete();
	}

	protected void create() {
		shell = displayHelper.createShell();
		shell.setLayout(new GridLayout(1, false));
	}

	protected void complete() {
		shell.setSize(600, 300);
		shell.open();
		displayHelper.flushPendingEvents();
	}
	
	@After
	public void tearDown() throws Exception {
//		Thread.sleep(3000);
		displayHelper.dispose();
	}

	protected void updateShell() {
		shell.redraw();
		shell.update();
		displayHelper.flushPendingEvents();
	}

	protected void checkLocationSizeColors(String objName, int x, int y, int width, int height, ImageChecker checker) {
		List<String> names = new ArrayList<String>();
		for (Control ch : ((Composite)shell.getChildren()[0]).getChildren()) {
			String cn = (String) ch.getData("org.eclipse.swtbot.widget.key");
			if (objName.equals(cn)) {
				checkSizeColors((Canvas)ch, x, y, width, height, checker);
				return;
			}
			names.add(cn);
		}
		throw new RuntimeException(objName + " was not a valid child: " + names);
	}
	
	protected void checkSizeColors(Canvas canvas, int x, int y, int width, int height, ImageChecker checker) {
		{
			Point pt = canvas.getLocation();
			assertEquals(x, pt.x);
			assertEquals(y, pt.y);
		}
		Image image;
		{
			Point pt = canvas.getSize();
			assertEquals(width, pt.x);
			assertEquals(height, pt.y);
			image = new Image(canvas.getDisplay(), pt.x, pt.y);
		}
		GC gc = new GC(canvas);
		PaletteData palette = image.getImageData().palette;
		for (int i=0;i<5;i++) {
			try {
				gc.copyArea(image, 0, 0);
				checker.checkImage(new ImageProxy() {
					@Override
					public void assertColorOfPixel(int swtColor, int x, int y) {
						Color color = displayHelper.getDisplay().getSystemColor(swtColor);
						assertColorOfPixel(color, x, y);
					}
					
					@Override
					public void assertColorOfPixel(Color color, int x, int y) {
						RGB actual = palette.getRGB(image.getImageData().getPixel(x, y));
						boolean match = 
							actual.red >= color.getRed()-5 && actual.red <= color.getRed() + 5 &&
							actual.green >= color.getGreen()-5 && actual.green <= color.getGreen() + 5 &&
							actual.blue >= color.getBlue()-5 && actual.blue <= color.getBlue() + 5;
						if (!match)
							fail("Color " + actual + " was not close enough to " + color.getRGB() + " at " + x + " " + y);
					}
				});
				break;
			} catch (AssertionError ex) {
				if (i == 4)
					throw ex;
				try { Thread.sleep(100); } catch (InterruptedException e2) { }
				displayHelper.flushPendingEvents();
			}
		}
		image.dispose();
		gc.dispose();
	}

	public void showFor(int time) {
		for (int i=0;i<time/50;i++) {
			displayHelper.flushPendingEvents();
			try {
				Thread.sleep(50);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}
}
