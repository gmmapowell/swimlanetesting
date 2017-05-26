package com.gmmapowell.swimlane.tests.swtutil;

import org.eclipse.swt.graphics.Color;

public interface ImageProxy {
	public void assertColorOfPixel(Color expected, int x, int y);
	public void assertColorOfPixel(int swtColor, int x, int y);
}
