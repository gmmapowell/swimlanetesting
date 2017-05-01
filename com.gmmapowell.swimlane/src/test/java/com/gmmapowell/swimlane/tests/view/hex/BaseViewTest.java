package com.gmmapowell.swimlane.tests.view.hex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;

import com.gmmapowell.swimlane.eclipse.interfaces.HexagonDataModel;
import com.gmmapowell.swimlane.eclipse.interfaces.ModelDispatcher;
import com.gmmapowell.swimlane.eclipse.models.SolidModelDispatcher;
import com.gmmapowell.swimlane.eclipse.views.HexView;
import com.gmmapowell.swimlane.tests.swtutil.ImageChecker;
import com.gmmapowell.swimlane.tests.swtutil.ImageProxy;
import com.gmmapowell.swimlane.tests.swtutil.TestBase;

public abstract class BaseViewTest extends TestBase {
	public Shell shell;
	public HexView hv;
	private ModelDispatcher md;

	@Before
	public void setup() throws Exception {
		shell = displayHelper.createShell();
		shell.setLayout(new GridLayout(1, false));
		md = new SolidModelDispatcher();
		hv = new HexView(shell, md);
		shell.setSize(600, 300);
		shell.open();
		displayHelper.flushPendingEvents();
	}

	@After
	public void tearDown() throws Exception {
//		Thread.sleep(3000);
		displayHelper.dispose();
	}

	protected void pushModel(HexagonDataModel testModel) {
		md.setModel(testModel);
		shell.redraw();
		shell.update();
		displayHelper.flushPendingEvents();
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
}
