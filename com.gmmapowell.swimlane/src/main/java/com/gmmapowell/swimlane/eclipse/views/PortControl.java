package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class PortControl {
	private final Canvas canvas;

	public PortControl(Composite view, String portId) {
		canvas = new Canvas(view, SWT.NONE);
		canvas.setData("com.gmmapowell.swimlane.type", "port");
		canvas.setData("org.eclipse.swtbot.widget.key", portId);
	}

}
