package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.widgets.Canvas;

public class AcceptanceBarLayout extends LayoutPlacer {
	private final String mask;

	public AcceptanceBarLayout(String mask, Canvas bar) {
		super(bar);
		this.mask = mask;
	}

	@Override
	public void constrain(SwimlaneLayoutConstraints constraints) {
		constraints.acceptanceCalled(mask, this);
	}
}