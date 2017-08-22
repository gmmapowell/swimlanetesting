package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.widgets.Canvas;

import com.gmmapowell.swimlane.eclipse.interfaces.SwimlaneLayoutData;

public abstract class LayoutPlacer implements SwimlaneLayoutData {
	protected final Canvas canvas;

	public LayoutPlacer(Canvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public void layout(int xpos, int ypos, int xmax, int ymax) {
		canvas.setLocation(xpos, ypos);
		canvas.setSize(xmax, ymax);
	}

}