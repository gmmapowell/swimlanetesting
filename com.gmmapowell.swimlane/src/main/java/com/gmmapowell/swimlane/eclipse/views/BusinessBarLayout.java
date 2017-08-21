package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.widgets.Canvas;

import com.gmmapowell.swimlane.eclipse.interfaces.SwimlaneLayoutData;

public class BusinessBarLayout implements SwimlaneLayoutData {
	private final HexagonBackground hc;
	private final Canvas bar;

	public BusinessBarLayout(HexagonBackground hc, Canvas bar) {
		this.hc = hc;
		this.bar = bar;
	}

	@Override
	public void constrain(SwimlaneLayoutConstraints constraints) {
		constraints.businessBarIn(hc, this);
	}

	@Override
	public void layout(int xpos, int ypos, int xmax, int ymax) {
		bar.setLocation(xpos, ypos);
		bar.setSize(xmax, ymax);
	}

}
