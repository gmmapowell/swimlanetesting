package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Canvas;

import com.gmmapowell.swimlane.eclipse.interfaces.PortData;

public class PortPaintListener implements PaintListener {
	private final Canvas canvas;
//	private final PortLocation loc;

	public PortPaintListener(Canvas canvas, PortData p) {
		this.canvas = canvas;
//		this.loc = p.getLocation();
	}

	@Override
	public void paintControl(PaintEvent e) {
		throw new RuntimeException("not implemented");
/*
		Color port = new Color(canvas.getDisplay(), 200, 200, 155);
		GC gc = new GC(canvas);
//		gc.setBackground(canvas.getDisplay().getSystemColor(SWT.COLOR_BLACK));
//		gc.fillRectangle(0, 0, canvas.getSize().x, canvas.getSize().y);
		gc.setBackground(port);
		int midx = canvas.getSize().x/2, ex = loc.x(midx), rx = loc.x(midx-10);
		int midy = canvas.getSize().y/2, ey = loc.y(midy);
		// think southeast, where locx and locy are both 1
		// go around the points anticlockwise from eastern edge
		gc.fillPolygon(new int[] { midx+ex, midy-ey,  midx+rx, midy-ey, midx-ex, midy+ey, midx-rx, midy+ey });
		gc.dispose();
		port.dispose();
*/
		}

}
