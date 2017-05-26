package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class AdapterControl {
	private final Canvas canvas;

	public AdapterControl(Composite view, String id) {
		canvas = new Canvas(view, SWT.NONE);
		canvas.setData("com.gmmapowell.swimlane.type", "adapter");
		canvas.setData("org.eclipse.swtbot.widget.key", id);
	}

}
