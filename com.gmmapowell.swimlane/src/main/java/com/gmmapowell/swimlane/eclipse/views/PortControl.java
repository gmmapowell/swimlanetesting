package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;

public class PortControl {
	private final Canvas canvas;

	public PortControl(Composite view, String portId, int hex, PortLocation loc) {
		canvas = new Canvas(view, SWT.NONE);
		canvas.setData("com.gmmapowell.swimlane.type", "port");
		canvas.setData("com.gmmapowell.swimlane.hexagon", hex);
		canvas.setData("com.gmmapowell.swimlane.location", loc);
		canvas.setData("org.eclipse.swtbot.widget.key", portId);
		canvas.addPaintListener(new PortPaintListener(canvas, loc));
	}

	public Control getCanvas() {
		return canvas;
	}

}
