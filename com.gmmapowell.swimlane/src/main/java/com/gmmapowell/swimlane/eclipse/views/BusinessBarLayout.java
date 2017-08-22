package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.widgets.Canvas;

public class BusinessBarLayout extends LayoutPlacer {
	private final HexagonBackground hc;

	public BusinessBarLayout(HexagonBackground hc, Canvas bar) {
		super(bar);
		this.hc = hc;
	}

	@Override
	public void constrain(SwimlaneLayoutConstraints constraints) {
		constraints.businessBarIn(hc, this);
	}

}
