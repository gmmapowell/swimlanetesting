package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.widgets.Canvas;

import com.gmmapowell.swimlane.eclipse.interfaces.SwimlaneLayoutData;

public abstract class BarLayout implements SwimlaneLayoutData {
	protected final Canvas bar;

	public BarLayout(Canvas bar) {
		this.bar = bar;
	}

	@Override
	public void layout(int xpos, int ypos, int xmax, int ymax) {
		bar.setLocation(xpos, ypos);
		bar.setSize(xmax, ymax);
	}

}