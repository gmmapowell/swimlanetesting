package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import com.gmmapowell.swimlane.eclipse.interfaces.PortData;
import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;

public class PortControl extends LayoutPlacer implements PaintListener {
	private final int hex;
	private final PortLocation loc;

	public PortControl(Composite parent, int hex, PortLocation loc, PortData port) {
//	public PortControl(Composite view, String portId, int hex, PortData p) {
		super(new Canvas(parent, SWT.NONE));
		this.hex = hex;
		this.loc = loc;
//		canvas.setData("com.gmmapowell.swimlane.type", "port");
//		canvas.setData("com.gmmapowell.swimlane.hexagon", hex);
//		canvas.setData("com.gmmapowell.swimlane.location", p.getLocation());
		canvas.setData("org.eclipse.swtbot.widget.key", "swimlane.port." + hex + "." + loc);
		canvas.setLayoutData(this);
		canvas.addPaintListener(this);
//		canvas.setBackground(canvas.getDisplay().getSystemColor(SWT.TRANSPARENT));
		canvas.moveAbove(null); // move to z-axis top
//		String name = p.getName();
//		canvas.setToolTipText(name.substring(name.lastIndexOf('.')+1));
	}

//	public Control getCanvas() {
//		return canvas;
//	}

	@Override
	public void constrain(SwimlaneLayoutConstraints constraints) {
		constraints.port(hex, loc, this);
	}

	@Override
	public void paintControl(PaintEvent e) {
		Color port = new Color(canvas.getDisplay(), 200, 200, 155);
		GC gc = new GC(canvas);
//		gc.setBackground(canvas.getDisplay().getSystemColor(SWT.TRANSPARENT));
//		gc.fillRectangle(0, 0, canvas.getSize().x, canvas.getSize().y);
		gc.setBackground(port);
		int midx = canvas.getSize().x / 2, ex = loc.x(midx), rx = loc.x(midx - 10);
		int midy = canvas.getSize().y / 2, ey = loc.y(midy);
		// think southeast, where locx and locy are both 1
		// go around the points anticlockwise from eastern edge
		gc.fillPolygon(new int[] { midx + ex, midy - ey, midx + rx, midy - ey, midx - ex, midy + ey, midx - rx, midy + ey });
		gc.dispose();
		port.dispose();
	}

}
