package com.gmmapowell.swimlane.eclipse.views;

import org.eclipse.swt.widgets.Canvas;

import com.gmmapowell.swimlane.eclipse.interfaces.PortLocation;

public class AdapterBarLayout extends LayoutPlacer {
	private final int hex;
	private final PortLocation ploc;
	private final int aloc;

	public AdapterBarLayout(int hex, PortLocation ploc, int aloc, Canvas canvas) {
		super(canvas);
		this.hex = hex;
		this.ploc = ploc;
		this.aloc = aloc;
	}

	@Override
	public void constrain(SwimlaneLayoutConstraints constraints) {
		constraints.adapter(hex, ploc, aloc, this);
	}

}
