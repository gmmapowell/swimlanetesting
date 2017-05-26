package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;

public class HexagonPaintListener implements PaintListener {
	private final Canvas canvas;

	public HexagonPaintListener(Canvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public void paintControl(PaintEvent e) {
		int mx = canvas.getSize().x/2;
		int my = canvas.getSize().y/2;
		Color c = new Color(canvas.getDisplay(), 220, 220, 170);
		GC gc = new GC(canvas);
		gc.setBackground(c);
		gc.setForeground(canvas.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		gc.fillRectangle(mx-20, my-20, 41, 41);
		gc.dispose();
		c.dispose();
	}

}
