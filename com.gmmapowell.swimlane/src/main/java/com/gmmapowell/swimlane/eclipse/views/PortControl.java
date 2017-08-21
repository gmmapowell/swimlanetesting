package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import com.gmmapowell.swimlane.eclipse.interfaces.PortData;

public class PortControl {
	private final Canvas canvas;

	public PortControl(Composite view, String portId, int hex, PortData p) {
		canvas = new Canvas(view, SWT.NONE);
//		canvas.setData("com.gmmapowell.swimlane.type", "port");
//		canvas.setData("com.gmmapowell.swimlane.hexagon", hex);
//		canvas.setData("com.gmmapowell.swimlane.location", p.getLocation());
//		canvas.setData("org.eclipse.swtbot.widget.key", portId);
//		canvas.addPaintListener(new PortPaintListener(canvas, p));
//		String name = p.getName();
//		canvas.setToolTipText(name.substring(name.lastIndexOf('.')+1));
	}

//	public Control getCanvas() {
//		return canvas;
//	}

}
