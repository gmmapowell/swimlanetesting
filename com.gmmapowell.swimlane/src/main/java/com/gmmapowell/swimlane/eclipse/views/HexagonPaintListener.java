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
		System.out.println("mid = " + mx + " " + my);
		int a = figureA(canvas.getSize().x, canvas.getSize().y);
		int h = (int) (Math.sqrt(3)*a);
		int ty = my-h, by = my+h;
		System.out.println("a = " + a + " h = " + h);
		
		Color c = new Color(canvas.getDisplay(), 220, 220, 170);
		GC gc = new GC(canvas);
		gc.setBackground(c);
		gc.setForeground(canvas.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		gc.fillPolygon(new int[] { mx-2*a, my, mx-a, ty, mx+a, ty, mx+2*a, my, mx+a, by, mx-a, by });
		gc.dispose();
		c.dispose();
	}

	// TODO: much more logic and more testing
	public int figureA(int wx, int hy) {
		int ya = hy*6/25;
		int xa = wx/5;
		return Math.min(xa, ya);
	}

}
