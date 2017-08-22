package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.widgets.Canvas;

public class UtilityBarLayout extends LayoutPlacer {

	public UtilityBarLayout(Canvas bar) {
		super(bar);
	}

	@Override
	public void constrain(SwimlaneLayoutConstraints constraints) {
		constraints.utility(this);
	}
}